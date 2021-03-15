# Airline-App

# Overview
This is a simple airline tracker app that runs on android. Gradle was used to help to manage and build this project.

Airline-App/app/src/main/java/com/example/airlineapp/ contains most relevant java source code.

Airline-App/app/src/main/res/layout/ contains all UI related xml code.

The IDE and emulator software I used is Android Studio. The app is developed using the "Pixel 2 API 29" emulator.

# To build and run this project
```sh
$ gradlew clean
```
```sh
$ gradlew build
```
```sh
$ $ANDROID_HOME/emulator/emulator @Pixel_2_API_29
```

# Features
You can add a flight to the tracker by typing the flight info in the required format.
The app will go thru parse your input and perform some format checking to ensure the data is acceptable.
The error message will pop up if the flight wasn't added.

You can browse all the flights of an airline by either click on the tab of the list or search for its airline name. 
The flight info will be displayed in a more readable manner. 

You can also search for flights from the same airline that travel from target airport to target airport.
The search result will show these flights in a more readable manner as well.

# Code Review Notes
The fields and methods naming is meaningful from the start, and the errors are all thrown with correct names and caught.

Many functions that include format checking and XML parsing features were extremely lengthy, and there were some duplications caused by copy-pasting the same code and modifying on top of that.

Since we used to use the maven site feature to configures some codebase reports and analysis, the comments explain the functions and parameters of the methods. Additional comments are also added for private methods created when breaking the large functions into small reusable utilities.
