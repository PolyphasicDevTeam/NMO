The **twilio** integration allows you to use the Twilio communication APIs (https://www.twilio.com/) with NMO. You can use this integration to allow NMO to dial phone numbers.

### Prerequisites
In order to use this module you must have an account with Twilio. Fortunately, Twilio offers a free trial which is adequate enough to set up the NMO integration.

You will require **Account SID** and **Auth Token** which can be found on the Dashboard. You must also set up a phone number in the **Phone Numbers** section. Finally you must add the numbers of the phones you wish to dial in the **Verified Caller IDs** section.

Now you are ready to configure NMO.

### Configuration
The configuration for this looks as follows:
```json
    "twilio": {
      "enabled": true,
      "accountSID": "REDACTED",
      "authToken": "REDACTED",
      "numberFrom": "+44 1234 567890",
      "callingURI": "http://twimlets.com/holdmusic?Bucket\u003dcom.twilio.music.ambient",
      "phoneNumbers": [
        {
          "name": "FIRST NUMBER",
          "number": "+44 1122 334455",
          "hidden": false,
          "secret": false
        },
        {
          "name": "MOBILE",
          "number": "+44 7654 321098",
          "hidden": false,
          "secret": false
        }
      ]
    },
```
`accountSID` and `authToken` must be set to the SID and Token values found on the Dashboard.

`numberFrom` must be set to the phone number which you set up in the Phone Numbers section.

`callingURI` must be set to the URI of a Twimlet XML file. This describes what audio to play if the call is answered. By default, NMO automatically fills this field with the URI of an XML file which plays ambient music. Since the purpose of NMO integration here is pretty much just to make the phone ring to wake you up, you can probably leave this default value.

The `phoneNumbers` array contains a list of each number you wish NMO to be able to dial. For each number you must specify the following:

* `name`: A human readable name for this number
* `number`: The number
* `hidden`: Whether or not the action for dialling this number is hidden from the frontend
* `secret`: Whether or not the action for dialling this number is hidden from the Web UI

### Please note
If you are using the free trial, you are required to make at least one call every 30 days, otherwise Twilio deletes the phone number, which would obviously break the NMO integration.

## Event system actions
(For more information about the event system see the [[Events]] page)

The following actions are defined by the twilio module:

* `/twilio/x` (x = number index): Dial the phone number