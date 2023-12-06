package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;
import java.util.List;

public abstract class Transacao {
    protected String id;
    protected String idCliente;
    protected String data;
    protected List<ItemProduto> itens;

    public Transacao() {
        this.id = GeradorID.getNextId().toString();
    }

    public Transacao(String idCliente, String data, List<ItemProduto> itens) {
        this.idCliente = idCliente;
        this.data = data;
        this.itens = itens;
    }

    // Getters e Setters

    public void adicionarItemTransacao(ItemProduto ItemProduto) {
        this.itens.add(ItemProduto);
    }

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

    public List<ItemProduto> getItens() {
        return itens;
    }

    public void setItens(List<ItemProduto> itens) {
        this.itens = itens;
    }
}
