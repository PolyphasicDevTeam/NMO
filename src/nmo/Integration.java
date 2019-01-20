package nmo;

import java.util.LinkedHashMap;

public abstract class Integration
{
	public Integration(String id)
	{
		this.id = id;
	}

	public final String id;

	public abstract boolean isEnabled();

	public abstract void init() throws Exception;

	public abstract void update() throws Exception;

	public abstract void shutdown() throws Exception;

	public final LinkedHashMap<String, Action> actions = new LinkedHashMap<>();

	public final LinkedHashMap<String, Action> getActions()
	{
		return this.actions;
	}
}
