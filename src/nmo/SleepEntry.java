package nmo;

import java.util.Calendar;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.annotations.Expose;

public class SleepEntry implements Comparable<SleepEntry>
{
	public SleepEntry()
	{
		this.approachWarning = 5;
	}

	public SleepEntry(int start, int end, String name)
	{
		this.start = start;
		this.end = end;
		this.name = name;
		this.approachWarning = 5;
	}

	public SleepEntry(int startH, int startM, int endH, int endM, String name)
	{
		this((60 * startH) + startM, (60 * endH) + endM, name);
	}

	@Expose
	public int start;

	@Expose
	public int end;

	@Expose
	public String name = "";

	@Expose
	public ScheduleEntryType type = ScheduleEntryType.NAP;

	@Expose
	public int approachWarning;

	@Override
	public int compareTo(SleepEntry o)
	{
		int i = Integer.compare(this.start, o.start);
		if (i == 0)
		{
			i = Integer.compare(this.end, o.end);
		}
		return i;
	}

	public boolean containsTimeValue(long now)
	{
		return now >= this.nextStartTime && now < this.nextEndTime;
	}

	public String describe()
	{
		return this.name + " :: " + this.describeTime();
	}

	public String describeTime()
	{
		return StringUtils.leftPad("" + (this.start / 60), 2, "0") + ":" + StringUtils.leftPad("" + (this.start % 60), 2, "0") + " - " + StringUtils.leftPad("" + (this.end / 60), 2, "0") + ":" + StringUtils.leftPad("" + (this.end % 60), 2, "0");
	}

	public transient long nextStartTime;
	public transient long nextEndTime;

	public void updateNextTriggerTime()
	{
		int newEnd = this.end < this.start ? this.end + 1440 : this.end;
		long currentTime = MainDialog.now;
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(currentTime);
		calendar2.set(Calendar.HOUR_OF_DAY, this.start / 60);
		calendar2.set(Calendar.MINUTE, this.start % 60);
		calendar2.set(Calendar.SECOND, 0);
		calendar2.set(Calendar.MILLISECOND, 0);
		long m = calendar2.getTimeInMillis();
		long n = m + ((newEnd - this.start) * 60000L);
		if (n < currentTime)
		{
			calendar2 = Calendar.getInstance();
			calendar2.setTimeInMillis(currentTime + 86400000L);
			calendar2.set(Calendar.HOUR_OF_DAY, this.start / 60);
			calendar2.set(Calendar.MINUTE, this.start % 60);
			calendar2.set(Calendar.SECOND, 0);
			calendar2.set(Calendar.MILLISECOND, 0);
			m = calendar2.getTimeInMillis();
			n = m + ((newEnd - this.start) * 60000L);
		}
		else if ((n - currentTime) >= 86400000L)
		{
			calendar2 = Calendar.getInstance();
			calendar2.setTimeInMillis(currentTime - 86400000L);
			calendar2.set(Calendar.HOUR_OF_DAY, this.start / 60);
			calendar2.set(Calendar.MINUTE, this.start % 60);
			calendar2.set(Calendar.SECOND, 0);
			calendar2.set(Calendar.MILLISECOND, 0);
			m = calendar2.getTimeInMillis();
			n = m + ((newEnd - this.start) * 60000L);
		}
		this.nextStartTime = m;
		this.nextEndTime = n;
	}

	public void fixTimes()
	{
		this.start = this.start % 1440;
		if (this.start < 0)
		{
			this.start += 1440;
		}
		this.end = this.end % 1440;
		if (this.end < 0)
		{
			this.end += 1440;
		}
	}
}
