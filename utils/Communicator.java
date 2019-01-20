package nmo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import nmo.CommonUtils;
import nmo.LogWrapper;
import nmo.exceptions.BadResponseException;
import nmo.exceptions.BlankResponseException;

public class Communicator
{
	private static final Logger log = LogWrapper.getLogger();

	@SuppressWarnings("unchecked")
	public static <T> T basicJsonMessage(String humanDesc, String path, Object constructable, Class<T> clazz, String authorization) throws Exception
	{
		HttpURLConnection connection = null;
		OutputStream out = null;
		InputStream in = null;

		try
		{
			log.info("Trying connection to " + path);
			connection = (HttpURLConnection) new URL(path).openConnection();
			connection.setConnectTimeout(2000);
			connection.setReadTimeout(2000);
			connection.setUseCaches(false);
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36"); //"NoMoreOversleeps/" + Main.VERSION.replace(" ", ""));
			if (authorization != null)
			{
				connection.setRequestProperty("Authorization", authorization);
			}
			if (constructable != null)
			{
				String request = CommonUtils.GSON.toJson(constructable);
				System.out.println(request);
				byte[] data = request.getBytes(CommonUtils.charsetUTF8);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
				connection.setRequestProperty("Content-Length", Integer.toString(data.length));
				connection.setRequestProperty("Content-Language", "en-US");
				connection.setDoOutput(true);
				connection.connect();
				out = connection.getOutputStream();
				out.write(data);
			}
			else
			{
				connection.setRequestMethod("GET");
				connection.connect();
			}
			int responseCode = connection.getResponseCode();
			if (responseCode == 200)
			{
				in = connection.getInputStream();
				if (clazz == null)
					return null;
				String REPLY = IOUtils.toString(in, CommonUtils.charsetUTF8);
				System.out.println(responseCode + " " + REPLY);
				if (clazz == String.class)
				{
					return (T) REPLY;
				}
				T response = CommonUtils.GSON.fromJson(REPLY, clazz);
				if (response == null)
					throw new BlankResponseException("Server sent back no data");
				return response;
			}
			else
			{
				in = connection.getErrorStream();
				System.out.println(responseCode + " " + IOUtils.toString(in, CommonUtils.charsetUTF8));
			}
			throw new BadResponseException(responseCode);
		}
		catch (Throwable t)
		{
			log.error("Communication error while performing task '" + humanDesc + "'");
			throw t;
		}
		finally
		{
			if (out != null)
			{
				try
				{
					out.close();
				}
				catch (IOException e)
				{
				}
			}

			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
				}
			}

			if (connection != null)
			{
				connection.disconnect();
			}
		}
	}
}
