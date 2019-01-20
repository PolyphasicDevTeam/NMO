NoMoreOversleeps uses information about your sleep and activity schedule to provide advance warning of sleep blocks and to automatically disable the alarm functions for the duration of your sleep block.

Here is an example of a valid schedule configuration:

```json
  "scheduleName": "DUAL CORE 4",
  "schedule": [
    {
      "start": 60,
      "end": 155,
      "name": "GRAVEYARD CORE",
      "type": "CORE",
      "approachWarning": 5
    },
    {
      "start": 300,
      "end": 395,
      "name": "DAWN CORE",
      "type": "CORE",
      "approachWarning": 5
    },
    {
      "start": 540,
      "end": 570,
      "name": "MORNING NAP",
      "type": "NAP",
      "approachWarning": 5
    },
    {
      "start": 780,
      "end": 810,
      "name": "LUNCH NAP",
      "type": "NAP",
      "approachWarning": 5
    },
    {
      "start": 1020,
      "end": 1050,
      "name": "AFTERNOON NAP",
      "type": "NAP",
      "approachWarning": 5
    },
    {
      "start": 1260,
      "end": 1290,
      "name": "EVENING NAP",
      "type": "NAP",
      "approachWarning": 5
    }
  ],
```

The specifics of this will now be broken down for you.

### Schedule Name
The `scheduleName` field should contain a human-readable name for your schedule. For example, DUAL CORE 4.
```json
  "scheduleName": "DUAL CORE 4",
```

### Schedule
The `schedule` section is an array of sleep blocks. In the above example there were 6 sleep blocks, with one array entry for each block. Here is a breakdown of how each block is formatted:
```json
    {
      "start": 60,
      "end": 155,
      "name": "GRAVEYARD CORE",
      "type": "CORE",
      "approachWarning": 5
    },
```

* `start` and `end` are numeric values which refer to when your sleep block starts and ends. The format for these fields is **minutes past midnight** so in the above example, `"start": 60` means to start at 1:00 AM and `"end": 155` means to end at 2:35 AM.
* `name` is a human-readable name for this sleep block. Call it whatever you like.
* `type` describes what type of block this is. It must be a value of either `CORE`, `NAP`, `SIESTA` or `AFK`. The AFK block type is technically treated as sleeping by NMO, but has been provided for any blocks of time where you would consistently like the alarm to be disabled (e.g. if you always go out between 1PM and 3PM).
* `approachWarning` is how many minutes beforehand you want to be warned about this sleep block's approach. This is to help prevent you from forgetting to sleep and to give you some cooldown time prior to sleeping. The default value is 5 minutes.

## Did it work?
After configuring the schedule, next time you start NoMoreOversleeps you should find the following:

* The name and timing of your schedule is described under the 'Current Schedule' section
* The current status, along with the next sleep block and a countdown should be displayed under 'Monitoring Control'.

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the schedule module:

* `/schedule/resetLastOversleep`: Reset the time since last oversleep.