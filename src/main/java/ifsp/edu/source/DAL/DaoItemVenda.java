package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.ItemVenda;

@Component
public class DaoItemVenda {

    // Método para incluir um ItemVenda no banco de dados
    public ItemVenda incluir(ItemVenda itemVenda) {
        DataBaseCom.conectar();
        String sqlInserirItemVenda = "INSERT INTO item_venda (id, id_produto, id_venda, qtde) VALUES (?, ?, ?, ?)";

        try {
            // Inserir o ItemVenda
            PreparedStatement psInserirItemVenda = DataBaseCom.getConnection().prepareStatement(sqlInserirItemVenda);
            psInserirItemVenda.setString(1, itemVenda.getId());
            psInserirItemVenda.setString(2, itemVenda.getLivro());
            psInserirItemVenda.setString(3, itemVenda.getVenda());
            psInserirItemVenda.setInt(4, itemVenda.getQuantidade());

            int rowsAffectedItemVenda = psInserirItemVenda.executeUpdate();

            if (rowsAffectedItemVenda > 0) {
                return itemVenda;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para alterar um ItemVenda existente no banco de dados
    public ItemVenda alterar(ItemVenda itemVenda) {
        DataBaseCom.conectar();

        if (findById(itemVenda.getId()) == null)
            return null; // Retorna null se o ItemVenda não existir

        try {
            // Atualize o ItemVenda
            String sqlString = "UPDATE item_venda SET id_produto=?, id_venda=?, qtde=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, itemVenda.getLivro());
            ps.setString(2, itemVenda.getVenda());
            ps.setInt(3, itemVenda.getQuantidade());
            ps.setString(4, itemVenda.getId());

            int rowsAffectedItemVenda = ps.executeUpdate();

            if (rowsAffectedItemVenda > 0) {
                return itemVenda;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Método para buscar um ItemVenda pelo ID no banco de dados
    public ItemVenda findById(String id) {
        DataBaseCom.conectar();
        ItemVenda itemVenda = null;

        try {
            String sqlString = "SELECT * FROM item_venda WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));
                itemVenda.setLivro(rs.getString("id_produto"));
                itemVenda.setVenda(rs.getString("id_venda"));
                itemVenda.setQuantidade(rs.getInt("qtde"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemVenda;
    }

    // Método para excluir um ItemVenda do banco de dados
    public boolean excluir(ItemVenda itemVenda) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_venda WHERE id=?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, itemVenda.getId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0; // Retorna true se a exclusão for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todos os ItemVenda no banco de dados
    public List<ItemVenda> listar() {
        List<ItemVenda> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery(
                    "SELECT i.*, p.nome AS nomeProduto, p.preco " +
                            "FROM item_venda i " +
                            "INNER JOIN produto p ON i.id_produto = p.id");

            while (rs.next()) {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));
                itemVenda.setLivro(rs.getString("id_produto"));
                itemVenda.setVenda(rs.getString("id_venda"));
                itemVenda.setQuantidade(rs.getInt("qtde"));
                itemVenda.setNomeProduto(rs.getString("nomeProduto"));
                itemVenda.setPreco(rs.getDouble("preco"));

                lista.add(itemVenda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
