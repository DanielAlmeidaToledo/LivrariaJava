package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

import java.util.ArrayList;
import java.util.List;

public abstract class Transacao {
    protected String id;
    protected String idCliente;
    protected String data;
    protected List<ItemVenda> itens;
    protected List<ItemCompra> itensCompra;

    public Transacao() {
        this.id = GeradorID.getNextId().toString();
        this.itens = new ArrayList<>();
        this.itensCompra = new ArrayList<>();
    }

    public Transacao(String idCliente, String data, List<ItemVenda> itens, List<ItemCompra> itensCompra) {
        this.idCliente = idCliente;
        this.data = data;
        this.itens = itens;
        this.itensCompra = itensCompra;
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

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public List<ItemCompra> getItensCompra() {
        return itensCompra;
    }

    public void setItensCompra(List<ItemCompra> itensCompra) {
        this.itensCompra = itensCompra;
    }
}
