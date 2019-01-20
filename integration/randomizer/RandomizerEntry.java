package nmo.integration.randomizer;

import com.google.gson.annotations.Expose;

public class RandomizerEntry
{
	@Expose
	public String name = "";

	@Expose
	public String[] actions = new String[0];

	@Expose
	public boolean hidden;

	@Expose
	public boolean secret;
}
