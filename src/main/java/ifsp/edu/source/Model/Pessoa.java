package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class Pessoa {
    private String id;
    private String nome;

    public Pessoa() {
        this.id = GeradorID.getNextId().toString(); // Gera um novo UUID para o ID da Pessoa
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setId(String id) {
        this.id = id;
    }
}
