package com.ipartek.formacion.supermercado.modelo.pojo;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;

public class Usuario {
	
private int id;
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String nombre;
	
	@NotBlank
	@Size(min = 2, max = 50)
	private String password;
	
	private Rol rol;
	
	public Usuario(int id, String nombre, String password) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.password = password;
		this.rol = new Rol();
	}
	
	public Usuario() {
		super();
		this.id = 0;
		this.nombre = "";
		this.password = "";
		this.rol = new Rol();
	}
	
	public Usuario(String nombre, String password) {
		this();
		this.nombre = nombre;
		this.password = password;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nombre=" + nombre + ", password=" + password + ", rol=" + rol + "]";
	}

	
}
