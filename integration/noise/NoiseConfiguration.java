package nmo.integration.noise;

import com.google.gson.annotations.Expose;

public class NoiseConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public StoredNoise[] noises = new StoredNoise[0];
}