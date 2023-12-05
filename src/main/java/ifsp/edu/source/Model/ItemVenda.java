package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class ItemVenda {
    private String id;
    private String idProduto; // Relação com Livro
    private String idVenda; // Relação com Venda
    private int quantidade;

    public ItemVenda() {
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

    public String getVenda() {
        return idVenda;
    }

    public void setVenda(String idVenda) {
        this.idVenda = idVenda;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemVenda [id=" + id + ", idProduto=" + idProduto + ", idVenda=" + idVenda + ", quantidade="
                + quantidade + "]";
    }
}
