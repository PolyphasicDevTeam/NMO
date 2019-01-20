The **WeMo** integration module allows you to connect NoMoreOversleeps with Belkin WeMo Insight smart sockets. In order to use this module, you require the WeMo smart socket to already be attached to the local LAN over WiFi and you must know the IP address of the socket (you can check your router control panel to find this out).

The following is the configuration for this module:

```json
    "wemo": {
      "enabled": true,
      "devices": [
        {
          "name": "Whatever",
          "description": "This is a device of stuff and things",
          "ipAddress": "1.2.3.4",
          "hidden": false,
          "secret": false
        }
      ]
    },
```

The `devices` array contains a list of each WeMo smart switch you wish NMO to control. For each device you must specify the following:

* `name`: The name of the switch, as configured in the WeMo app
* `description`: A human readable description of this switch and what it is used for
* `ipAddress`: The IP address which the switch can be contacted at
* `hidden`: Whether or not the actions for this switch are hidden from the frontend
* `secret`: Whether or not the actions for this switch are hidden from the Web UI

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the wemo module:

* `/wemo/x/on` (x = device index): Turn on the switch
* `/wemo/x/off` (x = device index): Turn off the switch
* `/wemo/x/toggle` (x = device index): Toggle the switch on or off (invert its state)