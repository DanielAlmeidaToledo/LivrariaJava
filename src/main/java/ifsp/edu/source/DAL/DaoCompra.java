package ifsp.edu.source.DAL;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import ifsp.edu.source.Model.ItemProduto;
import ifsp.edu.source.Model.Livro;
import ifsp.edu.source.Model.Compra;
import org.springframework.stereotype.Component;

@Component
public class DaoCompra {
    // Método para incluir uma Compra no banco de dados
    public Compra incluir(Compra Compra) {
        DataBaseCom.conectar();
        String sqlInserirCompra = "INSERT INTO compra (id, id_cliente, data) VALUES (?, ?, ?)";

        try {
            // Inserir a Compra
            PreparedStatement psInserirCompra = DataBaseCom.getConnection().prepareStatement(sqlInserirCompra);
            psInserirCompra.setString(1, Compra.getId());
            psInserirCompra.setString(2, Compra.getIdCliente());
            psInserirCompra.setString(3, Compra.getData());

            int rowsAffectedCompra = psInserirCompra.executeUpdate();

            if (rowsAffectedCompra > 0) {
                // Obter a lista de itens associados à Compra
                List<ItemProduto> itensCompra = Compra.getItens();
                // printa a lista de itens
                System.out.println("Lista de itens da Compra: ");
                System.out.println(Compra.getItens());

                // Inserir os itens associados à Compra chamando a controller de ItemProduto
                DaoItemCompra daoItemCompra = new DaoItemCompra();
                for (ItemProduto ItemProduto : itensCompra) {
                    // Associar o item à Compra antes de incluir no banco de dados
                    ItemProduto.setCompra(Compra.getId());
                    // Incluir o item de Compra no banco de dados
                    daoItemCompra.incluir(ItemProduto);
                }

                // Atualizar a quantidade de livros após a inserção bem-sucedida
                atualizarQuantidadeLivros(Compra.getId(), true);

                return Compra;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para atualizar a quantidade de livros
    private void atualizarQuantidadeLivros(String compraId, boolean decrementar) {
        // Obter a lista de itens associados à Compra
        List<ItemProduto> itensCompra = obterItensCompra(compraId);

        for (ItemProduto ItemProduto : itensCompra) {
            try {
                String sqlString = "UPDATE produto SET qtde = qtde " + (decrementar ? "- 1" : "+ 1") + " WHERE id = ?";
                PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
                ps.setString(1, ItemProduto.getLivro());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<ItemProduto> obterItensCompra(String compraId) {
        List<ItemProduto> itensCompra = new ArrayList<>();

        System.out.println("Obtendo itens da Compra " + compraId);

        DataBaseCom.conectar();
        String sqlString = "SELECT id, id_produto FROM item_produto WHERE id_compra = ?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compraId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemProduto ItemProduto = new ItemProduto();
                
                System.out.println("ItemProduto: " + rs.getString("id") + " - " + rs.getString("id_produto") + " - " + rs.getString("id_compra") + " - " + rs.getString("qtde") + " -");

                ItemProduto.setId(rs.getString("id"));
                ItemProduto.setLivro(rs.getString("id_produto"));

                // Você precisará obter o livro associado a partir do ID do produto
                Livro livro = obterLivro(rs.getString("id_produto"));
                ItemProduto.setLivro(livro.getId());

                // Adiciona o item associado à Compra à lista
                itensCompra.add(ItemProduto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return itensCompra;
    }

    // Método para obter um livro pelo ID
    private Livro obterLivro(String idProduto) {
        Livro livro = new Livro();

        try {
            String sqlString = "SELECT nome FROM produto WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, idProduto);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                livro.setId(idProduto);
                livro.setNome(rs.getString("nome"));
                livro.setQuantidade(rs.getInt("qtde"));
                livro.setPreco(rs.getDouble("preco"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return livro;
    }

    // Método para alterar uma Compra existente no banco de dados
    public boolean alterar(Compra Compra) {
        DataBaseCom.conectar();

        if (findById(Compra.getId()) == null)
            return false; // Retorna false se a Compra não existir

        try {
            // Antes de fazer alterações, obtenha a lista de livros associados à Compra atual
            List<String> livrosAntigos = obterLivrosDaCompra(Compra.getId());

            // Atualize a Compra
            String sqlString = "UPDATE compra SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, Compra.getIdCliente());
            ps.setString(2, Compra.getData());
            ps.setString(3, Compra.getId());
            ps.execute();

            // Obtenha a lista de livros associados à Compra após a alteração
            List<String> livrosNovos = obterLivrosDaCompra(Compra.getId());

            // Calcule as diferenças entre as duas listas para determinar os livros
            // adicionados e removidos
            List<String> livrosRemovidos = new ArrayList<>(livrosAntigos);
            livrosRemovidos.removeAll(livrosNovos);

            List<String> livrosAdicionados = new ArrayList<>(livrosNovos);
            livrosAdicionados.removeAll(livrosAntigos);

            // Atualize a quantidade de livros adicionando/removendo conforme necessário
            atualizarQuantidadeLivros(Compra.getId(), false);
            atualizarQuantidadeLivros(Compra.getId(), true);

            return true; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para obter a lista de livros associados à Compra
    private List<String> obterLivrosDaCompra(String compraId) {
        List<String> livros = new ArrayList<>();

        try {
            String sqlString = "SELECT id_produto FROM item_produto WHERE id_compra=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compraId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                livros.add(rs.getString("id_produto"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return livros;
    }

    // Método para buscar uma Compra pelo ID no banco de dados
    public Compra findById(String id) {
        DataBaseCom.conectar();
        Compra Compra = null;

        try {
            String sqlString = "SELECT * FROM compra WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Compra = new Compra();
                Compra.setId(rs.getString("id"));
                Compra.setIdCliente(rs.getString("id_cliente"));
                Compra.setData(rs.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Compra;
    }

    // Método para excluir uma Compra do banco de dados
    public boolean excluir(Compra Compra) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM compra WHERE id=?";

        try {
            // Obtenha a lista de livros associados à Compra
            List<String> livros = obterLivrosDaCompra(Compra.getId());

            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, Compra.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Atualize a quantidade de livros
                atualizarQuantidadeLivros(Compra.getId(), false);

                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as Compra no banco de dados
    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM compra");

            while (rs.next()) {
                Compra Compra = new Compra();
                Compra.setId(rs.getString("id"));
                Compra.setIdCliente(rs.getString("id_cliente"));
                Compra.setData(rs.getString("data"));
                Compra.setItens(obterItensCompra(Compra.getId()));

                lista.add(Compra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
