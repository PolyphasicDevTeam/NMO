package nmo.integration.wemo;

import com.google.gson.annotations.Expose;

public class WemoDeviceEntry
{
	@Expose
	public String name = "";

	@Expose
	public String description = "";

	@Expose
	public String ipAddress = "";

	@Expose
	public boolean hidden;

	@Expose
	public boolean secret;

	public transient boolean isSwitchedOn;
}
