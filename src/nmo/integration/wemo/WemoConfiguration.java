package nmo.integration.wemo;

import com.google.gson.annotations.Expose;

public class WemoConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public WemoDeviceEntry[] devices = new WemoDeviceEntry[0];
}
