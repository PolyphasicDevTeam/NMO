package nmo.integration.tplink;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import com.google.gson.JsonParser;

public class TPLinkDevice
{
	private String ipAddress;

	public TPLinkDevice(String ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	public boolean toggle(boolean state) throws IOException
	{
		String data = this.send("{\"system\":{\"set_relay_state\":{\"state\":" + (state ? 1 : 0) + "}}}}");
		return data.length() > 0 && new JsonParser().parse(data).getAsJsonObject().get("system").getAsJsonObject().get("set_relay_state").getAsJsonObject().get("err_code").getAsInt() == 0;
	}

	public boolean isOn() throws IOException
	{
		String data = this.send("{\"system\":{\"get_sysinfo\":null}}");
		return data.length() > 0 && new JsonParser().parse(data).getAsJsonObject().get("system").getAsJsonObject().get("get_sysinfo").getAsJsonObject().get("relay_state").getAsInt() == 1;
	}

	protected String send(String command) throws IOException
	{
		Socket socket = new Socket(this.ipAddress, 9999);
		socket.setSoTimeout(200);
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(this.encrypt(command));
		InputStream inputStream = socket.getInputStream();
		String data = this.decrypt(inputStream);
		outputStream.close();
		inputStream.close();
		socket.close();
		return data;
	}

	private byte[] encrypt(String command)
	{
		byte[] header = ByteBuffer.allocate(4).putInt(command.length()).array();
		ByteBuffer buf = ByteBuffer.allocate(header.length + command.length()).put(header);
		for (int i = 0, key = 0xAB; i < command.length(); i++)
		{
			key = command.charAt(i) ^ key;
			buf.put((byte) key);
		}
		return buf.array();
	}

	private String decrypt(InputStream inputStream) throws IOException
	{
		int in;
		int key = 0x2B;
		int nextKey;
		StringBuilder sb = new StringBuilder();
		while ((in = inputStream.read()) != -1)
		{
			nextKey = in;
			in = in ^ key;
			key = nextKey;
			sb.append((char) in);
		}
		return "{" + sb.toString().substring(5);
	}
}
