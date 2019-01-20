package nmo.integration.input;

import java.util.Map;
import org.apache.logging.log4j.Logger;
import com.ivan.xinput.XInputDevice;
import com.ivan.xinput.enums.XInputButton;
import com.ivan.xinput.exceptions.XInputNotLoadedException;
import com.ivan.xinput.listener.XInputDeviceListener;
import nmo.Action;
import nmo.ActivitySource;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;
import nmo.integration.input.XboxControllerConfiguration.PlayerConfiguration;

public class IntegrationXboxController extends Integration
{
	public IntegrationXboxController()
	{
		super("xboxController");
	}

	public static final IntegrationXboxController INSTANCE = new IntegrationXboxController();
	public static final ActivitySource[] XBOX_CONTROLLER = { new ActivitySource("xboxController/P1"), new ActivitySource("xboxController/P2"), new ActivitySource("xboxController/P3"), new ActivitySource("xboxController/P4") };
	private static final Logger log = LogWrapper.getLogger();
	XInputDevice[] devices = new XInputDevice[4];
	int[] vibrateTimers = new int[] { 0, 0, 0, 0 };
	XInputDeviceListener listener;

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.xboxController.P1.enabled || NMOConfiguration.INSTANCE.integrations.xboxController.P2.enabled || NMOConfiguration.INSTANCE.integrations.xboxController.P3.enabled || NMOConfiguration.INSTANCE.integrations.xboxController.P4.enabled;
	}

	@Override
	public void init() throws XInputNotLoadedException
	{
		final PlayerConfiguration[] confs = { NMOConfiguration.INSTANCE.integrations.xboxController.P1, NMOConfiguration.INSTANCE.integrations.xboxController.P2, NMOConfiguration.INSTANCE.integrations.xboxController.P3, NMOConfiguration.INSTANCE.integrations.xboxController.P4 };
		for (int i = 0; i < 4; i++)
		{
			final int idx = i;
			final int player = i + 1;
			if (!confs[idx].enabled)
			{
				continue;
			}
			this.devices[idx] = XInputDevice.getDeviceFor(idx);
			this.devices[idx].addListener(new XInputDeviceListener()
			{
				@Override
				public void disconnected()
				{
					// do nothing
				}

				@Override
				public void connected()
				{
					// do nothing
				}

				@Override
				public void buttonChanged(XInputButton arg0, boolean arg1)
				{
					MainDialog.resetActivityTimer(XBOX_CONTROLLER[idx]);
				}
			});
			this.actions.put("/xboxController/P" + player + "/vibrate", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationXboxController.this.devices[idx].setVibration(Short.MAX_VALUE, Short.MAX_VALUE);
					IntegrationXboxController.this.vibrateTimers[idx] = confs[idx].vibrationLength;
					log.info("Vibrating controller " + idx);
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return false;
				}

				@Override
				public String getName()
				{
					return "XBOX CONTROLLER P" + (player) + " VIBRATE";
				}

				@Override
				public String getDescription()
				{
					return "Vibrates the XBOX Controller assigned to player " + (player);
				}
			});
		}
		;
	}

	@Override
	public void update()
	{
		for (int i = 0; i < 4; i++)
		{
			if (this.devices[i] != null)
			{
				this.devices[i].poll();
				if (this.vibrateTimers[i] > 0)
				{
					this.vibrateTimers[i]--;
					if (this.vibrateTimers[i] == 0)
					{
						this.devices[i].setVibration(0, 0);
						log.info("Stopping vibration on controller " + i);
					}
				}
			}
		}
	}

	@Override
	public void shutdown()
	{
		for (int i = 0; i < 4; i++)
		{
			if (this.devices[i] != null && this.listener != null)
			{
				this.devices[i].removeListener(this.listener);
			}
		}
	}
}
