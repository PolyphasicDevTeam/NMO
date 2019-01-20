package nmo.integration.input;

import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import nmo.ActivitySource;
import nmo.Integration;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;

public class IntegrationKeyboard extends Integration
{
	public IntegrationKeyboard()
	{
		super("keyboard");
	}

	public static final IntegrationKeyboard INSTANCE = new IntegrationKeyboard();
	public static final ActivitySource KEY_TYPED = new ActivitySource("keyTyped");
	public static final ActivitySource KEY_PRESSED = new ActivitySource("keyPressed");
	public static final ActivitySource KEY_RELEASED = new ActivitySource("keyReleased");
	NativeKeyListener keyboardHook;

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.keyboard.enabled;
	}

	@Override
	public void init()
	{
		this.keyboardHook = new NativeKeyListener()
		{
			@Override
			public void nativeKeyTyped(NativeKeyEvent nativeEvent)
			{
				MainDialog.resetActivityTimer(KEY_TYPED);
			}

			@Override
			public void nativeKeyPressed(NativeKeyEvent nativeEvent)
			{
				MainDialog.resetActivityTimer(KEY_PRESSED);
			}

			@Override
			public void nativeKeyReleased(NativeKeyEvent nativeEvent)
			{
				MainDialog.resetActivityTimer(KEY_RELEASED);
			}
		};
		GlobalScreen.addNativeKeyListener(this.keyboardHook);
	}

	@Override
	public void update() throws Exception
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shutdown()
	{
		if (this.keyboardHook != null)
		{
			GlobalScreen.removeNativeKeyListener(this.keyboardHook);
		}
	}
}
