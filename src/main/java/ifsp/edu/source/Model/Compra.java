package ifsp.edu.source.Model;

import java.util.ArrayList;
import java.util.List;

public class Compra extends Transacao {
    private List<ItemCompra> itemCompra;

    public Compra() {
        this.itemCompra = new ArrayList<>();
    }

    public List<ItemCompra> getItensCompra() {
        return itemCompra;
    }

    public void setItensCompra(List<ItemCompra> itemCompra) {
        this.itemCompra = itemCompra;
    }

    @Override
    public String toString() {
        return "Compra [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itemCompra=" + itemCompra + "]";
    }
}