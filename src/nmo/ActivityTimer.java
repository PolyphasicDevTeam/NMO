package nmo;

import com.google.gson.annotations.Expose;

public class ActivityTimer
{
	@Expose
	public String name = "";

	@Expose
	public long secondsForFirstWarning = 300;

	@Expose
	public long secondsForSubsequentWarnings = 10;

	@Expose
	public long zombiePenaltyForFirstWarning = 0;

	@Expose
	public long zombiePenaltyForOversleepWarning = 0;

	@Expose
	public long zombiePenaltyForOtherWarnings = 0;

	@Expose
	public long zombiePenaltyLimit = 0;
}
