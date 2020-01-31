package com.ipartek.formacion.model.pojo;

public class Libro {
	
	private int id;
	private String titulo;
	private Autor autor;
	
	public Libro() {
		super();
		this.id = 0;
		this.titulo = "";
		this.autor = new Autor();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	@Override
	public String toString() {
		return "Libro [id=" + id + ", titulo=" + titulo + ", autor=" + autor + "]";
	}

}
