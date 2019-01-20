package nmo.integration.webui;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import nmo.Action;
import nmo.CommonUtils;
import nmo.Integration;
import nmo.MainDialog;
import nmo.config.NMOConfiguration;

public class PauseFakeIntegration extends Integration
{
	public PauseFakeIntegration()
	{
		super("pause");
	}

	public static PauseFakeIntegration INSTANCE = new PauseFakeIntegration();

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.webUI.allowRemotePauseControl;
	}

	@Override
	public void init() throws Exception
	{
		this.actions.put("/pause/0", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				Platform.runLater(new Runnable()
				{
					@Override
					public void run()
					{
						MainDialog.pausedUntil = 0;
						MainDialog.isCurrentlyPaused.set(false);
						Map<String, String[]> parameters = new HashMap<>();
						String context = "Unpaused via action trigger";
						parameters.put("context", new String[] { context });
						MainDialog.triggerEvent(context, NMOConfiguration.INSTANCE.events.pauseCancelled, parameters);
					}
				});
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
				return false;
			}

			@Override
			public String getName()
			{
				return "TRIGGERED UNPAUSE";
			}

			@Override
			public String getDescription()
			{
				return "Unpause";
			}
		});

		for (int i = 1; i <= 720; i++)
		{
			final int j = i;
			this.actions.put("/pause/" + i, new Action()
			{
				@Override
				public void onAction(final Map<String, String[]> parameters) throws Exception
				{
					Platform.runLater(new Runnable()
					{
						@Override
						public void run()
						{
							if (parameters != null)
							{
								for (String k : parameters.keySet())
								{
									System.out.println(k + " = " + parameters.get(k)[0]);
								}
							}
							String reason = getName();
							;
							if (parameters != null && parameters.containsKey("reason"))
							{
								reason = parameters.get("reason")[0];
							}
							MainDialog.pausedUntil = MainDialog.now + (j * 60000);
							MainDialog.pauseReason = reason;
							MainDialog.pauseIsScheduleRelated = false;
							Map<String, String[]> parameters = new HashMap<>();
							String context = "Triggered pause for " + j + " minutes for \"" + reason + "\" (until " + CommonUtils.dateFormatter.get().format(MainDialog.pausedUntil) + ")";
							parameters.put("context", new String[] { context });
							MainDialog.triggerEvent(context, NMOConfiguration.INSTANCE.events.pauseInitiated, parameters);
						}
					});
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
					return false;
				}

				@Override
				public String getName()
				{
					return "TRIGGERED " + j + " MINUTE PAUSE";
				}

				@Override
				public String getDescription()
				{
					return "Pause for " + j + " minutes";
				}
			});
		}
	}

	@Override
	public void update() throws Exception
	{

	}

	@Override
	public void shutdown() throws Exception
	{

	}
}
