# Robot 

## Getting Started
The robot must have:  
* two motors to control the wheels  
* one motor to control throwing arm  
* one motor to control release point angle  
* EV3 Gyro sensor  
* EV3 Ultrasonic sensor  
* EV3 Touch sensor  

## Configuration Values
These are values that control how the robot operates.  
All values can be adjusted with the use of a Properties file.  
See example.properties for Example Properties File  

mockEV3: false (mock EV3 should be false unless running a test without actual EV3 on)  
IP Address: 10.0.1.1  
Port A: Right Wheel: Large Motor  
Port B: Shooter Angle: Large Motor (Adjusts release point)  
Port C: Shooter Arm: Medium Motor  
Port D: Leftt Wheel: Large Motor  
Port 1: Gyro Sensor  
Port 2: Ultrasonic Sensor  
Port 3: Empty  
Port 4: Touch Sensor  
Max Shooter Angle: 25 (Maxium the release point can be set to)  
Shooter Set Up: -65 (Upon start up, shooter arm is set to 'resting' position)
Shooter Wind Up: 65  
Shooter Throw: -150  
Min Distance to Ultrasonic: 4 (Minimum distance ultrasonic sensor can be from any object)  
