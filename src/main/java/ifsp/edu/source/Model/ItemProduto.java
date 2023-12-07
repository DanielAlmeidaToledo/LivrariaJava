package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class ItemProduto {
    private String id;
    private String idVenda;
    private String idLivro;
    private int quantidade;
    private String nomeProduto;
    private double preco;

    public ItemProduto() {
        this.id = GeradorID.getNextId().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVenda() {
        return idVenda;
    }

    public void setVenda(String venda) {
        this.idVenda = venda;
    }

    public String getLivro() {
        return idLivro;
    }

    public void setLivro(String livro) {
        this.idLivro = livro;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemProduto [id=" + id + ", venda=" + idVenda + ", livro=" + idLivro + ", quantidade=" + quantidade + "]";
    }
}
