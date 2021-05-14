# Setup REST and MQTT



* You can import the project directly from a *Version Control System*, by providing the following URL: 
https://ewserver.di.unimi.it/gitlab/riccardopresotto/setup_rest_mqtt.git

<img src = './assets/img_1.png'>


* Otherwise, take care to import the project as a Gradle Project
* If required, trust the project and accept the Gradle auto-import
* Wait until the Gradle indexing process ends (it may take a few seconds)

## REST

* Run StartServer.java located in: *REST\_MQTT_example/src/main/java/REST/*
    * Don't worry about the red *INFO* console prints
* Use a REST Client application (i.e., Advanced REST Client) to test if the server works correctly
* For example you can run the following request:

<img src = './assets/img_2.jpeg'>


## MQTT
* Dowload and install the Mosquitto broker from here: *https://mosquitto.org/download/*
* Run the Mosquitto broker:
	* **MacOS**
		- brew services start mosquitto
		- mosquitto_sub -h localhost -v -t ‘#’  *(Instead ‘#’ specify the topics of interest)*
	* **Windows**
		- Run the file mosquitto.exe

* Run SubExample.java and PubExample.java located in *REST\_MQTT_example/src/main/java/MQTT/*
