package com.ipartek.formacion.controller;

import java.io.BufferedReader;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.ipartek.formacion.model.PokemonDAO;
import com.ipartek.formacion.model.pojo.Pokemon;
import com.ipartek.formacion.utils.Utilidades;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

/**
 * Servlet implementation class PokemonController
 */
@WebServlet("/api/pokemon/*")
public class PokemonController extends HttpServlet {
	
	private final static Logger LOG = Logger.getLogger(PokemonController.class);
	
	private static final long serialVersionUID = 1L;
    private static PokemonDAO dao;   
    private String pathInfo;
    private PrintWriter out;
	private BufferedReader reader;
	private String responseBody;
	private int statusCode;

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dao = PokemonDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		dao = null;
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		out = response.getWriter();
		
		response.setContentType("application/json");
		response.setCharacterEncoding("utf-8");
		
		super.service(request, response);
		
		response.setStatus(statusCode); 	// Escribe el codigo de estado en la respuesta
		out.print(responseBody);			// Escribe el contenido en el cuerpo de la respuesta
		out.flush();
		
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String pNombre = request.getParameter("nombre");

		LOG.trace("peticion GET");
		
		String pathInfo = request.getPathInfo();

		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		try {
			
			int pathInfoData = Utilidades.obtenerId(pathInfo);
			
			ArrayList<Pokemon> lista;

			if (pathInfoData == -1) { // Si el pathInfo es "/" el resultado del metodo es -1 entonces se le muestran todos los productos
				
				
				if (("".equals(pNombre) || pNombre == null )) {

					lista = (ArrayList<Pokemon>) dao.getAll();
					
					statusCode = HttpServletResponse.SC_OK;
					
				} else {
					
					lista = (ArrayList<Pokemon>) dao.getBySearchParam(pNombre);
					
					if(lista.size() == 0) {
						
						statusCode = HttpServletResponse.SC_NO_CONTENT;
						
					}
					
				}

				responseBody = new Gson().toJson(lista).toString(); // conversion de Java a Json y escribir en la variable de body

			} else {

					Pokemon pokemon = dao.getById(pathInfoData);
					
					if(pokemon.getId() != 0){
						
						statusCode = HttpServletResponse.SC_OK;
						responseBody = new Gson().toJson(pokemon).toString();
					
					} else {
						
						statusCode = HttpServletResponse.SC_NOT_FOUND;
						responseBody = "No existe ningun pokemon con el id " + pathInfoData + " :(";
						
					}
				
			}

		} catch (Exception e) {

			LOG.info(e);

			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			responseBody = "Tienes que enviar el tipo de datos correctos. Ejemplo supermecado-rest/producto/1";
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("POST crear recurso");
		
		reader = request.getReader();               
		Gson gson = new Gson();
		
		Pokemon pokemon;
		try {
			pokemon = gson.fromJson(reader, Pokemon.class);					// Obtiene los datos del body y los conbierte en un obajeto de Producto
			
			try {
				Pokemon pokemonCreado = dao.create(pokemon);
				
				statusCode = HttpServletResponse.SC_CREATED;
				
			} catch (MySQLIntegrityConstraintViolationException esql) {
				
				LOG.error("No se ha podido insertar el registro en la base de datos. Error -> " + esql);
				
				statusCode = HttpServletResponse.SC_CONFLICT;
				responseBody = "No se ha podido crear el pokemon porque el ID o el Nombre ya existen en la base de datos.";
				
				
			} catch (Exception e) {
				LOG.error(e);
				
				statusCode =  HttpServletResponse.SC_CONFLICT;
				responseBody = "No se ha podido crear el pokemon por algun error de la base de datos.";
			}
			
			
		} catch (JsonSyntaxException e1) {
			LOG.error(e1);

			statusCode = HttpServletResponse.SC_CONFLICT;
			responseBody = "El formato de los datos es incorrecto.";
			
		} catch (JsonIOException e1) {
			LOG.error(e1);
			
			statusCode = HttpServletResponse.SC_CONFLICT;
			responseBody = "Tienes que enviar el tipo de datos correctos.";
			
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("PUT modificar recurso");
		
		String pathInfo = request.getPathInfo();
		reader = request.getReader();               
		Gson gson = new Gson();
		
		try {
			
			int pathInfoData = Utilidades.obtenerId(pathInfo);
			
			if(pathInfoData == -1) {
				
				statusCode =  HttpServletResponse.SC_BAD_REQUEST;
				responseBody = "Tienes que indicar el producto que quieres modificar. Ejemplo supermecado-rest/producto/1";
				
			} else {
					
					try {
						
						Pokemon pokemon = dao.getById(pathInfoData);	
						
						try {
							
							pokemon = gson.fromJson(reader, Pokemon.class);
							
							dao.update(pathInfoData, pokemon);
							
							statusCode = HttpServletResponse.SC_CREATED;
							
							
						} catch (Exception e) {
							LOG.error(e);

							statusCode = HttpServletResponse.SC_CONFLICT;
							responseBody = "El formato de los datos es incorrecto.";
							
						}
							
					} catch (Exception e) {
						LOG.error(e);
						
						statusCode = HttpServletResponse.SC_ACCEPTED;
						responseBody = "No existe ningun pokemon con ese ID :(";
					}
			}
			
			
		}  catch (Exception e) {
			LOG.info("El parametro pasado no es un numero. Error -> " + e);
			
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			responseBody = "Tienes que enviar el tipo de datos correctos.";
			
		}	
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("DELETE eliminar recurso");
		
		String pathInfo = request.getPathInfo();
		
		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );	
		
		try {
			
			int pathInfoData = Utilidades.obtenerId(pathInfo);
			
			if(pathInfoData == -1) {
				
				statusCode =  HttpServletResponse.SC_BAD_REQUEST;
				responseBody = "Tienes que indicar el pokemon que quieres eliminar.";
				
			} else {
					
					try {
						
						Pokemon pokemon = dao.getById(pathInfoData);	
						
						dao.delete(pathInfoData);
			
						statusCode = HttpServletResponse.SC_OK;
						
					} catch (MySQLIntegrityConstraintViolationException esql) {
						
						statusCode =  HttpServletResponse.SC_CONFLICT;
						responseBody = "El pokemon no se puede eliminar porque esta vinculado con otros datos de la base de datos. Error -> " + esql;					
					
					} catch (Exception e) {
						
						statusCode =  HttpServletResponse.SC_NOT_FOUND;
					}			
				
			}
				
		} catch (Exception e) {
			LOG.info(e);
			
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			responseBody = "Tienes que enviar el tipo de datos correctos.";
			
		}
	}

}
