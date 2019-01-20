package nmo.exceptions;

public class BlankResponseException extends RuntimeException
{
	private static final long serialVersionUID = -4229720761435828007L;

	public BlankResponseException(String m)
	{
		super(m);
	}
}
