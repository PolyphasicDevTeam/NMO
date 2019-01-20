package nmo.utils;

public class FormattingHelper
{

	public static String formatTimeElapsedWithDays(long now, long startingTime)
	{
		// Truncate to whole seconds
		now /= 1000;
		startingTime /= 1000;
		long elapsed = Math.max(0, now - startingTime);

		long days = elapsed / 86400;
		elapsed = elapsed % 86400;

		long hours = elapsed / 3600;
		elapsed = elapsed % 3600;

		long minutes = elapsed / 60;

		return String.format("%01dd %01dh %01dm", days, hours, minutes);
	}

	public static String formatTimeElapsedWithoutDays(long now, long startingTime)
	{
		// Truncate to whole seconds
		now /= 1000;
		startingTime /= 1000;
		long elapsed = Math.max(0, now - startingTime);

		long hours = elapsed / 3600;
		elapsed = elapsed % 3600;

		long minutes = elapsed / 60;

		return String.format("%01dh %01dm", hours, minutes);
	}

}
