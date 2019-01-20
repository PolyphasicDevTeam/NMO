package nmo.utils;

import java.io.PrintStream;
import java.util.Calendar;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.ConsoleAppender;
import org.apache.logging.log4j.core.appender.RollingRandomAccessFileAppender;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.OnStartupTriggeringPolicy;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;
import nmo.Main;
import nmo.PlatformData;
import nmo.StdOutErrOutputStream;

public class Logging
{
	static int minCopyrightYear = 2017; // min year
	public static Logger log = null;

	static PrintStream oldOut = System.out;
	static PrintStream oldErr = System.err;

	public static void initialize()
	{
		// REDIRECT JAVA UTIL LOGGER TO LOG4J2 (MUST BE BEFORE ALL LOG4J2 CALLS)
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");

		// STARTING CONFIGURATION
		final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
		org.apache.logging.log4j.core.config.Configuration configuration = loggerContext.getConfiguration();
		LoggerConfig rootLogger = configuration.getLoggerConfig("");
		rootLogger.setLevel(Level.ALL);

		// PATTERNS
		PatternLayout consolePattern = PatternLayout.createLayout("%d{yyyy-MM-dd HH:mm:ss} [%level] [%logger{1}]: %msg%n", configuration, null, null, true, false, null, null);
		PatternLayout logfilePattern = PatternLayout.createLayout("%d{yyyy-MM-dd HH:mm:ss} [%level] [%logger]: %msg%n", configuration, null, null, true, false, null, null);

		// LOG FILE STRINGS
		String logName = "NoMoreOversleeps";
		String logFilePrefix = PlatformData.installationDirectory.getAbsolutePath().replace("\\", "/") + "/logs/" + WordUtils.capitalizeFully(logName, new char[] { '_', '-', ' ' }).replaceAll("_", "").replaceAll("_", "").replaceAll("-", "").replaceAll(" ", "");

		// CLIENT LOG FILE APPENDER (ROLLING)
		RollingRandomAccessFileAppender clientInfoLogFile = RollingRandomAccessFileAppender.createAppender(logFilePrefix + "-0.log", logFilePrefix + "-%i.log", null, "InfoFile", null, null, OnStartupTriggeringPolicy.createPolicy(), DefaultRolloverStrategy.createStrategy("2", "1", "min", null, configuration), logfilePattern, null, null, null, null, configuration);
		clientInfoLogFile.start();
		configuration.addAppender(clientInfoLogFile);
		rootLogger.addAppender(clientInfoLogFile, Level.INFO, null);

		/*
		// FINER DETAIL LOG FILE (REPLACED ON EACH RUN)
		RandomAccessFileAppender detailLogFile = RandomAccessFileAppender.createAppender(logFilePrefix + "-latest-fine.log", "false", "DetailFile", null, null, null, logfilePattern, null, null, null, configuration);
		detailLogFile.start();
		configuration.addAppender(detailLogFile);
		rootLogger.addAppender(detailLogFile, Level.ALL, null);
		*/

		// CONSOLE APPENDER
		ConsoleAppender console = ConsoleAppender.createAppender(consolePattern, null, "SYSTEM_OUT", "Console", null, null); // must be named "Console" to work correctly
		console.start();
		configuration.addAppender(console);
		rootLogger.addAppender(console, Level.INFO, null);

		// UPDATE LOGGERS
		loggerContext.updateLoggers();

		// REDIRECT STDOUT AND STDERR TO LOG4J2
		System.setOut(new PrintStream(new StdOutErrOutputStream(LogManager.getLogger("java.lang.System.out"), Level.INFO)));
		System.setErr(new PrintStream(new StdOutErrOutputStream(LogManager.getLogger("java.lang.System.err"), Level.ERROR)));

		// set main engine log
		log = LogManager.getLogger();

		// print opening header
		log.info("===============================================================================================================");
		log.info(" NoMoreOversleeps v" + Main.VERSION);
		log.info(" (c) NMO developers, " + Math.max(Calendar.getInstance().get(Calendar.YEAR), minCopyrightYear));
		log.info("===============================================================================================================");
		log.debug("The system log manager is " + System.getProperty("java.util.logging.manager"));
		log.info("Install path: " + PlatformData.installationDirectory.getAbsolutePath());
		log.info("Computer name: " + PlatformData.computerName);
		log.info("Platform: " + PlatformData.platformName);
	}

	public static void shutdown()
	{
		if (log != null)
		{
			log.info("===============================================================================================================");
			log.info(" Thank you for using NoMoreOversleeps");
			log.info("===============================================================================================================");
		}
		try
		{
			Thread.sleep(100);
		}
		catch (InterruptedException e)
		{
			// 
		}
		System.setOut(oldOut);
		System.setErr(oldErr);
	}
}
