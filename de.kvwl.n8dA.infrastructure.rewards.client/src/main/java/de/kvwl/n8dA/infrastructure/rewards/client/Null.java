package de.kvwl.n8dA.infrastructure.rewards.client;

/**
 * 
 * Hilfsklasse mit Methoden für den Umgang mit null Werten.
 */
public class Null
{

	/**
	 * Testet auf null value und ersetzt den Wert ggf.
	 * 
	 * @param test Wert zum Überprüfen
	 * @param replace Ersatzwert
	 */
	public static <T> T nvl(T test, T replace)
	{

		if (test == null)
		{

			return replace;
		}

		return test;
	}

	/**
	 * Überprüft, ob mindestens ein Wert null ist.
	 */
	public static boolean isAnyNull(Object... objs)
	{

		for (int i = 0; i < objs.length; i++)
		{

			if (objs[i] == null)
			{

				return true;
			}
		}

		return false;
	}

	/**
	 * Überprüft, ob mindestens einer der Strings null oder empty ist.
	 */
	public static boolean isAnyEmpty(String... strings)
	{

		for (int i = 0; i < strings.length; i++)
		{

			if (strings[i] == null || strings[i].isEmpty())
			{

				return true;
			}
		}

		return false;
	}

	/**
	 * Überprüft, ob alle Werte null sind.
	 */
	public static boolean isNull(Object... objs)
	{

		for (int i = 0; i < objs.length; i++)
		{

			if (objs[i] != null)
			{

				return false;
			}
		}

		return true;
	}

	/**
	 * Überprüft, ob alle Werte ungleic null sind.
	 */
	public static boolean isNotNull(Object... objs)
	{

		for (int i = 0; i < objs.length; i++)
		{

			if (objs[i] == null)
			{

				return false;
			}
		}

		return true;
	}
}
