package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;
import ifsp.edu.source.Model.Compra;

@Component
public class DaoCompra {

    // Método para incluir uma compra no banco de dados
    public Compra incluir(Compra compra) {
        DataBaseCom.conectar();
        String sqlString = "INSERT INTO compra (id, id_cliente, data) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compra.getId());
            ps.setString(2, compra.getIdCliente());

            // Verifica se a data foi fornecida na requisição
            if (compra.getData() == null || compra.getData().isEmpty()) {
                // Se não foi fornecida, preenche com a data atual
                LocalDate dataAtual = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                ps.setString(3, dataAtual.format(formatter));
            } else {
                // Se foi fornecida, usa a data fornecida
                ps.setString(3, compra.getData());
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0)
                return compra;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para alterar uma compra existente no banco de dados
    public boolean alterar(Compra compra) {
        DataBaseCom.conectar();

        if (findById(compra.getId()) == null)
            return false; // Retorna false se a compra não existir

        try {
            String sqlString = "UPDATE compra SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, compra.getIdCliente());
            ps.setString(2, compra.getData());
            ps.setString(3, compra.getId());
            ps.execute();

            return true; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para buscar uma compra pelo ID no banco de dados
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return compra;
    }

    // Método para excluir uma compra do banco de dados
    public boolean excluir(Compra compra) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM compra WHERE id=?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compra.getId());

            return ps.executeUpdate() > 0; // Retorna true se a exclusão for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as compras no banco de dados
    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM compra");

            while (rs.next()) {
                Compra compra = new Compra();
                compra.setId(rs.getString("id"));
                compra.setIdCliente(rs.getString("id_cliente"));
                compra.setData(rs.getString("data"));

                lista.add(compra);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
