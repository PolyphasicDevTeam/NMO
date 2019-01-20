The **noise** integration allows you to play arbitrary WAV or MP3 audio clips. This is probably the most commonly used integration in NMO.

The configuration for this looks as follows:
```json
    "noise": {
      "enabled": true,
      "noises": [
        {
          "name": "SOME NOISE",
          "description": "This is a description of the noise",
          "path": "C:\\NMO\\sounds\\NMO_NOISE.mp3",
          "hidden": false,
          "secret": false
        },
        {
          "name": "A DIFFERENT NOISE",
          "description": "This is a totally different noise",
          "path": "sounds/some_other_noise.wav",
          "hidden": false,
          "secret": false
        },
        {
          "name": "URL ALARM",
          "description": "added to test that remote url works as a noise source",
          "path": "http://www.example.com/alarm.mp3",
          "hidden": false,
          "secret": false
        }
      ]
    },
```
The `noises` array contains a list of each noise you wish NMO to be able to play. For each noise you must specify the following:

* `name`: A human readable name for this noise
* `description`: A human readable description of this noise
* `path`: The path to the noise file (either absolute path, relative path or remote http/https URL)
* `hidden`: Whether or not the action for this noise is hidden from the frontend
* `secret`: Whether or not the action for this noise is hidden from the Web UI

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the noise module:

* `/noise/x` (x = noise index): Play the noise