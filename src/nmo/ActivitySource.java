package nmo;

public final class ActivitySource
{
	public ActivitySource(String type)
	{
		this.type = type;
		this.time = System.currentTimeMillis();
	}

	public final String type;
	public volatile long time;
}
