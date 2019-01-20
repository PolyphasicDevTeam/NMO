package nmo.integration.input;

import com.google.gson.annotations.Expose;

public class XboxControllerConfiguration
{
	public static class PlayerConfiguration
	{
		@Expose
		public boolean enabled;

		@Expose
		public int vibrationLength = 90;
	}

	@Expose
	public PlayerConfiguration P1 = new PlayerConfiguration();

	@Expose
	public PlayerConfiguration P2 = new PlayerConfiguration();

	@Expose
	public PlayerConfiguration P3 = new PlayerConfiguration();

	@Expose
	public PlayerConfiguration P4 = new PlayerConfiguration();
}