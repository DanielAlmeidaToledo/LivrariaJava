package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ifsp.edu.source.Model.ItemCompra;

public class DaoItensCompra {

    public boolean incluir(ItemCompra itemCompra) {
        DataBaseCom.conectar();

        String sqlString = "INSERT INTO item_compra (id_compra, id_produto, quantidade) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setLong(1, itemCompra.getIdCompra());
            ps.setLong(2, itemCompra.getIdProduto());
            ps.setInt(3, itemCompra.getQuantidade());

            return ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean alterar(ItemCompra itemCompra) {
        DataBaseCom.conectar();
        if (findById(itemCompra.getId()) == null) {
            return false;
        }
        try {
            String sqlString = "UPDATE item_compra SET id_compra=?, id_produto=?, quantidade=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setLong(1, itemCompra.getIdCompra());
            ps.setLong(2, itemCompra.getIdProduto());
            ps.setInt(3, itemCompra.getQuantidade());
            ps.setLong(4, itemCompra.getId());

            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public ItemCompra findById(long id) {
        DataBaseCom.conectar();
        ItemCompra itemCompra = null;
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM item_compra WHERE id=" + id);
            while (rs.next()) {
                itemCompra = new ItemCompra();
                itemCompra.setId(rs.getLong("id"));
                itemCompra.setIdCompra(rs.getLong("id_compra"));
                itemCompra.setIdProduto(rs.getLong("id_produto"));
                itemCompra.setQuantidade(rs.getInt("quantidade"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemCompra;
    }

    public boolean excluir(long id) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_compra WHERE id=" + id;
        try {
            DataBaseCom.getStatement().executeUpdate(sqlString);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<ItemCompra> listar() {
        List<ItemCompra> lista = new ArrayList<>();
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM item_compra");
            while (rs.next()) {
                ItemCompra itemCompra = new ItemCompra();
                itemCompra.setId(rs.getLong("id"));
                itemCompra.setIdCompra(rs.getLong("id_compra"));
                itemCompra.setIdProduto(rs.getLong("id_produto"));
                itemCompra.setQuantidade(rs.getInt("quantidade"));
                lista.add(itemCompra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
