package nmo;

import java.lang.Thread.State;
import java.lang.management.ManagementFactory;
import java.lang.management.MonitorInfo;
import java.lang.management.ThreadInfo;
import java.util.ArrayList;
import org.apache.logging.log4j.Logger;

/** Debug handling functions
 */
public final class DebugHandler
{
	private DebugHandler() throws UnsupportedOperationException
	{
		throw new UnsupportedOperationException(); // nope, you can't construct this class
	}

	static Logger log = LogWrapper.getLogger();

	/** Returns an array list of debug information for all Java threads; useful for trying to diagnose the cause of odd behaviour.
	 * 
	 * @return An array list of debug information
	 */
	public static ArrayList<String> getThreadDebugData()
	{
		ArrayList<String> strings = new ArrayList<>();

		strings.add("------------------------------------------------------------");
		strings.add("Debugging information for Java virtual machine");
		ThreadInfo[] threads = ManagementFactory.getThreadMXBean().dumpAllThreads(true, true);
		for (ThreadInfo thread : threads)
		{
			if (thread.getThreadState() != State.WAITING)
			{
				strings.add("------------------------------------------------------------");
				strings.add(thread.getThreadName());
				strings.add("\tPID: " + thread.getThreadId() + " | Suspended: " + thread.isSuspended() + " | Native: " + thread.isInNative() + " | State: " + thread.getThreadState());
				if (thread.getLockedMonitors().length != 0)
				{
					strings.add("\tThread is waiting on monitor(s):");
					for (MonitorInfo monitor : thread.getLockedMonitors())
					{
						strings.add("\t\tLocked on:" + monitor.getLockedStackFrame());
					}
				}
				strings.add("\tStack trace:");

				StackTraceElement[] stack = thread.getStackTrace();
				for (int line = 0; line < stack.length; line++)
				{
					strings.add("\t\t" + stack[line].toString());
				}
			}
		}
		strings.add("------------------------------------------------------------");
		strings.add("End of debugging information");
		strings.add("------------------------------------------------------------");

		return strings;
	}

	/** Prints out debug information for all Java threads; useful for trying to diagnose the cause of odd behaviour. */
	public static void dumpThreadDebugData()
	{
		for (String s : getThreadDebugData())
		{
			log.error(s);
		}
	}
}
