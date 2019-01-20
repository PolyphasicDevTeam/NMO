package nmo;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.nio.file.Files;

public class LockingHelper
{
	// We can't use the logger here because this class is used by launcher to obtain lock BEFORE logger starts

	@SuppressWarnings("resource")
	public static synchronized FileLock getLock(File file)
	{
		System.out.println("Attempting to acquire file lock for " + file.getAbsolutePath());
		FileChannel channel = null;
		try
		{
			channel = new RandomAccessFile(file, "rw").getChannel();
			return channel.tryLock();
		}
		catch (IOException | OverlappingFileLockException e)
		{
			System.err.println("Failed to lock channel");
			e.printStackTrace();

			if (channel != null)
			{
				try
				{
					channel.close();
				}
				catch (IOException e1)
				{
					System.err.println("Failed to close lock channel");
					e1.printStackTrace();
				}
			}
			return null;
		}
	}

	public static synchronized void releaseLock(File file, FileLock lock, boolean deleteAfterwards)
	{
		System.out.println("Attempting to release file lock for " + file.getAbsolutePath());

		if (lock != null)
		{
			try
			{
				lock.release();
			}
			catch (IOException e)
			{
				System.err.println("Failed to release lock");
				e.printStackTrace();
			}

			try
			{
				lock.acquiredBy().close();
			}
			catch (IOException e)
			{
				System.err.println("Failed to close lock channel");
				e.printStackTrace();
			}

			if (deleteAfterwards && file.exists())
			{
				try
				{
					Files.delete(file.toPath());
				}
				catch (IOException e)
				{
					System.err.println("Failed to delete file");
					e.printStackTrace();
				}
			}
		}
	}
}
