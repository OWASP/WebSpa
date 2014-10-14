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

	@Test
	public void testIsAnswerPositive() {
		assertTrue(WSUtil.isAnswerPositive("yes"));
		assertTrue(WSUtil.isAnswerPositive("Yes"));
		assertTrue(WSUtil.isAnswerPositive("YES"));
		assertTrue(WSUtil.isAnswerPositive("y"));
		assertTrue(WSUtil.isAnswerPositive("Y"));

		assertFalse(WSUtil.isAnswerPositive("no"));
		assertFalse(WSUtil.isAnswerPositive("No"));
		assertFalse(WSUtil.isAnswerPositive("NO"));
		assertFalse(WSUtil.isAnswerPositive("n"));
		assertFalse(WSUtil.isAnswerPositive("N"));
		assertFalse(WSUtil.isAnswerPositive("not an expected possible answer"));
	}

}
