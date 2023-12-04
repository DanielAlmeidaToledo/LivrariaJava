package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;
import java.util.List;

public abstract class Transacao {
    protected String id;
    protected String idCliente;
    protected String data;
    protected List<ItemTransacao> itens;

    public Transacao() {
        this.id = GeradorID.getNextId();
    }

    public Transacao(String idCliente, String data, List<ItemTransacao> itens) {
        this.id = GeradorID.getNextId();
        this.idCliente = idCliente;
        this.data = data;
        this.itens = itens;
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public List<ItemTransacao> getItens() {
        return itens;
    }

    public void setItens(List<ItemTransacao> itens) {
        this.itens = itens;
    }
}
