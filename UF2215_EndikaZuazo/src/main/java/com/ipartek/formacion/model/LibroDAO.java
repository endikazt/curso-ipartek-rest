package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Autor;
import com.ipartek.formacion.model.pojo.Libro;

public class LibroDAO implements IDAO<Libro>{
	
	private static LibroDAO INSTANCE = null;
	
	private final static Logger LOG = Logger.getLogger(LibroDAO.class);
	
	private static final String SQL_GET_ALL = "SELECT id_libro, titulo, l.id_autor, nombre FROM libros l, autores a WHERE l.id_autor = a.id_autor ORDER BY id_libro ASC LIMIT 50";
	private static final String SQL_GET_BY_ID = "SELECT id_libro, titulo, l.id_autor, nombre FROM libros l, autores a WHERE l.id_autor = a.id_autor AND id_libro=?;";
	private static final String SQL_GET_BY_TITULO = "SELECT id_libro, titulo, l.id_autor, nombre FROM libros l, autores a WHERE l.id_autor = a.id_autor AND titulo LIKE ?;";

	private LibroDAO() {
		super();		
	}
	
	public static synchronized LibroDAO getIntance() {
		
		if(INSTANCE == null) {
			INSTANCE = new LibroDAO();
		}
		
		return INSTANCE;
		
	}

	@Override
	public ArrayList<Libro> getAll() {
		
		LOG.trace("Recuperar todos los libros de la base de datos.");
		
		ArrayList<Libro> lista = new ArrayList<Libro>();

		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {

			while (rs.next()) {
				
				Libro l = mapper(rs);
				lista.add(l);

			}

		} catch (SQLException e) {
			LOG.error(e);
		} catch (NumberFormatException e) {
			LOG.error(e);
		} catch (Exception e) {
			LOG.error(e);
		}

		return lista;
	}
	
	

	@Override
	public Libro getById(int id) {
		
		LOG.trace("Recuperar libro con id = " + id);
		
		Libro resul = new Libro();
		
		try (			
				Connection con = ConnectionManager.getConnection();				
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID);
						
			) {

			pst.setInt(1, id);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					
					resul = mapper(rs);		
				}
			}
		} catch (SQLException e) {
			LOG.error(e);
		}
		
		return resul;
	}
	
	@Override
	public Libro getByNombre(String titulo) {
		
		LOG.trace("Recuperar libro con el titulo = " + titulo);
		
		String nombre = "%" + titulo + "%";
		
		Libro resul = new Libro();
		
		try (			
				Connection con = ConnectionManager.getConnection();				
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_TITULO);
						
			) {

			pst.setString(1, nombre);

			try (ResultSet rs = pst.executeQuery()) {
				if (rs.next()) {
					
					resul = mapper(rs);		
				}
			}
		} catch (SQLException e) {
			LOG.error(e);
		}
		
		return resul;
	}

	@Override
	public Libro delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Libro update(int id, Libro pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Libro create(Libro pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	private Libro mapper(ResultSet rs) throws SQLException {
		
		Libro libro = new Libro();
		Autor autor = new Autor();
		
		libro.setId(rs.getInt("id_libro"));
		libro.setTitulo(rs.getString("titulo"));
		
		autor.setId(rs.getInt("id_autor"));
		autor.setNombre(rs.getString("nombre"));
		
		libro.setAutor(autor);
		
		
		return libro;
	}
}
