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
import com.ipartek.formacion.supermercado.modelo.dao.ProductoDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;


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

		
		super.service(request, response);   // llama a doGEt, doPost, doPut, doDelete
		
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.trace("peticion GET");
		
		String pathInfo = request.getPathInfo();
		
		LOG.debug("mirar pathInfo:" + pathInfo + " para saber si es listado o detalle" );
		
		//obtener productos de la BD
		ArrayList<Producto> lista = (ArrayList<Producto>) productoDao.getAll();
		
		// prepara la response		
		response.setContentType("application/json"); 
		response.setCharacterEncoding("utf-8");
		
		// response body
		PrintWriter out = response.getWriter();		               // out se encarga de poder escribir datos en el body
		String jsonResponseBody = new Gson().toJson(lista);		   // conversion de Java a Json
		out.print(jsonResponseBody.toString()); 	
		out.flush();                                               // termina de escribir datos en response body
		
		// response status code
		response.setStatus( HttpServletResponse.SC_OK );
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		LOG.debug("POST crear recurso");
		
		// convertir json del request body a Objeto
		BufferedReader reader = request.getReader();               
		Gson gson = new Gson();
		Producto producto = gson.fromJson(reader, Producto.class);
		
		LOG.debug(" Json convertido a Objeto: " + producto);
		
		response.setStatus( HttpServletResponse.SC_NOT_IMPLEMENTED );
	}

	/**
	 * @see HttpServlet#doPut(HttpServletRequest, HttpServletResponse)
	 */
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus( HttpServletResponse.SC_NOT_IMPLEMENTED );
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setStatus( HttpServletResponse.SC_NOT_IMPLEMENTED );
	}

}
