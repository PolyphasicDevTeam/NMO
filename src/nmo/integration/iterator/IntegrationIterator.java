package nmo.integration.iterator;

import java.util.Map;
import nmo.Action;
import nmo.Integration;
import nmo.Main;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;

public class IntegrationIterator extends Integration
{
	public static final IntegrationIterator INSTANCE = new IntegrationIterator();

	private IntegrationIterator()
	{
		super("iterator");
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.iterator.enabled;
	}

	@Override
	public void init() throws Exception
	{
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.iterator.iterators.length; i++)
		{
			final IteratorEntry iterator = NMOConfiguration.INSTANCE.integrations.iterator.iterators[i];
			this.actions.put("/iterator/" + i, new Action()
			{
				String description = null;

				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					String randPath = iterator.actions[iterator.option];
					MainDialog.triggerEvent("Iterator " + iterator.name + " fired", new String[] { randPath }, parameters);
					iterator.option = (iterator.option + 1) % iterator.actions.length;
				}

				@Override
				public String getName()
				{
					return "ITERATE " + iterator.name;
				}

				@Override
				public String getDescription()
				{
					if (this.description == null)
					{
						this.description = "";
						for (int j = 0; j < iterator.actions.length; j++)
						{
							String desc = null;
							for (Integration integration : Main.integrations)
							{
								Action action = integration.getActions().get(iterator.actions[j]);
								if (action != null)
								{
									desc = action.getName();
									break;
								}
							}
							this.description += "\n* " + (desc == null ? iterator.actions[j] : desc);
						}
					}
					return "Iterates between the following actions:" + this.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return iterator.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return iterator.secret;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return iterator.secret;
				}
			});

			this.actions.put("/iterator/" + i + "/reset", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					iterator.option = 0;
				}

				@Override
				public String getName()
				{
					return "RESET ITERATOR: " + iterator.name;
				}

				@Override
				public String getDescription()
				{
					return "Resets this iterator to make it start again from the beginning";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return true;
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
		// nothing to do
	}

	@Override
	public void shutdown() throws Exception
	{
		// nothing to do
	}

}
