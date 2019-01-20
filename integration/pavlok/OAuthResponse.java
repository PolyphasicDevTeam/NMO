package nmo.integration.pavlok;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OAuthResponse
{
	@Expose
	@SerializedName("access_token")
	public String access_token;

	@Expose
	@SerializedName("token_type")
	public String token_type;

	@Expose
	@SerializedName("expires_in")
	public long expires_in;

	@Expose
	@SerializedName("refresh_token")
	public String refresh_token;

	@Expose
	@SerializedName("scope")
	public String scope;

	@Expose
	@SerializedName("created_at")
	public long created_at;

	@Expose
	@SerializedName("device")
	public String device;
}