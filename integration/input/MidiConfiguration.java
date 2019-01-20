package nmo.integration.input;

import java.util.ArrayList;
import com.google.gson.annotations.Expose;

public class MidiConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public ArrayList<String> transmitters = new ArrayList<>();
}