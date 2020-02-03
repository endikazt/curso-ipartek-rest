package com.ipartek.formacion.model.pojo;

import java.util.ArrayList;

public class Curso {
	
	private int id;
	
	private String nombre;
	
	private String finicio;
	
	private String ffin;
	
	private int horas;
	
	private Profesor profesor;
	
	private ArrayList<Valoracion> valoraciones;

	public Curso() {
		super();
		this.id = 0;
		this.nombre = "";
		this.finicio = "";
		this.ffin = "";
		this.horas = 0;
		this.profesor = new Profesor();
		this.valoraciones = new ArrayList<Valoracion>();
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

	public String getFinicio() {
		return finicio;
	}

	public void setFinicio(String finicio) {
		this.finicio = finicio;
	}

	public String getFfin() {
		return ffin;
	}

	public void setFfin(String ffin) {
		this.ffin = ffin;
	}

	public int getHoras() {
		return horas;
	}

	public void setHoras(int horas) {
		this.horas = horas;
	}

	public Profesor getProfesor() {
		return profesor;
	}

	public void setProfesor(Profesor profesor) {
		this.profesor = profesor;
	}

	public ArrayList<Valoracion> getValoraciones() {
		return valoraciones;
	}

	public void setValoraciones(ArrayList<Valoracion> valoraciones) {
		this.valoraciones = valoraciones;
	}

	@Override
	public String toString() {
		return "Curso [id=" + id + ", nombre=" + nombre + ", finicio=" + finicio + ", ffin=" + ffin + ", horas=" + horas
				+ ", profesor=" + profesor + ", valoraciones=" + valoraciones + "]";
	}

}
