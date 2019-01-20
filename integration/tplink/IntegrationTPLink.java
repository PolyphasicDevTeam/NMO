package nmo.integration.tplink;

import java.util.Map;
import nmo.Action;
import nmo.Integration;
import nmo.config.NMOConfiguration;

public class IntegrationTPLink extends Integration
{
	public static final IntegrationTPLink INSTANCE = new IntegrationTPLink();
	private int updateloop = 0;

	private IntegrationTPLink()
	{
		super("tplink");
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.tplink.enabled;
	}

	@Override
	public void init() throws Exception
	{
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.tplink.devices.length; i++)
		{
			final TPLinkDeviceEntry entry = NMOConfiguration.INSTANCE.integrations.tplink.devices[i];
			final TPLinkDevice device = new TPLinkDevice(entry.ipAddress);
			this.actions.put("/tplink/" + i + "/on", new Action()
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
					return "Turns on TP-Link device '" + entry.name + "'.\n" + entry.description;
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
			this.actions.put("/tplink/" + i + "/off", new Action()
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
					return "Turns off TP-Link device '" + entry.name + "'.\n" + entry.description;
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
			this.actions.put("/tplink/" + i + "/toggle", new Action()
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
					return "Toggles the state (on/off) of the TP-Link device '" + entry.name + "'.\n" + entry.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
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
		if (this.updateloop > 30)
		{
			this.updateloop -= 30;
			for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.tplink.devices.length; i++)
			{
				final TPLinkDeviceEntry entry = NMOConfiguration.INSTANCE.integrations.tplink.devices[i];
				final TPLinkDevice device = new TPLinkDevice(entry.ipAddress);
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
