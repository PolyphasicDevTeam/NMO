package nmo.integration.randomizer;

import com.google.gson.annotations.Expose;

public class RandomizerConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public RandomizerEntry[] randomizers = new RandomizerEntry[0];
}
