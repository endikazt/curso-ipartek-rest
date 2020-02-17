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

public class HabilidadDAO implements IDAO<Habilidad>{
	
	private static HabilidadDAO INSTANCE;
	
	private final static Logger LOG = Logger.getLogger(HabilidadDAO.class);
	
	private HabilidadDAO() {
		super();
	}

	public static synchronized HabilidadDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new HabilidadDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Habilidad> getAll() {
		
		ArrayList<Habilidad> listaHabilidad = new ArrayList<Habilidad>();

		try (Connection con = ConnectionManager.getConnection();
				
			CallableStatement cs = con.prepareCall("{CALL pa_habilidades_getall()}");
				
			){
				
			try (ResultSet rs = cs.executeQuery();) {
				
				while (rs.next()) {
					
					Habilidad h = mapper(rs);
					
					listaHabilidad.add(h);

				}
				
			}
			

		} catch (Exception e) {
			LOG.error(e);
		}
	
		return listaHabilidad;
	}

	private Habilidad mapper(ResultSet rs) throws SQLException {
		
		Habilidad h = new Habilidad();
		
		h.setId(rs.getInt("id_habilidad"));
		h.setNombre(rs.getString("nombre_habilidad"));
		
		return h;
	}

	@Override
	public Habilidad getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad update(int id, Habilidad pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Habilidad create(Habilidad pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
