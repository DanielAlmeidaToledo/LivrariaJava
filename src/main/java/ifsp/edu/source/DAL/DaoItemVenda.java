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

    // Método para incluir um item de compra no banco de dados
    public boolean incluir(ItemVenda itemVenda) {
        DataBaseCom.conectar();
        String sqlString = "INSERT INTO item_produto (id_compra, id_produto, quantidade) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, itemVenda.getIdTransacao());
            ps.setString(2, itemVenda.getIdProduto());
            ps.setInt(3, itemVenda.getQuantidade());

            // Retorna true se a inclusão for bem-sucedida
            return ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // Método para alterar um item de compra existente no banco de dados
    public boolean alterar(ItemVenda itemVenda) {
        DataBaseCom.conectar();

        if (findById(itemVenda.getId()) == null)
            return false; // Retorna false se o item de compra não existir

        try {
            String sqlString = "UPDATE item_produto SET id_venda=?, id_produto=?, quantidade=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, itemVenda.getIdTransacao());
            ps.setString(2, itemVenda.getIdProduto());
            ps.setInt(3, itemVenda.getQuantidade());
            ps.setString(4, itemVenda.getId());

            return ps.execute(); // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não existir
    }

    // Método para buscar um item de compra pelo ID no banco de dados
    public ItemVenda findById(String id) {
        DataBaseCom.conectar();
        ItemVenda itemVenda = null;

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM item_produto WHERE id=" + id);

            while (rs.next()) {
                itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));
                itemVenda.setIdTransacao(rs.getString("id_transacao"));
                itemVenda.setIdProduto(rs.getString("id_produto"));
                itemVenda.setQuantidade(rs.getInt("quantidade"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemVenda;
    }

    // Método para excluir um item de compra pelo ID
    public boolean excluir(String id) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_compra WHERE id=" + id;

        try {
            // Retorna true se a exclusão for bem-sucedida
            DataBaseCom.getStatement().executeUpdate(sqlString);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não existir
    }

    // Método para listar todos os itens de compra no banco de dados
    public List<ItemVenda> listar() {
        List<ItemVenda> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM item_produto");

            while (rs.next()) {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));
                itemVenda.setIdTransacao(rs.getString("id_transacao"));
                itemVenda.setIdProduto(rs.getString("id_produto"));
                itemVenda.setQuantidade(rs.getInt("quantidade"));
                lista.add(itemVenda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
