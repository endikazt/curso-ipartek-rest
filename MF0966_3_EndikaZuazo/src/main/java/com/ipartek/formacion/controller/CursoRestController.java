package com.ipartek.formacion.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.model.CursoDAO;
import com.ipartek.formacion.model.pojo.Curso;
import com.ipartek.formacion.supermercado.utils.Utilidades;


/**
 * Servlet implementation class ProductoRestController
 */
@WebServlet( { "/cursos/*"} )
public class CursoRestController extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(CursoRestController.class);
	private CursoDAO cursoDao;
	private String responseBody;
	private String pathInfo;
	private int pathInfoData;
	private int statusCode;
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		cursoDao = CursoDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		cursoDao = null;
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepara la response		
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8");
		
		responseBody = null;
		pathInfo = request.getPathInfo();
		
		try { 
			
			pathInfoData = -1;
			pathInfoData = Utilidades.obtenerId(pathInfo);
			
		    // llama a doGEt, doPost, doPut, doDelete
			super.service(request, response);  
			
		}catch (Exception e) {
			
			statusCode =  HttpServletResponse.SC_BAD_REQUEST;
			responseBody = e.getMessage();		
			
					
		}finally {	
			
			response.setStatus( statusCode );
			
			if ( responseBody != null ) {
				
				PrintWriter out = response.getWriter();		               
				out.print(responseBody); 	
				out.flush();  
			}	
		}	
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.trace("peticion GET");
		
		String pathInfo = request.getPathInfo();

		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
			try {

				pathInfoData = Utilidades.obtenerId(pathInfo); // Comprueba si el id pasado es valido o no
				
				ArrayList<Curso> lista;
				Curso curso;

				if (pathInfoData == -1) { // Si el pathInfo es "/" el resultado del metodo es -1 entonces se le muestran todos los libros
				

						lista = (ArrayList<Curso>) cursoDao.getAll();
						
						statusCode = HttpServletResponse.SC_OK;
						responseBody = new Gson().toJson(lista).toString();

				} else {
					

					curso = cursoDao.getById(pathInfoData);
					
					if(curso.getId() != 0) {
					
						statusCode = HttpServletResponse.SC_OK;
						responseBody = new Gson().toJson(curso).toString();

					} else {
						
						statusCode = HttpServletResponse.SC_NO_CONTENT;
						
					}
					
				}

			} catch (Exception e) {

				LOG.info(e);

				statusCode = HttpServletResponse.SC_BAD_REQUEST;
				responseBody = "Tienes que enviar el tipo de datos correctos. Ejemplo uf2215endika/libro/1 o uf2215endika/libro/?titulo=nombre";

			} 
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}
}
