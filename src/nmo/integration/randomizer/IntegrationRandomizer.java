package nmo.integration.randomizer;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import nmo.Action;
import nmo.Integration;
import nmo.Main;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;

public class IntegrationRandomizer extends Integration
{
	public static final IntegrationRandomizer INSTANCE = new IntegrationRandomizer();

	private IntegrationRandomizer()
	{
		super("randomizer");
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.randomizer.enabled;
	}

	@Override
	public void init() throws Exception
	{
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.randomizer.randomizers.length; i++)
		{
			final RandomizerEntry randomizer = NMOConfiguration.INSTANCE.integrations.randomizer.randomizers[i];
			this.actions.put("/randomizer/" + i, new Action()
			{
				String description = null;

				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					int option = ThreadLocalRandom.current().nextInt(randomizer.actions.length);
					String randPath = randomizer.actions[option];
					MainDialog.triggerEvent("Randomizer " + randomizer.name + " fired", new String[] { randPath }, parameters);
				}

				@Override
				public String getName()
				{
					return "RANDOMIZE " + randomizer.name;
				}

				@Override
				public String getDescription()
				{
					if (this.description == null)
					{
						this.description = "";
						for (int j = 0; j < randomizer.actions.length; j++)
						{
							String desc = null;
							for (Integration integration : Main.integrations)
							{
								Action action = integration.getActions().get(randomizer.actions[j]);
								if (action != null)
								{
									desc = action.getName();
									break;
								}
							}
							this.description += "\n* " + (desc == null ? randomizer.actions[j] : desc);
						}
					}
					return "Randomly selects one of the following actions:" + this.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return randomizer.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return randomizer.secret;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return randomizer.secret;
				}
			});
		}
	}

	@Override
	public void update() throws Exception
	{
		// nothing to do
	}

	@Override
	public void shutdown() throws Exception
	{
		// nothing to do
	}

}
