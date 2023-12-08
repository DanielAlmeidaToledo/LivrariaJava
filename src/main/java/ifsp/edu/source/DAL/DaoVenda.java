package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Venda;
import ifsp.edu.source.Model.ItemProduto;

@Component
public class DaoVenda {
    // Método para incluir uma Venda no banco de dados
    public Venda incluir(Venda venda) {
        DataBaseCom.conectar();
        String sqlInserirVenda = "INSERT INTO venda (id, id_cliente, data) VALUES (?, ?, ?)";

        try {
            // Inserir a Venda
            PreparedStatement psInserirVenda = DataBaseCom.getConnection().prepareStatement(sqlInserirVenda);
            psInserirVenda.setString(1, venda.getId());
            psInserirVenda.setString(2, venda.getIdCliente());
            psInserirVenda.setString(3, venda.getData());

            int rowsAffectedVenda = psInserirVenda.executeUpdate();

            if (rowsAffectedVenda > 0) {
                // Obter a lista de itens associados à Venda
                List<ItemProduto> itensVenda = venda.getItens();

                // Inserir os itens associados à Venda chamando a controller de ItemProduto
                DaoItemVenda daoItemVenda = new DaoItemVenda();
                for (ItemProduto itemProduto : itensVenda) {

                    String sqlConsultaLivro = "SELECT * FROM produto WHERE id = ?";
                    PreparedStatement psConsultaLivro = DataBaseCom.getConnection().prepareStatement(sqlConsultaLivro);

                    psConsultaLivro.setString(1, itemProduto.getLivro());
                    ResultSet rs = psConsultaLivro.executeQuery();

                    // Associar o item à Venda antes de incluir no banco de dados
                    itemProduto.setVenda(venda.getId());
                    itemProduto.setNomeProduto(rs.getString("nome"));
                    itemProduto.setPreco(rs.getDouble("preco"));
                    
                    // Incluir o item de Venda no banco de dados
                    daoItemVenda.incluir(itemProduto);

                    String idLivro = itemProduto.getLivro();
                    int qtdeAtual =  rs.getInt("qtde") - itemProduto.getQuantidade();

                    atualizarQuantidadeLivros(idLivro, qtdeAtual);
                }
                return venda;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null; // Retorna null se falhar
    }

    // Método para atualizar a quantidade de livros
    private void atualizarQuantidadeLivros(String idLivro, int quantidade) {
    
        try {
            if (quantidade >= 0) {
                String sqlString = "UPDATE produto SET qtde = ? WHERE id = ?";
                PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
                ps.setInt(1, quantidade);
                ps.setString(2, idLivro);
                ps.executeUpdate();
            } else {
                // Lidere com a situação de quantidade insuficiente
                System.out.println("Quantidade insuficiente para o livro com ID: " + idLivro);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Método para obter a lista de itens associados à Venda
    private List<ItemProduto> obterItensVenda(String vendaId) {
        List<ItemProduto> itensVenda = new ArrayList<>();
    
        DataBaseCom.conectar();
        String sqlString = "SELECT i.id, i.id_produto, i.qtde, p.nome AS nome_produto, p.preco "
                         + "FROM item_produto i "
                         + "JOIN produto p ON i.id_produto = p.id "
                         + "WHERE i.id_venda = ?";
    
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, vendaId);
    
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                ItemProduto itemProduto = new ItemProduto();
                itemProduto.setId(rs.getString("id"));
                itemProduto.setLivro(rs.getString("id_produto"));
                itemProduto.setQuantidade(rs.getInt("qtde"));
                itemProduto.setNomeProduto(rs.getString("nome_produto"));
                itemProduto.setPreco(rs.getDouble("preco"));
                itemProduto.setVenda(vendaId);
    
                // Adiciona o item associado à Venda à lista
                itensVenda.add(itemProduto);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return itensVenda;
    }

    // Método para alterar uma Venda existente no banco de dados
    public boolean alterar(Venda venda) {
        DataBaseCom.conectar();
    
        if (findById(venda.getId()) == null)
            return false; // Retorna false se a Venda não existir
    
        try {
            // Antes de fazer alterações, obtenha a lista de livros associados à Venda atual
            List<String> livrosAntigos = obterLivrosDaVenda(venda.getId());
    
            // Atualize a Venda
            String sqlString = "UPDATE venda SET id_cliente=?, data=? WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
    
            ps.setString(1, venda.getIdCliente());
            ps.setString(2, venda.getData());
            ps.setString(3, venda.getId());
            int rowsAffectedVenda = ps.executeUpdate();
    
            if (rowsAffectedVenda > 0) {
                // Obter a lista de itens associados à Venda
                List<ItemProduto> itensVenda = venda.getItens();
    
                // Inserir os itens associados à Venda chamando a controller de ItemProduto
                DaoItemVenda daoItemVenda = new DaoItemVenda();
                for (ItemProduto itemProduto : itensVenda) {
                    String idLivro = itemProduto.getLivro();
    
                    // Consultar a quantidade atual do livro na tabela produto
                    int qtdeAtual = obterQuantidadeLivro(idLivro);
                    int quantidadeAlterada = itemProduto.getQuantidade() - obterQuantidadeLivroVenda(idLivro, venda.getId());

                    int quantidadeAtualizada = qtdeAtual - quantidadeAlterada;
    
                    // Atualizar a quantidade de livros
                    atualizarQuantidadeLivros(idLivro,  qtdeAtual - quantidadeAtualizada);

                    if(quantidadeAtualizada < 0) {
                        // excluir o item de venda
                        daoItemVenda.excluir(itemProduto);
                    } else {
                        // Atualizar ou incluir o item de venda
                        daoItemVenda.incluir(itemProduto);
                    }
                }
    
                // Remover livros que não estão mais na venda
                for (String livroAntigo : livrosAntigos) {
                    if (!itensVenda.contains(livroAntigo)) {

                        // Consultar a quantidade atual do livro na tabela produto
                        int qtdeAtual = obterQuantidadeLivro(livroAntigo);
    
                        // Atualizar a quantidade de livros
                        atualizarQuantidadeLivros(livroAntigo, qtdeAtual);
                    }
                }
    
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return false; // Retorna false se falhar
    }
    

    // Método para obter a lista de livros associados à Venda
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

    // Método para buscar uma Venda pelo ID no banco de dados
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
                venda.setItens(obterItensVenda(venda.getId()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return venda;
    }

    // Método para buscar a quantidade de livros na tabela produto pelo ID do livro
    private int obterQuantidadeLivro(String livroId) {
        DataBaseCom.conectar();
        String sqlString = "SELECT qtde FROM produto WHERE id = ?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, livroId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("qtde");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return 0; // Retorna 0 se falhar
    }

    private int obterQuantidadeLivroVenda(String livroId, String vendaId) {
        DataBaseCom.conectar();
        int quantidade = 0;
    
        String sqlString = "SELECT qtde FROM item_produto WHERE id_produto = ? AND id_venda = ?";
    
        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, livroId);
            ps.setString(2, vendaId);
    
            ResultSet rs = ps.executeQuery();
    
            if (rs.next()) {
                quantidade = rs.getInt("qtde");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    
        return quantidade;
    }
    

    // Método para excluir uma Venda do banco de dados
    public boolean excluir(Venda venda) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM venda WHERE id=?";

        try {
            // Obtenha a lista de livros associados à Venda
            // List<String> livros = obterLivrosDaVenda(venda.getId());

            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, venda.getId());

            // Realiza a contagem de quantidade de livros por produto
            for (ItemProduto itemProduto : venda.getItens()) {
                String sqlConsultaLivro = "SELECT * FROM produto WHERE id = ?";
                PreparedStatement psConsultaLivro = DataBaseCom.getConnection().prepareStatement(sqlConsultaLivro);

                psConsultaLivro.setString(1, itemProduto.getLivro());
                ResultSet rs = psConsultaLivro.executeQuery();

                String idLivro = itemProduto.getLivro();
                int qtdeAtual =  rs.getInt("qtde") + itemProduto.getQuantidade();

                atualizarQuantidadeLivros(idLivro, qtdeAtual);
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as Venda no banco de dados
    public List<Venda> listar() {
        List<Venda> lista = new ArrayList<>();

        try {
            ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM venda");

            while (rs.next()) {
                Venda venda = new Venda();
                venda.setId(rs.getString("id"));
                venda.setIdCliente(rs.getString("id_cliente"));
                venda.setData(rs.getString("data"));
                venda.setItens(obterItensVenda(venda.getId()));

                lista.add(venda);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
