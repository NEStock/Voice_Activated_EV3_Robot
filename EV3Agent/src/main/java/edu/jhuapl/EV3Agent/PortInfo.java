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

import java.util.Properties;
import org.slf4j.LoggerFactory;

public class PortInfo {
	private boolean mockEV3;
	private String ipAddress;
    private String rightWheelPort;
    private String shooterAnglePort;
    private String shooterPort;
    private String leftWheelPort;
    private char rightWheelSize;
    private char shooterAngleSize;
    private char shooterSize;
    private char leftWheelSize;
	private int maxShooterAngle;
	private int shooterSetUp;
	private int shooterWindUp;
	private int shooterThrow;
	private String gyroSensorPort;
	private String touchSensorPort;
	private String ultrasonicSensorPort;
	private int minDistToUltrasonic;
	private static final char MEDIUM = 'M';
	private static final char LARGE = 'L';
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(PortInfo.class);
	
	/**
	 * Constructor
	 * Sets up port information based on information in properties file
	 * If no properties file, sets up default values
	 */
    public PortInfo(Properties prop) throws IllegalArgumentException {
    	prop = new Properties();
    	//true for mock, false for ev3
    	String bool = prop.getProperty("mockEV3", "false"); 
    	mockEV3 = validateBool(bool);
    	ipAddress = prop.getProperty("ipAddress", "10.0.1.1");
    	rightWheelPort = prop.getProperty("rightWheelPort", "A");
    	validateMotorPort(rightWheelPort);
    	shooterAnglePort = prop.getProperty("shooterAnglePort", "B");
    	validateMotorPort(shooterAnglePort);
    	shooterPort = prop.getProperty("shooterPort", "C");
    	validateMotorPort(shooterPort);
    	leftWheelPort = prop.getProperty("leftWheelPort", "D");
    	validateMotorPort(leftWheelPort);
    	String size = prop.getProperty("rightWheelSize", "L");
    	rightWheelSize = validateSize(size);
    	size = prop.getProperty("shooterAngleSize", "L");
    	shooterAngleSize = validateSize(size);
    	size = prop.getProperty("shooterSize", "M");
    	shooterSize = validateSize(size);
    	size = prop.getProperty("leftWheelSize", "L");
    	leftWheelSize = validateSize(size);
    	String angle = prop.getProperty("maxShooterAngle", "25");
    	maxShooterAngle = validateInt(angle);
    	angle = prop.getProperty("shooterSetUp", "-65");
    	shooterSetUp = validateInt(angle);
    	angle = prop.getProperty("shooterWindUp", "65");
    	shooterWindUp = validateInt(angle);
    	angle = prop.getProperty("shooterThrow", "-150");
    	shooterThrow = validateInt(angle);
    	gyroSensorPort = prop.getProperty("gyroSensorPort", "1");
    	validateSensorPort(gyroSensorPort);
    	touchSensorPort = prop.getProperty("touchSensorPort", "4");
    	validateSensorPort(touchSensorPort);
    	ultrasonicSensorPort =prop.getProperty("ultrasonicSensorPort", "2");
    	validateSensorPort(ultrasonicSensorPort);
    	String dist = prop.getProperty("minDistToUltrasonic", "4");
    	minDistToUltrasonic = validateInt(dist);
    }
    /**
     * Default Constructor
     * Calls Constructor with parameter null
     */
    public PortInfo() {
    	this(null);
    }
    /**
     * Validates that the String reads either true or false
     * @param bool
     * @return boolean value representing String parameter
     */
    private boolean validateBool(String bool) {
    	if(!bool.equals("true") && !bool.equals("false")){
    		logger.error("Invalid boolean value set to " + bool);
    		throw new IllegalArgumentException("Invalid boolean set to " + bool);
    	}
    	else if(bool.equals("true")){
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    /**
     * Validates that port is A, B, C, or D
     * @param port
     * @throws IllegalArgumentException
     */
    private void validateMotorPort(String port) throws IllegalArgumentException {
    	if(!port.equals("A") && !port.equals("B") && !port.equals("C") && !port.equals("D")){
    		logger.error("Invalid motor port set to " + port);
    		throw new IllegalArgumentException("Invalid motor port set to " + port);
    	}
    }
    /**
     * Validates that size is L or M
     * @param size
     * @return
     * @throws IllegalArgumentException
     */
    private char validateSize(String size) throws IllegalArgumentException {
    	if(!size.equals("L") && !size.equals("M")){
    		logger.error("Invalid motor size set to " + size);
    		throw new IllegalArgumentException("Invalid motor size set to " + size);
    	}
    	else if(size.equals("L")){
    		return LARGE;
    	}
    	else{
    		return MEDIUM;
    	}
    }
    /**
     * Validates that angle is an integer value
     * @param angle
     * @return integer value representing String parameter
     * @throws IllegalArgumentException
     */
    private int validateInt(String angle) throws IllegalArgumentException {
    	try{
			return Integer.parseInt(angle);
    	} catch(IllegalArgumentException e){
    		logger.error("Invalid max angle set to " + angle + " " + e.getMessage());
    		throw new IllegalArgumentException("Invalid integer set to " + angle);
    	}
    }
    /**
     * Validates port is 1, 2, 3, or 4
     * @param port
     */
    private void validateSensorPort(String port){
    	if(!port.equals("1") && !port.equals("2") && !port.equals("3") && !port.equals("4")){
    		logger.error("Invalid sensor port set to " + port);
    		throw new IllegalArgumentException("Invalid sensor port set to " + port);
    	}
    }
    /**
     * Accessors and Modifiers for each instance variable
     */   
    public void setMockEV3(boolean mockEV3){
    	this.mockEV3 = mockEV3;
    }
    public boolean getMockEV3(){
    	return mockEV3;
    }
    public void setIPAddress(String ipAddress){
    	this.ipAddress = ipAddress;
    }
    public String getIPAddress(){
    	return ipAddress;
    }
    public void setRightWheelPort(String rightWheelPort){
    	this.rightWheelPort = rightWheelPort;
    }    
    public String getRightWheelPort(){
    	return rightWheelPort;
    }    
    public void setRightWheelSize(char rightWheelSize){
    	this.rightWheelSize = rightWheelSize;
    }    
    public char getRightWheelSize(){
    	return rightWheelSize;
    }
    public void setShooterAnglePort(String shooterAnglePort){
    	this.shooterAnglePort = shooterAnglePort;
    }
    public String getShooterAnglePort(){
    	return shooterAnglePort;
    }    
    public void setShooterAngleSize(char shooterAngleSize){
    	this.shooterAngleSize = shooterAngleSize;
    }    
    public char getShooterAngleSize(){
    	return shooterAngleSize;
    }
    public void setShooterPort(String shooterPort){
    	this.shooterPort = shooterPort;
    }    
    public String getShooterPort(){
    	return shooterPort;
    }    
    public void setShooterSize(char shooterSize){
    	this.shooterSize = shooterSize;
    }    
    public char getShooterSize(){
    	return shooterSize;
    }
    public void setLefttWheelPort(String leftWheelPort){
    	this.leftWheelPort = leftWheelPort;
    }    
    public String getLeftWheelPort(){
    	return leftWheelPort;
    }
    public void setLefttWheelSize(char leftWheelSize){
    	this.leftWheelSize = leftWheelSize;
    }
    public char getLeftWheelSize(){
    	return leftWheelSize;
    }
    public void setMaxShooterAngle(int maxShooterAngle){
    	this.maxShooterAngle = maxShooterAngle;
    }    
    public int getMaxShooterAngle(){
    	return maxShooterAngle;
    }    
    public void setShooterSetUp(int shooterSetUp){
    	this.shooterSetUp = shooterSetUp;
    }
    public int getShooterSetUp(){
    	return shooterSetUp;
    }
    public void setShooterWindUp(int shooterWindUp){
    	this.shooterWindUp = shooterWindUp;
    }    
    public int getShooterWindUp(){
    	return shooterWindUp;
    }
    public void setShooterThrow(int shooterThrow){
    	this.shooterThrow = shooterThrow;
    }
    public int getShooterThrow(){
    	return shooterThrow;
    }
    public void setGyroSensorPort(String gyroSensorPort){
    	this.gyroSensorPort = gyroSensorPort;
    }
    public String getGyroSensorPort(){
    	return gyroSensorPort;
    }
    public void setTouchSensorPort(String touchSensorPort){
    	this.touchSensorPort = touchSensorPort;
    }
    public String getTouchSensorPort(){
    	return touchSensorPort;
    }
    public void setUltrasonicSensorPort(String ultrasonicSensorPort){
    	this.ultrasonicSensorPort = ultrasonicSensorPort;
    }
    public String getUltrasonicSensorPort(){
    	return ultrasonicSensorPort;
    }
    public void setMinDistToUltrasonic(int minDistToUltrasonic){
    	this.minDistToUltrasonic = minDistToUltrasonic;
    }
    public int getMinDistToUltrasonic(){
    	return minDistToUltrasonic;
    }
}
