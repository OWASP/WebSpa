package net.seleucus.wsp.io;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.Test;

public class FileCheckerTest {

	@Test(expected=FileNotFoundException.class)
	public final void testFileChecker1() throws Exception {
		new FileChecker(null);
	}
	
	@Test(expected=FileNotFoundException.class)
	public final void testFileChecker2() throws Exception {
		new FileChecker("");
	}
	
	@Test(expected=IOException.class)
	public final void testFileChecker3() throws Exception {
		new FileChecker("     ");
	}

	@Test(expected=IOException.class)
	public final void testFileChecker4() throws Exception {
		new FileChecker("directory-name/file.extension-different-to-dir-name");
	}

}
