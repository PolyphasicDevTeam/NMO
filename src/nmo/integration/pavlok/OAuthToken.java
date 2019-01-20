package nmo.integration.pavlok;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import nmo.Main;

public class OAuthToken
{
	public OAuthToken(String code)
	{
		this.code = code;
	}

	@Expose
	@SerializedName("client_id")
	public String client_id = Main.CLIENT_ID;

	@Expose
	@SerializedName("client_secret")
	public String client_secret = Main.CLIENT_SECRET;

	@Expose
	@SerializedName("code")
	public String code = "";

	@Expose
	@SerializedName("grant_type")
	public String grant_type = "authorization_code";

	@Expose
	@SerializedName("redirect_uri")
	public String redirect_uri = Main.CLIENT_CALLBACK;
}
