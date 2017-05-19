# EV3Agent  
EV3Agent is a [Dorset](https://github.com/DorsetProject) intelligent agent. It takes in commands to control a LEGO Mindstorms EV3 Robot.  

## Getting Started 
* You will need an empty SD card of at least 2GB and no more than 32GB, formatted as FAT32, and that has 400MB of free space. SDXC cards are not supported.  
[LeJOS Windows Installation and SD Card Setup](https://sourceforge.net/p/lejos/wiki/Windows%20Installation/)  
[Mac or Linux Installation](https://sourceforge.net/p/lejos/wiki/Installing%20leJOS/)
* Pair EV3 with your computer via bluetooth  
[Pairing and Connecting to a PAN](https://sourceforge.net/p/lejos/wiki/Configuring%20Bluetooth%20PAN/)  

## Versioning  
* Java 7 (This might work on 8, but hasn't been tested)
* Maven 3.1+  

## Compile Agent

```
cd AGENT_DIRECTORY
mvn clean package install
```

## Run  
* Connect EV3 to a Personal Area Network(PAN). Instructions at PAN link above.  

#### For Command Line Demo:  
```
    cd COMMANDLINE_DIRECTORY  
    mvn clean package install 
    java -jar target/CommandLine-0.0.1.jar
```  
#### For Web Demo:  
```
    cd WEB_DIRECTORY
    mvn clean package install
    run.bat  
```
* Open web browser to localhost:8888  

## Valid Commands  

* **angle [number]** Adjusts the angle of the release point 0-25 degrees  
* **turn [number]** Turns the robot specified amount 0-360 degrees  
* **move [number]** Moves the robot specified amount in centimeters  
* **fire** Throws ball using shooting arm  
* **stop** Stops all motor’s motion 
* **quit** Ends session with EV3  
* **battery**	Returns battery voltage  
* **manual** Waits for button on robot to be pressed and fires  
* **forward**	Moves the robot forward until “stop” is called  
* **backward** Moves the robot backward until “stop” is called 
* **commands** Returns a list of commands  
* **help [command]** Returns function of specified command  