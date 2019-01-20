package nmo.integration.pavlok;

import java.util.Map;
import org.apache.logging.log4j.Logger;
import nmo.Action;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.config.NMOConfiguration;
import nmo.utils.Communicator;

public class IntegrationPavlok extends Integration
{
	public IntegrationPavlok()
	{
		super("pavlok");
	}

	public static final IntegrationPavlok INSTANCE = new IntegrationPavlok();
	private static final Logger log = LogWrapper.getLogger();

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.pavlok.enabled;
	}

	@Override
	public void init()
	{
		this.actions.put("/pavlok/led", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				IntegrationPavlok.this.led(4, "received a LED flash from NMO");
			}

			@Override
			public String getName()
			{
				return "FLASH PAVLOK LED";
			}

			@Override
			public String getDescription()
			{
				return "Makes the Pavlok wearable shock bracelet flash its LED light.";
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return false;
			}
		});
		this.actions.put("/pavlok/beep", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				IntegrationPavlok.this.beep(255, "received a beep from NMO");
			}

			@Override
			public String getName()
			{
				return "BEEP PAVLOK";
			}

			@Override
			public String getDescription()
			{
				return "Makes the Pavlok wearable shock bracelet beep loudly.";
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return false;
			}
		});
		this.actions.put("/pavlok/vibration", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				IntegrationPavlok.this.vibration(255, "received a vibration from NMO");
			}

			@Override
			public String getName()
			{
				return "VIBRATE PAVLOK";
			}

			@Override
			public String getDescription()
			{
				return "Makes the Pavlok wearable shock bracelet vibrate.";
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return false;
			}
		});
		this.actions.put("/pavlok/shock", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				IntegrationPavlok.this.shock(255, "received a zap from NMO");
			}

			@Override
			public String getName()
			{
				return "SHOCK PAVLOK";
			}

			@Override
			public String getDescription()
			{
				return "Makes the Pavlok wearable shock bracelet send an electric shock to the wearer.";
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return false;
			}
		});
	}

	@Override
	public void update() throws Exception
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void shutdown()
	{
		// TODO Auto-generated method stub
	}

	public static OAuthResponse postAuthToken(String code) throws Exception
	{
		return Communicator.basicJsonMessage("get oauthtoken", "http://pavlok-mvp.herokuapp.com/oauth/token", new OAuthToken(code), OAuthResponse.class, null);
	}

	public void led(long amount, String reason) throws Exception
	{
		log.info("Sending: led " + amount + " (" + reason + ")");
		Communicator.basicJsonMessage("led", "http://pavlok-mvp.herokuapp.com/api/v1/stimuli/led/" + amount, new Stimuli(amount, NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token, reason), String.class, "Bearer " + NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token);
	}

	public void beep(long amount, String reason) throws Exception
	{
		log.info("Sending: beep " + amount + " (" + reason + ")");
		Communicator.basicJsonMessage("beep", "http://pavlok-mvp.herokuapp.com/api/v1/stimuli/beep/" + amount, new Stimuli(amount, NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token, reason), String.class, "Bearer " + NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token);
	}

	public void vibration(long amount, String reason) throws Exception
	{
		log.info("Sending: vibration " + amount + " (" + reason + ")");
		Communicator.basicJsonMessage("vibration", "http://pavlok-mvp.herokuapp.com/api/v1/stimuli/vibration/" + amount, new Stimuli(amount, NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token, reason), String.class, "Bearer " + NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token);
	}

	public void shock(long amount, String reason) throws Exception
	{
		log.info("Sending: shock " + amount + " (" + reason + ")");
		Communicator.basicJsonMessage("shock", "http://pavlok-mvp.herokuapp.com/api/v1/stimuli/shock/" + amount, new Stimuli(amount, NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token, reason), String.class, "Bearer " + NMOConfiguration.INSTANCE.integrations.pavlok.auth.access_token);
	}
}
