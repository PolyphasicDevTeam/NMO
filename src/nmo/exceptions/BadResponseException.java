package nmo.exceptions;

public class BadResponseException extends RuntimeException
{
	private static final long serialVersionUID = 2361240796306749515L;
	public final int response;

	public BadResponseException(int response)
	{
		super("Unexpected response from server: " + response);
		this.response = response;
	}
}
