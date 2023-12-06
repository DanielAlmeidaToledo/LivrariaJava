package ifsp.edu.source.Model;

public class ItemProduto {
    private String id;
    private String compra;
    private String livro;
    private int quantidade;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompra() {
        return compra;
    }

    public void setCompra(String compra) {
        this.compra = compra;
    }

    public String getLivro() {
        return livro;
    }

    public void setLivro(String livro) {
        this.livro = livro;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    @Override
    public String toString() {
        return "ItemProduto [id=" + id + ", compra=" + compra + ", livro=" + livro + ", quantidade=" + quantidade + "]";
    }
}
