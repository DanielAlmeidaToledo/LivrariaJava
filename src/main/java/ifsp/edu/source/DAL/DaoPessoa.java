package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Pessoa;

@Component
public class DaoPessoa {

    // Método para incluir uma pessoa no banco de dados
    public Pessoa incluir(Pessoa pessoa) {
        try {
            DataBaseCom.conectar();
            String sqlString = "INSERT INTO pessoa (id, nome) VALUES (?, ?)";

            try (PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString)) {
                ps.setString(1, pessoa.getId());
                ps.setString(2, pessoa.getNome());

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0)
                    return pessoa;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } 

        return null; // Retorna null se falhar
    }

    // Método para alterar as informações de uma pessoa existente no banco de dados
    public boolean alterar(Pessoa pessoa) {
        try {
            DataBaseCom.conectar();

            if (findById(pessoa.getId()) == null)
                return false; // Retorna false se a pessoa não existir

            String sqlString = "UPDATE pessoa SET nome=? WHERE id=?";
            try (PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString)) {
                ps.setString(1, pessoa.getNome());
                ps.setString(2, pessoa.getId());
                ps.execute();

                return true; // Retorna true se a alteração for bem-sucedida
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para buscar uma pessoa pelo ID no banco de dados
    public Pessoa findById(String id) {
        try {
            DataBaseCom.conectar();
            Pessoa pessoa = null;

            String sqlQuery = "SELECT * FROM pessoa WHERE id = ?";
            try (PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlQuery)) {
                ps.setString(1, id);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        pessoa = new Pessoa();
                        pessoa.setId(rs.getString("id"));
                        pessoa.setNome(rs.getString("nome"));
                    }
                }
            }

            return pessoa; // Retorna a pessoa ou null se não encontrada
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para excluir uma pessoa pelo ID
    public boolean excluir(Pessoa pessoa) {
        try {
            DataBaseCom.conectar();
            String sqlString = "DELETE FROM pessoa WHERE id=?";

            try (PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString)) {
                ps.setString(1, pessoa.getId());
                ps.executeUpdate();

                return true; // Retorna true se a exclusão for bem-sucedida
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as pessoas no banco de dados
    public List<Pessoa> listar() {
        List<Pessoa> lista = new ArrayList<>();

        try {
            try (ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM pessoa")) {
                while (rs.next()) {
                    Pessoa pessoa = new Pessoa();
                    pessoa.setId(rs.getString("id"));
                    pessoa.setNome(rs.getString("nome"));
                    lista.add(pessoa);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista; // Retorna a lista de pessoas
    }
}
