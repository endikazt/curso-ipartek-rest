package com.ipartek.formacion.supermercado.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilidadesTest {
	
	/*

	@Test
	public void testObtenerId() throws Exception {
		
		assertEquals(-1, Utilidades.obtenerId(null) );
		
		assertEquals(-1, Utilidades.obtenerId("/") );
		assertEquals(-1, Utilidades.obtenerId("/pepe") );
		assertEquals(-1, Utilidades.obtenerId("/pepe/") );
		assertEquals(2, Utilidades.obtenerId("/2") );
		assertEquals(2, Utilidades.obtenerId("/2/") );
		assertEquals(99, Utilidades.obtenerId("/99/") );
		
		try {
			assertEquals(99, Utilidades.obtenerId("/99/333/hola/") );
			fail("Deberia haber lanzado Exception");
			
		}catch (Exception e) {
			
			assertTrue(true);
		}	
		
		
		
		
	}
	
	*/
	

	@Test
	public void contarPalabrasTest1() {
		
		assertEquals(0, Utilidades.contarPalabras(null));
		assertEquals(0, Utilidades.contarPalabras(""));
		assertEquals(0, Utilidades.contarPalabras("                 "));
		assertEquals(2, Utilidades.contarPalabras("Hola mundo"));
		assertEquals(2, Utilidades.contarPalabras("Hola               mundo"));
		assertEquals(2, Utilidades.contarPalabras("        Hola           mundo         "));
		assertEquals(2, Utilidades.contarPalabras("Hola, mundo"));
		
	}
	
	@Test
	public void contarPalabrasTest2() {
		
		assertEquals(2, Utilidades.contarPalabras("Hola, mundo"));
		assertEquals(2, Utilidades.contarPalabras("Hola...?mundo"));
	}
	

}
