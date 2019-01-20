package nmo.integration.webui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;
import org.bitlet.weupnp.PortMappingEntry;
import nmo.LogWrapper;
import nmo.config.NMOConfiguration;

public class PortForwarding
{
	private static final Logger log = LogWrapper.getLogger();

	public static String[] LAN_IP_RANGES = { "10.", "172.16.", "172.17.", "172.18.", "172.19.", "172.20.", "172.21.", "172.22.", "172.23.", "172.24.", "172.25.", "172.26.", "172.27.", "172.28.", "172.29.", "172.30.", "172.31.", "192.168." };
	public static String[] CGNAT_IP_RANGES = { "100.64.", "100.65.", "100.66.", "100.67.", "100.68.", "100.69.", "100.70.", "100.71.", "100.72.", "100.73.", "100.74.", "100.75.", "100.76.", "100.77.", "100.78.", "100.79.", "100.80.", "100.81.", "100.82.", "100.83.", "100.84.", "100.85.", "100.86.", "100.87.", "100.88.", "100.89.", "100.90.", "100.91.", "100.92.", "100.93.", "100.94.", "100.95.", "100.96.", "100.97.", "100.98.", "100.99.", "100.100.", "100.101.", "100.102.", "100.103.", "100.104.", "100.105.", "100.106.", "100.107.", "100.108.", "100.109.", "100.110.", "100.111.", "100.112.", "100.113.", "100.114.", "100.115.", "100.116.", "100.117.", "100.118.", "100.119.", "100.120.", "100.121.", "100.122.", "100.123.", "100.124.", "100.125.", "100.126.", "100.127." };

	public static String getExternalIP() throws Exception
	{
		URL url = new URL("http://checkip.amazonaws.com");
		BufferedReader in = null;
		try
		{
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String ip = in.readLine();
			return ip;
		}
		finally
		{
			if (in != null)
			{
				try
				{
					in.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private static void logAndPrint(ArrayList<String> messages, String msg)
	{
		messages.add(msg);
		log.info(msg);
	}

	public static void attemptAutomaticPortForwarding(ArrayList<String> messages) throws Exception
	{
		logAndPrint(messages, "The internet connection IP appears to be " + getExternalIP());
		GatewayDiscover discover = new GatewayDiscover();
		Map<InetAddress, GatewayDevice> devices = discover.discover();
		logAndPrint(messages, "There were " + devices.size() + " internet connection gateways detected which support UPnP");
		if (devices.size() == 0)
		{
			logAndPrint(messages, "Automatic forwarding failed (no gateway devices found)");
			return;
		}
		for (InetAddress key : devices.keySet())
		{
			logAndPrint(messages, "------------");
			GatewayDevice device = devices.get(key);
			String EXTERNAL_IP = device.getExternalIPAddress();
			logAndPrint(messages, "UPnP gateway on interface " + key.getHostAddress() + " has an external IP of " + EXTERNAL_IP);
			for (String r : LAN_IP_RANGES)
			{
				if (EXTERNAL_IP.startsWith(r))
				{
					logAndPrint(messages, "  >> THERE APPEAR TO BE MULTIPLE ROUTERS BETWEEN HERE AND THE INTERNET");
					logAndPrint(messages, "  >> !!! THE AUTO-PORT-FORWARD WILL PROBABLY HAVE NO EFFECT !!!");
					break;
				}
			}
			for (String r : CGNAT_IP_RANGES)
			{
				if (EXTERNAL_IP.startsWith(r))
				{
					logAndPrint(messages, "  >> THIS CONNECTION IS A CARRIER GRADE NAT");
					logAndPrint(messages, "  >> !!! THE AUTO-PORT-FORWARD WILL PROBABLY HAVE NO EFFECT !!!");
					break;
				}
			}
			for (int m = 0; m < Integer.MAX_VALUE; m++)
			{
				PortMappingEntry e = new PortMappingEntry();
				if (device.getGenericPortMappingEntry(m, e))
				{
					log.info("  * Mapped : " + e.getPortMappingDescription() + " / " + e.getExternalPort() + " " + e.getProtocol() + " -> " + e.getInternalClient() + ":" + e.getInternalPort());
				}
				else
				{
					break;
				}
			}
			/*
			PortMappingEntry e = new PortMappingEntry();
			boolean res = device.getSpecificPortMappingEntry(NMOConfiguration.instance.integrations.webUI.jettyPort, "TCP", e);
			log.info(res);
			if (res)
			{
				log.info("Mapped : " + e.getExternalPort() + "->" + e.getInternalClient() + ":" + e.getInternalPort());
			}
			*/
			logAndPrint(messages, "Attempting to delete any existing mappings for port " + NMOConfiguration.INSTANCE.integrations.webUI.jettyPort + "...");
			boolean b = device.deletePortMapping(NMOConfiguration.INSTANCE.integrations.webUI.jettyPort, "TCP");
			logAndPrint(messages, b ? "... successful!" : "... failed :(");
			logAndPrint(messages, "Attempting to create new NMO mapping for port " + NMOConfiguration.INSTANCE.integrations.webUI.jettyPort + "...");
			b = device.addPortMapping(NMOConfiguration.INSTANCE.integrations.webUI.jettyPort, NMOConfiguration.INSTANCE.integrations.webUI.jettyPort, device.getLocalAddress().getHostAddress(), "TCP", "NoMoreOversleeps");
			logAndPrint(messages, b ? "... successful!" : "... failed :(");
		}
		logAndPrint(messages, "------------");
	}
}
