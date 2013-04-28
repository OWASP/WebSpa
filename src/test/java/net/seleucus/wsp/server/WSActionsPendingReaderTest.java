package net.seleucus.wsp.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class WSActionsPendingReaderTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Test
	public void shouldBeAbleToInstantiateWSActionsPendingReaderIfFileExists() throws Exception {
		new WSActionsPendingReader("actions/wsp-server-actions-pending.acti");
	}
	
	@Test(expected=FileNotFoundException.class)
	public void shouldThrowAnExceptionIfTheFileDoesNotExist() throws Exception {
		new WSActionsPendingReader("actions/doesnotexist.acti");
	}
	
	@Test(expected=IOException.class)
	public void shouldThrowAnExceptionIfTheFileIsUnreadable() throws Exception {
		assumeThat(System.getProperty("os.name").toLowerCase(), not(containsString("win")));
		String pathToUnreadable = "actions/unreadable.acti";
		URL rootLocation = ClassLoader.getSystemResource(pathToUnreadable);
		File unreadable = new File(rootLocation.getFile());
		unreadable.setReadable(false);
		new WSActionsPendingReader(pathToUnreadable);
	}
	
	@Test
	public void shouldThrowAnExceptionIfTheFileHasMoreThan1024Lines() throws Exception {
		thrown.expect(InvalidActionsPendingFileException.class);
		thrown.expectMessage("Web-Spa Actions Pending File Has More Than 1024 Lines");
		new WSActionsPendingReader("actions/wsp-server-actions-more-than-1024.acti");
	}
	
	
	@Test
	public void shouldThrowAnExceptionIfTheFileHasMoreThan65535Characters() throws Exception {
		thrown.expect(InvalidActionsPendingFileException.class);
		thrown.expectMessage("Web-Spa Actions Pending File Has More Than 65535 Characters");
		new WSActionsPendingReader("actions/wsp-server-actions-more-than-65535-chars.acti");
	}

}


