The **discord** integration allows you to connect NMO with Discord. By doing this, your schedule status will be displayed as the name of the game you are playing. You can also automatically send messages to Discord servers, groups or friends.

The configuration for this looks as follows:

```json
    "discord": {
      "enabled": true,
      "authToken": "REDACTED",
      "messages": [
        {
          "name": "OVERSLEEPING MESSAGE",
          "description": "I appear to be oversleeping. FGS",
          "targetType": "SERVER",
          "targetID": 370561888259670016,
          "message": "<@&370561458465144833> I appear to be oversleeping. FGS"
        }
      ]
    },
```

`authToken` must be set to your Discord authentication token. To find this out, press CTRL+SHIFT+I in Discord to open the Inspector, then navigate to the Application tab and open Local Storage for https://discordapp.com/. Under this storage area the token should be located.

The `messages` array contains a list of each message you wish NMO to be able to send to Discord. For each message you must specify the following:

* `name`: A human readable name for this message
* `description`: A human readable description of this message
* `targetType`: The type of the place you want to send the message, must be `SERVER`, `GROUP` or `USER`
* `targetID`: The ID of the place you want to send the message (you can copy this from Discord using developer mode)
* `message`: The message to send

Please note that SERVER target is for targeting channels in servers rather than servers themselves. Consequently the targetID of a SERVER message should be set equal to the ID of a channel in the desired server and not the ID of the server itself.

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the discord module:

* `/discord/x` (x = message index): Send the message