The file **stats.json** contains statistics about the currently active schedule.

Here is an example:
```json
{
  "scheduleStartedOn": 1507939200000,
  "scheduleLastOversleep": 1509386007708,
  "schedulePersonalBest": 1641600000
}
```
The following fields are present:

* `scheduleStartedOn`: The number of milliseconds since Unix epoch (1 Jan 1970) representing when you started your schedule
* `scheduleLastOversleep`: The number of milliseconds since Unix epoch when you last overslept. Set to 0 if you haven't yet.
* `schedulePersonalBest`: Total number of milliseconds that elapsed during your best attempt at this schedule. Set to 0 to auto-calculate this value on next launch.

To convert dates into milliseconds since Unix epoch you can use http://www.epochconverter.com/ (make sure to use milliseconds and not seconds!)

Please note that NMO overwrites this file when it closes. You should therefore only modify the stats.json while NMO is NOT running.