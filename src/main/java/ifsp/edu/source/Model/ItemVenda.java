package ifsp.edu.source.Model;

public class ItemVenda extends ItemTransacao {
    public ItemVenda() {
        // Construtor padrão
    }

    public ItemVenda(String idTransacao, String idProduto, int quantidade) {
        super(idTransacao, idProduto, quantidade);
    }

    @Override
    public String toString() {
        return "ItemVenda [id=" + id + ", idTransacao=" + idTransacao + ", idProduto=" + idProduto + ", quantidade="
                + quantidade + "]";
    }
}
