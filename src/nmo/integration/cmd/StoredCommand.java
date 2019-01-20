package nmo.integration.cmd;

import com.google.gson.annotations.Expose;

public class StoredCommand
{
	@Expose
	public String name = "";

	@Expose
	public String description = "";

	@Expose
	public String[] command = new String[0];

	@Expose
	public String workingDir = "";

	@Expose
	public boolean hidden;

	@Expose
	public boolean secret;
}
