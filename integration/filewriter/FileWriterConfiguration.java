package nmo.integration.filewriter;

import com.google.gson.annotations.Expose;

public class FileWriterConfiguration
{
	@Expose
	public boolean scheduleName = false;

	@Expose
	public boolean scheduleStartedOn = false;

	@Expose
	public boolean scheduleLastOversleep = false;

	@Expose
	public boolean schedulePersonalBest = false;

	@Expose
	public boolean timeToNextSleepBlock = false;
}