package nmo.integration.philipshue;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.apache.logging.log4j.Logger;
import com.philips.lighting.hue.sdk.PHAccessPoint;
import com.philips.lighting.hue.sdk.PHBridgeSearchManager;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.hue.sdk.PHSDKListener;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHHueParsingError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLight.PHLightColorMode;
import com.philips.lighting.model.PHLightState;
import nmo.Action;
import nmo.Integration;
import nmo.LogWrapper;
import nmo.PlatformData;
import nmo.config.NMOConfiguration;

public class IntegrationPhilipsHue extends Integration
{
	public IntegrationPhilipsHue()
	{
		super("philipsHue");
	}

	public static final IntegrationPhilipsHue INSTANCE = new IntegrationPhilipsHue();
	private static final Logger log = LogWrapper.getLogger();
	public PHHueSDK sdk;
	public PHBridge activeBridge;
	public PHSDKListener listener;
	//public volatile int lightState = -1;
	public volatile LinkedHashMap<String, Integer> lightStates = new LinkedHashMap<>();
	public volatile LinkedHashMap<String, PHLight> lights = new LinkedHashMap<>();

	@Override
	public boolean isEnabled()
	{
		return NMOConfiguration.INSTANCE.integrations.philipsHue.enabled;
	}

	@Override
	public void init()
	{
		for (int i = 0; i < NMOConfiguration.INSTANCE.integrations.philipsHue.lights.length + NMOConfiguration.INSTANCE.integrations.philipsHue.lightsWithColour.length; i++)
		{
			final boolean isColouredLight = i >= NMOConfiguration.INSTANCE.integrations.philipsHue.lights.length;
			final String bulbName = isColouredLight ? NMOConfiguration.INSTANCE.integrations.philipsHue.lightsWithColour[i - NMOConfiguration.INSTANCE.integrations.philipsHue.lights.length] : NMOConfiguration.INSTANCE.integrations.philipsHue.lights[i];
			this.lightStates.put(bulbName, -1);
			this.actions.put("/philipsHue/" + i + "/off", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationPhilipsHue.this.setBrightness(bulbName, 0);
				}

				@Override
				public String getName()
				{
					return "SET " + bulbName + " TO OFF";
				}

				@Override
				public String getDescription()
				{
					return "Turns off Philips Hue light source '" + bulbName + "'.";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return false;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return false;
				}
			});
			this.actions.put("/philipsHue/" + i + "/25", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationPhilipsHue.this.setBrightness(bulbName, 63);
				}

				@Override
				public String getName()
				{
					return "SET " + bulbName + "'s BRIGHTNESS TO 25%";
				}

				@Override
				public String getDescription()
				{
					return "Turns on Philips Hue light source '" + bulbName + "' and sets brightness to 25%.";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return false;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return false;
				}
			});
			this.actions.put("/philipsHue/" + i + "/50", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationPhilipsHue.this.setBrightness(bulbName, 127);
				}

				@Override
				public String getName()
				{
					return "SET " + bulbName + "'s BRIGHTNESS TO 50%";
				}

				@Override
				public String getDescription()
				{
					return "Turns on Philips Hue light source '" + bulbName + "' and sets brightness to 50%.";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return false;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return false;
				}
			});
			this.actions.put("/philipsHue/" + i + "/75", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationPhilipsHue.this.setBrightness(bulbName, 190);
				}

				@Override
				public String getName()
				{
					return "SET " + bulbName + "'s BRIGHTNESS TO 75%";
				}

				@Override
				public String getDescription()
				{
					return "Turns on Philips Hue light source '" + bulbName + "' and sets brightness to 75%.";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return false;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return false;
				}
			});
			this.actions.put("/philipsHue/" + i + "/100", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					IntegrationPhilipsHue.this.setBrightness(bulbName, 254);
				}

				@Override
				public String getName()
				{
					return "SET " + bulbName + "'s BRIGHTNESS TO 100%";
				}

				@Override
				public String getDescription()
				{
					return "Turns on Philips Hue light source '" + bulbName + "' and sets brightness to 100%.";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return false;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return false;
				}
			});
			if (isColouredLight)
			{
				int j = 0;
				for (final String ck : NMOConfiguration.INSTANCE.integrations.philipsHue.colours.keySet())
				{
					final Hue hue = NMOConfiguration.INSTANCE.integrations.philipsHue.colours.get(ck);
					this.actions.put("/philipsHue/" + i + "/colour/" + j, new Action()
					{
						@Override
						public void onAction(Map<String, String[]> parameters) throws Exception
						{
							IntegrationPhilipsHue.this.setHue(bulbName, hue);
						}

						@Override
						public String getName()
						{
							return "SET " + bulbName + "'s COLOUR TO " + ck;
						}

						@Override
						public String getDescription()
						{
							return "Changes the colour of the Philips Hue coloured light source '" + bulbName + "' to '" + ck + "' (" + hue.r + "," + hue.g + "," + hue.b + ").";
						}

						@Override
						public boolean isHiddenFromFrontend()
						{
							return false;
						}

						@Override
						public boolean isHiddenFromWebUI()
						{
							return false;
						}

						@Override
						public boolean isBlockedFromWebUI()
						{
							return false;
						}
					});
					j++;
				}
				this.actions.put("/philipsHue/" + i + "/colour/random", new Action()
				{
					@Override
					public void onAction(Map<String, String[]> parameters) throws Exception
					{
						IntegrationPhilipsHue.this.setHue(bulbName, null);
					}

					@Override
					public String getName()
					{
						return "SET " + bulbName + "'s COLOUR TO RANDOM";
					}

					@Override
					public String getDescription()
					{
						return "Changes the colour of the Philips Hue coloured light source '" + bulbName + "' to a random value.";
					}

					@Override
					public boolean isHiddenFromFrontend()
					{
						return false;
					}

					@Override
					public boolean isHiddenFromWebUI()
					{
						return false;
					}

					@Override
					public boolean isBlockedFromWebUI()
					{
						return false;
					}
				});
			}
			this.actions.put("/philipsHue/" + i + "/toggle", new Action()
			{
				@Override
				public void onAction(Map<String, String[]> parameters) throws Exception
				{
					boolean isOff = IntegrationPhilipsHue.this.lightStates.get(bulbName) == -1;
					IntegrationPhilipsHue.this.setBrightness(bulbName, isOff ? 254 : 0);
				}

				@Override
				public String getName()
				{
					return "TOGGLE " + bulbName;
				}

				@Override
				public String getDescription()
				{
					return "Toggles the state (on/off) of the Philips Hue light source '" + bulbName + "'.";
				}

				@Override
				public boolean isHiddenFromFrontend()
				{
					return false;
				}

				@Override
				public boolean isHiddenFromWebUI()
				{
					return true;
				}

				@Override
				public boolean isBlockedFromWebUI()
				{
					return true;
				}
			});
		}

		this.sdk = PHHueSDK.getInstance();
		this.sdk.setAppName("NoMoreOversleeps");
		this.sdk.setDeviceName(PlatformData.computerName);
		this.sdk.getNotificationManager().registerSDKListener(this.listener = new PHSDKListener()
		{
			@Override
			public void onParsingErrors(List<PHHueParsingError> arg0)
			{
				// TODO
			}

			@Override
			public void onError(int code, String message)
			{
				log.info("Hue SDK error " + code + ": " + message);
				if (code == PHHueError.BRIDGE_NOT_RESPONDING)
				{
					PHBridgeSearchManager sm = (PHBridgeSearchManager) IntegrationPhilipsHue.this.sdk.getSDKService(PHHueSDK.SEARCH_BRIDGE);
					sm.search(true, true);
				}
			}

			@Override
			public void onConnectionResumed(PHBridge bridge)
			{
				// Don't do anything. This happens so frequently that printing anything causes massive log spam.
			}

			@Override
			public void onConnectionLost(PHAccessPoint accessPoint)
			{
				log.info("Connection to Hue Bridge at " + accessPoint.getIpAddress() + " has been lost");
			}

			@Override
			public void onCacheUpdated(List<Integer> cacheNotificationsList, PHBridge bridge)
			{
				if (cacheNotificationsList.contains(PHMessageType.LIGHTS_CACHE_UPDATED))
				{
					List<PHLight> u = bridge.getResourceCache().getAllLights();
					if (!u.isEmpty())
					{
						for (PHLight light : u)
						{
							PHLightState phls = light.getLastKnownLightState();
							String bulbName = light.getName();
							int state = !phls.isReachable() ? -2 : phls.isOn() ? phls.getBrightness() : -1;
							Integer hue = phls.getHue();
							IntegrationPhilipsHue.this.lights.put(bulbName, light);
							IntegrationPhilipsHue.this.lightStates.put(bulbName, state);
							log.info("Updating light state: " + bulbName + " = " + state + "," + hue);
						}
					}
				}
			}

			@Override
			public void onBridgeConnected(PHBridge bridge, String username)
			{
				log.info("Connection to Hue Bridge at " + NMOConfiguration.INSTANCE.integrations.philipsHue.bridgeIP + " has been established");
				log.info("Bridge API authorization username: " + username);
				IntegrationPhilipsHue.this.sdk.setSelectedBridge(bridge);
				IntegrationPhilipsHue.this.sdk.enableHeartbeat(bridge, 1000);
				IntegrationPhilipsHue.this.activeBridge = bridge;
				NMOConfiguration.INSTANCE.integrations.philipsHue.bridgeUsername = username;
				try
				{
					NMOConfiguration.save();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				List<PHLight> u = bridge.getResourceCache().getAllLights();
				if (!u.isEmpty())
				{
					for (PHLight light : u)
					{
						PHLightState phls = light.getLastKnownLightState();
						String bulbName = light.getName();
						int state = !phls.isReachable() ? -2 : phls.isOn() ? phls.getBrightness() : -1;
						Integer hue = phls.getHue();
						IntegrationPhilipsHue.this.lights.put(bulbName, light);
						IntegrationPhilipsHue.this.lightStates.put(bulbName, state);
						log.info("Updating light state: " + bulbName + " = " + state + "," + hue);
					}
				}
			}

			@Override
			public void onAuthenticationRequired(PHAccessPoint accessPoint)
			{
				log.info("Authentication required. Please push the authentication button on the Hue Bridge!");
				NMOConfiguration.INSTANCE.integrations.philipsHue.bridgeIP = accessPoint.getIpAddress();
				IntegrationPhilipsHue.this.sdk.startPushlinkAuthentication(accessPoint);
			}

			@Override
			public void onAccessPointsFound(List<PHAccessPoint> accessPointList)
			{
				log.info(accessPointList.size() + " access points found");
				if (!accessPointList.isEmpty())
				{
					PHAccessPoint accessPoint = accessPointList.get(0);
					log.info("Attempting connection to " + accessPoint.getIpAddress());
					IntegrationPhilipsHue.this.sdk.connect(accessPoint);
				}
			}
		});

		log.info("Attempting to reconnect to Hue Bridge...");
		PHAccessPoint accessPoint = new PHAccessPoint();
		accessPoint.setIpAddress(NMOConfiguration.INSTANCE.integrations.philipsHue.bridgeIP);
		accessPoint.setUsername(NMOConfiguration.INSTANCE.integrations.philipsHue.bridgeUsername);
		this.sdk.connect(accessPoint);
	}

	@Override
	public void update() throws Exception
	{
		// TODO Auto-generated method stub
	}

	public void setBrightness(String name, int brightness) throws IOException
	{
		PHLight light = this.lights.get(name);
		if (light == null)
			throw new IOException("No such light: " + name);
		PHLightState lightState = new PHLightState();
		lightState.setOn(brightness > 0);
		lightState.setBrightness(brightness, true);
		this.activeBridge.updateLightState(light, lightState);
	}

	protected void setHue(String name, Hue hue) throws IOException
	{
		// conversion formula taken from https://stackoverflow.com/questions/22564187/rgb-to-philips-hue-hsb
		PHLight light = this.lights.get(name);
		if (light == null)
			throw new IOException("No such light: " + name);
		float x, y;
		if (hue == null)
		{
			x = ThreadLocalRandom.current().nextFloat();
			y = ThreadLocalRandom.current().nextFloat();
		}
		else
		{
			double r = (hue.r / 255.0);
			double g = (hue.g / 255.0);
			double b = (hue.b / 255.0);
			float red, green, blue;
			if (r > 0.04045)
			{
				red = (float) Math.pow((r + 0.055) / (1.0 + 0.055), 2.4);
			}
			else
			{
				red = (float) (r / 12.92);
			}
			if (g > 0.04045)
			{
				green = (float) Math.pow((g + 0.055) / (1.0 + 0.055), 2.4);
			}
			else
			{
				green = (float) (g / 12.92);
			}
			if (b > 0.04045)
			{
				blue = (float) Math.pow((b + 0.055) / (1.0 + 0.055), 2.4);
			}
			else
			{
				blue = (float) (b / 12.92);
			}
			float X = (float) (red * 0.649926 + green * 0.103455 + blue * 0.197109);
			float Y = (float) (red * 0.234327 + green * 0.743075 + blue * 0.022598);
			float Z = (float) (red * 0.0000000 + green * 0.053077 + blue * 0.935763); // I reduced the blue multiplier a little bit here (from 1.035763) because the white seemed too blue
			x = X / (X + Y + Z);
			y = Y / (X + Y + Z);
		}
		PHLightState lightState = new PHLightState();
		lightState.setColorMode(PHLightColorMode.COLORMODE_XY);
		lightState.setX(x);
		lightState.setY(y);
		this.activeBridge.updateLightState(light, lightState);
	}

	@Override
	public void shutdown()
	{
		if (this.sdk != null)
		{
			this.sdk.disableAllHeartbeat();
			if (this.activeBridge != null)
			{
				this.sdk.disconnect(this.activeBridge);
				this.activeBridge = null;
			}
			this.sdk.destroySDK();
			this.sdk = null;
		}
	}
}
