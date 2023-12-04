package ifsp.edu.source.Model;

import ifsp.edu.source.Util.GeradorID;

public class Livro {
	private String id;
	private String nome;
	private int quantidade;
	private double preco;

	public Livro() {
		this.id = GeradorID.getNextId().toString();
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
