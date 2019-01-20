The **events** system is at the heart of the core function of NoMoreOversleeps. Events are fired in response to certain things happening within the logic of NMO (for example, an event is fired whenever a sleep block starts, whenever a timer triggers an activity warning, etc). The configuration file allows you to set up a list of actions which are automatically executed in response to each of these events, based on your personal preferences.

Essentially, the events system allows you to configure "whenever event X happens, automatically do actions Y and Z". For example, you could configure it so that whenever a nap is approaching, a sound is played to remind you about the nap. Or you could make it so that if the oversleep warning threshold is reached, your computer monitors and speakers automatically turn on. The events system in NMO is incredibly customizable to the point where the possibilities here are essentially limited only by your imagination.

Each integration module in NMO adds a series of related actions which all follow the syntax `/integrationName/actionName`. For example, the noise module (which allows you to play custom noises) might add an action like `/noise/4` which plays noise 4. After checking for the specific code for the action you want, you can add the code to the desired event in the configuration file and NMO will then execute it automatically whenever that event is fired.

Here is an example of a valid events configuration:

```json
  "events": {
    "coreApproaching": [
      "/noise/41"
    ],
    "coreStarted": [],
    "coreEnded": [
      "/timer/0"
    ],
    "napApproaching": [
      "/noise/17"
    ],
    "napStarted": [],
    "napEnded": [
      "/timer/0"
    ],
    "siestaApproaching": [],
    "siestaStarted": [],
    "siestaEnded": [],
    "afkApproaching": [],
    "afkStarted": [],
    "afkEnded": [],
    "activityWarning1": [
      "/noise/4"
    ],
    "oversleepWarning": [
      "/noise/1",
      "/noise/2",
      "/noise/3",
      "/noise/4",
      "/discord/0"
    ],
    "activityWarning2": [
      "/cmd/0",
      "/cmd/3",
      "/cmd/5",
      "/noise/4",
      "/philipsHue/0/toggle"
    ],
    "pauseInitiated": [],
    "pauseCancelled": [],
    "pauseExpired": [],
    "custom": []
  },
```

The responses for each event are configured by simply providing an array of actions that should be triggered.

Here is a list of all of the events and what they are for:

* `coreApproaching`, `napApproaching`, `siestaApproaching` and `afkApproaching` are fired automatically before each matching sleep block in line with the `approachWarning` value that is configured on the schedule
* `coreStarted`, `napStarted`, `siestaStarted` and `afkStarted` are fired when those specific sleep blocks start
* `coreEnded`, `napEnded`, `siestaEnded` and `afkEnded` are fired when those specific sleep blocks end
* `activityWarning1` is fired for the first activity warning (when the active timer reaches the `secondsForFirstWarning` threshold)
* `oversleepWarning` is fired as a one-off event for the activity warning which matches the `oversleepWarningThreshold`
* `activityWarning2` is fired for all other activity warnings
* `pauseInitiated` is fired whenever you pause the activity timer
* `pauseCancelled` is fired whenever you manually unpause the activity timer
* `pauseExpired` is fired whenever NMO automatically unpauses the activity timer due to the pause having expired

## Custom events
Since version 0.11 it is possible to configure custom events which trigger automatically based on the system clock. The following shows a valid custom event configuration:

```json
    "custom": [
      {
        "name": "At end of first core sleep",
        "days": [
          "SUN",
          "MON",
          "TUE",
          "WED",
          "THU",
          "FRI",
          "SAT"
        ],
        "times": [
          155
        ],
        "actions": [
          "/discord/1"
        ]
      },
      {
        "name": "At end of second core sleep",
        "days": [
          "SUN",
          "MON",
          "TUE",
          "WED",
          "THU",
          "FRI",
          "SAT"
        ],
        "times": [
          395
        ],
        "actions": [
          "/discord/2"
        ]
      }
    ]
```

Here you can see an array of custom events. For each custom event there are the following fields:

* `name` - the name of the event
* `days` - an array of which days this event should fire on. The day names must be three-characters and can be `MON`, `TUE`, `WED`, `THU`, `FRI`, `SAT` or `SUN`
* `times` - an array of which times this event should fire on. Like with the sleep schedule entries, these values are in minutes past midnight.
* `actions` - an array of event actions which should be executed when this event fires.

## Did it work?
After configuring the events, they should be visible on the frontend.

Actions which have been recognized will be printed in green. Actions which are not recognized will be printed in red. This makes it easy to see if you have made a mistake with your action strings.