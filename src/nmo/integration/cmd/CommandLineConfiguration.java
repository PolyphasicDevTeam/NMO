package nmo.integration.cmd;

import com.google.gson.annotations.Expose;

public class CommandLineConfiguration
{
	@Expose
	public boolean enabled;

	@Expose
	public StoredCommand[] commands = new StoredCommand[0];
}
