package com.ipartek.formacion.supermercado.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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
import com.ipartek.formacion.supermercado.modelo.dao.ProductoDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


/**
 * Servlet implementation class ProductoRestController
 */
@WebServlet( { "/producto/*" } )
public class ProductoRestController extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(ProductoRestController.class);
	private ProductoDAO productoDao;
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		productoDao = ProductoDAO.getInstance();
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		productoDao = null;
	}

	/**
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// prepara la response		
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8");
		
		super.service(request, response);   // llama a doGEt, doPost, doPut, doDelete
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.trace("peticion GET");
		
		String pathInfo = request.getPathInfo();
		PrintWriter out = response.getWriter();
		String jsonResponseBody = "";
		
		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		if("/".equals(pathInfo)) {
			
			//obtener productos de la BD
			ArrayList<Producto> lista = (ArrayList<Producto>) productoDao.getAll();
			
			response.setStatus( HttpServletResponse.SC_OK);
			
			jsonResponseBody = new Gson().toJson(lista);	 // conversion de Java a Json	
			out.print(jsonResponseBody.toString()); 		 // out se encarga de poder escribir datos en el body
			out.flush();  								     // termina de escribir datos en response body	
			
		} else {
				
			try {
				
				int pathInfoData = Integer.parseInt(pathInfo.substring(1));
				
				Producto producto = productoDao.getById(pathInfoData);
				
				if(producto == null) {
					
					response.setStatus( HttpServletResponse.SC_ACCEPTED);
					out.print("No existe ningun producto con ese ID :(");
					
				} else {
					
					response.setStatus( HttpServletResponse.SC_OK);
					jsonResponseBody = new Gson().toJson(producto);	
					out.print(jsonResponseBody.toString());
					out.flush(); 	
					
				}
				
			} catch (Exception e) {
				LOG.info("El parametro pasado no es un numero. Error -> " + e);
				
				// response status code
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				out.print("Tienes que enviar el tipo de datos correctos. Ejemplo supermecado-rest/producto/1");
				
			}
			
			
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.debug("POST crear recurso");
		
		PrintWriter out = response.getWriter();
		
		// convertir json del request body a Objeto
		BufferedReader reader = request.getReader();               
		Gson gson = new Gson();
		
		Producto producto;
		try {
			producto = gson.fromJson(reader, Producto.class);
			
			try {
				productoDao.create(producto);
				
				response.setStatus( HttpServletResponse.SC_CREATED);
				out.print("Producto creado correctamente :)");
				
			} catch (MySQLIntegrityConstraintViolationException esql) {
				
				LOG.error("No se ha podido insertar el registro en la base de datos. Error -> " + esql);
				
				response.setStatus( HttpServletResponse.SC_CONFLICT);
				out.print("No se ha podido crear el producto porque el ID o el Nombre ya existen en la base de datos.");
				
				
			} catch (Exception e) {
				LOG.error(e);
				
				response.setStatus( HttpServletResponse.SC_CONFLICT);
				out.print("No se ha podido crear el producto por algun error de la base de datos.");
			}
			
			
		} catch (JsonSyntaxException e1) {
			LOG.error(e1);

			response.setStatus( HttpServletResponse.SC_CONFLICT);
			out.print("El formato de los datos es incorrecto.");
			
		} catch (JsonIOException e1) {
			LOG.error(e1);
			
			response.setStatus( HttpServletResponse.SC_CONFLICT);
			out.print("Tienes que enviar el tipo de datos correctos.");
			
		}
	
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("PUT modificar recurso");
		
		String pathInfo = request.getPathInfo();
		PrintWriter out = response.getWriter();
		String jsonResponseBody = "";
		BufferedReader reader = request.getReader();               
		Gson gson = new Gson();
		
		if("/".equals(pathInfo)) {
			
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
			out.print("Tienes que indicar el producto que quieres modificar. Ejemplo supermecado-rest/producto/1");
			
		} else {
				
			try {
				
				int pathInfoData = Integer.parseInt(pathInfo.substring(1));
				
				Producto producto = productoDao.getById(pathInfoData);
				
				if(producto == null) {
					
					response.setStatus( HttpServletResponse.SC_ACCEPTED);
					out.print("No existe ningun producto con ese ID :(");
					
				} else {
					
					try {
						
						producto = gson.fromJson(reader, Producto.class);
						
						response.setStatus( HttpServletResponse.SC_CREATED);
						
						jsonResponseBody = new Gson().toJson(producto);	
						out.print("Producto modificado correctamente.");
						out.print(jsonResponseBody.toString());
						out.flush(); 	
					
						
					} catch (Exception e) {
						LOG.error(e);

						response.setStatus( HttpServletResponse.SC_CONFLICT);
						out.print("El formato de los datos es incorrecto.");
						
					}
				}
						
				
			} catch (Exception e) {
				LOG.info("El parametro pasado no es un numero. Error -> " + e);
				
				// response status code
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				out.print("Tienes que enviar el tipo de datos correctos. Ejemplo supermecado-rest/producto/1");
				
			}	
			
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.debug("DELETE eliminar recurso");
		
		String pathInfo = request.getPathInfo();
		PrintWriter out = response.getWriter();
		String jsonResponseBody = "";
		
		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		if("/".equals(pathInfo)) {
			
			response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
			out.print("Tienes que indicar el producto que quieres eliminar. Ejemplo supermecado-rest/producto/1");			     // termina de escribir datos en response body	
			
		} else {
				
			try {
				
				int pathInfoData = Integer.parseInt(pathInfo.substring(1));
				
				Producto producto = productoDao.getById(pathInfoData);
				
				if(producto == null) {
					
					response.setStatus( HttpServletResponse.SC_ACCEPTED);
					out.print("No existe ningun producto con ese ID :(");
					
				} else {
					
					productoDao.delete(pathInfoData);
					
					response.setStatus( HttpServletResponse.SC_OK);
					
				}
				
			} catch (Exception e) {
				LOG.info("El parametro pasado no es un numero. Error -> " + e);
				
				// response status code
				response.setStatus( HttpServletResponse.SC_BAD_REQUEST);
				out.print("Tienes que enviar el tipo de datos correctos. Ejemplo supermecado-rest/producto/1");
				
			}
			
		}
	}

}
