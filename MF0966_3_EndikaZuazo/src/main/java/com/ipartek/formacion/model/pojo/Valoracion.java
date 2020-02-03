package com.ipartek.formacion.model.pojo;

public class Valoracion {
	
	private int id;
	
	private int id_curso;
	
	private int  usuario;
	
	private int valoracion;
	
	private String comentario;
	
	

	public Valoracion() {
		super();
		this.id = 0;
		this.id_curso = 0;
		this.usuario =  0;
		this.valoracion = 0;
		this.comentario = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_curso() {
		return id_curso;
	}

	public void setId_curso(int id_curso) {
		this.id_curso = id_curso;
	}

	public int getUsuario() {
		return usuario;
	}

	public void setUsuario(int usuario) {
		this.usuario = usuario;
	}

	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Override
	public String toString() {
		return "Valoracion [id=" + id + ", id_curso=" + id_curso + ", usuario=" + usuario + ", valoracion=" + valoracion
				+ ", comentario=" + comentario + "]";
	}

}
