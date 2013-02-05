package net.seleucus.wsp.server;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assume.assumeThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

public class WSActionsPendingReaderTest {
	
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

}


