The alarm function in NoMoreOversleeps is not like an ordinary alarm. Instead, it is built around a programmable timer representing how long it has been since you were last active at your computer. The timer constantly counts upwards and whenever activity is detected it is reset back to 0. If you stop being active, and the timer value goes past a certain threshold, NMO will attempt to get your attention by firing an **activity warning**. If this fails to get your attention, it will then begin to repeatedly fire subsequent warnings until activity is detected again.

Therefore, in the event that you are active at your computer, the timer should be continually reset to 0 and will never go off. But, if you cease being active for whatever reason (e.g. you fall asleep at your computer), the timer will eventually hit the warning threshold and will then try to get your attention.

This makes NMO a unique but very powerful alarm for those doing a polyphasic adaptation, as it will attempt to wake you whenever it thinks you have dozed off at the wrong time, instead of simply attempting to wake you at the end of your sleep blocks like a conventional alarm would. Consequently, if you use the software correctly, you are forced to remain awake during waking hours, and if you fall asleep at your computer or fail to wake quickly enough from a sleep block the alarm will just go off again to wake you back up.

In the event you need to go away from keyboard at any time which is not in your configured sleep schedule, NMO also includes a 'pause' feature which can be used to temporarily pause the timer and prevent it from firing. You can pause from the GUI from any length between 5 minutes and 12 hours. You will be required to insert a pause reason and solve a CAPTCHA in order to enable the pause.

## Configuration

The timers section of the configuration allows you to set up multiple different timers, each timer being set with different warning thresholds. NMO will automatically configure a default timer on first launch with the first warning after 5 minutes and then subsequent warnings every 10 seconds. If you add additional timers to the configuration file you can switch between them either manually or automatically, allowing you to set different thresholds for different situations.

Below shows an example of a valid configuration for timers:

```json
  "timers": [
    {
      "name": "MEDIUM TIMER",
      "secondsForFirstWarning": 300,
      "secondsForSubsequentWarnings": 10
    },
    {
      "name": "SHORT TIMER",
      "secondsForFirstWarning": 90,
      "secondsForSubsequentWarnings": 10
    },
    {
      "name": "LONG TIMER",
      "secondsForFirstWarning": 600,
      "secondsForSubsequentWarnings": 10
    }
  ],
  "oversleepWarningThreshold": 5,
```
Now to break this down for you.

### Timers
The `timers` section is an array of possible timers. In the above example there were 3 timers, with one array entry for each timer, and by default the first timer in the list will be active on startup. Here is a breakdown of how each timer entry is formatted:
```json
    {
      "name": "MEDIUM TIMER",
      "secondsForFirstWarning": 300,
      "secondsForSubsequentWarnings": 10
    },
```

* `name` is a human-readable for this timer. Call it whatever you like.
* `secondsForFirstWarning` is how many seconds of inactivity to wait before NMO makes its first attempt to get your attention. This first warning is intended as a gentle nudge and in normal use is likely to be triggered quite frequently. It is strongly recommended that you set a small value for this first warning (the default is 5 minutes) because very long timers can result in you being woken too slowly in the event you did actually doze off.
* `secondsForSubsequentWarnings` is how many seconds of inactivity to wait for further warnings after the first. The default value is 10. What this means is that, once the first warning has gone off, a subsequent warning will be triggered every 10 seconds.

Thus, if you set `secondsForFirstWarning` to 300, and `secondsForSubsequentWarnings` to 10, the first warning will be triggered after 300 seconds of inactivity, and then subsequent warnings will be triggered after 310, 320, 330, etc.

### Oversleep Warning Threshold
```json
  "oversleepWarningThreshold": 5,
```
For the first few warnings that are triggered, you will be considered **missing**. Once the `oversleepWarningThreshold` value has been reached (set to 5 by default) NMO considers you to be **oversleeping** instead, and will perform some additional actions to try to wake you up. It is strongly recommended to just leave the default value here and not change it.

## Did it work?
After setting your timers they should be visible on the frontend.

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the timers module:

* `/timer/x` (where `x` is the array index starting from 0): Switch to that timer.