package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import ifsp.edu.source.Model.Pessoa;

public class DaoPessoa {

    public Pessoa incluir(Pessoa pessoa) {
        DataBaseCom.conectar();
        String sqlString = "INSERT INTO pessoa (nome) VALUES (?)";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, pessoa.getNome());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    long id = generatedKeys.getLong(1);
                    pessoa.setId(id);
                    return pessoa;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;  // Retorna null se falhar
    }

    public boolean alterar(Pessoa pessoa) {
        DataBaseCom.conectar();
        if (findById(pessoa.getId()) == null) {
            return false;
        }
        try {
            String sqlString = "UPDATE pessoa SET nome=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, pessoa.getNome());
            ps.setLong(2, pessoa.getId());

            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public Pessoa findById(long id) {
        DataBaseCom.conectar();
        Pessoa pessoa = null;
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM pessoa WHERE id=" + id);
            while (rs.next()) {
                pessoa = new Pessoa();
                pessoa.setId(rs.getLong("id"));
                pessoa.setNome(rs.getString("nome"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pessoa;
    }

    public boolean excluir(Pessoa pessoa) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM pessoa WHERE id=?";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setLong(1, pessoa.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<Pessoa> listar() {
        List<Pessoa> lista = new ArrayList<>();
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM pessoa");
            while (rs.next()) {
                Pessoa pessoa = new Pessoa();
                pessoa.setId(rs.getLong("id"));
                pessoa.setNome(rs.getString("nome"));
                lista.add(pessoa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}
