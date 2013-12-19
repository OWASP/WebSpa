package net.seleucus.wsp.main;

import static org.junit.Assert.assertEquals;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class WebSpaTest {
	
	@Test
	public void testProcessParametersReturnsMinusOneIfNoArguments() {
		
		WebSpa myWebSpa = new WebSpa(System.console());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 0, 0);
		assertEquals(argArray.length, 0);
		assertEquals(myWebSpa.processParameters(argArray), -1);

	}

	@Test
	public void testProcessParametersReturnsZeroIfHelpIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(System.console());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 0, 1);
		assertEquals(argArray[0], "-help");
		assertEquals(myWebSpa.processParameters(argArray), 0);
		
	}
	
	@Test
	public void testProcessParameterReturnsOneIfClientIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(System.console());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 1, 2);
		assertEquals(argArray[0], "-client");
		assertEquals(myWebSpa.processParameters(argArray), 1);
		
	}
	
	@Test
	public void testProcessParameterReturnsTwoIfServerIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(System.console());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 2, 3);
		assertEquals(argArray[0], "-server");
		assertEquals(myWebSpa.processParameters(argArray), 2);
		
	}
	
	@Test
	public void testProcessParameterReturnsThreeIfVersionIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(System.console());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 3, 4);
		assertEquals(argArray[0], "-version");
		assertEquals(myWebSpa.processParameters(argArray), 3);
		
	}

	@Test
	public void testProcessParameterProcessesOnlyTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(System.console());
		assertEquals(WebSpa.ALLOWED_FIRST_PARAM[0], "-help");
		assertEquals(myWebSpa.processParameters(WebSpa.ALLOWED_FIRST_PARAM), 0);
		
	}
	
}
