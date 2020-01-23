package com.ipartek.formacion.supermercado.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
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
import com.ipartek.formacion.supermercado.utils.Utilidades;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;


/**
 * Servlet implementation class ProductoRestController
 */
@WebServlet( { "/producto/*"} )
public class ProductoRestController extends HttpServlet {
	
	
	private static final long serialVersionUID = 1L;
	private final static Logger LOG = Logger.getLogger(ProductoRestController.class);
	private ProductoDAO productoDao;
	private PrintWriter out;
	private BufferedReader reader;
	private String responseBody;
	private int statusCode;
	

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		productoDao = ProductoDAO.getIntance();
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

		out = response.getWriter();
		
		// prepara la response		
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8");
		
		super.service(request, response);   // llama a doGEt, doPost, doPut, doDelete
		
		response.setStatus(statusCode); 	// Escribe el codigo de estado en la respuesta
		out.print(responseBody);			// Escribe el contenido en el cuerpo de la respuesta
		out.flush();
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.trace("peticion GET");
		
		String pathInfo = request.getPathInfo();

		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		try {
			
			int pathInfoData = Utilidades.obtenerId(pathInfo); 			// Comprueba si el id pasado es valido o no
			
			if(pathInfoData == -1) {									// Si el pathInfo es "/" el resultado del metodo es -1 entonces se le muestran todos los productos
				
				ArrayList<Producto> lista = (ArrayList<Producto>) productoDao.getAll();
				
				statusCode = HttpServletResponse.SC_OK;
				
				responseBody = new Gson().toJson(lista).toString();	 // conversion de Java a Json y escribir en la variable de body
				
			} else {
									
					try {
						
						Producto producto = productoDao.getById(pathInfoData);
						statusCode = HttpServletResponse.SC_OK;
						responseBody = new Gson().toJson(producto).toString();	
							
					} catch (Exception e) {

						statusCode = HttpServletResponse.SC_NOT_FOUND;
						responseBody = "No existe ningun producto con el id " + pathInfoData + " :(";
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
		
		Producto producto;
		try {
			producto = gson.fromJson(reader, Producto.class);					// Obtiene los datos del body y los conbierte en un obajeto de Producto
			
			try {
				Producto productoCreado = productoDao.create(producto);
				
				statusCode = HttpServletResponse.SC_CREATED;
				responseBody = "Producto creado correctamente :) \n" + productoCreado.toString();
				
			} catch (MySQLIntegrityConstraintViolationException esql) {
				
				LOG.error("No se ha podido insertar el registro en la base de datos. Error -> " + esql);
				
				statusCode = HttpServletResponse.SC_CONFLICT;
				responseBody = "No se ha podido crear el producto porque el ID o el Nombre ya existen en la base de datos.";
				
				
			} catch (Exception e) {
				LOG.error(e);
				
				statusCode =  HttpServletResponse.SC_CONFLICT;
				responseBody = "No se ha podido crear el producto por algun error de la base de datos.";
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
						
						Producto producto = productoDao.getById(pathInfoData);	
						
						try {
							
							producto = gson.fromJson(reader, Producto.class);
							
							productoDao.update(pathInfoData, producto);
							
							statusCode = HttpServletResponse.SC_CREATED;
							responseBody = "Producto modificado correctamente. \n " + new Gson().toJson(producto).toString();	
							
							
						} catch (Exception e) {
							LOG.error(e);

							statusCode = HttpServletResponse.SC_CONFLICT;
							responseBody = "El formato de los datos es incorrecto.";
							
						}
							
					} catch (Exception e) {
						LOG.error(e);
						
						statusCode = HttpServletResponse.SC_ACCEPTED;
						responseBody = "No existe ningun producto con ese ID :(";
					}
			}
			
			
		}  catch (Exception e) {
			LOG.info("El parametro pasado no es un numero. Error -> " + e);
			
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			responseBody = "Tienes que enviar el tipo de datos correctos. Ejemplo supermecado-rest/producto/1";
			
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
				responseBody = "Tienes que indicar el producto que quieres eliminar. Ejemplo supermecado-rest/producto/1";
				
			} else {
					
					try {
						
						Producto producto = productoDao.getById(pathInfoData);	
						
						productoDao.deleteLogico(pathInfoData);
			
						statusCode = HttpServletResponse.SC_OK;
						responseBody = "Producto " + pathInfoData + " eliminado correctamente :)";
						
					} catch (MySQLIntegrityConstraintViolationException esql) {
						
						statusCode =  HttpServletResponse.SC_CONFLICT;
						responseBody = "El producto no se puede eliminar porque esta vinculado con otros datos de la base de datos. Errror -> " + esql;					
					
					} catch (Exception e) {
						
						statusCode =  HttpServletResponse.SC_NOT_FOUND;
						responseBody = "No existe ningun producto con ese ID :(";
					}			
				
			}
				
		} catch (Exception e) {
			LOG.info(e);
			
			statusCode = HttpServletResponse.SC_BAD_REQUEST;
			responseBody = "Tienes que enviar el tipo de datos correctos. Ejemplo supermecado-rest/producto/1";
			
		}
	}

}
