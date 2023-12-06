package ifsp.edu.source.Model;

import java.util.List;
import java.util.ArrayList;

public class Venda extends Transacao {
    private List<ItemProduto> itensVenda; // Relação com ItemProduto

    public Venda() {
        this.itensVenda = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    // Métodos getters e setters

    public List<ItemProduto> getItensVenda() {
        return itensVenda;
    }

    public void adicionarItemVenda(ItemProduto ItemProduto) {
        this.itensVenda.add(ItemProduto);
    }

    @Override
    public String toString() {
        return "Venda [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itens=" + itens + ", itensVenda="
                + itensVenda + "]";
    }

}
