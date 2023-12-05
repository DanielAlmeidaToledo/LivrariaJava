package ifsp.edu.source.DAL;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import ifsp.edu.source.Model.ItemVenda;
import ifsp.edu.source.Model.Livro;
import ifsp.edu.source.Model.Venda;

import org.springframework.stereotype.Component;

@Component
public class DaoItemVenda {

    // Método para incluir um item de venda no banco de dados
    public ItemVenda incluir(ItemVenda itemVenda) {
        DataBaseCom.conectar();
        String sqlString = "INSERT INTO item_produto (id, id_venda, id_produto, qtde) VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, itemVenda.getId());
            ps.setString(2, itemVenda.getVenda());
            ps.setString(3, itemVenda.getLivro());
            ps.setInt(4, itemVenda.getQuantidade());

            if (ps.executeUpdate() > 0) {
                // Ao incluir, retorne o itemVenda com os dados atualizados
                return findById(itemVenda.getId());
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se a inclusão falhar
    }

    // Método para alterar um item de venda existente no banco de dados
    public boolean alterar(ItemVenda itemVenda) {
        DataBaseCom.conectar();

        if (findById(itemVenda.getId()) == null)
            return false; // Retorna false se o item de venda não existir

        try {
            String sqlString = "UPDATE item_produto SET id_venda=?, id_produto=?, qtde=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, itemVenda.getVenda());
            ps.setString(2, itemVenda.getLivro());
            ps.setInt(3, itemVenda.getQuantidade());
            ps.setString(4, itemVenda.getId());

            return ps.executeUpdate() > 0; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não existir
    }

    // Método para buscar um item de venda pelo ID no banco de dados
    public ItemVenda findById(String id) {
        DataBaseCom.conectar();
        ItemVenda itemVenda = null;

        try {
            String sqlString = "SELECT ip.id, ip.id_venda, ip.id_produto, l.nome as nome_livro, v.data as data_venda FROM item_produto ip "
                    +
                    "INNER JOIN produto l ON ip.id_produto = l.id " +
                    "INNER JOIN venda v ON ip.id_venda = v.id " +
                    "WHERE ip.id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));

                Livro livro = new Livro();
                livro.setId(rs.getString("id_produto"));
                livro.setNome(rs.getString("nome_livro"));
                itemVenda.setLivro(livro.getId());

                Venda venda = new Venda();
                venda.setId(rs.getString("id_venda"));
                venda.setData(rs.getString("data_venda"));
                itemVenda.setVenda(venda.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return itemVenda;
    }

    // Método para excluir um item de venda pelo ID
    public boolean excluir(ItemVenda itemVenda) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM item_produto WHERE id=?";

        try {
            // Retorna true se a exclusão for bem-sucedida
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, itemVenda.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se não existir
    }

    // Método para listar todos os itens de venda no banco de dados
    public List<ItemVenda> listar() {
        List<ItemVenda> lista = new ArrayList<>();

        try {
            String sqlString = "SELECT ip.id, ip.id_venda, ip.id_produto, l.nome as nome_livro, v.data as data_venda FROM item_produto ip "
                    +
                    "INNER JOIN produto l ON ip.id_produto = l.id " +
                    "INNER JOIN venda v ON ip.id_venda = v.id";
            ResultSet rs = DataBaseCom.getStatement().executeQuery(sqlString);

            while (rs.next()) {
                ItemVenda itemVenda = new ItemVenda();
                itemVenda.setId(rs.getString("id"));

                Livro livro = new Livro();
                livro.setId(rs.getString("id_produto"));
                livro.setNome(rs.getString("nome_livro"));
                itemVenda.setLivro(livro.getId());

                Venda venda = new Venda();
                venda.setId(rs.getString("id_venda"));
                venda.setData(rs.getString("data_venda"));
                itemVenda.setVenda(venda.getId());

                lista.add(itemVenda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
