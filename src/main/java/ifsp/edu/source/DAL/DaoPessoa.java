package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;
import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Pessoa;

@Component
public class DaoPessoa {

    // Método para incluir uma pessoa no banco de dados
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

    // Método para alterar as informações de uma pessoa existente no banco de dados
    public boolean alterar(Pessoa pessoa) {
        DataBaseCom.conectar();
        if (findById(pessoa.getId()) == null) {
            return false; // Retorna false se a pessoa não existir
        }
        try {
            String sqlString = "UPDATE pessoa SET nome=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, pessoa.getNome());
            ps.setLong(2, pessoa.getId());

            // Retorna true se a alteração for bem-sucedida
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para buscar uma pessoa pelo ID no banco de dados
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
        // Retorna a pessoa ou null se não encontrada
        return pessoa;
    }

    // Método para excluir uma pessoa pelo ID
    public boolean excluir(Pessoa pessoa) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM pessoa WHERE id=?";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setLong(1, pessoa.getId());
            ps.executeUpdate();
            // Retorna true se a exclusão for bem-sucedida
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para listar todas as pessoas no banco de dados
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
        // Retorna a lista de pessoas
        return lista;
    }
}
