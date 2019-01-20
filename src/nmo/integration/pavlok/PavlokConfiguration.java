package nmo.integration.pavlok;

import com.google.gson.annotations.Expose;

public class PavlokConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public OAuthResponse auth = null;
}
