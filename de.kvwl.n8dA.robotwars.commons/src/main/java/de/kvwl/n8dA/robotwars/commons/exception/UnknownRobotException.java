package de.kvwl.n8dA.robotwars.commons.exception;

public class UnknownRobotException extends Exception
{

	private static final long serialVersionUID = 1L;

	public UnknownRobotException()
	{
		super();
	}

	public UnknownRobotException(String text)
	{
		super(text);
	}

}
