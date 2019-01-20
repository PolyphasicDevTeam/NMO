**Pavlok** is a wristband manufactured by Behavioral Technology Inc. which can flash, beep, vibrate and give electric shocks to the wearer.

NoMoreOversleeps can communicate with the Pavlok to perform these actions by using the Pavlok Unlocked web service. In order for the actions triggered by NMO to reach the Pavlok, you must be signed in to Pavlok Unlocked on your phone, the phone must have an active internet connection, and the Pavlok must be paired with the phone over Bluetooth.

Below is the relevant configuration for Pavlok:
```json
    "pavlok": {
      "enabled": false,
      "auth": {
        "access_token": "REDACTED",
        "token_type": "bearer",
        "expires_in": 0,
        "refresh_token": "REDACTED",
        "scope": "user",
        "created_at": 0,
        "device": "pavlok"
      }
    },
```
The only thing which is required to be filled out for this module is `enabled`, which should be set to either true or false depending on if you wish to use it or not.

The `auth` section is automatically populated by NMO. If you do not have a valid `auth` section, then when you start NMO you will be prompted with the login screen for Pavlok Unlocked, and after logging in the auth section will be automatically populated with valid data. In this sense, the auth data is self-correcting, and you should consequently not modify it.

In the event you wish to log out and change to a different account, you can simply remove `auth` section from the config file. You will then see the login prompt again when you next start NMO.

## Remarks about Pavlok
### Reliability
Pavlok integration was the first integration added to NoMoreOversleeps. Unfortunately, it ended up not being very useful due to the glitchy and unreliable nature of the Pavlok's Bluetooth connection and push notifications, along with the fact that Pavlok's Heroku server seems to frequently be offline.

Frankly, when the communication method is NMO -> Heroku -> Push Notification -> App -> Bluetooth -> Pavlok, and most of these stages are intermittently dodgy, the chance of this working effectively is quite slim.

### Known issues
* The original API key used for Pavlok integration was blanked out as part of the public fork. Currently this renders the Pavlok integration non-functional. New API keys will have to be issued and added to the program in order to fix this.
* Occasionally, logging in to Pavlok just results in a 404 screen. I have no idea why. Just keep restarting the app and retrying until it works.
* When your login key to Pavlok API expires it isn't renewed. This doesn't seem to make any difference because it doesn't appear to matter that it's expired and is accepted by the API anyway :/
* LED flash on Pavlok 2 doesn't work. This is an issue/limitation with Pavlok 2 and not an error with NMO.

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the pavlok module:

* `/pavlok/led`: Flash the LED light on the Pavlok
* `/pavlok/beep`: Beep the Pavlok
* `/pavlok/vibration`: Vibrate the Pavlok
* `/pavlok/shock`: Shock the Pavlok