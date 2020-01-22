package com.ipartek.formacion.supermercado.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class UtilidadesTest {

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

}
