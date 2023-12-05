package ifsp.edu.source.Model;

import java.util.List;
import java.util.ArrayList;

public class Venda extends Transacao {
    private List<ItemVenda> itensVenda; // Relação com ItemVenda

    public Venda() {
        this.itensVenda = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    // Métodos getters e setters

    public List<ItemVenda> getItensVenda() {
        return itensVenda;
    }

    public void adicionarItemVenda(ItemVenda itemVenda) {
        this.itensVenda.add(itemVenda);
    }

    @Override
    public String toString() {
        return "Venda [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itens=" + itens + ", itensVenda="
                + itensVenda + "]";
    }

}
