package com.ipartek.formacion.supermercado.modelo.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.modelo.ConnectionManager;
import com.ipartek.formacion.supermercado.modelo.pojo.Categoria;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;

public class CategoriaDAO implements IDAO<Categoria>{
	
	private static CategoriaDAO INSTANCE = null;
	
	private final static Logger LOG = Logger.getLogger(CategoriaDAO.class);
	
	// NO hace falta usar la sql porque ya estamos usando un procedure para que haga lo mismo
	//private static final String SQL_GET_ALL = "SELECT id, nombre FROM categoria ORDER BY id DESC LIMIT 500;";
	//private static final String SQL_GET_BY_ID = "SELECT id, nombre FROM categoria WHERE id=?;";
	//private static final String SQL_INSERT = "INSERT INTO categoria (nombre) VALUES (?);";
	//private static final String SQL_UPDATE = "UPDATE categoria SET nombre= ? WHERE id = ?;";
	//private static final String SQL_DELETE = "DELETE FROM categoria WHERE id = ?;";
	
	private CategoriaDAO() {
		super();		
	}
	
	public static synchronized CategoriaDAO getIntance() {
		
		if(INSTANCE == null) {
			INSTANCE = new CategoriaDAO();
		}
		
		return INSTANCE;
		
	}

	@Override
	public ArrayList<Categoria> getAll() {
		
		LOG.trace("Recuperar todas las categorias de la base de datos.");
		
		ArrayList<Categoria> lista = new ArrayList<Categoria>();

		try (Connection con = ConnectionManager.getConnection();
				
			CallableStatement cs = con.prepareCall("{CALL pa_categoria_getall()}");
				
			){
				
			try (ResultSet rs = cs.executeQuery();) {
				
				while (rs.next()) {
					
					Categoria c = mapper(rs);
					lista.add(c);

				}
				
			}

		} catch (Exception e) {
			LOG.error(e);
		}

		return lista;
	}

	@Override
	public Categoria getById(int id) throws Exception {
		
		Categoria resul = null;
		
		LOG.trace("Recuperar la categoria " + id + " de la base de datos.");
		
		try (			
				Connection con = ConnectionManager.getConnection();				
				CallableStatement cs = con.prepareCall("{CALL pa_categoria_getbyid(?)}");
						
			) {

			cs.setInt(1, id);

			try (ResultSet rs = cs.executeQuery()) {
				if (rs.next()) {
					
					resul = mapper(rs);
				} else {
					
					throw new Exception("Categoria no encontrada. ID = " + id);
					
				}
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}
		
		return resul;
	}

	@Override
	public Categoria delete(int id) throws Exception {
		
		LOG.trace("Eliminar la categoria " + id);
		
		Categoria resul = this.getById(id);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			CallableStatement cs = con.prepareCall("{CALL pa_categoria_delete(?)}");
				
		) {

			cs.setInt(1, id);
			
			int affetedRows = cs.executeUpdate();
			if (affetedRows == 1) {
				
				LOG.info("Eliminacion completada. Producto = " + resul.toString());
				
			} else {
				
				throw new Exception("No se ha podido eliminar el registro. El usuario ni existe o tiene productos asociados.");
				
			}

		}
		
		return resul;
	}

	@Override
	public Categoria update(int id, Categoria pojo) throws Exception {
		
		Categoria resul = null;
		
		LOG.trace("Modificar la categoria " + id + ". Datos a modificar -> " + pojo);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			CallableStatement cs = con.prepareCall("{CALL pa_categoria_update(?,?)}");		
				
		) {
			
			cs.setInt(1, id);
			cs.setString(2, pojo.getNombre());

			int affectedRows = cs.executeUpdate();
			if (affectedRows == 1) {
				
				resul = this.getById(id);
				
				LOG.trace("Categoria " + id + " modificada. Datos de la categoria -> " + resul);
				
			} else {
				
				throw new Exception("No se encontro registro para id=" + id);
				
			}
		}
		
		return resul;
	}

	@Override
	public Categoria create(Categoria pojo) throws Exception {
		
		Categoria resul = null;
		
		LOG.trace("Crear nueva categoria -> " + pojo);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			CallableStatement cs = con.prepareCall("{CALL pa_categoria_insert(?,?)}");
					
		) {
			
			//Parametro de entrada

			cs.setString(1, pojo.getNombre());
			
			//Parametro de salida
			
			cs.registerOutParameter(2, java.sql.Types.INTEGER);
			
			LOG.debug(cs);
			
			cs.executeUpdate();
			
			// Una cez ejecutado recogemo el paraemtro de salida
			
			resul = pojo;
			
			resul.setId(cs.getInt(2));
			
			LOG.trace("Categoria creada -> " + resul);

		}

		return resul;
	}
	
	private Categoria mapper(ResultSet rs) throws SQLException {
		
		Categoria u = new Categoria();
		u.setId(rs.getInt("id"));
		u.setNombre(rs.getString("nombre"));
		
		return u;
	}

	@Override
	public ArrayList<Producto> getAllByUser(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
}
