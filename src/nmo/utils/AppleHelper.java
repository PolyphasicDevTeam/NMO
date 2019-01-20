package nmo.utils;

import java.net.URL;
import javax.imageio.ImageIO;
import com.apple.eawt.Application;

public class AppleHelper
{
	@SuppressWarnings("deprecation")
	public static void integrate()
	{
		try
		{
			Application appleEawtApplication = Application.getApplication();
			appleEawtApplication.setDockIconImage(ImageIO.read(new URL(JavaFxHelper.buildResourcePath("icon.png"))));
			appleEawtApplication.setEnabledAboutMenu(false);
			appleEawtApplication.setAboutHandler(null);
			appleEawtApplication.setEnabledPreferencesMenu(false);
			appleEawtApplication.setPreferencesHandler(null);
		}
		catch (Throwable e)
		{
			e.printStackTrace();
		}
	}
}
