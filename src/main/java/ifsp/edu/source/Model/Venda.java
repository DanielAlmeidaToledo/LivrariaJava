package ifsp.edu.source.Model;

import java.util.List;
import ifsp.edu.source.Util.GeradorID;

public class Venda extends Transacao {
    public Venda() {
        // Construtor padrão
    }

    public Venda(String idCliente, String data, List<ItemTransacao> itens) {
        super(idCliente, data, itens);
        this.id = GeradorID.getNextId().toString();
    }

    @Override
    public String toString() {
        return "Venda [id=" + id + ", idCliente=" + idCliente + ", data=" + data + ", itens=" + itens + "]";
    }
}
