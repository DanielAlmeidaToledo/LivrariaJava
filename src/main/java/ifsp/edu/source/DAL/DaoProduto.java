package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Produto;

@Component
public class DaoProduto {

    // Método para incluir um produto no banco de dados
    public boolean incluir(Produto produto) {
        DataBaseCom.conectar();

        String sqlString = "INSERT INTO produto (nome, qtde, preco) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setDouble(3, produto.getPreco());

            // Retorna true se a inclusão for bem-sucedida
            return ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para alterar as informações de um produto existente no banco de dados
    public boolean alterar(Produto produto) {
        DataBaseCom.conectar();
        if (findById(produto.getId()) == null) {
            return false; // Retorna false se o produto não existir
        }
        try {
            String sqlString = "UPDATE produto SET nome=?, qtde=?, preco=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setDouble(3, produto.getPreco());
            ps.setInt(4, produto.getId());

            // Retorna true se a alteração for bem-sucedida
            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        // Retorna false se falhar
        return false;
    }

    // Método para buscar um produto pelo ID no banco de dados
    public Produto findById(long id) {
        DataBaseCom.conectar();
        Produto produto = null;
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM produto WHERE id=" + id);
            while (rs.next()) {
                produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("qtde"));
                produto.setPreco(rs.getDouble("preco"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retorna o produto ou null se não encontrado
        return produto;
    }

    // Método para excluir um produto pelo ID
    public boolean excluir(long id) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM produto WHERE id=" + id;
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

    // Método para listar todos os produtos no banco de dados
    public List<Produto> listar() {
        List<Produto> lista = new ArrayList<>();
        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM produto");
            while (rs.next()) {
                Produto produto = new Produto();
                produto.setId(rs.getInt("id"));
                produto.setNome(rs.getString("nome"));
                produto.setQuantidade(rs.getInt("qtde"));
                produto.setPreco(rs.getDouble("preco"));
                lista.add(produto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Retorna a lista de produtos
        return lista;
    }
}
