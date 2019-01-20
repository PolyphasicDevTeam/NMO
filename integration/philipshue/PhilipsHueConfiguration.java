package nmo.integration.philipshue;

import java.util.LinkedHashMap;
import com.google.gson.annotations.Expose;

public class PhilipsHueConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public String bridgeIP = "";

	@Expose
	public String bridgeUsername = "";

	@Expose
	public String[] lights = new String[0];

	@Expose
	public String[] lightsWithColour = new String[0];

	@Expose
	public LinkedHashMap<String, Hue> colours = new LinkedHashMap<>();
}