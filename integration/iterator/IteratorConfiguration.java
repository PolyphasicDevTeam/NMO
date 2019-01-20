package nmo.integration.iterator;

import com.google.gson.annotations.Expose;

public class IteratorConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public IteratorEntry[] iterators = new IteratorEntry[0];
}
