package de.kvwl.n8dA.robotwars.commons.utils;

public class Null {

	/**
	 * Testet auf null value und ersetzt den Wert ggf.
	 * 
	 * @param test
	 *            Wert zum Überprüfen
	 * @param replace
	 *            Ersatzwert
	 */
	public static <T> T nvl(T test, T replace) {

		if (test == null) {

			return replace;
		}

		return test;
	}

	/**
	 * Überprüft, ob mindestens ein Wert null ist.
	 */
	public static boolean isAnyNull(Object... objs) {

		for (int i = 0; i < objs.length; i++) {

			if (objs[i] == null) {

				return true;
			}
		}

		return false;
	}

	/**
	 * Überprüft, ob alle Werte null sind.
	 */
	public static boolean isNull(Object... objs) {

		for (int i = 0; i < objs.length; i++) {

			if (objs[i] != null) {

				return false;
			}
		}

		return true;
	}

	/**
	 * Überprüft, ob alle Werte ungleic null sind.
	 */
	public static boolean isNotNull(Object... objs) {

		for (int i = 0; i < objs.length; i++) {

			if (objs[i] == null) {

				return false;
			}
		}

		return true;
	}
}
