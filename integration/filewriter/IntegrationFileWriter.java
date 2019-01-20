package nmo.integration.filewriter;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import com.ivan.xinput.exceptions.XInputNotLoadedException;
import nmo.Integration;
import nmo.MainDialog;
import nmo.PlatformData;
import nmo.config.NMOConfiguration;
import nmo.config.NMOStatistics;
import nmo.utils.FormattingHelper;

public class IntegrationFileWriter extends Integration
{
	public IntegrationFileWriter()
	{
		super("fileWriter");
	}

	public static final IntegrationFileWriter INSTANCE = new IntegrationFileWriter();
	private static int lastSecond;
	private static File scheduleNameFile;
	private static File scheduleStartedOnFile;
	private static File scheduleLastOversleepFile;
	private static File schedulePersonalBestFile;
	private static File timeToNextSleepBlockFile;

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleName || NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleStartedOn || NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleLastOversleep || NMOConfiguration.INSTANCE.integrations.fileWriter.schedulePersonalBest || NMOConfiguration.INSTANCE.integrations.fileWriter.timeToNextSleepBlock;
	}

	@Override
	public void init() throws XInputNotLoadedException
	{
		File directory = new File(PlatformData.installationDirectory, "out");
		directory.mkdirs();
		scheduleNameFile = new File(directory, "scheduleName");
		scheduleStartedOnFile = new File(directory, "scheduleStartedOn");
		scheduleLastOversleepFile = new File(directory, "scheduleLastOversleep");
		schedulePersonalBestFile = new File(directory, "schedulePersonalBest");
		timeToNextSleepBlockFile = new File(directory, "timeToNextSleepBlock");
	}

	@Override
	public void update()
	{
		Calendar calendar = Calendar.getInstance();
		int second = calendar.get(Calendar.SECOND);
		long now = calendar.getTimeInMillis();
		if (lastSecond != second)
		{
			lastSecond = second;
			try
			{
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleName)
				{
					FileUtils.writeStringToFile(scheduleNameFile, NMOConfiguration.INSTANCE.scheduleName, Charsets.UTF_8, false);
				}
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleStartedOn)
				{
					FileUtils.writeStringToFile(scheduleStartedOnFile, FormattingHelper.formatTimeElapsedWithDays(NMOStatistics.INSTANCE.scheduleStartedOn == 0 ? 0 : now, NMOStatistics.INSTANCE.scheduleStartedOn), Charsets.UTF_8, false);
				}
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.scheduleLastOversleep)
				{
					FileUtils.writeStringToFile(scheduleLastOversleepFile, FormattingHelper.formatTimeElapsedWithDays(NMOStatistics.INSTANCE.scheduleStartedOn == 0 ? 0 : now, NMOStatistics.INSTANCE.scheduleLastOversleep), Charsets.UTF_8, false);
				}
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.schedulePersonalBest)
				{
					FileUtils.writeStringToFile(schedulePersonalBestFile, MainDialog.nextSleepBlock == null ? "N/A" : FormattingHelper.formatTimeElapsedWithDays(NMOStatistics.INSTANCE.schedulePersonalBest, 0), Charsets.UTF_8, false);
				}
				if (NMOConfiguration.INSTANCE.integrations.fileWriter.timeToNextSleepBlock)
				{
					boolean currentlySleeping = MainDialog.nextSleepBlock == null ? false : MainDialog.nextSleepBlock.containsTimeValue(System.currentTimeMillis());
					String pros = MainDialog.nextActivityWarningID >= NMOConfiguration.INSTANCE.oversleepWarningThreshold ? "OVERSLEEPING" : MainDialog.nextActivityWarningID > 0 ? "MISSING" : "AWAKE";
					if (currentlySleeping)
					{
						long tims = MainDialog.nextSleepBlock.nextEndTime;
						FileUtils.writeStringToFile(timeToNextSleepBlockFile, MainDialog.nextSleepBlock.name + " [ends in " + FormattingHelper.formatTimeElapsedWithoutDays(tims, now - 59999) + "]", Charsets.UTF_8, false);
					}
					else if (MainDialog.isCurrentlyPaused.get())
					{
						FileUtils.writeStringToFile(timeToNextSleepBlockFile, "AFK [" + MainDialog.pauseReason + " - " + FormattingHelper.formatTimeElapsedWithoutDays(MainDialog.pausedUntil, now - 59999) + " left]", Charsets.UTF_8, false);
					}
					else if (MainDialog.nextSleepBlock == null)
					{
						FileUtils.writeStringToFile(timeToNextSleepBlockFile, pros, Charsets.UTF_8, false);
					}
					else
					{
						long tims = MainDialog.nextSleepBlock.nextStartTime;
						FileUtils.writeStringToFile(timeToNextSleepBlockFile, pros + " [" + FormattingHelper.formatTimeElapsedWithoutDays(tims, now - 59999) + " until " + MainDialog.nextSleepBlock.name + "]", Charsets.UTF_8, false);
					}
				}
			}
			catch (IOException t)
			{
				t.printStackTrace();
			}
		}
	}

	@Override
	public void shutdown()
	{
		// nothing to do
	}
}
