package nmo;

import java.util.Map;
import nmo.config.NMOConfiguration;

public class ActivityTimerFakeIntegration extends Integration
{
	public ActivityTimerFakeIntegration()
	{
		super("timer");
	}

	public static ActivityTimerFakeIntegration INSTANCE = new ActivityTimerFakeIntegration();

	@Override
	public boolean isEnabled()
	{
		return true;
	}

	@Override
	public void init() throws Exception
	{
		if (NMOConfiguration.INSTANCE.timers.isEmpty())
		{
			ActivityTimer activityWarningTimer = new ActivityTimer();
			activityWarningTimer.name = "DEFAULT TIMER";
			activityWarningTimer.secondsForFirstWarning = 300;
			activityWarningTimer.secondsForSubsequentWarnings = 10;
			NMOConfiguration.INSTANCE.timers.add(activityWarningTimer);
			NMOConfiguration.save();
		}
		final int numTimers = NMOConfiguration.INSTANCE.timers.size();
		if (numTimers > 1)
		{
			for (int i = 0; i < numTimers; i++)
			{
				final ActivityTimer timer = NMOConfiguration.INSTANCE.timers.get(i);
				this.actions.put("/timer/" + i, new Action()
				{
					@Override
					public void onAction(Map<String, String[]> parameters) throws Exception
					{
						MainDialog.pendingTimer = timer;
					}

					@Override
					public String getName()
					{
						return "SET TIMER: " + timer.name + " (" + timer.secondsForFirstWarning + "s/" + timer.secondsForSubsequentWarnings + "s)";
					}

					@Override
					public boolean isHiddenFromFrontend()
					{
						return false;
					}

					@Override
					public boolean isHiddenFromWebUI()
					{
						return numTimers < 2;
					}

					@Override
					public boolean isBlockedFromWebUI()
					{
						return numTimers < 2;
					}

					@Override
					public String getDescription()
					{
						return "Sets the active timer to '" + timer.name + "'.\nFirst activity warning after " + timer.secondsForFirstWarning + "s and subsequent activity warnings every " + timer.secondsForSubsequentWarnings + "s.\n" + (timer.zombiePenaltyLimit == 0 ? "Zombie warning will not be triggered." : "Zombie penalty limit of " + timer.zombiePenaltyLimit + "s, increasing by " + timer.zombiePenaltyForFirstWarning + "s for first warning, " + timer.zombiePenaltyForOversleepWarning + "s for oversleep warning and " + timer.zombiePenaltyForOtherWarnings + "s for other warnings.");
					}
				});
			}
		}
		if (NMOConfiguration.INSTANCE.timers.size() > NMOConfiguration.INSTANCE.defaultTimer)
		{
			MainDialog.timer = NMOConfiguration.INSTANCE.timers.get(NMOConfiguration.INSTANCE.defaultTimer);
		}
		else
		{
			throw new RuntimeException("Invalid default timer " + NMOConfiguration.INSTANCE.defaultTimer + " set, which does not exist!");
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
