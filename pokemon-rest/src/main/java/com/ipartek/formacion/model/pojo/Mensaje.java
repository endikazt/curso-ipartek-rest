package com.ipartek.formacion.model.pojo;

public class Mensaje {
	
	private int tipo;
	private String mensaje;
	
	public Mensaje() {
		super();
		this.tipo = 0;
		this.mensaje = "";
	}
	
	public Mensaje(int tipo, String mensaje) {
		super();
		this.tipo = tipo;
		this.mensaje = mensaje;
	}

	public int getTipo() {
		return tipo;
	}
	
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	@Override
	public String toString() {
		return "Mensaje [tipo=" + tipo + ", mensaje=" + mensaje + "]";
	}

}
