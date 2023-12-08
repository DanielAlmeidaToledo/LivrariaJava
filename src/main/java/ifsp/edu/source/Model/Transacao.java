package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;
public abstract class Transacao {
    protected String id;
    protected String idCliente;
    protected String data;

    public Transacao() {
        this.id = GeradorID.getNextId().toString();
    }

    public Transacao(String idCliente, String data) {
        this.idCliente = idCliente;
        this.data = data;
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
}
