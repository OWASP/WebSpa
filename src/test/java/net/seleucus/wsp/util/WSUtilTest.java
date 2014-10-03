package net.seleucus.wsp.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class WSUtilTest {

	@Test
	public void testHasMinJreRequirements() {
		String javaSpecVersion[] = System.getProperty(
				"java.specification.version").split("\\.");

		assertTrue(WSUtil.hasMinJreRequirements(
				Integer.valueOf(javaSpecVersion[0]),
				Integer.valueOf(javaSpecVersion[1])));
		assertTrue(WSUtil.hasMinJreRequirements(
				Integer.valueOf(javaSpecVersion[0]),
				Integer.valueOf(javaSpecVersion[1]) - 1));
		assertFalse(WSUtil.hasMinJreRequirements(
				Integer.valueOf(javaSpecVersion[0]),
				Integer.valueOf(javaSpecVersion[1]) + 1));
	}

}
