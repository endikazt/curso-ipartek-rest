package com.ipartek.formacion.supermercado.utils;

public class Utilidades {
	
	/**
	 * 
	 * @param pathInfo
	 * @return
	 * @throws Exception
	 */
	
	public static int obtenerId(String pathInfo) throws Exception {
		
		int resul = 0;
		
		if(pathInfo.equals("/")) {
			
			resul = -1;			
			
		} else {

			try {
			
				if(pathInfo.endsWith("/")) {				
					
					resul = Integer.parseInt(pathInfo.substring(1, pathInfo.length()-1));
					
				} else {
					
					resul = Integer.parseInt(pathInfo.substring(1));
					
				}
				
			} catch (Exception e) {
				
				throw new Exception("Formato incorrecto. Solo puedes enviar un solo parametro de producto.");
				
			}
		
		}
		
		return resul;		
		
	}
	
}
