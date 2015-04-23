package de.kvwl.n8dA.infrastructure.rewards.client;

/**
 * Wird bei ungültigen eingaben von Spielernamen geworfen. Z.b. null oder empty strings
 */
public class PlayerNameException extends Exception
{
	private static final long serialVersionUID = 1L;

	public PlayerNameException(String string)
	{

		super(string);
	}

}
