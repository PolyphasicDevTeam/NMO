## Prerequisites
NoMoreOversleeps is a JavaFX application. In order to use it, you are required to have Java 8u40 or later installed. If you are using OpenJDK on Linux, you must also install OpenJFX because the JavaFX component is packaged separately.

## Installation
Download the source code and compile it (`gradlew build`). Java 8 is currently required to compile (Java 9 does not work).

## Running for the first time
After getting your hands on NoMoreOversleeps and copying the distribution somewhere, the next step is to launch it. This is as simple as double clicking on **nmo.jar**. If your installation of Java is configured correctly then the program will be launched.

If you prefer command line, you can alternatively launch it like this: `java -jar nmo.jar`

Following the initial launch, you will notice two new JSON files (**config.json** and **stats.json**). These files provide configuration instructions to NoMoreOversleeps. You will need to edit them by hand using a text editor in order to set up the software to work the way you want.

Detailed configuration instructions can be found in the sidebar of this wiki. The distribution of NMO also includes several sample config files, so if you get stuck during the configuration of NMO you can look at the sample configs as a reference point.

Please be aware that the config.json and stats.json files are required to be perfectly valid JSON (i.e. no syntax errors). If you make a syntax error in your config file, you will be unable to start NMO. If you encounter this problem you can use a JSON validator such as https://jsonlint.com/ in order to find the syntax error and fix it.

### What to do if NMO won't start
If you edit the config files and NMO won't start, please make sure that:

* Your config files doesn't have any syntax errors (you can verify this using jsonlint)
* You haven't left any important sections of the config blank
* If you enabled the web UI, make sure you set at least 1 webcam

If you still can't figure out the problem, you should check out the log file **NoMoreOversleeps-0.log** which is found in the logs folder. It should contain information which will hopefully help identify the problem.

If you still can't figure it out, you can send your config and log file to be checked for errors - make sure to redact private stuff such as API tokens first though!