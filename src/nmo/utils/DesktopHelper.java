package nmo.utils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import nmo.PlatformData;
import nmo.PlatformType;

public class DesktopHelper
{
	public static void browse(String uri) throws IOException, URISyntaxException
	{
		if (PlatformData.platformType == PlatformType.LINUX)
		{
			String[] array = new String[] { "xdg-open", uri };
			Runtime.getRuntime().exec(array);
		}
		else
		{
			java.awt.Desktop.getDesktop().browse(new URI(uri));
		}
	}

	public static void open(File file) throws IOException
	{
		if (PlatformData.platformType == PlatformType.LINUX)
		{
			String[] array = new String[] { "xdg-open", file.getAbsolutePath() };
			Runtime.getRuntime().exec(array);
		}
		else
		{
			java.awt.Desktop.getDesktop().open(file);
		}
	}
}
