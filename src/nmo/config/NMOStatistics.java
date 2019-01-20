package nmo.config;

import com.google.gson.annotations.Expose;
import nmo.Configuration;

public class NMOStatistics
{
	public static NMOStatistics INSTANCE;

	public static void load() throws Exception
	{
		INSTANCE = Configuration.load(NMOStatistics.class, "stats.json");
	}

	public static void save() throws Exception
	{
		Configuration.save(INSTANCE, "stats.json");
	}

	@Expose
	public long scheduleStartedOn = 0;

	@Expose
	public long scheduleLastOversleep = 0;

	@Expose
	public long schedulePersonalBest = 0;
}
