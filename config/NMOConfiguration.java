package nmo.config;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;
import nmo.ActivityTimer;
import nmo.Configuration;
import nmo.CustomEvent;
import nmo.SleepEntry;
import nmo.integration.cmd.CommandLineConfiguration;
import nmo.integration.discord.DiscordConfiguration;
import nmo.integration.filewriter.FileWriterConfiguration;
import nmo.integration.input.KeyboardConfiguration;
import nmo.integration.input.MidiConfiguration;
import nmo.integration.input.MouseConfiguration;
import nmo.integration.input.XboxControllerConfiguration;
import nmo.integration.iterator.IteratorConfiguration;
import nmo.integration.noise.NoiseConfiguration;
import nmo.integration.pavlok.PavlokConfiguration;
import nmo.integration.philipshue.PhilipsHueConfiguration;
import nmo.integration.randomizer.RandomizerConfiguration;
import nmo.integration.tplink.TPLinkConfiguration;
import nmo.integration.twilio.TwilioConfiguration;
import nmo.integration.webui.WebUIConfiguration;
import nmo.integration.wemo.WemoConfiguration;

public class NMOConfiguration
{
	public static NMOConfiguration INSTANCE;

	public static void load() throws Exception
	{
		INSTANCE = Configuration.load(NMOConfiguration.class, "config.json");
	}

	public static void save() throws Exception
	{
		Configuration.save(INSTANCE, "config.json");
	}

	@Expose
	public String scheduleName = "";

	@Expose
	public ArrayList<SleepEntry> schedule = new ArrayList<>();

	@Expose
	public ArrayList<ActivityTimer> timers = new ArrayList<>();

	@Expose
	public int defaultTimer = 0;

	@Expose
	public int oversleepWarningThreshold = 5;

	@Expose
	public int garbageCollectionFrequency = 3600;

	@Expose
	public EventConfiguration events = new EventConfiguration();

	public static class EventConfiguration
	{
		@Expose
		public String[] coreApproaching = new String[0];

		@Expose
		public String[] coreStarted = new String[0];

		@Expose
		public String[] coreEnded = new String[0];

		@Expose
		public String[] napApproaching = new String[0];

		@Expose
		public String[] napStarted = new String[0];

		@Expose
		public String[] napEnded = new String[0];

		@Expose
		public String[] siestaApproaching = new String[0];

		@Expose
		public String[] siestaStarted = new String[0];

		@Expose
		public String[] siestaEnded = new String[0];

		@Expose
		public String[] afkApproaching = new String[0];

		@Expose
		public String[] afkStarted = new String[0];

		@Expose
		public String[] afkEnded = new String[0];

		@Expose
		public String[] activityWarning1 = new String[0];

		@Expose
		public String[] activityWarning2 = new String[0];

		@Expose
		public String[] oversleepWarning = new String[0];

		@Expose
		public String[] zombiePenaltyLimitReached = new String[0];

		@Expose
		public String[] pauseInitiated = new String[0];

		@Expose
		public String[] pauseCancelled = new String[0];

		@Expose
		public String[] pauseExpired = new String[0];

		@Expose
		public CustomEvent[] custom = new CustomEvent[0];
	}

	@Expose
	public IntegrationConfiguration integrations = new IntegrationConfiguration();

	public static class IntegrationConfiguration
	{
		@Expose
		public KeyboardConfiguration keyboard = new KeyboardConfiguration();

		@Expose
		public MouseConfiguration mouse = new MouseConfiguration();

		@Expose
		public XboxControllerConfiguration xboxController = new XboxControllerConfiguration();

		@Expose
		public MidiConfiguration midiTransmitter = new MidiConfiguration();

		@Expose
		public WebUIConfiguration webUI = new WebUIConfiguration();

		@Expose
		public PavlokConfiguration pavlok = new PavlokConfiguration();

		@Expose
		public TwilioConfiguration twilio = new TwilioConfiguration();

		@Expose
		public PhilipsHueConfiguration philipsHue = new PhilipsHueConfiguration();

		@Expose
		public TPLinkConfiguration tplink = new TPLinkConfiguration();

		@Expose
		public WemoConfiguration wemo = new WemoConfiguration();

		@Expose
		public NoiseConfiguration noise = new NoiseConfiguration();

		@Expose
		public CommandLineConfiguration cmd = new CommandLineConfiguration();

		@Expose
		public FileWriterConfiguration fileWriter = new FileWriterConfiguration();

		@Expose
		public DiscordConfiguration discord = new DiscordConfiguration();

		@Expose
		public IteratorConfiguration iterator = new IteratorConfiguration();

		@Expose
		public RandomizerConfiguration randomizer = new RandomizerConfiguration();
	}
}
