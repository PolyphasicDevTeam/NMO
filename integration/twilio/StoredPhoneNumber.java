package nmo.integration.twilio;

import com.google.gson.annotations.Expose;

public class StoredPhoneNumber
{
	@Expose
	public String name;

	@Expose
	public String number;

	@Expose
	public boolean hidden;

	@Expose
	public boolean secret;
}