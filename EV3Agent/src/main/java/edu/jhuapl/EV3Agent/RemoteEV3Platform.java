/*
 * Copyright 2016 The Johns Hopkins University Applied Physics Laboratory LLC
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.jhuapl.EV3Agent;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.slf4j.LoggerFactory;

import lejos.hardware.Battery;
import lejos.remote.ev3.RMIRegulatedMotor;
import lejos.remote.ev3.RMISampleProvider;
import lejos.remote.ev3.RemoteEV3;

public class RemoteEV3Platform implements EV3AgentPlatform{
	
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EV3Agent.class);
	private RemoteEV3 ev3;
	private RMIRegulatedMotor rightWheel;
	private RMIRegulatedMotor shooterAngle;
	private RMIRegulatedMotor shooter;
	private RMIRegulatedMotor leftWheel;
	private PortInfo portInfo;
	private RMISampleProvider gyroSampleProvider;
	private RMISampleProvider touchSampleProvider;
	private RMISampleProvider ultrasonicSampleProvider;
	
	/**
	 * Constructor
	 * @param prop Properties file
	 */
	public RemoteEV3Platform(PortInfo prop){
		this.portInfo = prop;
	}
	/**
	 * Sets EV3 to null
	 */
	public void ev3Null(){
		ev3 = null;
	}
	/**
	 * Determines if EV3 is on and connected
	 * @return boolean value
	 */
	public boolean ev3On(){
		if (ev3 == null){
			return false;
		}
		else{
			return true;
		}
	}
	/**
	 * Connects with EV3 via IP Address
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void setUpEV3() throws RemoteException, MalformedURLException, NotBoundException {
		ev3 = new RemoteEV3(portInfo.getIPAddress());
	}
	/**
	 * Connects with EV3 ports
	 * @throws RemoteException 
	 */
	public void setUpPorts() {
		rightWheel = ev3.createRegulatedMotor(portInfo.getRightWheelPort(), portInfo.getRightWheelSize()); 
		shooterAngle = ev3.createRegulatedMotor(portInfo.getShooterAnglePort(), portInfo.getShooterAngleSize()); 
		shooter = ev3.createRegulatedMotor(portInfo.getShooterPort(), portInfo.getShooterSize()); 
		leftWheel = ev3.createRegulatedMotor(portInfo.getLeftWheelPort(), portInfo.getLeftWheelSize());
		gyroSampleProvider = ev3.createSampleProvider(("S" + portInfo.getGyroSensorPort()), "lejos.hardware.sensor.EV3GyroSensor", "Angle");
		ultrasonicSampleProvider = ev3.createSampleProvider(("S" + portInfo.getUltrasonicSensorPort()), "lejos.hardware.sensor.EV3UltrasonicSensor", "Distance");
		touchSampleProvider = ev3.createSampleProvider(("S" + portInfo.getTouchSensorPort()), "lejos.hardware.sensor.EV3TouchSensor", "Touch");
	}
	/**
	 * Moves shooter arm to starting position
	 * @throws RemoteException
	 */
	public void setUpShooter() throws RemoteException{
		shooter.setSpeed(30);
		shooter.rotate(portInfo.getShooterSetUp());
	}
	/**
	 * Attempts to close ports
	 */
	public void closePorts(){
		try{
			rightWheel.close();
		} catch(RemoteException e){
			logger.error("Could not close Right Wheel Port " + e.getMessage());
		}
		try{
			shooterAngle.close();
		} catch(RemoteException e){
			logger.error("Could not close Shooter Angle Port " + e.getMessage());
		}
		try{
			shooter.close();
		} catch(RemoteException e){
			logger.error("Could not close Shooter Port " + e.getMessage());
		}
		try{
			leftWheel.close();
		} catch(RemoteException e){
			logger.error("Could not close Left Wheel Port " + e.getMessage());
		}
		try{
			gyroSampleProvider.close();
		} catch (RemoteException e){
			logger.error("Could not close Gyro Sensor Port " + e.getMessage());
		}
		try{
			touchSampleProvider.close();
		} catch (RemoteException e){
			logger.error("Could not close Touch Sensor Port " + e.getMessage());
		}
		try{
			ultrasonicSampleProvider.close();
		} catch (RemoteException e){
			logger.error("Could not close Ultrasonic Sensor Port " + e.getMessage());
		}
	}
	/**
	 * Commands robot to wind up, fire, and reset to rest position
	 * @throws RemoteException
	 */
	public void shoot() throws RemoteException{
		shooter.setSpeed(75);
		shooter.rotate(portInfo.getShooterWindUp());
		shooter.setSpeed(1250);
		shooter.rotate(portInfo.getShooterThrow());
		shooter.setSpeed(30);
		shooter.rotate(Math.abs(portInfo.getShooterThrow())-Math.abs(portInfo.getShooterWindUp()));
	}
	/**
	 * Stops all motors
	 * @throws RemoteException
	 */
	public void stop() throws RemoteException{
		rightWheel.stop(true);
		leftWheel.stop(true);
		shooterAngle.stop(true);
		shooter.stop(true);
	}
	/**
	 * Closes motors and sensors
	 * Resets Release angle to 0
	 * @throws RemoteException
	 */
	public void resetMotorsSensors(int bAngleCurrent) throws RemoteException{
		bAngleCurrent = changeShooterAngle(-bAngleCurrent, bAngleCurrent);
		rightWheel.close();
		shooterAngle.close();
		shooter.close();
		leftWheel.close();
		gyroSampleProvider.close();
		touchSampleProvider.close();
		ultrasonicSampleProvider.close();
	}
	/**
	 * @return double Battery Voltage
	 */
	public double getBattery(){
		return Battery.getVoltage();
	}
	/**
	 * Changes angle of shooter by inputAngle
	 * @param inputAngle
	 * @param bAngleCurrent
	 * @return integer new current angle
	 * @throws RemoteException
	 */
	public int changeShooterAngle(int inputAngle, int bAngleCurrent) throws RemoteException{
		shooterAngle.setSpeed(20);
		shooterAngle.rotate(inputAngle);
		bAngleCurrent += inputAngle;
		return bAngleCurrent;
	}
	/**
	 * Determines which wheel to rotate
	 * Rotates wheel until sample provider indicates robot has turned desired amount
	 * @param endAngle
	 * @throws RemoteException
	 */
	public void turn(int endAngle) throws RemoteException{
		boolean complete = false;
		float[] samples = gyroSampleProvider.fetchSample();
		int start = (int)samples[0];
		int end = start + endAngle;
		final double errorMargin = 0.1;
		if(endAngle > 0){
			leftWheel.stop(true);
			rightWheel.setSpeed(110);
			while(!complete){
				rightWheel.forward();
				samples = gyroSampleProvider.fetchSample();
				if(Math.abs(samples[0] - end) <= errorMargin || samples[0] > end){
					complete = true;
				}
			}
			rightWheel.stop(true);
		}
		else{
			rightWheel.stop(true);
			leftWheel.setSpeed(110);
			while(!complete){
				leftWheel.forward();
				samples = gyroSampleProvider.fetchSample();
				if(Math.abs(samples[0] - end) <= errorMargin || samples[0] < end){
					complete = true;
				}
			}
			leftWheel.stop(true);
		}
	}
	/**
	 * Waits for button to be manually pressed
	 * Calls shoot method when pressed
	 * @throws RemoteException
	 */
	public void touchSensor() throws RemoteException{
		boolean complete = false;
		float[] samples = touchSampleProvider.fetchSample();
		final int PRESSED = 1;
		while(!complete){
			samples = touchSampleProvider.fetchSample();
			if(samples[0] == PRESSED){
				complete = true;
				shoot();
			}
		}
	}
	/**
	 * Commands robot to move forward
	 * @throws RemoteException
	 */
	public void moveForward() throws RemoteException{
		rightWheel.forward();
		leftWheel.forward();
	}
	/**
	 * Commands robot to move backward
	 * @throws RemoteException
	 */
	public void moveBackward() throws RemoteException{
		rightWheel.backward();
		leftWheel.backward();
	}
	/**
	 * Determines and rotates appropriate motor until Ultrasonic Sensor 
	 * reports that the robot has moved desired amount.
	 * @param distanceToMove
	 * @param bAngleCurrent
	 * @throws RemoteException
	 */
	public boolean move(int distanceToMove, int bAngleCurrent) throws RemoteException{
		bAngleCurrent = changeShooterAngle(-bAngleCurrent, bAngleCurrent);
		boolean complete = false;
		final int errorMargin = 1;
		float[] samples = ultrasonicSampleProvider.fetchSample();
		if(100*samples[0] == Double.POSITIVE_INFINITY){
			return false;
		}
		int startDistance = (int)(100*samples[0]); //conversion from meters to centimeters
		int end = startDistance - distanceToMove;
		if(end < portInfo.getMinDistToUltrasonic()){
			end = portInfo.getMinDistToUltrasonic();
		}
		if(distanceToMove > 0){				
			moveForward();
			while(!complete){
				samples = ultrasonicSampleProvider.fetchSample();
				if(100*samples[0] == Double.POSITIVE_INFINITY){
					stop();
					return true;
				}
				if(Math.abs((int)(100*samples[0]) - end) <= errorMargin || (int)(100*samples[0]) < end){
					complete = true;
				}
			}
			stop();
		}
		else{	
			moveBackward();
			while(!complete){
				samples = ultrasonicSampleProvider.fetchSample();
				if(100*samples[0] == Double.POSITIVE_INFINITY){
					stop();
					return true;
				}
				if(Math.abs((int)100*samples[0] - end) <= errorMargin || ((int)100*samples[0] > end)){
					complete = true;
				}
			}
			stop();
		}
		return true;
	}
}
