package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class ItemCompra {
    private String id;
    private String idCompra;
    private String idLivro;
    private int quantidade;
    private String nomeProduto;
    private double preco;

    public ItemCompra() {
        this.id = GeradorID.getNextId().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompra() {
        return idCompra;
    }

    public void setCompra(String compra) {
        this.idCompra = compra;
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
        return "ItemCompra [id=" + id + ", idCompra=" + idCompra + ", idLivro=" + idLivro + ", quantidade=" + quantidade
                + "]";
    }
}
