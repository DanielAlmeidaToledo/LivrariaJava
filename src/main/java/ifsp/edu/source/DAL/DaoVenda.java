package ifsp.edu.source.DAL;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import ifsp.edu.source.Model.ItemTransacao;
import ifsp.edu.source.Model.ItemVenda;
import ifsp.edu.source.Model.Livro;
import ifsp.edu.source.Model.Venda;
import org.springframework.stereotype.Component;

@Component
public class DaoVenda {
    // Método para incluir uma venda no banco de dados
    public Venda incluir(Venda venda) {
        DataBaseCom.conectar();
        String sqlInserirVenda = "INSERT INTO venda (id, id_cliente, data) VALUES (?, ?, ?)";

        try {
            // Inserir a venda
            PreparedStatement psInserirVenda = DataBaseCom.getConnection().prepareStatement(sqlInserirVenda);
            psInserirVenda.setString(1, venda.getId());
            psInserirVenda.setString(2, venda.getIdCliente());
            psInserirVenda.setString(3, venda.getData());

            int rowsAffectedVenda = psInserirVenda.executeUpdate();

            if (rowsAffectedVenda > 0) {
                // Obter a lista de itens associados à venda
                List<ItemVenda> itensVenda = venda.getItensVenda();

                // Inserir os itens associados à venda chamando a controller de ItemVenda
                DaoItemVenda daoItemVenda = new DaoItemVenda();
                for (ItemVenda itemVenda : itensVenda) {
                    // Associar o item à venda antes de incluir no banco de dados
                    itemVenda.setVenda(venda.getId());
                    // Incluir o item de venda no banco de dados
                    daoItemVenda.incluir(itemVenda);
                }

                // Atualizar a quantidade de livros após a inserção bem-sucedida
                atualizarQuantidadeLivros(venda.getId(), true);

                return venda;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para atualizar a quantidade de livros
    private void atualizarQuantidadeLivros(String vendaId, boolean decrementar) {
        // Obter a lista de itens associados à venda
        List<ItemVenda> itensVenda = obterItensVenda(vendaId);

        for (ItemVenda itemVenda : itensVenda) {
            try {
                String sqlString = "UPDATE produto SET qtde = qtde " + (decrementar ? "- 1" : "+ 1") + " WHERE id = ?";
                PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
                ps.setString(1, itemVenda.getLivro());
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private List<ItemVenda> obterItensVenda(String vendaId) {
        List<ItemVenda> itensVenda = new ArrayList<>();

        DataBaseCom.conectar();
        String sqlString = "SELECT id, id_produto FROM item_produto WHERE id_venda = ?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, vendaId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));
                itemVenda.setLivro(rs.getString("id_produto"));

                // Você precisará obter o livro associado a partir do ID do produto
                Livro livro = obterLivro(rs.getString("id_produto"));
                itemVenda.setLivro(livro.getId());

                // Adiciona o item associado à venda à lista
                itensVenda.add(itemVenda);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return itensVenda;
    }

    // Método para obter um livro pelo ID
    private Livro obterLivro(String idProduto) {
        Livro livro = new Livro();

        try {
            String sqlString = "SELECT nome, qtde, preco FROM produto WHERE id=?";
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

    // Método para alterar uma venda existente no banco de dados
    public boolean alterar(Venda venda) {
        DataBaseCom.conectar();

        if (findById(venda.getId()) == null)
            return false; // Retorna false se a venda não existir

        try {
            // Antes de fazer alterações, obtenha a lista de livros associados à venda atual
            List<String> livrosAntigos = obterLivrosDaVenda(venda.getId());

            // Atualize a venda
            String sqlString = "UPDATE venda SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, venda.getIdCliente());
            ps.setString(2, venda.getData());
            ps.setString(3, venda.getId());
            ps.execute();

            // Obtenha a lista de livros associados à venda após a alteração
            List<String> livrosNovos = obterLivrosDaVenda(venda.getId());

            // Calcule as diferenças entre as duas listas para determinar os livros
            // adicionados e removidos
            List<String> livrosRemovidos = new ArrayList<>(livrosAntigos);
            livrosRemovidos.removeAll(livrosNovos);

            List<String> livrosAdicionados = new ArrayList<>(livrosNovos);
            livrosAdicionados.removeAll(livrosAntigos);

            // Atualize a quantidade de livros adicionando/removendo conforme necessário
            atualizarQuantidadeLivros(venda.getId(), false);
            atualizarQuantidadeLivros(venda.getId(), true);

            return true; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para obter a lista de livros associados à venda
    private List<String> obterLivrosDaVenda(String vendaId) {
        List<String> livros = new ArrayList<>();

        try {
            String sqlString = "SELECT id_produto FROM item_produto WHERE id_venda=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, vendaId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                livros.add(rs.getString("id_produto"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return livros;
    }

    // Método para buscar uma venda pelo ID no banco de dados
    public Venda findById(String id) {
        DataBaseCom.conectar();
        Venda venda = null;

        try {
            String sqlString = "SELECT * FROM venda WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                venda = new Venda();
                venda.setId(rs.getString("id"));
                venda.setIdCliente(rs.getString("id_cliente"));
                venda.setData(rs.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return venda;
    }

    // Método para excluir uma venda do banco de dados
    public boolean excluir(Venda venda) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM venda WHERE id=?";

        try {
            // Obtenha a lista de livros associados à venda
            List<String> livros = obterLivrosDaVenda(venda.getId());

            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, venda.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Atualize a quantidade de livros
                atualizarQuantidadeLivros(venda.getId(), false);

                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as venda no banco de dados
    public List<Venda> listar() {
        List<Venda> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM venda");

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getString("id"));
                venda.setIdCliente(rs.getString("id_cliente"));
                venda.setData(rs.getString("data"));

                lista.add(venda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
