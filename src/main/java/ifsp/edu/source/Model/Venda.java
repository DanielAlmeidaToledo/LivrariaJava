package ifsp.edu.source.Model;

import java.util.List;

public class Venda extends Transacao {

    private List<ItemVenda> itens;

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "Venda [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itens=" + itens + "]";
    }
}