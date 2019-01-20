The **webUI** integration is probably the most complex integration. This integration sets up a port-forwardable web UI which allows others to remotely control your NMO installation.

## Prerequisites
### Port forwarding
In order to make use of the web UI you are required to set up port forwarding on your router. The default port is TCP 19992 which must be forwarded to the device running NMO. (If you do not have access to your router control panel or your ISP has issued you with a carrier-grade NAT this will not be possible.) While an automatic port-forward function is included in NMO it is not guaranteed to work and for best results you should set up the forwarding in your router control panel by hand.

### Dynamic DNS service
Since your IP address is likely to keep changing you are advised to set up a Dynamic DNS service. Since version 0.14 we now offer subdomains at **nmo.polyphasic.net** (for example, **joebloggs.nmo.polyphasic.net**) and if you want one of these you should ask the admins to set this up for you. Alternatively you can make use of any other DDNS service which is compatible with the dyndns2 protocol. Instructions are provided here for using DuckDNS which has historically been used by NMO users.

## Configuration
There are two files responsible for configuring the web UI.
### webusers.properties
This file provides username+password combinations to Jetty which is to secure the web UI against bots etc. It is in the following format:
```
USERNAMEHERE=PASSWORDHERE,user
```
Replace USERNAMEHERE and PASSWORDHERE with the username and password you want to use for your web UI. You can specify multiple combinations (one per line) but usually just a single combination is suffice.
### config.json
The following shows an example configuration of the web UI module in config.json:

```json
    "webUI": {
      "enabled": true,
      "username": "Joe Bloggs",
      "openUiLocally": false,
      "jettyPort": 19992,
      "webcams": {
        "CAMERA 1": "Microsoft® LifeCam Cinema(TM) 2",
        "CAMERA 2": "Microsoft® LifeCam HD-3000 0",
        "CAMERA 3": "SplitCam Video Driver 3"
      },
      "webcamSecurityKey": "REDACTED",
      "allowRemotePauseControl": false,
      "readProxyForwardingHeaders": false,
      "message": "Hello!<br/>Please use the buttons below to wake me up if I fall asleep.<br/>Thanks.",
      "ddns": {
        "enabled": true,
        "provider": "https://domains.google.com/",
        "domain": "YOUR-DOMAIN-GOES-HERE.nmo.polyphasic.net",
        "username": "YOUR USERNAME GOES HERE",
        "password": "YOUR PASSWORD GOES HERE",
        "updateFrequency": 300
      }
    },
```

The `username` field is for you to input your username. If you don't populate this it will be automatically populated by the name of your computer.

The `openUiLocally` value determines whether or not pressing the Launch Web UI button on the frontend should open the web UI locally. Most people can leave this set to false. (A small minority of poorly designed routers do not support the remote page load from within the internal network and will need this set to true.)

The `jettyPort` specifies which port you wish to run the web UI on. The default is 19992. Leave it like this unless you have good reason to change it.

The `webcams` array contains a list of all the webcams you wish to use. This is a simple mapping of friendly-name to device-name and you can specify as many cameras as you like (although you must specify at least 1 in order to use the web UI). The device-names of all connected cameras will be printed to the NMO log file on startup and you can check this log file to help determine what should go here.

The `webcamSecurityKey` is automatically populated by NMO with a cryptographically secure random key code. The key code prevents unauthorized people from accessing the webcam feed. If you have security problems just delete this key from the config file and it will be regenerated with a new secure value on next startup.

The `allowRemotePauseControl`, if enabled, will add a copy of the pause controls to the web UI. Please note that by doing this, anyone with access to the web UI can pause or unpause the timer system.

The `readProxyForwardingHeaders` should be set to true if you are running NMO behind a reverse proxy. For most people this should be left to false. Enabling the reverse proxy features will enable reading IP addresses from the X-Forwarded-For header, and since v0.15 will also cause the 'open web UI' to use HTTPS port 443 (which is assumed to be the proxy's endpoint) instead of using HTTP on the configured Jetty port.

The `message` is displayed on the web page above the buttons. You can put whatever you like here. If you use HTML formatting it will be preserved when rendering the web page.

### DDNS

The `ddns` subsection provides an automatic update service for your dynamic DNS. Any DDNS service which supports the dyndns2 protocol is supported. Below are instructions for the two main ones used by NMO:

#### nmo.polyphasic.net

To use this DDNS provider you must ask the admins for a domain, username and password. Once you have these, fill this out like follows:

```json
      "ddns": {
        "enabled": true,
        "provider": "https://domains.google.com/",
        "domain": "YOUR-DOMAIN-GOES-HERE.nmo.polyphasic.net",
        "username": "YOUR USERNAME GOES HERE",
        "password": "YOUR PASSWORD GOES HERE",
        "updateFrequency": 300
      },
```

#### Duck DNS

To use this DDNS provider you must register for an account with Duck DNS and create a new domain there. Once you have done this, fill this out like follows:

```json
      "ddns": {
        "enabled": true,
        "provider": "https://www.duckdns.org/",
        "domain": "YOUR-DOMAIN-GOES-HERE.duckdns.org",
        "username": "nouser",
        "password": "YOUR DUCK DNS UPDATE TOKEN GOES HERE",
        "updateFrequency": 300
      },
```

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the webUI module:

* `/webUI/cameraprivacy/on`: Enable camera privacy mode
* `/webUI/cameraprivacy/off`: Disable camera privacy mode