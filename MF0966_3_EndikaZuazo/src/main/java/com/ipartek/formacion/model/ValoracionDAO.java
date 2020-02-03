package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Valoracion;

public class ValoracionDAO implements IDAO<Valoracion>{
	
	private static ValoracionDAO INSTANCE;
	
	private final static Logger LOG = Logger.getLogger(ValoracionDAO.class);
	
	private static final String SQL_GET_ALL = "SELECT id_valoracion, id_curso, id_usuario, valoracion, comentario FROM valoracion ORDER BY id_valoracion ASC LIMIT 50;";
	private static final String SQL_GET_BY_ID = "SELECT id_valoracion, id_curso, id_usuario, valoracion, comentario FROM valoracion WHERE id_valoracion = ?;";
	private static final String SQL_GET_BY_ID_CURSO = "SELECT id_valoracion, id_curso, id_usuario, valoracion, comentario FROM valoracion WHERE id_curso = ?;";
	private static final String SQL_GET_BY_ID_USUARIO = "SELECT id_valoracion, id_curso, id_usuario, valoracion, comentario FROM valoracion WHERE id_usuario = ?;";
	private static final String SQL_INSERT = "INSERT INTO valoracion (id_curso, id_usuario, valoracion, comentario) VALUES ( ? , ?, ?, ?);";
	private static final String SQL_UPDATE = "UPDATE valoracion SET id_curso= ?, id_usuario = ?, valoracion = ?, comentario = ? WHERE id_valoracion = ?;";
	private static final String SQL_DELETE = "DELETE FROM valoracion WHERE id_valoracion = ?;";
	
	private ValoracionDAO() {
		super();
	}

	public static synchronized ValoracionDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new ValoracionDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Valoracion> getAll() {
		
		ArrayList<Valoracion> lista = new ArrayList<Valoracion>();
		
		LOG.trace("Recuperar todos las valoracion de la base de datos.");
	
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {
	
			while (rs.next()) {
				
				Valoracion v = mapper(rs);
				lista.add(v);
	
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return lista;
	}

	@Override
	public Valoracion getById(int id) {
		LOG.trace("Recuperar la valoracion con el id = " + id);
		
		Valoracion resul = null;
		
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
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resul;
	}
	
	public List<Valoracion> getByIdCurso(int id) {
		LOG.trace("Recuperar las valoraciones con el id_curso = " + id);
		
		ArrayList<Valoracion> lista = new ArrayList<Valoracion>();
		
		LOG.trace("Recuperar todos las valoracion de la base de datos.");
	
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID_CURSO);
				
			) {
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
	
			while (rs.next()) {
				
				Valoracion v = mapper(rs);
				lista.add(v);
	
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return lista;
	}
	
	public List<Valoracion> getByIdUsuario(int id) {
		LOG.trace("Recuperar las valoraciones con el id_usuario = " + id);
		
		ArrayList<Valoracion> lista = new ArrayList<Valoracion>();
		
		LOG.trace("Recuperar todos las valoracion de la base de datos.");
	
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_BY_ID_CURSO);
				
			) {
			
			pst.setInt(1, id);
			
			ResultSet rs = pst.executeQuery();
	
			while (rs.next()) {
				
				Valoracion v = mapper(rs);
				lista.add(v);
	
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return lista;	
	}

	@Override
	public Valoracion delete(int id) throws Exception {
		LOG.trace("Eliminar la valoracion con el id = " + id);
		
		Valoracion resul = this.getById(id);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_DELETE);
				
		) {
	
			pst.setInt(1, id);
			
			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				
				LOG.info("Eliminacion completada. Valoracion = " + resul.toString());
				
			} else {
				
				throw new Exception("No se ha podido eliminar el registro. La valoracion no existe o tiene usuario o cursos asociados.");
				
			}
	
		}
		
		return resul;
	}

	@Override
	public Valoracion update(int id, Valoracion pojo) throws Exception {
	LOG.trace("Modificar la valoracion con el id = " + id + ". Campo a modificar = " + pojo);
		
		Valoracion resul = null;
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_UPDATE);		
				
		) {
	
			pst.setInt(1, pojo.getId_curso());
			pst.setInt(2, pojo.getUsuario());
			pst.setInt(3, pojo.getValoracion());
			pst.setString(4, pojo.getComentario());
			pst.setInt(5, id);
	
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				
				resul = this.getById(id);
				
			} else {
				
				throw new Exception("No se encontro registro para id = " + id);
				
			}
		}
		
		return resul;
	}

	@Override
	public Valoracion create(Valoracion pojo) throws Exception {
		LOG.trace("Crea valoracion = " + pojo);
		
		Valoracion resul = null;
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);		
				
		) {
	
			pst.setInt(1, pojo.getId_curso());
			pst.setInt(2, pojo.getUsuario());
			pst.setInt(3, pojo.getValoracion());
			pst.setString(4, pojo.getComentario());
	
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
				}
				
				LOG.trace("Valoracion " + pojo + " creada.");
				
				resul = pojo;
	
			}
		}
		
		return resul;
	}

	@Override
	public Valoracion getByNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Valoracion mapper(ResultSet rs) throws SQLException {
		
		Valoracion valor = new Valoracion();
		
		valor.setId(rs.getInt("id_valoracion"));
		valor.setId_curso(rs.getInt("id_curso"));
		valor.setUsuario(rs.getInt("id_usuario"));
		valor.setValoracion(rs.getInt("valoracion"));
		valor.setComentario(rs.getString("comentario"));
		
		return valor;
		
		
	}

}
