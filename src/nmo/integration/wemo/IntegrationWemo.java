package nmo.integration.wemo;

import java.util.Map;
import nmo.Action;
import nmo.Integration;
import nmo.config.NMOConfiguration;

public class IntegrationWemo extends Integration
{
	public static final IntegrationWemo INSTANCE = new IntegrationWemo();
	private int updateloop = 0;

	private IntegrationWemo()
	{
		super("wemo");
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.wemo.enabled;
	}

	@Override
	public void init() throws Exception
	{
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.wemo.devices.length; i++)
		{
			final WemoDeviceEntry entry = NMOConfiguration.INSTANCE.integrations.wemo.devices[i];
			final WemoDevice device = new WemoDevice(entry.ipAddress);
			this.actions.put("/wemo/" + i + "/on", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					device.toggle(true);
				}

				@Override
				public String getName()
				{
					return "TURN ON " + entry.name;
				}

				@Override
				public String getDescription()
				{
					return "Turns on WeMo Insight device '" + entry.name + "'.\n" + entry.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return entry.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return entry.secret;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return entry.secret;
				}
			});
			this.actions.put("/wemo/" + i + "/off", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					device.toggle(false);
				}

				@Override
				public String getName()
				{
					return "TURN OFF " + entry.name;
				}

				@Override
				public String getDescription()
				{
					return "Turns off WeMo Insight device '" + entry.name + "'.\n" + entry.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return entry.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return entry.secret;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return entry.secret;
				}
			});
			this.actions.put("/wemo/" + i + "/toggle", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					device.toggle(!device.isOn());
				}

				@Override
				public String getName()
				{
					return "TOGGLE " + entry.name;
				}

				@Override
				public String getDescription()
				{
					return "Toggles the state (on/off) of the WeMo Insight device '" + entry.name + "'.\n" + entry.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return entry.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return true;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return true;
				}
			});
		}
	}

	@Override
	public void update() throws Exception
	{
		this.updateloop++;
		if (this.updateloop > 120)
		{
			this.updateloop -= 120;
			for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.wemo.devices.length; i++)
			{
				final WemoDeviceEntry entry = NMOConfiguration.INSTANCE.integrations.wemo.devices[i];
				final WemoDevice device = new WemoDevice(entry.ipAddress);
				entry.isSwitchedOn = device.isOn();
			}
		}
	}

	@Override
	public void shutdown() throws Exception
	{
		// nothing to do
	}

}
