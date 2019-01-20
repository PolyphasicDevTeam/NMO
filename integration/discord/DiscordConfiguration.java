package nmo.integration.discord;

import com.google.gson.annotations.Expose;

public class DiscordConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public String authToken = "";

	@Expose
	public SendableMessage[] messages = new SendableMessage[0];
}