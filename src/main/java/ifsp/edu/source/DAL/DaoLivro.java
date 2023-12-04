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
            ps.setInt(1, livro.getId());
            ps.setString(2, livro.getNome());
            ps.setInt(3, livro.getQuantidade());
            ps.setDouble(4, livro.getPreco());

            // Retorna true se a inclusão for bem-sucedida
            boolean ri = ps.execute();
            return ri;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para alterar as informações de um livro existente no banco de dados
    public boolean alterar(Livro livro) {
        DataBaseCom.conectar();
        if (findById(livro.getId()) == null) {
            return false; // Retorna false se o livro não existir
        }
        try {
            String sqlString = "update produto set nome=? where id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, livro.getNome());
            ps.setInt(2, livro.getId());

            // Retorna true se a alteração for bem-sucedida
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para buscar um livro pelo ID no banco de dados
    public Livro findById(long id) {
        DataBaseCom.conectar();
        Livro livro = null;
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("select * from produto where id=" + id);
            while (rs.next()) {
                livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setNome(rs.getString("nome"));
                livro.setQuantidade(rs.getInt("qtde"));
                livro.setPreco(rs.getDouble("preco"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retorna o livro ou null se não encontrado
        return livro;
    }

    // Método para excluir um livro pelo ID
    public boolean excluir(Livro livro) {
        DataBaseCom.conectar();
        String sqlString = "delete from produto where id=" + livro.getId();
        try {
            // Retorna true se a exclusão for bem-sucedida
            DataBaseCom.getStatement().executeUpdate(sqlString);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método alternativo para excluir um livro pelo ID
    public boolean excluir(long id) {
        DataBaseCom.conectar();
        String sqlString = "delete from produto where id=" + id;
        try {
            // Retorna true se a exclusão for bem-sucedida
            DataBaseCom.getStatement().executeUpdate(sqlString);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para listar todos os livros no banco de dados
    public List<Livro> listar() {
        List<Livro> lista = new ArrayList<>();
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("select * from produto");
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getInt("id"));
                livro.setNome(rs.getString("nome"));
                livro.setQuantidade(rs.getInt("qtde"));
                livro.setPreco(rs.getDouble("preco"));
                lista.add(livro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retorna a lista de livros
        return lista;
    }
}
