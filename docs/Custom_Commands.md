The **cmd** integration allows you to execute external programs using custom commands.

The configuration for this looks as follows:

```json
    "cmd": {
      "enabled": true,
      "commands": [
        {
          "name": "UNMUTE SOUND-OUT",
          "description": "Unmutes the sound output device.",
          "command": [
            "C:\\Tools\\nircmd64\\nircmd", "mutesysvolume", "0"
          ],
          "workingDir": "C:\\Tools\\nircmd64",
          "hidden": false,
          "secret": false
        }
      ]
    },
```

The `commands` array contains a list of each command you wish NMO to be able to execute. For each command you must specify the following:

* `name`: A human readable name for this command
* `description`: A human readable description of this command
* `command`: An array containing the path to the command binary along with any parameters
* `workingDir`: The working directory to use when executing the command
* `hidden`: Whether or not the action for this command is hidden from the frontend
* `secret`: Whether or not the action for this command is hidden from the Web UI

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the cmd module:

* `/cmd/x` (x = command index): Execute the command