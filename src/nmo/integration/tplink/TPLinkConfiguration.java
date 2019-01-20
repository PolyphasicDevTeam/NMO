package nmo.integration.tplink;

import com.google.gson.annotations.Expose;

public class TPLinkConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public TPLinkDeviceEntry[] devices = new TPLinkDeviceEntry[0];
}
