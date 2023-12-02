package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ifsp.edu.source.Model.Produto;

public class DaoProduto {

    public boolean incluir(Produto produto) {
        DataBaseCom.conectar();

        String sqlString = "INSERT INTO produto (nome, qtde, preco) VALUES (?, ?, ?)";
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setDouble(3, produto.getPreco());

            return ps.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean alterar(Produto produto) {
        DataBaseCom.conectar();
        if (findById(produto.getId()) == null) {
            return false;
        }
        try {
            String sqlString = "UPDATE produto SET nome=?, qtde=?, preco=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, produto.getNome());
            ps.setInt(2, produto.getQuantidade());
            ps.setDouble(3, produto.getPreco());
            ps.setInt(4, produto.getId());

            ps.execute();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

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
        return produto;
    }

    public boolean excluir(long id) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM produto WHERE id=" + id;
        try {
            DataBaseCom.getStatement().executeUpdate(sqlString);
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

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
        return lista;
    }
}

