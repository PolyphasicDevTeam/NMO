package nmo.exceptions;

public class BadRequestException extends RuntimeException
{
	private static final long serialVersionUID = 2361240796306749515L;

	public BadRequestException(String message)
	{
		super(message);
	}
}
