package nmo.integration.pavlok;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Stimuli
{
	public Stimuli(long value, String access_token, String reason)
	{
		this.value = value;
		this.access_token = access_token;
		this.reason = reason;
	}

	@Expose
	@SerializedName("value")
	public long value;

	@Expose
	@SerializedName("access_token")
	public String access_token;

	@Expose
	@SerializedName("reason")
	public String reason;
}
