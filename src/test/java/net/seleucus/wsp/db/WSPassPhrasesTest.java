package net.seleucus.wsp.db;

import static org.junit.Assert.*;

import java.nio.CharBuffer;

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

	@Test
	public void testUpdatePassPhraseShouldReturnFalseIfIDDoesNotExist() throws Exception {
	
		WSDatabase myDB = new WSDatabase();
		
		final int ppID = 28;
		final CharSequence passSeq = CharBuffer.wrap("default".toCharArray());
		
		assertFalse(myDB.passPhrases.updatePassPhrase(ppID, passSeq));
		
	}
	
	@Test
	public void testUpdatePassPhraseShouldReturnFalseIfIDIsNegative() throws Exception {
		
		WSDatabase myDB = new WSDatabase();
		
		final int ppID = -16;
		final CharSequence passSeq = CharBuffer.wrap("default".toCharArray());
		
		assertFalse(myDB.passPhrases.updatePassPhrase(ppID, passSeq));
		
	}
	
	@Test
	public void testUpdatePassPhraseShouldReturnTrueIfIDExists() throws Exception {
		
		final CharSequence passSeq = CharBuffer.wrap("default".toCharArray());
		final int ppID = 11;
		
		WSDatabase myDB = new WSDatabase();
		myDB.users.addUser("FirstName LastName", passSeq, "some@email.com", "+1(808) 212 4455");
		
		assertTrue(myDB.passPhrases.updatePassPhrase(ppID, passSeq));
		
	}

}
