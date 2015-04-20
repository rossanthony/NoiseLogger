# NoiseLogger Android App
Takes a 1 second sound sample every 15 seconds and sends the max amplitude to the cloud along with lat/long coordinates.

MUC Coursework 2015: for the Mobile and Ubiquitous Computing module at the Department of Computer Science and Information Systems at Birkbeck College, University of London.

Data is posted to ThingSpeak API, see: [public stats page](https://thingspeak.com/channels/33660) or [Json feed](http://api.thingspeak.com/channels/33660/feed.json?key=8DOZPI1LTOFZ0PSZ).

Screenshots are included documenting the data captured during testing, see [here](SCREENSHOT-from-ThingSpeak-dashboard.png) and [here](SCREENSHOT - after travelling across London.pdf).

Built using the latest version of Android Studio on Mac OS. Tested and debugged on Nexus 4, running Android 5.0.1 (API 21) with developer mode enabled, installed via USB.

All source files for the build are included, as well as debug and release .apk files in the root NoiseLogger folder.

NOTE: upon initial boot of the App the sound logger is not enabled, it can be enabled via the switch on the main app screen. This then triggers a background service to run via an Alarm Manager at an interval of 15 seconds.

##Licence

[CC0 1.0 Universal](LICENSE) I hearby confirm that all code within app/src/main/java is my own code except where otherwise credited in the comments above certain methods, where I have reused snippets of code provided in tutorials or forums online.