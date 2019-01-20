package nmo;

import java.util.Map;
import nmo.config.NMOStatistics;

public class ScheduleFakeIntegration extends Integration
{
	public ScheduleFakeIntegration()
	{
		super("schedule");
	}

	public static ScheduleFakeIntegration INSTANCE = new ScheduleFakeIntegration();

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public void init() throws Exception
	{
		if (NMOStatistics.INSTANCE.scheduleStartedOn > 0)
		{
			this.actions.put("/schedule/resetLastOversleep", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					NMOStatistics.INSTANCE.scheduleLastOversleep = System.currentTimeMillis();
					try
					{
						NMOStatistics.save();
					}
					catch (Throwable t)
					{
						t.printStackTrace(); // damn
					}
				}

				@Override
				public String getName()
				{
					return "RESET LAST OVERSLEEP";
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

				@Override
				public String getDescription()
				{
					return "Resets time since last oversleep.";
				}
			});
		}
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
}
