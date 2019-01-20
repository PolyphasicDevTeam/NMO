package nmo.integration.noise;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.mp4parser.IsoFile;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javazoom.spi.mpeg.sampled.file.MpegAudioFileReader;
import nmo.Action;
import nmo.CommonUtils;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.config.NMOConfiguration;

public class IntegrationNoise extends Integration
{
	private static final Logger log = LogWrapper.getLogger();

	public IntegrationNoise()
	{
		super("noise");
	}

	public static final IntegrationNoise INSTANCE = new IntegrationNoise();
	public static List<PlayingNoise> PLAYING_NOISES = Collections.synchronizedList(new ArrayList<PlayingNoise>());

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.noise.enabled;
	}

	@Override
	public void init()
	{
		log.info("Loading noises...");
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.noise.noises.length; i++)
		{
			final StoredNoise noise = NMOConfiguration.INSTANCE.integrations.noise.noises[i];
			try
			{
				if (noise.path.toLowerCase(Locale.ENGLISH).startsWith("http:") || noise.path.toLowerCase(Locale.ENGLISH).startsWith("https:"))
				{
					// format unsupported
					noise.duration = -1;
				}
				else if (noise.path.toLowerCase(Locale.ENGLISH).endsWith(".wav"))
				{
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(noise.path));
					AudioFormat format = audioInputStream.getFormat();
					long frameLen = audioInputStream.getFrameLength();
					long duration = (long) ((frameLen + 0.0D) / format.getFrameRate() + 0.99D);
					noise.duration = duration;
				}
				else if (noise.path.toLowerCase(Locale.ENGLISH).endsWith(".mp3"))
				{
					File file = new File(noise.path);
					AudioFileFormat baseFileFormat = new MpegAudioFileReader().getAudioFileFormat(file);
					Map<String, Object> properties = baseFileFormat.properties();
					long duration = (Long) properties.get("duration") / 1000000L;
					noise.duration = duration;
				}
				else if (noise.path.toLowerCase(Locale.ENGLISH).endsWith(".m4a") || noise.path.toLowerCase(Locale.ENGLISH).endsWith(".m4p") || noise.path.toLowerCase(Locale.ENGLISH).endsWith(".aac"))
				{
					File file = new File(noise.path);
					try (IsoFile isoFile = new IsoFile(file))
					{
						double lengthInSeconds = (double) isoFile.getMovieBox().getMovieHeaderBox().getDuration() / isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
						noise.duration = (long) lengthInSeconds;
					}
				}
				else
				{
					// format unsupported
					noise.duration = -1;
				}
				log.info("Duration of " + noise.path + " is " + noise.duration + " seconds");
			}
			catch (Throwable t)
			{
				t.printStackTrace();
				// unknown;
			}
			this.actions.put("/noise/" + i, new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationNoise.this.play(noise);
				}

				@Override
				public String getName()
				{
					String durationString = noise.duration == -1 ? "" : " (" + (noise.duration / 60) + ":" + StringUtils.leftPad("" + noise.duration % 60, 2, "0") + ")";
					return "PLAY " + noise.name + durationString;
				}

				@Override
				public String getDescription()
				{
					String durationString = noise.duration == -1 ? "" : " (" + (noise.duration / 60) + ":" + StringUtils.leftPad("" + noise.duration % 60, 2, "0") + ")";
					return "Plays the audio clip '" + noise.name + "'" + durationString + ".\n" + noise.description;
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return noise.hidden;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return noise.secret;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return noise.secret;
				}
			});
		}
		this.actions.put("/noise/stop", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				PlayingNoise[] noises = PLAYING_NOISES.toArray(new PlayingNoise[0]);
				for (PlayingNoise noise : noises)
				{
					noise.stop();
				}
			}

			@Override
			public String getName()
			{
				return "STOP ALL NOISES";
			}

			@Override
			public String getDescription()
			{
				return "Stops all noises from playing immediately.";
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

	public String getNoiseList()
	{
		PlayingNoise[] noises = PLAYING_NOISES.toArray(new PlayingNoise[0]);
		if (noises.length == 0)
		{
			return "STOPPED";
		}
		else
		{
			String pnl = "";
			for (int i = 0; i < noises.length; i++)
			{
				pnl += (i > 0 ? ", " : "PLAYING (" + noises.length + "): ") + noises[i].name;
			}
			return pnl;
		}
	}

	public void play(StoredNoise noise)
	{
		Media media;
		if (noise.path.startsWith("http://") || noise.path.startsWith("https://"))
		{
			media = new Media(noise.path);
		}
		else
		{
			File file = CommonUtils.redirectRelativePathToAppDirectory(noise.path);
			media = new Media(file.getAbsoluteFile().toURI().toString());
		}
		final PlayingNoise playingNoise = new PlayingNoise(new MediaPlayer(media), noise.name);
		Runnable endHook = new Runnable()
		{
			@Override
			public void run()
			{
				playingNoise.stop();
			}
		};
		playingNoise.player.setOnEndOfMedia(endHook);
		playingNoise.player.setOnError(endHook);
		PLAYING_NOISES.add(playingNoise);
		playingNoise.player.play();
	}
}
