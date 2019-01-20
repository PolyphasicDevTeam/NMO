The **fileWriter** integration continually writes information about the current state of NMO to files on the disk. The purpose of this file is to allow for accessing that data using an external program. For instance, you can read these files using OBS or XSplit in order to display information about your current sleep schedule as part of a video stream.

If you have no reason to use this integration you should leave it disabled because it constantly writes files to the disk which may cause unnecessary disk wear if you are not using the outputted files for anything.

The configuration for this looks as follows:

```json
    "fileWriter": {
      "scheduleName": true,
      "scheduleStartedOn": true,
      "scheduleLastOversleep": true,
      "schedulePersonalBest": true,
      "timeToNextSleepBlock": true
    },
```

If any of these are enabled, a folder called `out` will be created in the NMO installation directory and the relevant information will be written to the file with the respective name.