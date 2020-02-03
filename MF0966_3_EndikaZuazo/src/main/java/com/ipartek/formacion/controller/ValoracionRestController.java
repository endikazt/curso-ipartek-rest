package com.ipartek.formacion.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.ipartek.formacion.model.ValoracionDAO;
import com.ipartek.formacion.model.pojo.Valoracion;
import com.ipartek.formacion.supermercado.utils.Utilidades;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;


/**
 * Servlet implementation class ProductoRestController
 */
@WebServlet( { "/valoracion/*"} )
public class ValoracionRestController extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(ValoracionRestController.class);
	private ValoracionDAO dao;
	private String responseBody;
	private String pathInfo;
	private int pathInfoData;
	private int statusCode;
	private ValidatorFactory factory;
	private Validator validator;
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		dao = ValoracionDAO.getInstance();
		factory  = Validation.buildDefaultValidatorFactory();
		validator  = factory.getValidator();
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
				
				ArrayList<Valoracion> lista;
				Valoracion valoracion;

				if (pathInfoData == -1) { // Si el pathInfo es "/" el resultado del metodo es -1 entonces se le muestran todos los libros
				

						lista = (ArrayList<Valoracion>) dao.getAll();
						
						statusCode = HttpServletResponse.SC_OK;
						responseBody = new Gson().toJson(lista).toString();

				} else {
					

					valoracion = dao.getById(pathInfoData);
					
					if(valoracion.getId() != 0) {
					
						statusCode = HttpServletResponse.SC_OK;
						responseBody = new Gson().toJson(valoracion).toString();

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
		
		try {
			
			// convertir json del request body a Objeto
			BufferedReader reader = request.getReader();               
			Gson gson = new Gson();
			Valoracion valoracion = gson.fromJson(reader, Valoracion.class);
			LOG.debug(" Json convertido a Objeto: " + valoracion);
		
			//validar objeto
			Set<ConstraintViolation<Valoracion>>  validacionesErrores = validator.validate(valoracion);		
			if ( validacionesErrores.isEmpty() ) {
		
				Valoracion valoracionGuardar = null;
			
				valoracionGuardar = dao.create(valoracion);
				statusCode =  HttpServletResponse.SC_CREATED;			
				
				responseBody = "Valoracion creada correctamente " + new Gson().toJson(valoracionGuardar);
				
			} else {
				
				statusCode =  HttpServletResponse.SC_BAD_REQUEST;				
				responseBody = "Valores de la valoracion incorrectos";
				ArrayList<String> errores = new ArrayList<String>();
				for (ConstraintViolation<Valoracion> error : validacionesErrores) {					 
					errores.add( error.getPropertyPath() + " " + error.getMessage() );
				}				
							
				responseBody = errores.toString();
				
				
			}	
			
		}catch ( MySQLIntegrityConstraintViolationException e) {	
			
			statusCode =  HttpServletResponse.SC_CONFLICT;
			responseBody = "nombre de producto repetido";
			
		} catch (Exception e) {
			
			statusCode =  HttpServletResponse.SC_BAD_REQUEST;
			responseBody = e.getMessage();		
		}
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		try {
			
			// convertir json del request body a Objeto
			BufferedReader reader = request.getReader();               
			Gson gson = new Gson();
			Valoracion valoracion = gson.fromJson(reader, Valoracion.class);
			LOG.debug(" Json convertido a Objeto: " + valoracion);
		
			//validar objeto
			Set<ConstraintViolation<Valoracion>>  validacionesErrores = validator.validate(valoracion);		
			if ( validacionesErrores.isEmpty() ) {
				
				if(pathInfoData != -1) {				
		
				Valoracion valoracionGuardar = null;
			
				valoracionGuardar = dao.update(pathInfoData, valoracion);
				statusCode =  HttpServletResponse.SC_OK;			
				
				responseBody = "Valoracion modificada corretcamenta => " + new Gson().toJson(valoracionGuardar);
				
				}
				
			} else {
				
				statusCode =  HttpServletResponse.SC_BAD_REQUEST;				
				responseBody = "Valores de la valoracion incorrectos";
				ArrayList<String> errores = new ArrayList<String>();
				for (ConstraintViolation<Valoracion> error : validacionesErrores) {					 
					errores.add( error.getPropertyPath() + " " + error.getMessage() );
				}				
							
				responseBody = errores.toString();
				
				
			}	
			
		}catch ( MySQLIntegrityConstraintViolationException e) {	
			
			statusCode =  HttpServletResponse.SC_CONFLICT;
			responseBody = "nombre de producto repetido";
			
		} catch (Exception e) {
			
			statusCode =  HttpServletResponse.SC_BAD_REQUEST;
			responseBody = e.getMessage();		
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
				responseBody = "Tienes que indicar el id de la valoracion que quieres eliminar. Ejemplo mfendika/valoracion/1";
				
			} else {
					
					try {
						
						Valoracion vaoracion = dao.getById(pathInfoData);	
						
						dao.delete(pathInfoData);
			
						statusCode = HttpServletResponse.SC_OK;
						responseBody = "Valoracion " + pathInfoData + " eliminada correctamente :)";
						
					} catch (MySQLIntegrityConstraintViolationException esql) {
						
						statusCode =  HttpServletResponse.SC_CONFLICT;
						responseBody = "La valoracion no se puede eliminar porque esta vinculado con otros datos de la base de datos. Errror -> " + esql;					
					
					} catch (Exception e) {
						
						statusCode =  HttpServletResponse.SC_NOT_FOUND;
						responseBody = "No existe ninguna valoracion con ese ID :(";
					}			
				
			}
				
		} catch (Exception e) {
			LOG.info(e);
			
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			responseBody = "Tienes que enviar el tipo de datos correctos. Ejemplo mfendika/valoracion/1";
			
		}
	}
}
