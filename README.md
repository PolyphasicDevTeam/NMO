# NoMoreOversleeps Polyphasic Sleeping Alarm #

This is a public fork of NoMoreOversleeps (NMO), a very simple JavaFX application which is designed to help you adapt to a new polyphasic sleeping schedule. NMO is essentially an alarm
clock on steroids - it tries to make sure you are awake during waking blocks, and sleeping during naps.

It was originally created for personal use, so some parts of the software are a little crude and there are a lot of rough edges. The procedure to set up NMO is also a bit convoluted.
You may essentially consider it an alpha version. Despite this, there have been a couple of people who have made use of the program and its popularity is slowly rising.
Hopefully you can find it helpful for your own polyphasic adaptations.

### Features ###

NoMoreOversleeps consists of two parts - an automated background monitor and a manual remote control. The automated part will try to wake you back up if it thinks you are asleep when
you shouldn't be. The manual portion offers the ability for a third party to monitor you remotely, so that they can do the same. In this way, you have the advantage of both a computer
and a human (or more than one) being able to monitor you, which will hopefully allow you to avoid any unwanted oversleeps.

Most parts of the program are configurable, with the number of customization options being slowly expanded. The software goes with a modular approach, allowing both the automated tracking
system and the selection of wake-up actions to be tailored to your needs as desired.

#### Automated monitoring ####

To determine if you are asleep when you shouldn't be, NoMoreOversleeps takes a look at your sleeping schedule and compares it with the system clock. If it is currently a time when you
should be awake, NoMoreOversleeps monitors the activity of input devices on your computer (currently supporting keyboard, mouse, Xbox360-compatible controllers and MIDI devices) and will
try to get your attention every X seconds if it fails to detect activity in any of those devices within a Y minute interval. The number of seconds, the detection interval and the method
used to get your attention can be customized as desired; you can also set multiple of these for different situations.

In the event you are deliberately going to be away from your computer for a certain amount of time, e.g. you're going shopping or going to work, you can pause the activity detection
feature of NoMoreOversleeps for a choosable length of time between 5 minutes and 12 hours. This will prevent NoMoreOversleeps from trying to get your attention. When pausing the
automated monitoring system, you are required to input the reason for the pause.

The pause function is also automatically activated during each of your sleep blocks in order to avoid a scenario where you go to sleep on time but get woken up mid-nap because you
forgot to pause the activity tracking.

An action of your choice can also be automatically performed X minutes before each sleep block starts so that you remember to go to sleep on time.

#### Manual monitoring ####

NoMoreOversleeps features a port-forwardable password-protected web UI which displays what part of your sleep schedule you are currently in, along with a live webcam feed. This allows
people to remotely monitor you, letting them see whether or not you are at your computer, along with how awake you are, if you're nodding off in front of your screen, etc.

In the event that the people who are monitoring you think that you're asleep at the wrong time, they can perform any of the actions you have configured to try and wake you up.

#### Custom events ####

In addition to the automated and manual monitoring, it is possible to create custom events which trigger actions at specific times on specific days of the week.

#### Configurable actions ####

The following actions can currently be configured for use with both automated and manual monitoring:

* **Activity warning timer change**: Switch to a different activity warning timer
* **Noise/Sound playback**: Configure any number of sounds of your choice
* **Command line execution**: Run any application on your machine
* **Pavlok**: Flash, beep, vibrate or shock
* **Twilio**: Call a phone number (e.g. your mobile)
* **Discord**: Send a predefined message to a server, group or user
* **Philips Hue smart light bulbs**: Turn the light bulbs on or off
* **TP-Link & WeMo Insight smart switches**: Turn the switches on or off

In particular, the command line execution can be particularly valuable, as it allows you to link NMO to other utility programs to perform more advanced functionality beyond that which
is offered by NMO out of the box. For example, a popular utility for NMO is [NirCmd](http://www.nirsoft.net/utils/nircmd.html) which can be used to create actions that automatically
unmute your sound card, change system volume to maximum and/or switch your sound output back to speakers if you left your headphones plugged in - then these actions can be remotely
triggered from the Web UI or as part of your alarm routine.

Randomizer and iterator modules are now also included, allowing you to create multiple groups of actions. When a randomizer group is executed, it triggers a random action from the group; you can use this
e.g. to play random noises from a list. When an iterator group is executed, it triggers actions from the group in order, with one trigger per execution; this allows you to perform more complex action
sequences if you so desire.

In the future, the list of supported actions will hopefully be expanded. If you're good at programming in Java, feel free to add your own (the implementation of new actions is quite simple).

#### Discord integration ####

There is a Discord integration module used by the Polyphasic Sleep Discord community which will update your 'Playing' status with the current schedule information
(AWAKE / SLEEPING / MISSING / OVERSLEEPING / PAUSED). To make use of this feature, simply enable the Discord integration module and paste in your authtoken.

#### Live text file data output ####

NoMoreOversleeps can optionally output text files containing information such as current schedule, time since start of schedule, time since last oversleep, personal best time, and current
schedule status (time remaining of/until sleep block). These text files can be used with software such as OBS to overlay the data onto a video stream, allowing you to live-stream your
polyphasic sleeping attempt, or even just render the data onto a personal local recording of your polyphasic sleeping attempt for archival or investigation purposes.

### General disclaimer ###

NoMoreOversleeps was designed to be used by people who mostly sit at their computer, have access to most/all of the integration features, and have a reliable buddy to watch their feed. While it can be
quite an effective alarm when used properly, it can be entirely useless if used wrongly. If you choose to use NMO, then you do so at your own risk - the authors do NOT take any responsibility for the
success or failure of your polyphasic adaptation ;)

### Setup and usage instructions ###

Documentation is included in the docs folder.

### Development instructions ###

Here is the basic procedure:

* Clone the code from the repository so that you can pull and update to future versions easily
* Open a command window or terminal in the folder where you placed the code
* Use the gradle wrapper to generate project files for the IDE of your choice (either `gradlew eclipse` or `gradlew idea`)
* Import the project into your IDE and run the application from the Main class to verify it launches
* Close the application
* Go into the `run` folder (it should be next to the `src` folder) and modify the configs
* Restart the application

If you want to avoid using an IDE, you can also just build the jar file and libraries using `gradlew build`, although this isn't anywhere near as user-friendly for editing.

To update the code to the newest release, simply pull and update as you normally would with any repository-based project. After that, simply run `gradlew eclipse` or `gradlew idea` again in order to rebuild the IDE project for latest version.

### Known issues ###

#### Pavlok ####
* The original API key used for Pavlok integration was blanked out as part of the public fork. Currently this renders the Pavlok integration non-functional. New API keys will have to be issued and added to the program in order to fix this.
* Occasionally logging in to Pavlok just results in a 404 screen for unknown reasons. Just keep restarting the app and retrying until it works.
* When your login key to Pavlok API expires it isn't renewed. This doesn't seem to make any difference because it doesn't appear to matter that it's expired and is accepted by the API anyway :/
* The Pavlok integration is not very useful due to the glitchy and unreliable nature of the Pavlok's bluetooth connection and push notifications along with the fact that Pavlok's Heroku server seems to frequently be offline.
* LED flash on Pavlok 2 doesn't work. This is an issue/limitation with Pavlok 2 and not an error with NMO.

#### Home Automation ####
* TP-LINK switches, WeMo switches and Philips Hue bridges are connected via IP address which means that the config must be updated if the IP of the switch/bridge changes. It would be better to use name-based device detection over UPnP instead.

#### Other
* Sometimes there is a weird memory fragmentation issue which causes the system to become very stuttery. When this happens, it may persist even after quitting NMO. The reasons for this are unknown, although it seems to be a JVM glitch as there doesn't seem to be anything specifically in the NMO code that would trigger it. If you encounter this error you can fix it by rebooting your system.