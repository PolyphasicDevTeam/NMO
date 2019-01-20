package nmo.integration.webui;

import java.net.InetAddress;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import nmo.Action;
import nmo.CommonUtils;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.PlatformData;
import nmo.config.NMOConfiguration;
import nmo.config.NMOUsers;
import nmo.utils.Communicator;

public class IntegrationWebUI extends Integration
{
	private static final Logger log = LogWrapper.getLogger();
	public static final IntegrationWebUI INSTANCE = new IntegrationWebUI();
	private String ddnsLastIP = "";
	private int ddnsUpdateTimer = -1; // forces immediate update on startup

	private IntegrationWebUI()
	{
		super("webUI");
	}

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.webUI.enabled;
	}

	@Override
	public void init() throws Exception
	{
		if (CommonUtils.isNullOrEmpty(NMOConfiguration.INSTANCE.integrations.webUI.webcamSecurityKey))
		{
			NMOConfiguration.INSTANCE.integrations.webUI.webcamSecurityKey = CommonUtils.generateAsciiCryptoKey(64);
			NMOConfiguration.save();
		}
		if (CommonUtils.isNullOrEmpty(NMOConfiguration.INSTANCE.integrations.webUI.cameraProxyKey))
		{
			NMOConfiguration.INSTANCE.integrations.webUI.cameraProxyKey = CommonUtils.generateAsciiCryptoKey(64);
			NMOConfiguration.save();
		}
		if (CommonUtils.isNullOrEmpty(NMOConfiguration.INSTANCE.integrations.webUI.username))
		{
			NMOConfiguration.INSTANCE.integrations.webUI.username = PlatformData.computerName;
			NMOConfiguration.save();
		}
		if (NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain.contains(":"))
		{
			throw new RuntimeException("webUI domain not valid: " + NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain);
		}
		if (NMOConfiguration.INSTANCE.integrations.webUI.readProxyForwardingHeaders && NMOConfiguration.INSTANCE.integrations.webUI.ddns.enabled)
		{
			throw new RuntimeException("webUI ddns must not be enabled at the same time as proxy forwarding headers");
		}
		if (NMOConfiguration.INSTANCE.integrations.webUI.readProxyForwardingHeaders && CommonUtils.isNullOrEmpty(NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain))
		{
			throw new RuntimeException("webUI domain must be set to proxy server's domain when proxy forwarding headers are enabled");
		}
		WebcamCapture.init();
		WebServer.initialize();
		if (NMOConfiguration.INSTANCE.integrations.webUI.ddns.enabled)
		{
			try
			{
				InetAddress address = InetAddress.getByName(NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain);
				this.ddnsLastIP = address.getHostAddress();
			}
			catch (Throwable t)
			{
				this.ddnsLastIP = "unavailable";
			}
			log.info("DDNS Last IP:  " + this.ddnsLastIP);
		}
		this.actions.put("/webUI/cameraprivacy/on", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				WebcamCapture.privacyMode = true;
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return true;
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return true;
			}

			@Override
			public String getName()
			{
				return "TURN ON CAMERA PRIVACY";
			}

			@Override
			public String getDescription()
			{
				return "Enables camera privacy mode. (Greys out the webcam image)";
			}
		});
		this.actions.put("/webUI/cameraprivacy/off", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				WebcamCapture.privacyMode = false;
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return true;
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return true;
			}

			@Override
			public String getName()
			{
				return "TURN OFF CAMERA PRIVACY";
			}

			@Override
			public String getDescription()
			{
				return "Disables camera privacy mode.";
			}
		});
		this.actions.put("/webUI/reloadUsers", new Action()
		{
			@Override
			public void onAction(Map<String, String[]> parameters) throws Exception
			{
				NMOUsers.load();
				WebcamWebSocketHandler[] sockets = WebcamCapture.webcams[0].getConnections();
				for (int i = 0; i < sockets.length; i++)
				{
					sockets[i].udesc = null;
				}
			}

			@Override
			public boolean isHiddenFromWebUI()
			{
				return true;
			}

			@Override
			public boolean isHiddenFromFrontend()
			{
				return false;
			}

			@Override
			public boolean isBlockedFromWebUI()
			{
				return true;
			}

			@Override
			public String getName()
			{
				return "RELOAD USERS";
			}

			@Override
			public String getDescription()
			{
				return "Reloads the list of users from users.json.";
			}
		});
	}

	@Override
	public void update() throws Exception
	{
		WebcamCapture.update();
		if (NMOConfiguration.INSTANCE.integrations.webUI.ddns.enabled)
		{
			this.ddnsUpdateTimer++;
			if (this.ddnsUpdateTimer == 0 || this.ddnsUpdateTimer >= (NMOConfiguration.INSTANCE.integrations.webUI.ddns.updateFrequency * 60))
			{
				try
				{
					String currentIP = PortForwarding.getExternalIP();
					if (!currentIP.equals(this.ddnsLastIP))
					{
						log.info("IP MISMATCH DETECTED. Contacting google domains service...");
						log.info("DDNS Last IP:  " + this.ddnsLastIP);
						log.info("Current IP:  " + currentIP);
						String userpass = NMOConfiguration.INSTANCE.integrations.webUI.ddns.username + ":" + NMOConfiguration.INSTANCE.integrations.webUI.ddns.password;
						String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes());
						String response = Communicator.basicJsonMessage("update IP", NMOConfiguration.INSTANCE.integrations.webUI.ddns.provider + "nic/update?hostname=" + NMOConfiguration.INSTANCE.integrations.webUI.ddns.domain + "&myip=" + currentIP, null, String.class, basicAuth);
						if (response.startsWith("good") || response.startsWith("nochg"))
						{
							System.out.println("... Everything OK now");
							this.ddnsLastIP = currentIP;
						}
						else
						{
							// cancel further updates
							System.out.println("... failed :/");
							this.ddnsUpdateTimer = Integer.MIN_VALUE;
						}
					}
					else
					{
						log.info("External IP has not changed. Skipping dynamic DNS update");
					}
				}
				catch (Throwable t)
				{
					throw new RuntimeException("Failed to update DDNS entry", t);
				}
				this.ddnsUpdateTimer = 0;
			}
		}
	}

	@Override
	public void shutdown() throws Exception
	{
		WebServer.shutdown();
		WebcamCapture.shutdown();
	}
}
