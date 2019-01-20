package nmo;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

/**
 * Redirects an OutputStream to a Log4j2 logger
 */
public class StdOutErrOutputStream extends OutputStream
{
	private final Logger logger;
	private final Level logLevel;

	public StdOutErrOutputStream(Logger logger, Level logLevel)
	{
		super();
		this.logger = logger;
		this.logLevel = logLevel;
	}

	@Override
	public void write(byte[] b) throws IOException
	{
		String string = new String(b);
		if (!string.trim().isEmpty())
			this.logger.log(this.logLevel, string);
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		String string = new String(b, off, len);
		if (!string.trim().isEmpty())
			this.logger.log(this.logLevel, string);
	}

	String msg = "";

	@Override
	public void write(int b) throws IOException
	{
		String string = String.valueOf((char) b);
		if (b == '\n' || b == '\r')
		{
			if (!this.msg.trim().isEmpty())
				this.logger.log(this.logLevel, this.msg);
			this.msg = "";
		}
		else
		{
			this.msg += string;
		}
	}
}