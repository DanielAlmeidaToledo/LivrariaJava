package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.ItemProduto;

@Component
public class DaoItemVenda {

    // Método para incluir um ItemProduto no banco de dados
    public ItemProduto incluir(ItemProduto itemProduto) {
        DataBaseCom.conectar();
        String sqlInserirItemVenda = "INSERT INTO item_produto (id, id_produto, id_venda, qtde) VALUES (?, ?, ?, ?)";

        try {
            // Inserir o ItemProduto
            PreparedStatement psInserirItemVenda = DataBaseCom.getConnection().prepareStatement(sqlInserirItemVenda);
            psInserirItemVenda.setString(1, itemProduto.getId());
            psInserirItemVenda.setString(2, itemProduto.getLivro());
            psInserirItemVenda.setString(3, itemProduto.getVenda());
            psInserirItemVenda.setInt(4, itemProduto.getQuantidade());

            int rowsAffectedItemVenda = psInserirItemVenda.executeUpdate();

            if (rowsAffectedItemVenda > 0) {
                return itemProduto;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para alterar um ItemProduto existente no banco de dados
    public ItemProduto alterar(ItemProduto itemProduto) {
        DataBaseCom.conectar();

        if (findById(itemProduto.getId()) == null)
            return null; // Retorna null se o ItemProduto não existir

        try {
            // Atualize o ItemProduto
            String sqlString = "UPDATE item_produto SET id_produto=?, id_venda=?, qtde=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, itemProduto.getLivro());
            ps.setString(2, itemProduto.getVenda());
            ps.setInt(3, itemProduto.getQuantidade());
            ps.setString(4, itemProduto.getId());

            int rowsAffectedItemVenda = ps.executeUpdate();

            if (rowsAffectedItemVenda > 0) {
                return itemProduto;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Método para buscar um ItemProduto pelo ID no banco de dados
    public ItemProduto findById(String id) {
        DataBaseCom.conectar();
        ItemProduto itemProduto = null;

        try {
            String sqlString = "SELECT * FROM item_produto WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                itemProduto = new ItemProduto();
                itemProduto.setId(rs.getString("id"));
                itemProduto.setLivro(rs.getString("id_produto"));
                itemProduto.setVenda(rs.getString("id_venda"));
                itemProduto.setQuantidade(rs.getInt("qtde"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemProduto;
    }

    // Método para excluir um ItemProduto do banco de dados
    public boolean excluir(ItemProduto itemProduto) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_produto WHERE id=?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, itemProduto.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Retorna true se a exclusão for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todos os ItemProduto no banco de dados
    public List<ItemProduto> listar() {
        List<ItemProduto> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery(
                    "SELECT i.*, p.nome AS nomeProduto, p.preco " +
                            "FROM item_produto i " +
                            "INNER JOIN produto p ON i.id_produto = p.id");

            while (rs.next()) {
                ItemProduto itemProduto = new ItemProduto();
                itemProduto.setId(rs.getString("id"));
                itemProduto.setLivro(rs.getString("id_produto"));
                itemProduto.setVenda(rs.getString("id_venda"));
                itemProduto.setQuantidade(rs.getInt("qtde"));
                itemProduto.setNomeProduto(rs.getString("nomeProduto"));
                itemProduto.setPreco(rs.getDouble("preco"));

                lista.add(itemProduto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
