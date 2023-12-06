package ifsp.edu.source.Model;

import java.util.List;
import java.util.ArrayList;

public class Compra extends Transacao {
    private List<ItemProduto> itensCompra; // Relação com ItemProduto

    public Compra() {
        this.itensCompra = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    // Métodos getters e setters

    public List<ItemProduto> getItensCompra() {
        return itensCompra;
    }

    public void adicionarItemCompra(ItemProduto ItemProduto) {
        this.itensCompra.add(ItemProduto);
    }

    @Override
    public String toString() {
        return "Compra [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itens=" + itens + ", itensCompra="
                + itensCompra + "]";
    }

}
