package ifsp.edu.source.DAL;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import ifsp.edu.source.Model.Compra;
import ifsp.edu.source.Model.ItemCompra;
import ifsp.edu.source.Model.Livro;

@Component
public class DaoCompra {
    // Método para incluir uma Compra no banco de dados
    public Compra incluir(Compra compra) {
        try {
            DataBaseCom.conectar();
            String sqlInserirCompra = "INSERT INTO compra (id, id_cliente, data) VALUES (?, ?, ?)";

            try (PreparedStatement psInserirCompra = DataBaseCom.getConnection().prepareStatement(sqlInserirCompra)) {
                psInserirCompra.setString(1, compra.getId());
                psInserirCompra.setString(2, compra.getIdCliente());
                psInserirCompra.setString(3, compra.getData());

                int rowsAffectedCompra = psInserirCompra.executeUpdate();

                if (rowsAffectedCompra > 0) {
                    List<ItemCompra> itensCompra = compra.getItensCompra();
                    DaoItemCompra daoItemCompra = new DaoItemCompra();

                    for (ItemCompra itemCompra : itensCompra) {
                        String sqlConsultaLivro = "SELECT * FROM produto WHERE id = ?";
                        try (PreparedStatement psConsultaLivro = DataBaseCom.getConnection()
                                .prepareStatement(sqlConsultaLivro)) {
                            psConsultaLivro.setString(1, itemCompra.getLivro());

                            try (ResultSet rs = psConsultaLivro.executeQuery()) {
                                itemCompra.setCompra(compra.getId());
                                itemCompra.setNomeProduto(rs.getString("nome"));
                                itemCompra.setPreco(rs.getDouble("preco"));

                                daoItemCompra.incluir(itemCompra);

                                String idLivro = itemCompra.getLivro();
                                int qtdeAtual = rs.getInt("qtde") + itemCompra.getQuantidade();

                                atualizarQuantidadeLivros(idLivro, qtdeAtual);
                            }
                        }
                    }
                    return compra;
                }
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

    // Método para obter a lista de itens associados à Compra
    private List<ItemCompra> obterItensCompra(String compraId) {
        List<ItemCompra> itensCompra = new ArrayList<>();

        DataBaseCom.conectar();
        String sqlString = "SELECT i.id, i.id_produto, i.qtde, p.nome, p.preco "
                + "FROM item_compra i "
                + "JOIN produto p ON i.id_produto = p.id "
                + "WHERE i.id_compra = ?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compraId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ItemCompra itemCompra = new ItemCompra();
                itemCompra.setId(rs.getString("id"));
                itemCompra.setLivro(rs.getString("id_produto"));
                itemCompra.setQuantidade(rs.getInt("qtde"));
                itemCompra.setNomeProduto(rs.getString("nome"));
                itemCompra.setPreco(rs.getDouble("preco"));
                itemCompra.setCompra(compraId);

                // Adiciona o item associado à Compra à lista
                itensCompra.add(itemCompra);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return itensCompra;
    }

    // Método para alterar uma Compra existente no banco de dados
    public boolean alterar(Compra compra) {
        DataBaseCom.conectar();

        if (findById(compra.getId()) == null)
            return false; // Retorna false se a Compra não existir

        try {
            // Atualiza a Compra
            String sqlString = "UPDATE compra SET id_cliente=?, data=? WHERE id=?";
            try (PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString)) {
                ps.setString(1, compra.getIdCliente());
                ps.setString(2, compra.getData());
                ps.setString(3, compra.getId());

                int rowsAffectedCompra = ps.executeUpdate();

                if (rowsAffectedCompra > 0) {
                    // Remove todos os itens associados à Compra
                    String sqlRemoverItens = "DELETE FROM item_compra WHERE id_compra=?";
                    try (PreparedStatement psRemoverItens = DataBaseCom.getConnection()
                            .prepareStatement(sqlRemoverItens)) {
                        psRemoverItens.setString(1, compra.getId());
                        psRemoverItens.executeUpdate();
                    }

                    // Adiciona os novos itens associados à Compra
                    for (ItemCompra itemCompra : compra.getItensCompra()) {
                        // Se o nome e o preço forem fornecidos no body, atualiza; caso contrário,
                        // mantém os dados existentes
                        if (itemCompra.getNomeProduto() == null || itemCompra.getPreco() == 0) {
                            // Consulta as informações atuais do produto no banco de dados
                            Livro livroAtual = obterLivroAtual(itemCompra.getLivro());

                            if (livroAtual != null) {
                                // Mantém o nome e o preço do produto existente
                                itemCompra.setNomeProduto(livroAtual.getNome());
                                itemCompra.setPreco(livroAtual.getPreco());
                            }
                        }

                        // Consulta a quantidade atual do livro na tabela produto
                        int qtdeAtual = obterQuantidadeLivro(itemCompra.getLivro());

                        // Atualiza a quantidade de livros
                        int novaQuantidade = itemCompra.getQuantidade();

                        // Consulta a quantidade atual do livro na tabela item_compra
                        int qtdeAtualLivroCompra = obterQuantidadeLivroCompra(itemCompra.getLivro(), compra.getId());
                        int quantidadeAtualizada;

                        if (novaQuantidade < 0)
                            continue;

                        // Calcula a diferença entre a nova quantidade e a quantidade anterior
                        Integer diferencaQuantidade = novaQuantidade - qtdeAtualLivroCompra;

                        // Atualiza a quantidade de livros no estoque
                        if (novaQuantidade > qtdeAtualLivroCompra)
                            quantidadeAtualizada = qtdeAtual - diferencaQuantidade;
                        else
                            quantidadeAtualizada = qtdeAtual + diferencaQuantidade;

                        if (quantidadeAtualizada < 0)
                            continue;

                        // Adiciona os novos itens associados à Compra
                        incluirItemCompra(itemCompra, compra.getId());

                        // Atualiza a quantidade de livros após adicionar à compra
                        atualizarQuantidadeLivros(itemCompra.getLivro(), quantidadeAtualizada);
                    }

                    return true;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para obter as informações atuais do livro no banco de dados
    private Livro obterLivroAtual(String livroId) {
        String sqlString = "SELECT * FROM produto WHERE id = ?";
        Livro livro = null;

        try (PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString)) {
            ps.setString(1, livroId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    livro = new Livro();
                    livro.setId(rs.getString("id"));
                    livro.setNome(rs.getString("nome"));
                    livro.setQuantidade(rs.getInt("qtde"));
                    livro.setPreco(rs.getDouble("preco"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return livro;
    }

    // Método para incluir um item de compra no banco de dados
    private void incluirItemCompra(ItemCompra itemCompra, String compraId) {
        String sqlInserirItem = "INSERT INTO item_compra (id, id_compra, id_produto, qtde) VALUES (?, ?, ?, ?)";

        try (PreparedStatement psInserirItem = DataBaseCom.getConnection().prepareStatement(sqlInserirItem)) {
            psInserirItem.setString(1, itemCompra.getId());
            psInserirItem.setString(2, compraId);
            psInserirItem.setString(3, itemCompra.getLivro());
            psInserirItem.setInt(4, itemCompra.getQuantidade());
            psInserirItem.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Método para buscar uma Compra pelo ID no banco de dados
    public Compra findById(String id) {
        DataBaseCom.conectar();
        Compra compra = null;

        try {
            String sqlString = "SELECT * FROM compra WHERE id=?";
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                compra = new Compra();
                compra.setId(rs.getString("id"));
                compra.setIdCliente(rs.getString("id_cliente"));
                compra.setData(rs.getString("data"));
                compra.setItensCompra(obterItensCompra(compra.getId()));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return compra;
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

    private int obterQuantidadeLivroCompra(String livroId, String compraId) {
        DataBaseCom.conectar();
        int quantidade = 0;

        String sqlString = "SELECT qtde FROM item_compra WHERE id_produto = ? AND id_compra = ?";

        try {
            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, livroId);
            ps.setString(2, compraId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                quantidade = rs.getInt("qtde");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return quantidade;
    }

    // Método para excluir uma Compra do banco de dados
    public boolean excluir(Compra compra) {
        DataBaseCom.conectar();
        String sqlString = "DELETE FROM compra WHERE id=?";

        try {
            // Obtenha a lista de livros associados à Compra

            PreparedStatement ps = DataBaseCom.getConnection().prepareStatement(sqlString);
            ps.setString(1, compra.getId());

            // Realiza a contagem de quantidade de livros por produto
            for (ItemCompra itemCompra : compra.getItensCompra()) {
                String sqlConsultaLivro = "SELECT * FROM produto WHERE id = ?";
                PreparedStatement psConsultaLivro = DataBaseCom.getConnection().prepareStatement(sqlConsultaLivro);

                psConsultaLivro.setString(1, itemCompra.getLivro());
                ResultSet rs = psConsultaLivro.executeQuery();

                String idLivro = itemCompra.getLivro();
                int qtdeAtual = rs.getInt("qtde") - itemCompra.getQuantidade();

                atualizarQuantidadeLivros(idLivro, qtdeAtual);
            }

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {

                // Remove todos os itens associados à Compra
                String sqlRemoverItens = "DELETE FROM item_compra WHERE id_compra=?";
                PreparedStatement psRemoverItens = DataBaseCom.getConnection().prepareStatement(sqlRemoverItens);
                psRemoverItens.setString(1, compra.getId());
                psRemoverItens.executeUpdate();

                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false; // Retorna false se falhar
    }

    // Método para listar todas as Compra no banco de dados
    public List<Compra> listar() {
        List<Compra> lista = new ArrayList<>();

        try {
            try (ResultSet rs = DataBaseCom.getStatement().executeQuery("SELECT * FROM compra")) {
                while (rs.next()) {
                    Compra compra = new Compra();
                    compra.setId(rs.getString("id"));
                    compra.setIdCliente(rs.getString("id_cliente"));
                    compra.setData(rs.getString("data"));
                    compra.setItensCompra(obterItensCompra(compra.getId()));

                    lista.add(compra);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return lista;
    }
}
