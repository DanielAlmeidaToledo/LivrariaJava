package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Livro;

@Component
public class DaoLivro {

    // Método para incluir um livro no banco de dados
    public boolean incluir(Livro livro) {
        DataBaseCom.conectar();
        String sqlString = "insert into produto values(?,?,?,?)";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, livro.getId());
            ps.setString(2, livro.getNome());
            ps.setInt(3, livro.getQuantidade());
            ps.setDouble(4, livro.getPreco());

            return ps.execute(); // Retorna true se a inclusão for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para alterar as informações de um livro existente no banco de dados
    public boolean alterar(Livro livro) {
        DataBaseCom.conectar();

        String id = livro.getId();

        if (findById(id) == null)
            return false; // Retorna false se o livro não existir

        try {
            String sqlString = "UPDATE produto SET nome=?, qtde=?, preco=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, livro.getNome());
            ps.setInt(2, livro.getQuantidade());
            ps.setDouble(3, livro.getPreco());
            ps.setString(4, id);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para buscar um livro pelo ID no banco de dados
    public Livro findById(String id) {
        DataBaseCom.conectar();
        Livro livro = null;

        try {
            // Ajuste na query para usar PreparedStatement e evitar SQL injection
            String sql = "SELECT * FROM produto WHERE id = ?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sql);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                livro = new Livro();
                livro.setId(rs.getString("id"));
                livro.setNome(rs.getString("nome"));
                livro.setQuantidade(rs.getInt("qtde"));
                livro.setPreco(rs.getLong("preco"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return livro; // Retorna o livro ou null se não encontrado
    }

    // Método para excluir um livro pelo ID
    public boolean excluir(String id) {
        DataBaseCom.conectar();
        String sqlString = "delete from produto where id=?";

        try {
            // Retorna true se a exclusão for bem-sucedida
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);
            ps.executeUpdate();

            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    // Método para listar todos os livros no banco de dados
    public List<Livro> listar() {
        List<Livro> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("select * from produto");

            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getString("id"));
                livro.setNome(rs.getString("nome"));
                livro.setQuantidade(rs.getInt("qtde"));
                livro.setPreco(rs.getDouble("preco"));

                lista.add(livro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
