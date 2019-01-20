package nmo.integration.twilio;

import com.google.gson.annotations.Expose;

public class TwilioConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public String accountSID = "";

	@Expose
	public String authToken = "";

	@Expose
	public String numberFrom = "";

	@Expose
	public String callingURI = "http://twimlets.com/holdmusic?Bucket=com.twilio.music.ambient";

	@Expose
	public StoredPhoneNumber[] phoneNumbers = new StoredPhoneNumber[0];
}