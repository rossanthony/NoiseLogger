# NoiseLogger Android App
Takes a 1 second sound sample every 15 seconds and sends the max amplitude to the cloud along with lat/long coordinates.

MUC Coursework 2015: for the Mobile and Ubiquitous Computing module at the Department of Computer Science and Information Systems at Birkbeck College, University of London.

Data is posted to ThingSpeak API, see: [public stats page](https://thingspeak.com/channels/33660) or [Json feed](http://api.thingspeak.com/channels/33660/feed.json?key=8DOZPI1LTOFZ0PSZ).

Built using the latest version of Android Studio on Mac OS.

Tested and debugged via USB on Nexus 4, running Android 5.0.1 (API 21).

NOTE: upon initial boot of the App the sound logging is not enabled, it can be enabled via the switch on the main app screen. This then triggers a background service to run via an Alarm Manager at an interval of 15 seconds.

[Licence: CC0 1.0 Universal](LICENSE)