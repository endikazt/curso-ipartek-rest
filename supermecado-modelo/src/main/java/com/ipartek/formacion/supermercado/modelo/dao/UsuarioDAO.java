package com.ipartek.formacion.supermercado.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.modelo.ConnectionManager;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
import com.ipartek.formacion.supermercado.modelo.pojo.Rol;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;

public class UsuarioDAO implements IUsuarioDAO {
	
	private static UsuarioDAO INSTANCE = null;
	
	private final static Logger LOG = Logger.getLogger(UsuarioDAO.class);
	
	private static final String SQL_GET_ALL = "SELECT id, nombre, password, id_rol FROM usuario ORDER BY id DESC LIMIT 500;";
	private static final String SQL_GET_BY_ID = "SELECT id, nombre, password, id_rol  FROM usuario WHERE id=?;";
	//private static final String SQL_GET_ALL_BY_NOMBRE = "SELECT nombre FROM producto ORDER BY u.nombre ASC LIMIT 500;";
	private static final String SQL_EXISTE = " SELECT id, nombre, password, id_rol FROM usuario WHERE nombre = ? AND password = ?;";
	//private static final String SQL_EXISTE_NOMBRE = " SELECT nombre FROM producto WHERE nombre = ?;";
	private static final String SQL_INSERT = "INSERT INTO usuario (nombre, password, id_rol) VALUES ( ? , ?, ?);";
	private static final String SQL_UPDATE = "UPDATE usuario SET nombre= ?, password = ?, id_rol = ? WHERE id = ?;";
	private static final String SQL_DELETE = "DELETE FROM usuario WHERE id = ?;";
	//private static final String SQL_DELETE_LOGICO = "UPDATE producto SET fecha_eliminacion = CURRENT_TIMESTAMP() WHERE id = ?;";
	
	private UsuarioDAO() {
		super();		
	}
	
	public static synchronized UsuarioDAO getIntance() {
		
		if(INSTANCE == null) {
			INSTANCE = new UsuarioDAO();
		}
		
		return INSTANCE;
		
	}
	
	@Override
	public ArrayList<Usuario> getAll() {
		
		ArrayList<Usuario> lista = new ArrayList<Usuario>();
		
		LOG.trace("Recuperar todos los usuarios de la base de datos.");
	
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {
	
			while (rs.next()) {
				
				Usuario p = mapper(rs);
				lista.add(p);
	
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return lista;
	}
	
	@Override
	public Usuario getById(int id) throws Exception {
		
		LOG.trace("Recuperar el usuario con el id = " + id);
		
		Usuario resul = null;
		
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
	
	@Override
	public Usuario delete(int id) throws Exception {
		
		LOG.trace("Eliminar el usuario con el id = " + id);
		
		Usuario resul = this.getById(id);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_DELETE);
				
		) {
	
			pst.setInt(1, id);
			
			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				
				LOG.info("Eliminacion completada. Producto = " + resul.toString());
				
			} else {
				
				throw new Exception("No se ha podido eliminar el registro. El usuario ni existe o tiene productos asociados.");
				
			}
	
		}
		
		return resul;
	}
	
	@Override
	public Usuario update(int id, Usuario pojo) throws Exception {
		
		LOG.trace("Modificar el usuario con el id = " + id + ". Campo a modificar = " + pojo);
		
		Usuario resul = null;
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_UPDATE);		
				
		) {
	
			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getPassword());
			pst.setInt(3, pojo.getRol().getId());
			pst.setInt(4, id);
	
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				
				resul = this.getById(id);
				
			} else {
				
				throw new Exception("No se encontro registro para id=" + id);
				
			}
		}
		
		return resul;
	}
	
	@Override
	public Usuario create(Usuario pojo) throws Exception {
		
		Usuario resul = null;
		
		LOG.trace("Crear el usuario -> " + pojo);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
					
		) {
	
			pst.setString(1, pojo.getNombre());
			pst.setString(2, pojo.getPassword());
			pst.setInt(3, pojo.getRol().getId());
	
			int affectedRows = pst.executeUpdate();
			if (affectedRows == 1) {
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					pojo.setId(rs.getInt(1));
				}
				
				LOG.trace("Usuario " + pojo + " creado.");
	
			}
	
		}
	
		return resul;
	}
	
	@Override
	public Usuario exist(String nombre, String password) {
		
		Usuario resul = null;
		
		try (			
				Connection con = ConnectionManager.getConnection();				
				PreparedStatement pst = con.prepareStatement(SQL_EXISTE);
						
			) {
	
			pst.setString(1, nombre);
			pst.setString(2, password);
			LOG.debug(pst);
	
			try (ResultSet rs = pst.executeQuery()) {
				
				if (rs.next()) {
					resul = mapper(rs);
				}
				
			}
			
		} catch (Exception e) {
			LOG.error(e);
		}
		
		return resul;
	}
	
	private Usuario mapper(ResultSet rs) throws SQLException {
		
		Usuario u = new Usuario();
		u.setId(rs.getInt("id"));
		u.setNombre(rs.getString("nombre"));
		u.setPassword(rs.getString("password"));
		
		int id_rol = rs.getInt("id_rol");
		
		Rol rol = new Rol();
		
		if(id_rol == 1) {
			
			rol.setId(Rol.ROL_ADMIN);
			rol.setNombre("ADMIN");
			
		}
		
		u.setRol(rol);
		
		return u;
	}
	
	@Override
	public ArrayList<Producto> getAllByUser(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
