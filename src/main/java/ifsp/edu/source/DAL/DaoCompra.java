package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Compra;
import ifsp.edu.source.Model.ItemProduto;
import ifsp.edu.source.Model.Livro;

@Component
public class DaoCompra {
    // Método para incluir uma Compra no banco de dados
    public Compra incluir(Compra compra) {
        DataBaseCom.conectar();
        String sqlInserirCompra = "INSERT INTO compra (id, id_cliente, data) VALUES (?, ?, ?)";

        try {
            // Inserir a Compra
            PreparedStatement psInserirCompra = DataBaseCom.getConnection().prepareStatement(sqlInserirCompra);
            psInserirCompra.setString(1, compra.getId());
            psInserirCompra.setString(2, compra.getIdCliente());
            psInserirCompra.setString(3, compra.getData());

            int rowsAffectedCompra = psInserirCompra.executeUpdate();

            if (rowsAffectedCompra > 0) {
                // Obter a lista de itens associados à Compra
                List<ItemProduto> itensCompra = compra.getItens();

                // Inserir os itens associados à Compra chamando a controller de ItemProduto
                DaoItemCompra daoItemCompra = new DaoItemCompra();
                for (ItemProduto itemProduto : itensCompra) {

                    String sqlConsultaLivro = "SELECT * FROM produto WHERE id = ?";
                    PreparedStatement psConsultaLivro = DataBaseCom.getConnection().prepareStatement(sqlConsultaLivro);

                    psConsultaLivro.setString(1, itemProduto.getLivro());
                    ResultSet rs = psConsultaLivro.executeQuery();

                    // Associar o item à Compra antes de incluir no banco de dados
                    itemProduto.setCompra(compra.getId());
                    itemProduto.setNomeProduto(rs.getString("nome"));
                    itemProduto.setPreco(rs.getDouble("preco"));
                    
                    // Incluir o item de Compra no banco de dados
                    daoItemCompra.incluir(itemProduto);
                }

                // Atualizar a quantidade de livros após a inserção bem-sucedida
                atualizarQuantidadeLivros(compra.getId(), true);

                return compra;
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

        for (ItemProduto itemProduto : itensCompra) {
            try {
                String sqlString = "UPDATE produto SET qtde = qtde " + (decrementar ? "- 1" : "+ 1") + " WHERE id = ?";
                PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
                ps.setString(1, itemProduto.getLivro());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<ItemProduto> obterItensCompra(String compraId) {
        List<ItemProduto> itensCompra = new ArrayList<>();
    
        DataBaseCom.conectar();
        String sqlString = "SELECT i.id, i.id_produto, i.qtde, p.nome AS nome_produto, p.preco "
                         + "FROM item_produto i "
                         + "JOIN produto p ON i.id_produto = p.id "
                         + "WHERE i.id_compra = ?";
    
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compraId);
    
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                ItemProduto itemProduto = new ItemProduto();
                itemProduto.setId(rs.getString("id"));
                itemProduto.setLivro(rs.getString("id_produto"));
                itemProduto.setQuantidade(rs.getInt("qtde"));
                itemProduto.setNomeProduto(rs.getString("nome_produto"));
                itemProduto.setPreco(rs.getDouble("preco"));
                itemProduto.setCompra(compraId);
    
                // Adiciona o item associado à Compra à lista
                itensCompra.add(itemProduto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return itensCompra;
    }

    // Método para alterar uma Compra existente no banco de dados
    public boolean alterar(Compra compra) {
        DataBaseCom.conectar();

        if (findById(compra.getId()) == null)
            return false; // Retorna false se a Compra não existir

        try {
            // Antes de fazer alterações, obtenha a lista de livros associados à Compra atual
            List<String> livrosAntigos = obterLivrosDaCompra(compra.getId());

            // Atualize a Compra
            String sqlString = "UPDATE compra SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, compra.getIdCliente());
            ps.setString(2, compra.getData());
            ps.setString(3, compra.getId());
            ps.execute();

            // Obtenha a lista de livros associados à Compra após a alteração
            List<String> livrosNovos = obterLivrosDaCompra(compra.getId());

            // Calcule as diferenças entre as duas listas para determinar os livros
            // adicionados e removidos
            List<String> livrosRemovidos = new ArrayList<>(livrosAntigos);
            livrosRemovidos.removeAll(livrosNovos);

            List<String> livrosAdicionados = new ArrayList<>(livrosNovos);
            livrosAdicionados.removeAll(livrosAntigos);

            // Atualize a quantidade de livros adicionando/removendo conforme necessário
            atualizarQuantidadeLivros(compra.getId(), false);
            atualizarQuantidadeLivros(compra.getId(), true);

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
        Compra compra = null;

        try {
            String sqlString = "SELECT * FROM compra WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                compra = new Compra();
                compra.setId(rs.getString("id"));
                compra.setIdCliente(rs.getString("id_cliente"));
                compra.setData(rs.getString("data"));
                compra.setItens(obterItensCompra(compra.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return compra;
    }

    // Método para excluir uma Compra do banco de dados
    public boolean excluir(Compra compra) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM compra WHERE id=?";

        try {
            // Obtenha a lista de livros associados à Compra
            List<String> livros = obterLivrosDaCompra(compra.getId());

            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compra.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Atualize a quantidade de livros
                atualizarQuantidadeLivros(compra.getId(), false);

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
                Compra compra = new Compra();
                compra.setId(rs.getString("id"));
                compra.setIdCliente(rs.getString("id_cliente"));
                compra.setData(rs.getString("data"));
                compra.setItens(obterItensCompra(compra.getId()));

                lista.add(compra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
