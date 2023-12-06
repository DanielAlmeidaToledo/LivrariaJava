package ifsp.edu.source.Model;

import java.util.List;

public class Compra extends Transacao {

    private List<ItemProduto> itens;

    public List<ItemProduto> getItens() {
        return itens;
    }

    public void setItens(List<ItemProduto> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Compra [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itens=" + itens + "]";
    }
}