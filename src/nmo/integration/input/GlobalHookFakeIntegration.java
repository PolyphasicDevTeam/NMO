package nmo.integration.input;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import nmo.Integration;
import nmo.config.NMOConfiguration;

public class GlobalHookFakeIntegration extends Integration
{
	public GlobalHookFakeIntegration()
	{
		super("globalHook");
	}

	public static final GlobalHookFakeIntegration INSTANCE = new GlobalHookFakeIntegration();

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.keyboard.enabled || NMOConfiguration.INSTANCE.integrations.mouse.enabled;
	}

	@Override
	public void init() throws Exception
	{
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
		try
		{
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException e)
		{
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update() throws Exception
	{

	}

	@Override
	public void shutdown() throws Exception
	{
		try
		{
			GlobalScreen.unregisterNativeHook();
		}
		catch (NativeHookException e)
		{
			throw new RuntimeException(e);
		}
	}
}
