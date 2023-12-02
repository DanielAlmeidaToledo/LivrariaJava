package ifsp.edu.source.Model;

public class Compra {
    private long id;
    private long idCliente;
    private String data;

    // Construtores, getters e setters

    public Compra() {
        // Construtor padrão
    }

    public Compra(long id, long idCliente, String data) {
        this.id = id;
        this.idCliente = idCliente;
        this.data = data;
    }

    // Getters e Setters

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(long idCliente) {
        this.idCliente = idCliente;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Compra [id=" + id + ", idCliente=" + idCliente + ", data=" + data + "]";
    }
}
