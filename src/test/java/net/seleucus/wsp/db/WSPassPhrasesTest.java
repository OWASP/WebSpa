package net.seleucus.wsp.db;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSPassPhrasesTest {

	@Before
	public void setUp() throws Exception {
		
		WSDatabaseTest.deleteAllDBFiles();
		
	}
	
	@After
	public void tearDown() throws Exception {
		
		WSDatabaseTest.deleteAllDBFiles();

	}

	@Test
	public void testGetLastModifiedDateReturnsDefaultIfTheIDIsZero() throws Exception {
		
		WSDatabase myDB = new WSDatabase();
		assertEquals(myDB.passPhrases.getLastModifiedDate(0), "0000-00-00 00:00:00.000");
		
	}
	
	@Test
	public void testGetLastModifiedDateReturnsDefaultIfNoUsersExist() throws Exception {
		
		WSDatabase myDB = new WSDatabase();
		assertEquals(myDB.passPhrases.getLastModifiedDate(1), "0000-00-00 00:00:00.000");
		
	}

}
