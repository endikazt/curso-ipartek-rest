package com.ipartek.formacion.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

public class PokemonDAO implements IDAO<Pokemon>{
	
	private static PokemonDAO INSTANCE;
	
	private final static Logger LOG = Logger.getLogger(PokemonDAO.class);
	
	private PokemonDAO() {
		super();
	}

	public static synchronized PokemonDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new PokemonDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Pokemon> getAll() {
		
		HashMap<Integer, Pokemon> listaPokemons = new HashMap<Integer, Pokemon>();
		ArrayList<Pokemon> lista = new ArrayList<Pokemon>();

		try (Connection con = ConnectionManager.getConnection();
				
			CallableStatement cs = con.prepareCall("{CALL pa_pokemons_getallwithhabilidades()}");
				
			){
				
			try (ResultSet rs = cs.executeQuery();) {
				
				while (rs.next()) {
					
					Pokemon p = mapper(rs);
					
					if(listaPokemons.containsKey(p.getId())) {
						
						listaPokemons.get(p.getId()).getHabilidades().add(p.getHabilidades().get(0));
						
					} else {
						
						listaPokemons.put(p.getId(), p);
						
					}

				}
				
			}
			
			

		} catch (Exception e) {
			LOG.error(e);
		}
		
		lista.addAll(listaPokemons.values());

		return lista;
	}
	
	public List<Pokemon> getBySearchParam(String searchParam) {
		
		HashMap<Integer, Pokemon> listaPokemons = new HashMap<Integer, Pokemon>();
		ArrayList<Pokemon> lista = new ArrayList<Pokemon>();

		try (Connection con = ConnectionManager.getConnection();
				
			CallableStatement cs = con.prepareCall("{CALL pa_pokemons_getbysearchparam(?)}");
				
			){
			
			cs.setString(1, searchParam);
				
			try (ResultSet rs = cs.executeQuery();) {
				
				while (rs.next()) {
					
					Pokemon p = mapper(rs);
					
					if(listaPokemons.containsKey(p.getId())) {
						
						listaPokemons.get(p.getId()).getHabilidades().add(p.getHabilidades().get(0));
						
					} else {
						
						listaPokemons.put(p.getId(), p);
						
					}

				}
				
			}
			
			

		} catch (Exception e) {
			LOG.error(e);
		}
		
		lista.addAll(listaPokemons.values());

		return lista;
	}

	@Override
	public Pokemon getById(int id) {
		
		Pokemon poke = new Pokemon();
		
		try (Connection con = ConnectionManager.getConnection();
				
			CallableStatement cs = con.prepareCall("{CALL pa_pokemons_getbyid(?)}");
				
			){
			
			cs.setInt(1, id);
				
			try (ResultSet rs = cs.executeQuery();) {
				
				while (rs.next()) {
					
					if(poke.getId() == 0) {
					
					poke = mapper(rs);
					
					} else {
						
						Habilidad habili = new Habilidad();
						habili.setId(rs.getInt("id_habilidad"));
						habili.setNombre(rs.getString("nombre_habilidad"));
						
						poke.getHabilidades().add(habili);			
						
					}		

				}
				
			}	
			

		} catch (Exception e) {
			LOG.error(e);
		}

		return poke;
	}

	@Override
	public Pokemon delete(int id) throws Exception {
		LOG.trace("Eliminar la pokemon " + id);
		
		Pokemon resul = this.getById(id);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			CallableStatement cs = con.prepareCall("{CALL pa_pokemons_delete(?)}");
				
		) {

			cs.setInt(1, id);
			
			int affetedRows = cs.executeUpdate();
			if (affetedRows == 1) {
				
				LOG.info("Eliminacion completada. Pokemon = " + resul.toString());
				
			} else {
				
				throw new Exception("No se ha podido eliminar el registro. El pokemon ni existe,");
				
			}

		}
		
		return resul;
	}

	@Override
	public Pokemon update(int id, Pokemon pojo) throws Exception {
		Pokemon resul = null;
		Pokemon pokeOriginal = this.getById(id);
		
		String sqlInsertHabilidad = "INSERT INTO po_ha (id_pokemon,id_habilidad) VALUES (?,?);";
		String sqlDeleteHabilidad = "DELETE FROM po_ha WHERE id_pokemon=? and id_habilidad=?;";
		
		LOG.trace("Modificar pokemon " + id + ". Datos a modificar -> " + pojo);
		
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			con.setAutoCommit(false);
			
			CallableStatement cs = con.prepareCall("{CALL pa_pokemons_update(?, ?, ?)}");
			
			cs.setString(1, pojo.getNombre());
			cs.setString(2, pojo.getImagen());
			cs.setInt(3, id);
			
			LOG.debug(cs);
			
			cs.executeUpdate();
			
			resul = pojo;
				
			ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
			for (Habilidad habilidad : habilidades) {
				
				PreparedStatement ps = con.prepareStatement(sqlInsertHabilidad, Statement.RETURN_GENERATED_KEYS);
				
				ps.setInt(1, pojo.getId());
				ps.setInt(2, habilidad.getId());
				
				ps.executeUpdate();
				
			}
			
			//SI TODO FUNCIONA BIEN			
			con.commit();
			
		} catch (MySQLIntegrityConstraintViolationException e) {
			
			con.rollback();
			e.printStackTrace();
			
			throw new MySQLIntegrityConstraintViolationException("nombre duplicado");
						
		}
		catch (Exception e) {
			
			con.rollback();
			e.printStackTrace();
			
			throw new Exception("Error de base de datos.");
				
		} finally {
			
			if ( con != null ) {
				con.close();
			}			
			
		}

		return resul;
		
	}

	@Override
	public Pokemon create(Pokemon pojo) throws Exception {
		Pokemon resul = null;
		
		String sqlInsertHabilidad = "INSERT INTO po_ha (id_pokemon,id_habilidad) VALUES (?,?);";
		
		LOG.trace("Crear nuevo pokemon -> " + pojo);
		LOG.trace("Crear nuevo pokemon -> " + pojo.getHabilidades());
		
		Connection con = null;
		try {
			con = ConnectionManager.getConnection();
			con.setAutoCommit(false);
			
			CallableStatement cs = con.prepareCall("{CALL pa_pokemons_create(?,?,?)}");
			
			cs.setString(1, pojo.getNombre());
			cs.setString(2, pojo.getImagen());
			
			//Parametro de salida
			
			cs.registerOutParameter(3, java.sql.Types.INTEGER);
			
			LOG.debug(cs);
			
			cs.executeUpdate();
			
			// Una vez ejecutado recogemos el paraemtro de salida
			
			resul = pojo;
			
			resul.setId(cs.getInt(3));
			
			if(resul.getId() != -1) {
				
				ArrayList<Habilidad> habilidades = (ArrayList<Habilidad>) pojo.getHabilidades();
				for (Habilidad habilidad : habilidades) {
					
					PreparedStatement ps = con.prepareStatement(sqlInsertHabilidad, Statement.RETURN_GENERATED_KEYS);
					
					ps.setInt(1, pojo.getId());
					ps.setInt(2, habilidad.getId());
					
					ps.executeUpdate();
					
				}	
				
			}
			
			
			//SI TODO FUNCIONA BIEN			
			con.commit();
		} catch (MySQLIntegrityConstraintViolationException e) {
			
			con.rollback();
			e.printStackTrace();
			
			throw new MySQLIntegrityConstraintViolationException("nombre duplicado");
						
		}
		catch (Exception e) {
			
			con.rollback();
			e.printStackTrace();
			
			throw new Exception("Error de base de datos.");
				
		} finally {
			
			if ( con != null ) {
				con.close();
			}			
			
		}

		return null;
	}
	
	private Pokemon mapper(ResultSet rs) throws SQLException {
		
		Pokemon p = new Pokemon();
		Habilidad h = new Habilidad();
		
		p.setId(rs.getInt("id_pokemon"));
		p.setNombre(rs.getString("pokemon"));
		p.setImagen(rs.getString("imagen"));
		
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		
		p.getHabilidades().add(h);
		
		return p;
	}
}
