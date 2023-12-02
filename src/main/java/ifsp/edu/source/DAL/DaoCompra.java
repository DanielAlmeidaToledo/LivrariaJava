package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ifsp.edu.source.Model.Compra;

public class DaoCompra {

    public boolean incluir(Compra compra) {
        DataBaseCom.conectar();

        String sqlString = "INSERT INTO compra (id_cliente, data) VALUES (?, ?)";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setLong(1, compra.getIdCliente());
            ps.setString(2, compra.getData());

            return ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean alterar(Compra compra) {
        DataBaseCom.conectar();
        if (findById(compra.getId()) == null) {
            return false;
        }
        try {
            String sqlString = "UPDATE compra SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setLong(1, compra.getIdCliente());
            ps.setString(2, compra.getData());
            ps.setLong(3, compra.getId());

            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Compra findById(long id) {
        DataBaseCom.conectar();
        Compra compra = null;
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM compra WHERE id=" + id);
            while (rs.next()) {
                compra = new Compra();
                compra.setId(rs.getLong("id"));
                compra.setIdCliente(rs.getLong("id_cliente"));
                compra.setData(rs.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return compra;
    }

    public boolean excluir(long id) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM compra WHERE id=" + id;
        try {
            DataBaseCom.getStatement().executeUpdate(sqlString);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM compra");
            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId(rs.getLong("id"));
                compra.setIdCliente(rs.getLong("id_cliente"));
                compra.setData(rs.getString("data"));
                lista.add(compra);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
