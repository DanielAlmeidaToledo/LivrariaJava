package ifsp.edu.source.DAL;

import java.util.List;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import ifsp.edu.source.Model.Venda;
import org.springframework.stereotype.Component;

@Component
public class DaoVenda {
    // Método para incluir uma venda no banco de dados
    public Venda incluir(Venda venda) {
        DataBaseCom.conectar();
        String sqlString = "INSERT INTO venda (id, id_cliente, data) VALUES (?, ?, ?)";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, venda.getId());
            ps.setString(2, venda.getIdCliente());
            ps.setString(3, venda.getData());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Obtenha a lista de livros associados à venda
                List<String> livros = obterLivrosDaVenda(venda.getId());

                // Atualize a quantidade de livros
                atualizarQuantidadeLivros(venda.getId(), livros, true);

                return venda;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para atualizar a quantidade de livros
    public void atualizarQuantidadeLivros(String vendaId, List<String> livros, boolean decrementar) {
        for (String livroId : livros) {
            try {
                String sqlString = "UPDATE produto SET qtde = qtde " + (decrementar ? "- 1" : "+ 1") + " WHERE id = ?";
                PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
                ps.setString(1, livroId);
                ps.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Método para alterar uma venda existente no banco de dados
    public boolean alterar(Venda venda) {
        DataBaseCom.conectar();

        if (findById(venda.getId()) == null)
            return false; // Retorna false se a venda não existir

        try {
            // Antes de fazer alterações, obtenha a lista de livros associados à venda atual
            List<String> livrosAntigos = obterLivrosDaVenda(venda.getId());

            // Atualize a venda
            String sqlString = "UPDATE venda SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);

            ps.setString(1, venda.getIdCliente());
            ps.setString(2, venda.getData());
            ps.setString(3, venda.getId());
            ps.execute();

            // Obtenha a lista de livros associados à venda após a alteração
            List<String> livrosNovos = obterLivrosDaVenda(venda.getId());

            // Calcule as diferenças entre as duas listas para determinar os livros
            // adicionados e removidos
            List<String> livrosRemovidos = new ArrayList<>(livrosAntigos);
            livrosRemovidos.removeAll(livrosNovos);

            List<String> livrosAdicionados = new ArrayList<>(livrosNovos);
            livrosAdicionados.removeAll(livrosAntigos);

            // Atualize a quantidade de livros adicionando/removendo conforme necessário
            atualizarQuantidadeLivros(venda.getId(), livrosRemovidos, false);
            atualizarQuantidadeLivros(venda.getId(), livrosAdicionados, true);

            return true; // Retorna true se a alteração for bem-sucedida
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para obter a lista de livros associados à venda
    private List<String> obterLivrosDaVenda(String vendaId) {
        List<String> livros = new ArrayList<>();

        try {
            String sqlString = "SELECT id_produto FROM item_produto WHERE id_venda=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, vendaId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                livros.add(rs.getString("id_produto"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return livros;
    }

    // Método para buscar uma venda pelo ID no banco de dados
    public Venda findById(String id) {
        DataBaseCom.conectar();
        Venda venda = null;

        try {
            String sqlString = "SELECT * FROM venda WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                venda = new Venda();
                venda.setId(rs.getString("id"));
                venda.setIdCliente(rs.getString("id_cliente"));
                venda.setData(rs.getString("data"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return venda;
    }

    // Método para excluir uma venda do banco de dados
    public boolean excluir(Venda venda) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM venda WHERE id=?";

        try {
            // Obtenha a lista de livros associados à venda
            List<String> livros = obterLivrosDaVenda(venda.getId());

            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, venda.getId());

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                // Atualize a quantidade de livros
                atualizarQuantidadeLivros(venda.getId(), livros, false);

                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as venda no banco de dados
    public List<Venda> listar() {
        List<Venda> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM venda");

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getString("id"));
                venda.setIdCliente(rs.getString("id_cliente"));
                venda.setData(rs.getString("data"));

                lista.add(venda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
