package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.ItemCompra;

@Component
public class DaoItemCompra {

    // Método para incluir um ItemCompra no banco de dados
    public ItemCompra incluir(ItemCompra itemCompra) {
        DataBaseCom.conectar();
        String sqlInserirItemCompra = "INSERT INTO item_compra (id, id_produto, id_compra, qtde) VALUES (?, ?, ?, ?)";

        try {
            // Inserir o ItemCompra
            PreparedStatement psInserirItemCompra = DataBaseCom.getConnection().prepareStatement(sqlInserirItemCompra);
            psInserirItemCompra.setString(1, itemCompra.getId());
            psInserirItemCompra.setString(2, itemCompra.getLivro());
            psInserirItemCompra.setString(3, itemCompra.getCompra());
            psInserirItemCompra.setInt(4, itemCompra.getQuantidade());

            int rowsAffectedItemCompra = psInserirItemCompra.executeUpdate();

            if (rowsAffectedItemCompra > 0) {
                return itemCompra;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para alterar um ItemCompra existente no banco de dados
    public ItemCompra alterar(ItemCompra itemCompra) {
        DataBaseCom.conectar();

        if (findById(itemCompra.getId()) == null)
            return null; // Retorna null se o ItemCompra não existir

        try {
            // Atualize o ItemCompra
            String sqlString = "UPDATE item_compra SET id_produto=?, id_compra=?, qtde=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, itemCompra.getLivro());
            ps.setString(2, itemCompra.getCompra());
            ps.setInt(3, itemCompra.getQuantidade());
            ps.setString(4, itemCompra.getId());

            int rowsAffectedItemCompra = ps.executeUpdate();

            if (rowsAffectedItemCompra > 0) {
                return itemCompra;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Método para buscar um ItemCompra pelo ID no banco de dados
    public ItemCompra findById(String id) {
        DataBaseCom.conectar();
        ItemCompra itemCompra = null;

        try {
            String sqlString = "SELECT * FROM item_compra WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                itemCompra = new ItemCompra();
                itemCompra.setId(rs.getString("id"));
                itemCompra.setLivro(rs.getString("id_produto"));
                itemCompra.setCompra(rs.getString("id_compra"));
                itemCompra.setQuantidade(rs.getInt("qtde"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemCompra;
    }

    // Método para excluir um ItemCompra do banco de dados
    public boolean excluir(ItemCompra itemCompra) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_compra WHERE id=?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, itemCompra.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Retorna true se a exclusão for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todos os ItemCompra no banco de dados
    public List<ItemCompra> listar() {
        List<ItemCompra> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery(
                    "SELECT i.*, p.nome AS nomeProduto, p.preco " +
                            "FROM item_compra i " +
                            "INNER JOIN produto p ON i.id_produto = p.id");

            while (rs.next()) {
                ItemCompra itemCompra = new ItemCompra();
                itemCompra.setId(rs.getString("id"));
                itemCompra.setLivro(rs.getString("id_produto"));
                itemCompra.setCompra(rs.getString("id_compra"));
                itemCompra.setQuantidade(rs.getInt("qtde"));
                itemCompra.setNomeProduto(rs.getString("nomeProduto"));
                itemCompra.setPreco(rs.getDouble("preco"));

                lista.add(itemCompra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
