package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.ItemProduto;

@Component
public class DaoItemCompra {

    // Método para incluir um ItemProduto no banco de dados
    public ItemProduto incluir(ItemProduto itemProduto) {
        System.out.println(">>> DaoItemCompra.incluir()");
        System.out.println(">>> itemProduto: " + itemProduto);

        DataBaseCom.conectar();
        String sqlInserirItemCompra = "INSERT INTO item_produto (id, id_produto, id_compra, qtde) VALUES (?, ?, ?, ?)";

        try {
            // Inserir o ItemProduto
            PreparedStatement psInserirItemCompra = DataBaseCom.getConnection().prepareStatement(sqlInserirItemCompra);
            psInserirItemCompra.setString(1, itemProduto.getId());
            psInserirItemCompra.setString(2, itemProduto.getLivro());
            psInserirItemCompra.setString(3, itemProduto.getCompra());
            psInserirItemCompra.setInt(4, itemProduto.getQuantidade());

            System.out.println(">>> itemProduto.getLivro(): " + itemProduto.getLivro());

            int rowsAffectedItemCompra = psInserirItemCompra.executeUpdate();

            if (rowsAffectedItemCompra > 0) {
                return itemProduto;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para alterar um ItemProduto existente no banco de dados
    public boolean alterar(ItemProduto itemProduto) {
        DataBaseCom.conectar();

        if (findById(itemProduto.getId()) == null)
            return false; // Retorna false se o ItemProduto não existir

        try {
            // Atualize o ItemProduto
            String sqlString = "UPDATE item_produto SET id_produto=?, id_compra=?, quantidade=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, itemProduto.getLivro());
            ps.setString(2, itemProduto.getCompra());
            ps.setInt(3, itemProduto.getQuantidade());
            ps.setString(4, itemProduto.getId());
            ps.execute();

            return true; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
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
                itemProduto.setCompra(rs.getString("id_compra"));
                itemProduto.setQuantidade(rs.getInt("quantidade"));
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
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM item_produto");

            while (rs.next()) {
                ItemProduto itemProduto = new ItemProduto();
                itemProduto.setId(rs.getString("id"));
                itemProduto.setLivro(rs.getString("id_produto"));
                itemProduto.setCompra(rs.getString("id_compra"));
                itemProduto.setQuantidade(rs.getInt("quantidade"));

                lista.add(itemProduto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
