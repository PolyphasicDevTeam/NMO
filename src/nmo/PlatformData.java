package nmo;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Locale;

/**
 * Stores information about the system and location from which the application is currently running
 */
public class PlatformData
{
	public static final String pathSeparator = System.getProperty("path.separator");
	public static final String fileSeparator = System.getProperty("file.separator");
	public static final File installationDirectory;
	public static final boolean is64bitJVM;
	public static final boolean is64bitOS;
	public static final PlatformType platformType;
	public static final String platformName;
	public static final String computerName;

	static
	{
		// determine platform type
		String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
		if (osName.contains("win"))
		{
			platformType = PlatformType.WINDOWS;
		}
		else if (osName.contains("mac"))
		{
			platformType = PlatformType.MAC;
		}
		else if (osName.contains("linux") || osName.contains("unix"))
		{
			platformType = PlatformType.LINUX;
		}
		else if (osName.contains("solaris") || osName.contains("sunos"))
		{
			platformType = PlatformType.SOLARIS;
		}
		else
		{
			platformType = PlatformType.UNKNOWN;
		}

		// determine platform name
		platformName = System.getProperty("os.name") + " version " + System.getProperty("os.version") + " (" + System.getProperty("os.arch") + ")";

		// determine computer name
		String computerName1 = platformType == PlatformType.WINDOWS ? System.getenv("COMPUTERNAME") : System.getenv("HOSTNAME");
		if (computerName1 == null || computerName1.isEmpty())
		{
			try
			{
				computerName1 = InetAddress.getLocalHost().getHostName();
			}
			catch (UnknownHostException e)
			{
				computerName1 = ManagementFactory.getRuntimeMXBean().getName();
			}
		}
		computerName = computerName1;

		// determine OS and JVM bit-rates
		is64bitJVM = System.getProperty("os.arch").contains("64");
		if (platformType == PlatformType.WINDOWS)
		{
			String pf86 = System.getenv("ProgramFiles(x86)");
			String pf64 = System.getenv("ProgramFiles");
			is64bitOS = is64bitJVM || pf86 != null || (pf64 != null && pf64.contains("(x86)"));
		}
		else
		{
			// is there a better way to do this?
			is64bitOS = is64bitJVM;
		}

		// FUCK YOU JAVA
		File launcherPathFile;
		try
		{
			URL url;
			try
			{
				url = PlatformData.class.getProtectionDomain().getCodeSource().getLocation();
			}
			catch (SecurityException ex)
			{
				url = PlatformData.class.getResource(PlatformData.class.getSimpleName() + ".class");
			}
			final String urlString = url.toExternalForm();
			final File urlStringFile = urlString.endsWith(".jar") ? new File(url.toURI()).getParentFile() : new File(new File(url.toURI()).getParentFile(), "run");
			launcherPathFile = urlStringFile.getAbsoluteFile();
		}
		catch (Throwable t)
		{
			launcherPathFile = new File(".").getAbsoluteFile();
		}
		installationDirectory = launcherPathFile;
	}
}
