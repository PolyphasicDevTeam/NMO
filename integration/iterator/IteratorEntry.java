package nmo.integration.iterator;

import com.google.gson.annotations.Expose;

public class IteratorEntry
{
	@Expose
	public String name = "";

	@Expose
	public String[] actions = new String[0];

	@Expose
	public boolean hidden;

	@Expose
	public boolean secret;

	public transient int option = 0;
}
