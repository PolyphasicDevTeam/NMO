package nmo;

import java.util.Calendar;
import java.util.TreeSet;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.annotations.Expose;
import nmo.integration.discord.Weekday;

public class CustomEvent implements Comparable<CustomEvent>
{
	@Expose
	public String name = "";

	@Expose
	public TreeSet<Weekday> days = new TreeSet<>();

	@Expose
	public int[] times = new int[0];

	@Expose
	public String[] actions = new String[0];

	public transient int originalOrder;
	public transient long nextTriggerTime;

	public void updateNextTriggerTime()
	{
		long currentTime = MainDialog.now;
		this.nextTriggerTime = Long.MAX_VALUE;

		for (Weekday weekday : this.days)
		{
			for (int time : this.times)
			{
				Calendar calendar2 = Calendar.getInstance();
				calendar2.setTimeInMillis(currentTime);
				calendar2.set(Calendar.DAY_OF_WEEK, weekday.ordinal() + 1);
				calendar2.set(Calendar.HOUR_OF_DAY, time / 60);
				calendar2.set(Calendar.MINUTE, time % 60);
				calendar2.set(Calendar.SECOND, 0);
				calendar2.set(Calendar.MILLISECOND, 0);
				long m = calendar2.getTimeInMillis();
				if (m < currentTime)
				{
					Calendar pre = Calendar.getInstance();
					pre.setTimeInMillis(m);
					long preo = pre.get(Calendar.DST_OFFSET);
					Calendar post = Calendar.getInstance();
					post.setTimeInMillis(m + 604800000L);
					long posto = post.get(Calendar.DST_OFFSET);
					m += 604800000L + (preo - posto);
				}
				if (m < this.nextTriggerTime)
				{
					this.nextTriggerTime = m;
				}
			}
		}
	}

	@Override
	public int compareTo(CustomEvent o)
	{
		int comp = Long.compare(this.nextTriggerTime, o.nextTriggerTime);
		return comp == 0 ? Integer.compare(this.originalOrder, o.originalOrder) : comp;
	}

	public String describe()
	{
		if (this.days.size() == 0 || this.times.length == 0)
		{
			return "NEVER";
		}
		String description = "";
		String s = "";
		if (this.days.size() == 7)
		{
			s = "DAILY";
		}
		else
		{
			for (Weekday weekday : this.days)
			{
				s += (s.isEmpty() ? "" : ", ") + weekday.name();
			}
		}
		description = s;
		s = "";
		for (int time : this.times)
		{
			int hour = time / 60;
			int minute = time % 60;
			s += (s.isEmpty() ? "" : ", ") + StringUtils.leftPad("" + hour, 2, "0") + ":" + StringUtils.leftPad("" + minute, 2, "0");
		}
		description += " at " + s;
		return description;
	}
}
