package net.seleucus.wsp.util;

public class WSUtil {

	/**
	 * Private constructor to avoid instantiation of this class.
	 */
	private WSUtil() {

	}

	/**
	 * Checks if the major and minor version for a JRE is satisfied. Returns
	 * true if the minimum requirement are fulfilled, false otherwise.
	 * 
	 * @param majorVersion
	 * @param minorVersion
	 * @return
	 */
	public static boolean hasMinJreRequirements(final int majorVersion,
			final int minorVersion) {

		String javaSpecVersion[] = System.getProperty(
				"java.specification.version").split("\\.");

		if (Integer.valueOf(javaSpecVersion[0]) >= majorVersion
				&& Integer.valueOf(javaSpecVersion[1]) >= minorVersion) {
			return true;
		}

		return false;
	}

	public static boolean isAnswerPositive(final String answer) {
		return "yes".equalsIgnoreCase(answer) || "y".equalsIgnoreCase(answer);
	}

}
