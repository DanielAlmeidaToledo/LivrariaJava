package ifsp.edu.source.DAL;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import ifsp.edu.source.Model.ItemProduto;
import ifsp.edu.source.Model.Livro;
import ifsp.edu.source.Model.Compra;

import org.springframework.stereotype.Component;

@Component
public class DaoItemCompra {

    // Método para incluir um item de Compra no banco de dados
    public ItemProduto incluir(ItemProduto ItemProduto) {
        DataBaseCom.conectar();
        String sqlString = "INSERT INTO item_produto (id, id_compra, id_produto, qtde) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, ItemProduto.getId());
            ps.setString(2, ItemProduto.getCompra());
            ps.setString(3, ItemProduto.getLivro());
            ps.setInt(4, ItemProduto.getQuantidade());

            if (ps.executeUpdate() > 0) {
                // Ao incluir, retorne o ItemProduto com os dados atualizados
                return findById(ItemProduto.getId());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se a inclusão falhar
    }

    // Método para alterar um item de Compra existente no banco de dados
    public boolean alterar(ItemProduto ItemProduto) {
        DataBaseCom.conectar();

        if (findById(ItemProduto.getId()) == null)
            return false; // Retorna false se o item de Compra não existir

        try {
            String sqlString = "UPDATE item_produto SET id_compra=?, id_produto=?, qtde=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, ItemProduto.getCompra());
            ps.setString(2, ItemProduto.getLivro());
            ps.setInt(3, ItemProduto.getQuantidade());
            ps.setString(4, ItemProduto.getId());

            return ps.executeUpdate() > 0; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não existir
    }

    // Método para buscar um item de Compra pelo ID no banco de dados
    public ItemProduto findById(String id) {
        DataBaseCom.conectar();
        ItemProduto ItemProduto = null;

        try {
            String sqlString = "SELECT ip.id, ip.id_compra, ip.id_produto, l.nome as nome_livro, v.data as data_compra FROM item_produto ip "
                    +
                    "INNER JOIN produto l ON ip.id_produto = l.id " +
                    "INNER JOIN Compra v ON ip.id_compra = v.id " +
                    "WHERE ip.id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemProduto = new ItemProduto();
                ItemProduto.setId(rs.getString("id"));

                Livro livro = new Livro();
                livro.setId(rs.getString("id_produto"));
                livro.setNome(rs.getString("nome_livro"));
                ItemProduto.setLivro(livro.getId());

                Compra Compra = new Compra();
                Compra.setId(rs.getString("id_compra"));
                Compra.setData(rs.getString("data_compra"));
                ItemProduto.setCompra(Compra.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ItemProduto;
    }

    // Método para excluir um item de Compra pelo ID
    public boolean excluir(ItemProduto ItemProduto) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_produto WHERE id=?";

        try {
            // Retorna true se a exclusão for bem-sucedida
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, ItemProduto.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não existir
    }

    // Método para listar todos os itens de Compra no banco de dados
    public List<ItemProduto> listar() {
        List<ItemProduto> lista = new ArrayList<>();

        try {
            String sqlString = "SELECT ip.id, ip.id_compra, ip.id_produto, l.nome as nome_livro, v.data as data_compra FROM item_produto ip "
                    +
                    "INNER JOIN produto l ON ip.id_produto = l.id " +
                    "INNER JOIN Compra v ON ip.id_compra = v.id";
            ResultSet rs = DataBaseCom.getStatement().executeQuery(sqlString);

            while (rs.next()) {
                ItemProduto ItemProduto = new ItemProduto();
                ItemProduto.setId(rs.getString("id"));

                Livro livro = new Livro();
                livro.setId(rs.getString("id_produto"));
                livro.setNome(rs.getString("nome_livro"));
                ItemProduto.setLivro(livro.getId());

                Compra Compra = new Compra();
                Compra.setId(rs.getString("id_compra"));
                Compra.setData(rs.getString("data_compra"));
                ItemProduto.setCompra(Compra.getId());

                lista.add(ItemProduto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
