package com.ipartek.formacion.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.ipartek.formacion.model.pojo.Curso;
import com.ipartek.formacion.model.pojo.Valoracion;

public class CursoDAO implements IDAO<Curso>{
	
	private static CursoDAO INSTANCE;
	
	private final static Logger LOG = Logger.getLogger(CursoDAO.class);
	
	private static final String SQL_GET_ALL = "SELECT c.id 'id_curso', c.nombre 'curso', finicio, ffin, horas, id_profesor, p.nombre 'nombre_profesor', p.apellidos 'apellidos_profesor' FROM curso c, profesor p WHERE c.id_profesor = p.id ORDER BY c.id ASC LIMIT 500;";
	private static final String SQL_GET_BY_ID = "SELECT c.id 'id_curso', c.nombre 'curso', finicio, ffin, horas, id_profesor, p.nombre 'nombre_profesor', p.apellidos 'apellidos_profesor' FROM curso c, profesor p WHERE c.id_profesor = p.id AND c.id = ?;";
	private static final String SQL_DELETE = "DELETE FROM curso WHERE id = ?;";
	
	private CursoDAO() {
		super();
	}

	public static synchronized CursoDAO getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CursoDAO();
		}
		return INSTANCE;
	}

	@Override
	public List<Curso> getAll() {
		
		ArrayList<Curso> lista = new ArrayList<Curso>();
		
		LOG.trace("Recuperar todos los cursos de la base de datos.");
	
		try (Connection con = ConnectionManager.getConnection();
				PreparedStatement pst = con.prepareStatement(SQL_GET_ALL);
				ResultSet rs = pst.executeQuery()) {
	
			while (rs.next()) {
				
				Curso c = mapper(rs);
				lista.add(c);
	
			}
	
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return lista;
	}

	@Override
	public Curso getById(int id) {
		
		LOG.trace("Recuperar el curso con el id = " + id);
		
		Curso resul = null;
		
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
	public Curso delete(int id) throws Exception {
		LOG.trace("Eliminar el curso con el id = " + id);
		
		Curso resul = this.getById(id);
		
		try (
				
			Connection con = ConnectionManager.getConnection();
			PreparedStatement pst = con.prepareStatement(SQL_DELETE);
				
		) {
	
			pst.setInt(1, id);
			
			int affetedRows = pst.executeUpdate();
			if (affetedRows == 1) {
				
				LOG.info("Eliminacion completada. Curso = " + resul.toString());
				
			} else {
				
				throw new Exception("No se ha podido eliminar el registro. El curso ni existe o tiene reseñas asociadas.");
				
			}
	
		}
		
		return resul;
	}

	@Override
	public Curso update(int id, Curso pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curso create(Curso pojo) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Curso getByNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private Curso mapper(ResultSet rs) throws SQLException {

		Curso curso = new Curso();
		
		ValoracionDAO dao = ValoracionDAO.getInstance();
		
		curso.setId(rs.getInt("id_curso"));
		curso.setNombre(rs.getString("curso"));
		curso.setFinicio(rs.getDate("finicio").toString());
		curso.setFfin(rs.getDate("ffin").toString());
		curso.setHoras(rs.getInt("horas"));
		curso.getProfesor().setId(rs.getInt("id_profesor"));
		curso.getProfesor().setNombre(rs.getString("nombre_profesor"));
		curso.getProfesor().setApellidos(rs.getString("apellidos_profesor"));
		
		curso.setValoraciones((ArrayList<Valoracion>) dao.getByIdCurso(curso.getId()));
		
		return curso;
	}

}
