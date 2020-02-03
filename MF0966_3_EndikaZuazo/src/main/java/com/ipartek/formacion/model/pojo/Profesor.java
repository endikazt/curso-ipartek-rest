package com.ipartek.formacion.model.pojo;

public class Profesor {
	
	private int id;
	
	private String nombre;
	
	private String apellidos;

	public Profesor() {
		super();
		this.id = 0;
		this.nombre = "";
		this.apellidos = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	@Override
	public String toString() {
		return "Profesor [id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + "]";
	}

}
