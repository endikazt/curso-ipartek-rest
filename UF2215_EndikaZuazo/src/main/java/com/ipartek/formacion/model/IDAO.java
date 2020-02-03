package com.ipartek.formacion.model;


import java.util.List;

import com.ipartek.formacion.model.pojo.Libro;


public interface IDAO<P> {
	
		
	/**
	 * Obtiene todos los datos
	 * @return lista de pojos
	 */
	List<P> getAll();
	
	/**
	 * recupera un pojo por su identificador
	 * @param id identificador
	 * @return pojo si lo encuentra, si no null
	 */
	P getById(int id);
	
	/**
	 * Elimina
	 * @param id identificador
	 * @return Pojo eliminado
	 * @throws Exception si no se puede eliminar o no encontrado
	 */
	P delete(int id) throws Exception;

	/**
	 * Modifica un Pojo
	 * @param id identificador
	 * @param pojo contiene los datos a modificar
	 * @return pojo modificado
	 * @throws Exception si no puede modificar o no lo encuentra
	 */
	P update(int id, P pojo)  throws Exception;
	
	/**
	 * crea un nuevo pojo
	 * @param pojo a crear
	 * @return pojo creado con el id nuevo
	 * @throws Exception si no puede crear
	 */
	P create(P pojo)  throws Exception;
	
	/**
	 * Recupera un pojo por su titulo/nombre
	 * @param titulo
	 * @return Pojo si lo encuentra, pojo vacio si no lo encuentra
	 */

	P getByNombre(String nombre);

}