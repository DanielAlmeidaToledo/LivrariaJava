package ifsp.edu.source.Model;

public class ItemCompra {
    private long id;
    private long idCompra;
    private long idProduto;
    private int quantidade;

    public ItemCompra() {
        // Construtor padrão
    }

    public ItemCompra(long idCompra, long idProduto, int quantidade) {
        this.idCompra = idCompra;
        this.idProduto = idProduto;
        this.quantidade = quantidade;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(long idCompra) {
        this.idCompra = idCompra;
    }

    public long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(long idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemCompra [id=" + id + ", idCompra=" + idCompra + ", idProduto=" + idProduto + ", quantidade=" + quantidade + "]";
    }
}
