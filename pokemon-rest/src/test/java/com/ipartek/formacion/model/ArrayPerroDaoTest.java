package com.ipartek.formacion.model;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ipartek.formacion.model.pojo.Perro;

public class ArrayPerroDaoTest {
	
	private static ArrayPerroDao dao;
	private static Perro mock;
	private static final int MOCK_ID_INEXISTENTE = -1;	
	private static int indice = 1;
	private static final String MOCK_NOMBRE = "pulgas mock";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		dao = ArrayPerroDao.getInstance();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		dao = null;
	}

	@Before
	public void setUp() throws Exception {
		
		
		mock = new Perro( indice , MOCK_NOMBRE );
		dao.create(mock);
		indice++;
	}

	@After
	public void tearDown() throws Exception {
		
		dao.delete(mock.getId());		
		mock = null;
		
	}

	@Test
	public void testGetAll() throws Exception {

		assertEquals("solo 1 perro ",1, dao.getAll().size() );
		
		//dao.delete( mock.getId() );		
		//assertEquals("No deberia quedar ninguno",0, dao.getAll().size() );
		
		
	}

	@Test
	public void testGetById() {
		
		assertNull("Es imposible que exista este perro",dao.getById(-1));
		
		Perro p = dao.getById(indice);
		assertSame("deberia existir pulgas",p, mock);
		
	}

	@Test
	public void testDelete() {

		try {
			
			//solucionar bug tearDown, creamos un perro, para eliminarlo
			
			Perro pEliminar = new Perro("Pulgas 345");			
			dao.create(pEliminar);
			Perro p = dao.delete(pEliminar.getId());
			assertSame(p, pEliminar);
			
			//assertEquals("No queda ninguno",0, dao.getAll().size() );
			
			dao.delete(MOCK_ID_INEXISTENTE);
			fail("deberia haber lanzaxo Exception");
			
		}catch (Exception e) {
			assertTrue("lanzada exception correctamente la eliminar perro que no existe",true);
		}	
		
		
		
	}

	@Test
	public void testUpdate() {
		//TODO testUpdate
	}

	@Test
	public void testCreate() {
		//TODO testCreate
	}

}
