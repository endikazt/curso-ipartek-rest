package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ipartek.formacion.model.pojo.Pokemon;

public class PokemonDAO implements IDAO<Pokemon>{
	
	private static PokemonDAO INSTANCE;

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

		String sql = "SELECT id, nombre FROM pokemon ORDER BY id DESC LIMIT 500";

		ArrayList<Pokemon> registros = new ArrayList<Pokemon>();
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(sql);
				ResultSet rs = pst.executeQuery() ) {
			
			while( rs.next() ) {
				// TODO mapper
				Pokemon p = new Pokemon();
				p.setId( rs.getInt("id"));
				p.setNombre( rs.getString("nombre"));
				
				registros.add(p);
			}
			

		} catch (Exception e) {
			// TODO: LOG
			e.printStackTrace();
		}

		return registros;
	}

	@Override
	public Pokemon getById(int id) {
		// TODO Auto-generated method stub
		return null;
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
}
