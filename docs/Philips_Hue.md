The **Philips Hue** integration module allows you to connect NoMoreOversleeps with Philips Hue smart light bulbs and light strips. In order to use this module, you require a Philips Hue Bridge to be on the local LAN and at least one bulb paired to it.

The following is the configuration for this module:
```json
    "philipsHue": {
      "enabled": true,
      "bridgeIP": "12.34.56.78",
      "bridgeUsername": "REDACTED",
      "lights": [
        "SOME BULB NAME GOES HERE"
      ]
    },
```
In order to activate Philips Hue for the first time you should leave `bridgeIP` and `bridgeUsername` blank. During startup, NMO will discover that no bridge is configured and will dump out the following message to the log file:

"Authentication required. Please push the authentication button on the Hue Bridge"

When you see this message in the log, push the authentication button. The IP and username of the bridge should then be automatically populated and the connection established. For best results here you should `tail -f NoMoreOversleeps-0.log` in order to get live feed of the log file (or, run NMO from the terminal)

`lights` is an array of light bulbs. The bulb names here should match those configured in the Hue app in order for this to work.

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the philipsHue module:

* `/philipsHue/x/25` (x = light index): Turn on the light and set it to 25% brightness
* `/philipsHue/x/50` (x = light index): Turn on the light and set it to 50% brightness
* `/philipsHue/x/75` (x = light index): Turn on the light and set it to 75% brightness
* `/philipsHue/x/100` (x = light index): Turn on the light and set it to 100% brightness
* `/philipsHue/x/off` (x = light index): Turn off the light
* `/philipsHue/x/toggle` (x = light index): Toggle the light on or off (invert its state)