package nmo;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.logging.log4j.Logger;
import javafx.scene.control.Dialog;
import javafx.scene.text.Font;
import nmo.config.NMOConfiguration;
import nmo.config.NMOStatistics;
import nmo.config.NMOUsers;
import nmo.integration.cmd.IntegrationCommandLine;
import nmo.integration.discord.IntegrationDiscord;
import nmo.integration.filewriter.IntegrationFileWriter;
import nmo.integration.input.GlobalHookFakeIntegration;
import nmo.integration.input.IntegrationKeyboard;
import nmo.integration.input.IntegrationMidiTransmitter;
import nmo.integration.input.IntegrationMouse;
import nmo.integration.input.IntegrationXboxController;
import nmo.integration.iterator.IntegrationIterator;
import nmo.integration.noise.IntegrationNoise;
import nmo.integration.pavlok.IntegrationPavlok;
import nmo.integration.philipshue.IntegrationPhilipsHue;
import nmo.integration.randomizer.IntegrationRandomizer;
import nmo.integration.tplink.IntegrationTPLink;
import nmo.integration.twilio.IntegrationTwilio;
import nmo.integration.webui.IntegrationWebUI;
import nmo.integration.webui.PauseFakeIntegration;
import nmo.integration.wemo.IntegrationWemo;
import nmo.utils.AppleHelper;
import nmo.utils.JavaFxHelper;
import nmo.utils.Logging;

public class Main
{
	private static final Logger log = LogWrapper.getLogger();

	//-------------------------------------------
	public static String VERSION = "0.15-dev";
	public static String JAVA_UPDATE_URL = "https://www.oracle.com/technetwork/java/javase/downloads/";

	//-------------------------------------------
	public static String CLIENT_ID = "";
	public static String CLIENT_SECRET = "";
	public static String CLIENT_CALLBACK = "";

	//-------------------------------------------

	public static final ArrayList<Integration> integrations = new ArrayList<>();
	static
	{
		integrations.add(GlobalHookFakeIntegration.INSTANCE);
		integrations.add(IntegrationKeyboard.INSTANCE);
		integrations.add(IntegrationMouse.INSTANCE);
		integrations.add(IntegrationMidiTransmitter.INSTANCE);
		integrations.add(IntegrationNoise.INSTANCE);
		integrations.add(IntegrationPavlok.INSTANCE);
		integrations.add(IntegrationXboxController.INSTANCE);
		integrations.add(IntegrationPhilipsHue.INSTANCE);
		integrations.add(IntegrationTPLink.INSTANCE);
		integrations.add(IntegrationWemo.INSTANCE);
		integrations.add(IntegrationTwilio.INSTANCE);
		integrations.add(IntegrationCommandLine.INSTANCE);
		integrations.add(IntegrationFileWriter.INSTANCE);
		integrations.add(IntegrationDiscord.INSTANCE);
		integrations.add(ActivityTimerFakeIntegration.INSTANCE);
		integrations.add(ScheduleFakeIntegration.INSTANCE);
		integrations.add(PauseFakeIntegration.INSTANCE);
		integrations.add(IntegrationWebUI.INSTANCE);
		integrations.add(IntegrationIterator.INSTANCE);
		integrations.add(IntegrationRandomizer.INSTANCE);
	}

	//-------------------------------------------

	@SuppressWarnings({ "rawtypes", "unused" })
	public static void main(String[] args)
	{
		System.setProperty("java.net.preferIPv4Stack", "true"); // force this for now

		try
		{
			try
			{
				Class<Dialog> dialogClass = Dialog.class;
				if ((!PlatformData.installationDirectory.isDirectory()) && (!PlatformData.installationDirectory.mkdirs()))
				{
					throw new RuntimeException("The installation directory could not be created: " + PlatformData.installationDirectory.getAbsolutePath());
				}
				if (!MasterLock.obtain())
				{
					if (PlatformData.installationDirectory.getPath().contains("Program Files"))
					{
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						JOptionPane.showMessageDialog(null, "Don't run NMO from Program Files you idiot!", "NoMoreOversleeps", JOptionPane.ERROR_MESSAGE);
						return;
					}
					else
					{
						UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
						JOptionPane.showMessageDialog(null, "NoMoreOversleeps is already running or does not have permission to start.", "NoMoreOversleeps", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}
				Logging.initialize();
				try
				{
					NMOConfiguration.load();
				}
				catch (Throwable t)
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JOptionPane.showMessageDialog(null, "Parsing of config.json failed; please check for JSON syntax errors", "NoMoreOversleeps", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try
				{
					NMOStatistics.load();
				}
				catch (Throwable t)
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JOptionPane.showMessageDialog(null, "Parsing of stats.json failed; please check for JSON syntax errors", "NoMoreOversleeps", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try
				{
					NMOUsers.load();
				}
				catch (Throwable t)
				{
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					JOptionPane.showMessageDialog(null, "Parsing of users.json failed; please check for JSON syntax errors", "NoMoreOversleeps", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//===========================================================================================
				// include these fonts so that the software looks the same on every platform
				//===========================================================================================
				Font.loadFont(JavaFxHelper.buildResourcePath("Roboto-Bold.ttf"), 10);
				Font.loadFont(JavaFxHelper.buildResourcePath("Roboto-Regular.ttf"), 10);
				Font.loadFont(JavaFxHelper.buildResourcePath("Inconsolata-Regular.ttf"), 10);
				//===========================================================================================
				if (PlatformData.platformType == PlatformType.MAC)
				{
					AppleHelper.integrate();
				}
				for (Integration integration : integrations)
				{
					if (integration.isEnabled())
					{
						log.info("Initializing integration module : " + integration.id);
						integration.init();
					}
				}
				MainDialog.launch(MainDialog.class, args);
				Collections.reverse(integrations);
				for (Integration integration : integrations)
				{
					if (integration.isEnabled())
					{
						log.info("Shutting down integration module : " + integration.id);
						integration.shutdown();
					}
				}
				Logging.shutdown();
				try
				{
					Configuration.save(NMOStatistics.INSTANCE, "stats.json");
				}
				catch (Throwable t)
				{
					t.printStackTrace(); // damn
				}
				MasterLock.release();
				System.exit(0);
			}
			catch (NoClassDefFoundError e1)
			{
				e1.printStackTrace();
				String javaString = PlatformData.platformType == PlatformType.LINUX ? "JDK 8 and JavaFX 8u40 or later are" : "Java 8u40 or later is";
				complain("<html>" + javaString + " required to run this application. <b>You must update Java in order to continue.</b><br>Would you like to update Java now? (Clicking 'Yes' will open your web browser to the Java download page.)</html>");
			}
		}
		catch (Throwable e2)
		{
			String message = e2.getClass().getName() + ": " + e2.getMessage();
			e2.printStackTrace();
			JOptionPane.showMessageDialog(null, "Unrecoverable error: " + message + "\nNoMoreOversleeps will now close.", "NoMoreOversleeps", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
	}

	static void complain(String message) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, URISyntaxException, InterruptedException
	{
		// Resort to a Swing Y/N dialog asking if the user wants to update Java.
		// If they click yes, their default browser will open to the JAVA_UPDATE_URL
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		int reply = JOptionPane.showConfirmDialog(null, message, "NoMoreOversleeps", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
		if (reply == 0)
		{
			String platformCode = "";
			if (PlatformData.platformType == PlatformType.WINDOWS)
			{
				platformCode = PlatformData.is64bitOS ? "?platform=win64" : "?platform=win32";
			}
			else if (PlatformData.platformType == PlatformType.MAC)
			{
				platformCode = "?platform=mac";
			}
			else
			{
				platformCode = "?platform=linux";
			}
			java.awt.Desktop.getDesktop().browse(new URI(Main.JAVA_UPDATE_URL + platformCode));
			Thread.sleep(100);
		}
	}
}
