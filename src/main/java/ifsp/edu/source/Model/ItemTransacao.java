package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class ItemTransacao {
    protected String id;
    protected String idTransacao;
    protected String idProduto;
    protected int quantidade;

    public ItemTransacao() {
        this.id = GeradorID.getNextId().toString();
    }

    public ItemTransacao(String idTransacao, String idProduto, int quantidade) {
        this.idTransacao = idTransacao;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdTransacao() {
        return idTransacao;
    }

    public void setIdTransacao(String idTransacao) {
        this.idTransacao = idTransacao;
    }

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
