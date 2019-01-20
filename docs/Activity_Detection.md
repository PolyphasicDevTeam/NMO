The activity detection integrations add different ways for NMO to detect system activity. You can enable or disable these from the configuration file, but it is STRONGLY RECOMMENDED to enable ALL modules which you are able to enable. This will provide the maximum coverage for activity detection which will improve the accuracy and usability of NoMoreOversleeps.

### Keyboard

```json
    "keyboard": {
      "enabled": true
    },
```

Here you can enable or disable the keyboard hook. If the keyboard hook is enabled, whenever you type a key on the keyboard the active timer will be reset to 0.

### Mouse

```json
    "mouse": {
      "enabled": true
    },
```

Here you can enable or disable the mouse hook. If the mouse hook is enabled, whenever you move the mouse cursor or press down one of the mouse buttons the active timer will be reset to 0.

### XBOX Controller

```json
    "xboxController": {
      "enabled": true
    },
```

Here you can enable or disable the XBOX Controller hook (IT ONLY WORKS ON WINDOWS). It allows you to detect input from any games controller which is compatible with XInput libraries (i.e. any controller which can be used as an Xbox 360 controller). Most modern games controllers should be supported. Whenever you press a button on the controller, the active timer will be reset to 0.

### MIDI Transmitter

```json
    "midiTransmitter": {
      "enabled": true,
      "transmitters": [
        "Alesis Recital "
      ]
    },
```

Here you can enable or disable the MIDI Transmitter hook. This allows you to capture data from MIDI transmitters such as digital pianos (either over USB or over traditional MIDI connectors). You are required to fill the `transmitters` array which contains a list of the transmitters to capture from. Whenever you send MIDI data from one of those transmitters (e.g. you press one of the keys down on a piano) the active timer will be reset to 0.
