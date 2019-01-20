package nmo.integration.noise;

import javafx.scene.media.MediaPlayer;

public class PlayingNoise
{
	PlayingNoise(MediaPlayer player, String name)
	{
		this.player = player;
		this.name = name;
	}

	final MediaPlayer player;
	final String name;

	public void stop()
	{
		try
		{
			if (this.player != null)
			{
				this.player.stop();
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		try
		{
			if (this.player != null)
			{
				this.player.dispose();
			}
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		IntegrationNoise.PLAYING_NOISES.remove(this);
	}
}