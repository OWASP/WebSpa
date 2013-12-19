package net.seleucus.wsp.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WSConstantsTest {

	@Test
	public void testAccessLogFileLocationConstant() {
		
		assertEquals(WSConstants.ACCESS_LOG_FILE_LOCATION, "access-log-file-location");

	}
	
	@Test
	public void testLoggingRegexForEachRequestConstant() {
		
		assertEquals(WSConstants.LOGGING_REGEX_FOR_EACH_REQUEST, "logging-regex-for-each-request");

	}
}
