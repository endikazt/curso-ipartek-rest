package com.ipartek.formacion.model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Habilidad;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.mysql.fabric.xmlrpc.base.Array;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pokemon update(int id, Pokemon pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pokemon create(Pokemon pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Pokemon mapper(ResultSet rs) throws SQLException {
		
		Pokemon p = new Pokemon();
		Habilidad h = new Habilidad();
		
		p.setId(rs.getInt("id_pokemon"));
		p.setNombre(rs.getString("pokemon"));
		
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		
		p.getHabilidades().add(h);
		
		return p;
	}
}
