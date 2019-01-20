The **TPLink** integration module allows you to connect NoMoreOversleeps with TP-Link smart sockets. In order to use this module, you require the TP-Link smart socket to already be attached to the local LAN over WiFi and you must know the IP address of the socket (you can check your router control panel to find this out).

The following is the configuration for this module:
```json
    "tplink": {
      "enabled": true,
      "devices": [
        {
          "name": "FGS DESK FAN",
          "description": "A somewhat crappy low-speed fan located on my desk and aimed at my face.",
          "ipAddress": "172.25.37.103",
          "hidden": false,
          "secret": false
        }
      ]
    },
```
The `devices` array contains a list of each TP-Link smart switch you wish NMO to control. For each device you must specify the following:

* `name`: The name of the switch, as configured in the TP-Link app
* `description`: A human readable description of this switch and what it is used for
* `ipAddress`: The IP address which the switch can be contacted at
* `hidden`: Whether or not the actions for this switch are hidden from the frontend
* `secret`: Whether or not the actions for this switch are hidden from the Web UI

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the tplink module:

* `/tplink/x/on` (x = device index): Turn on the switch
* `/tplink/x/off` (x = device index): Turn off the switch
* `/tplink/x/toggle` (x = device index): Toggle the switch on or off (invert its state)