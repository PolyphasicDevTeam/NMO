package nmo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.ReflectionUtil;

/** Provides a way of retrieving loggers that enforces Java logging manager property is set correctly.
 * The method {@link #getLogger()} is provided as the most convenient way to obtain a named Logger
 * based on the calling class name.
 */
public final class LogWrapper
{
	static
	{
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
	}

	/**
	 * Returns a Logger with the name of the calling class.
	 * @return The Logger for the calling class.
	 * @throws UnsupportedOperationException if the calling class cannot be determined.
	 */
	public static final Logger getLogger()
	{
		return LogManager.getLogger(ReflectionUtil.getCallerClass(2));
	}

	/**
	 * Returns a Logger with the specified name.
	 *
	 * @param name The logger name. If null the name of the calling class will be used.
	 * @return The Logger.
	 * @throws UnsupportedOperationException if {@code name} is {@code null} and the calling class cannot be determined.
	 */
	public static final Logger getLogger(String name)
	{
		return LogManager.getLogger(name);
	}
}
