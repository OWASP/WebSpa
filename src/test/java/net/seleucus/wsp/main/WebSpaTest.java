package net.seleucus.wsp.main;

import net.seleucus.wsp.console.WSConsole;
import org.junit.Test;

import static net.seleucus.wsp.main.CommandLineArgument.*;
import static org.junit.Assert.assertEquals;

public class WebSpaTest {
	
	@Test
	public void testProcessParametersReturnsMinusTwoIfNoArguments() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{}), SHOW_HELP);
	}
	
	@Test
	public void testProcessParametersReturnsMinusOneIfAnInvalidArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		final String[] invalidParameter = {"asdfasdf"};
		assertEquals(myWebSpa.processParameters(invalidParameter), SHOW_HELP);
	}

	@Test
	public void testProcessParametersReturnsZeroIfHelpIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{SHOW_HELP.getName()}), SHOW_HELP);
	}
	
	@Test
	public void testProcessParameterReturnsOneIfClientIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{CLIENT_MODE.getName()}), CLIENT_MODE);
	}
	
	@Test
	public void testProcessParameterReturnsTwoIfServerIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{SERVER_MODE.getName()}), SERVER_MODE);
	}
	
	@Test
	public void testProcessParameterReturnsThreeIfVersionIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{SHOW_VERSION.getName()}), SHOW_VERSION);
	}
	
	@Test
	public void testProcessParameterReturnsFourIfStartIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{START.getName()}), START);
	}

	@Test
	public void testProcessParameterReturnsFiveIfStopIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{STOP.getName()}), STOP);
	}

	@Test
	public void testProcessParameterReturnsSixIfStatusIsTheFirstArgument() {
		final WebSpa myWebSpa = new WebSpa(WSConsole.getWsConsole());
		assertEquals(myWebSpa.processParameters(new String[]{SHOW_STATUS.getName()}), SHOW_STATUS);
	}
}
