package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class ItemProduto {
    private String id;
    private String idProduto; // Relação com Livro
    private String idCompra; // Relação com Compra
    private int quantidade;

    public ItemProduto() {
        this.id = GeradorID.getNextId().toString();
    }

    // Métodos getters e setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLivro() {
        return idProduto;
    }

    public void setLivro(String idProduto) {
        this.idProduto = idProduto;
    }

    public String getCompra() {
        return idCompra;
    }

    public void setCompra(String idCompra) {
        this.idCompra = idCompra;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemProduto [id=" + id + ", idProduto=" + idProduto + ", idCompra=" + idCompra + ", quantidade="
                + quantidade + "]";
    }
}
