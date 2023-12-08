package ifsp.edu.source.Model;

import java.util.ArrayList;
import java.util.List;

public class Venda extends Transacao {
    private List<ItemVenda> itemVenda;

    public Venda() {
        this.itemVenda = new ArrayList<>();
    }

    public List<ItemVenda> getItensVenda() {
        return itemVenda;
    }

    public void setItensVenda(List<ItemVenda> itemVenda) {
        this.itemVenda = itemVenda;
    }

    @Override
    public String toString() {
        return "Venda [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itemVenda=" + itemVenda + "]";
    }
}