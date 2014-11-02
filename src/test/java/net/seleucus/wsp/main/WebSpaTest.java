package net.seleucus.wsp.main;

import static org.junit.Assert.assertEquals;
import net.seleucus.wsp.console.WSConsole;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

public class WebSpaTest {
	
	@Test
	public void testProcessParametersReturnsMinusTwoIfNoArguments() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 0, 0);
		assertEquals(argArray.length, 0);
		assertEquals(myWebSpa.processParameters(argArray), -2);

	}
	
	@Test
	public void testProcessParametersReturnsMinusOneIfAnInvalidArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] invalidParameter = {"asdfasdf"};
		assertEquals(myWebSpa.processParameters(invalidParameter), -1);
	}

	@Test
	public void testProcessParametersReturnsZeroIfHelpIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 0, 1);
		assertEquals(argArray[0], "-help");
		assertEquals(myWebSpa.processParameters(argArray), 0);
		
	}
	
	@Test
	public void testProcessParameterReturnsOneIfClientIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 1, 2);
		assertEquals(argArray[0], "-client");
		assertEquals(myWebSpa.processParameters(argArray), 1);
		
	}
	
	@Test
	public void testProcessParameterReturnsTwoIfServerIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 2, 3);
		assertEquals(argArray[0], "-server");
		assertEquals(myWebSpa.processParameters(argArray), 2);
		
	}
	
	@Test
	public void testProcessParameterReturnsThreeIfVersionIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 3, 4);
		assertEquals(argArray[0], "-version");
		assertEquals(myWebSpa.processParameters(argArray), 3);
		
	}
	
	@Test
	public void testProcessParameterReturnsFourIfStartIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 4, 5);
		assertEquals(argArray[0], "-start");
		assertEquals(myWebSpa.processParameters(argArray), 4);
		
	}

	@Test
	public void testProcessParameterReturnsFiveIfStopIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 5, 6);
		assertEquals(argArray[0], "-stop");
		assertEquals(myWebSpa.processParameters(argArray), 5);
		
	}

	@Test
	public void testProcessParameterReturnsSixIfStatusIsTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] argArray = ArrayUtils.subarray(WebSpa.ALLOWED_FIRST_PARAM, 6, 7);
		assertEquals(argArray[0], "-status");
		assertEquals(myWebSpa.processParameters(argArray), 6);
		
	}

	@Test
	public void testProcessParameterProcessesOnlyTheFirstArgument() {
		
		WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(WebSpa.ALLOWED_FIRST_PARAM[0], "-help");
		assertEquals(myWebSpa.processParameters(WebSpa.ALLOWED_FIRST_PARAM), 0);
		
	}
	
}
