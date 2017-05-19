/*
 * Copyright 2017 The Johns Hopkins University Applied Physics Laboratory LLC
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
/**
 * EV3Agent takes in commands to control a LEGO Mindstorms EV3 Robot. After taking in a 
 * command it determines which command it is being instructed to execute and sends a 
 * message to the robot via bluetooth.
 */
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import edu.jhuapl.dorset.agents.AbstractAgent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;
import edu.jhuapl.dorset.agents.Description;
import edu.jhuapl.dorset.nlp.Tokenizer;
import edu.jhuapl.dorset.nlp.WhiteSpaceTokenizer;

public class EV3Agent extends AbstractAgent{
	           
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(EV3Agent.class);
	private PortInfo portInfo;
	private int currentShooterAngle = 0;
	private String name = "EV3";
	private static final String SUMMARY = "Takes in commands to control LEGO Mindstorms EV3 Robot";
	private static final String[] EXAMPLE = new String[] {"fire", "angle 10", "turn 90"};
	private Description description;
	private EV3AgentPlatform agent;
	
	public EV3Agent(Properties prop){
		this.setDescription(new Description(name, SUMMARY, EXAMPLE));
		this.portInfo = new PortInfo(prop);
		if(portInfo.getMockEV3()){
			agent = new MockEV3Platform();
		}
		else{
			agent = new RemoteEV3Platform(portInfo);
		}
	}
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}	
	public Description getDescription(){
		return description;
	}
	public void setDescription(Description description){
		this.description = description;
	}
	public AgentResponse process(AgentRequest request){  
		logger.info("Handling the request: " + request.getText());
		if(!ev3On()){
			if(!setUp()){
				return cannotConnect();
			}
		}
		if(ev3On()){
			try{
				Tokenizer tokenizer = new WhiteSpaceTokenizer();
				String input = request.getText();
				input = input.toLowerCase();
				String[] inputTokenized = tokenizer.tokenize(input);
				String wordCommand = inputTokenized[0];
				int numberCommand;
				
				if(checkHelp(wordCommand)){
					return returnHelp(inputTokenized[1]);
				}
				else if(inputTokenized.length == 1){
					return(oneWordCommandHandler(wordCommand));
				}
				else if(inputTokenized.length == 2){
					try{
						numberCommand = Integer.parseInt(inputTokenized[1]);
						return(twoWordCommandHandler(wordCommand, numberCommand));
					} catch(Exception e1){
						logger.error("Invalid number " + e1.getMessage());
						return invalidNum();
					}
				}
				else{
					return invalidCommand();
				}
			} catch(Exception ex){
				logger.error("Could not process command " + ex.getMessage());
				try{
					closePorts();
				} catch(RemoteException e){
					logger.error("Error closing ports " + e.getMessage());
				}
				ev3Null();
				return reportError(ex);
			}
		}
		return cannotConnect();
	}
	/**
	 * Connects with the EV3 and ports
	 * @return boolean representing success
	 */
	public void ev3Null(){
		agent.ev3Null();
	}
	public boolean ev3On(){
		return agent.ev3On();
	}
	protected boolean setUp(){
		try{
			setUpEV3();
			setUpPorts();
			setUpShooter();
			return true;
		} catch(RemoteException | MalformedURLException | NotBoundException e){
			logger.error("Could not open ports " + e.getMessage());
			try{
				closePorts();
			} catch(RemoteException e1){
				logger.error("Error closing ports " + e1.getMessage());
			}
			return false;
		}
	}
	/**
	 * Connects with EV3 via IP Address
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	public void setUpEV3() throws RemoteException, MalformedURLException, 
			NotBoundException{
		agent.setUpEV3();
	}
	/**
	 * Connects with EV3 ports
	 * @throws RemoteException 
	 */
	public void setUpPorts() throws RemoteException{
		agent.setUpPorts();
	}
	/**
	 * Moves shooter arm to starting position
	 * @throws RemoteException
	 */
	public void setUpShooter() throws RemoteException{
		agent.setUpShooter();
	}
	/**
	 * Attempts to close ports
	 * @throws RemoteException
	 */
	public void closePorts() throws RemoteException{
		agent.closePorts();
	}
	/**
	 * Determines command being called
	 * @param command
	 * @return Instructions for what command does
	 */
	private AgentResponse returnHelp(String command){
		AgentResponse name = new AgentResponse("");
		if(command.equalsIgnoreCase("fire")){
			name = new AgentResponse("\"fire\" \nTells EV3 to fire once");
		}
		else if(command.equalsIgnoreCase("stop")){
			name = new AgentResponse("\"stop\" \nTells EV3 to stop all motion");
		}
		else if(command.equalsIgnoreCase("quit")){
			name = new AgentResponse("\"quit\" \nEnds program");
		}
		else if(command.equalsIgnoreCase("battery")){
			name = new AgentResponse("\"battery\" \nReturns EV3 battery level");
		}
		else if(command.equalsIgnoreCase("angle")){
			name = new AgentResponse("\"angle <number>\" \nChanges the angle of the shooter"
				+ "\nNumber should be from 1-25");
		}
		else if(command.equalsIgnoreCase("turn")){
			name = new AgentResponse("\"turn <number>\" \nChanges the angle the EV3 is facing"
				+ "\nNumber should be from 1-360");
		}
		else if(command.equalsIgnoreCase("manual")){
			name = new AgentResponse("\"button\" \nWaits for button on robot to be pressed "
				+ "and fires once");
		}
		else if(command.equalsIgnoreCase("forward")){
			name = new AgentResponse("\"forward\" \nMoves EV3 forward until \"stop\" command "
					+ "is called");
		}
		else if(command.equalsIgnoreCase("backward")){
			name = new AgentResponse("\"backward\" \nMoves EV3 backward until \"stop\" command "
					+ "is called");
		}
		else if(command.equalsIgnoreCase("move")){
			name = new AgentResponse("\"move <number>\" \nMoves EV3 \nNumber should be in centimeters");
		}
		else{
			name = new AgentResponse("Invalid command");
		}
		return name;
	}
	/**
	 * Determines if command is help
	 * @param wordCommand
	 * @return boolean value
	 */
	private boolean checkHelp(String wordCommand){
		switch(wordCommand){
			case "help":
				return true;
			default:
				return false;
		}
	}
	/**
	 * Determines command being called
	 * @param wordCommand
	 * @return Appropriate command method's response
	 * @throws RemoteException
	 */
	private AgentResponse oneWordCommandHandler(String wordCommand) 
			throws RemoteException {
		switch(wordCommand){
			case "fire":
				return returnFire();
			case "stop":
				return returnStop();
			case "quit":
				return endAll();
			case "battery":
				return returnBattery();
			case "commands":
				return returnCommandList();
			case "manual":
				return returnTouchSensor();
			case "forward":
				return returnMovingForward();
			case "backward":
				return returnMovingBackward();
			default:
				return invalidCommand();	
		}
	}
	/**
	 * Determines command being called
	 * @param wordCommand
	 * @param numberCommand
	 * @return Appropriate command method's response
	 * @throws RemoteException
	 */
	private AgentResponse twoWordCommandHandler(String wordCommand, int numberCommand) 
			throws RemoteException {
		switch(wordCommand){
			case "angle":
				if(checkShooterAngle(numberCommand)){ //new angle must be 0-25
					currentShooterAngle = changeShooterAngle(numberCommand, currentShooterAngle);
					return returnShooterAngleChange(numberCommand, currentShooterAngle);
				}
				else{
					return invalidAngle(currentShooterAngle);
				}
			case "turn":
				if(checkTurnAngle(numberCommand)){	//turns 0-360
					return returnTurn(numberCommand);
				}
				else{
					return invalidNum();
				}
			case "move":
				return returnMoving(numberCommand);
			default:
				return invalidCommand();
		}
	}
	/**
	 * Calls shoot command
	 * @return Agent Response Firing
	 * @throws RemoteException
	 */
	private AgentResponse returnFire() throws RemoteException {
		AgentResponse name = new AgentResponse("Firing");
		shoot();
		return name;
	}
	/**
	 * Commands robot to wind up, fire, and reset to rest position
	 * @throws RemoteException
	 */
	public void shoot() throws RemoteException {
		agent.shoot();
	}
	/**
	 * @return AgentResponse Stopped
	 * @throws RemoteException
	 */
	private AgentResponse returnStop() throws RemoteException {
		AgentResponse name = new AgentResponse("Stopped");
		stop();
		return name;
	}
	/**
	 * Stops all motors
	 * @throws RemoteException
	 */
	public void stop() throws RemoteException { 
		agent.stop();
	}
	/**
	 * Ends session with EV3 and closes motor ports
	 * @return AgentResponse Closing message
	 * @throws RemoteException
	 */
	private AgentResponse endAll() throws RemoteException {
		AgentResponse name = new AgentResponse("\nThank you for using the EV3 demo."
			+ "\nClosing motors");
		resetMotorsSensors();
		return name;
	}
	/**
	 * Closes motors and sensors
	 * Resets Release angle to 0
	 * @throws RemoteException
	 */
	private void resetMotorsSensors() throws RemoteException {
		agent.resetMotorsSensors(currentShooterAngle);
		currentShooterAngle = 0;
	}
	/**
	 * @return AgentResponse Battery Voltage
	 */
	private AgentResponse returnBattery(){
		AgentResponse name = new AgentResponse("Battery Voltage: " + Double.toString(getBattery()));
		return name;
	}
	/**
	 * @return double Battery Voltage
	 */
	private double getBattery(){
		return agent.getBattery();
	}
	/**
	 * @return AgentResponse List of commands
	 */
	private AgentResponse returnCommandList(){
		AgentResponse name = new AgentResponse("\t\"fire\"   \"stop\"   \"quit\"   \"battery\" "
				+ "\n\t \"forward\"   \"backward\"   \"manual\"   \n\t\"angle <number>\"   "
				+ "\"turn <number>\"   \"move <number>\""
				+ "\n\tTo learn about a specific command, type \"help <first word of command>\"");
		return name;
	}
	/**
	 * Checks whether robot is able to move by the amount specified by intputAngle 
	 * @param inputAngle
	 * @return boolean value
	 * @throws RemoteException
	 */
	public boolean checkShooterAngle(int inputAngle) throws RemoteException {
		if(inputAngle > 0 && inputAngle <= portInfo.getMaxShooterAngle()){
			if(currentShooterAngle + inputAngle <= portInfo.getMaxShooterAngle()){
				return true;
			}
			else{
				return false;	
			}
		}
		else if(inputAngle < 0 && inputAngle >= -portInfo.getMaxShooterAngle()){
			if(currentShooterAngle + inputAngle >= 0){
				return true;
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
	}
	/**
	 * Changes angle of shooter by inputAngle
	 * @param inputAngle
	 * @param bAngleCurrent
	 * @return int new current angle
	 * @throws RemoteException
	 */
	public int changeShooterAngle(int inputAngle, int bAngleCurrent) throws RemoteException {
		return agent.changeShooterAngle(inputAngle, bAngleCurrent);
	}
	/**
	 * @param inputAngle
	 * @param newAngle
	 * @return Changing angle
	 * @throws RemoteException
	 */
	private AgentResponse returnShooterAngleChange(int inputAngle, int newAngle) 
			throws RemoteException {
		AgentResponse name = new AgentResponse("Changing angle by " + inputAngle
			 + " degrees \nNew angle of shooter: " + newAngle);
		return name;
	}
	/**
	 * Checks if turn angle is between -360 and 360
	 * @param angle
	 * @return boolean value
	 */
	private boolean checkTurnAngle(int angle){
		if((angle >= -360) && (angle <= 360)){
			return true;
		}
		else{
			return false;
		}
	}
	/**
	 * Determines and rotates appropriate motor until Gyro Sensor 
	 * reports that the robot has reached desired angle of rotation.
	 * @param endAngle
	 * @return AgentResponse Turning
	 * @throws RemoteException
	 */
	private AgentResponse returnTurn(int endAngle) throws RemoteException{
		AgentResponse name = new AgentResponse("Turning");
		turn(endAngle);
		return name;
	}
	/**
	 * Determines which wheel to rotate
	 * Rotates wheel until sample provider indicates robot has turned desired amount
	 * @param endAngle
	 * @throws RemoteException
	 */
	public void turn(int endAngle) throws RemoteException {
		agent.turn(endAngle);
	}
	private AgentResponse returnTouchSensor() throws RemoteException {
		AgentResponse name = new AgentResponse("Firing in response to button");
		touchSensor();
		return name;
	}
	/**
	 * Waits for button to be manually pressed
	 * Calls shoot method when pressed
	 * @throws RemoteException
	 */
	public void touchSensor() throws RemoteException{
		agent.touchSensor();
	}
	/**
	 * @return AgentResponse Moving Forward
	 * @throws RemoteException
	 */
	private AgentResponse returnMovingForward() throws RemoteException {
		AgentResponse name = new AgentResponse("Moving forward");
		moveForward();
		return name;
	}
	/**
	 * @return AgentResponse Moving Backward
	 * @throws RemoteException
	 */
	private AgentResponse returnMovingBackward() throws RemoteException {
		AgentResponse name = new AgentResponse("Moving backward");
		moveBackward();
		return name;
	}
	/**
	 * Commands robot to move forward
	 * @throws RemoteException
	 */
	public void moveForward() throws RemoteException {
		agent.moveForward();
	}
	/**
	 * Commands robot to move backward
	 * @throws RemoteException
	 */
	public void moveBackward() throws RemoteException {
		agent.moveBackward();
	}
	/**
	 * @param numberCommand
	 * @return AgentResponse Moving 
	 * @throws RemoteException
	 */
	private AgentResponse returnMoving(int numberCommand) throws RemoteException {
		AgentResponse name;
		if(move(numberCommand)){
			name = new AgentResponse("Moving " + numberCommand);
		}
		else{
			name = new AgentResponse("Moving could not be completed. \nStarting distance was not determinable");
		}
		return name;
	}
	/**
	 * Determines and rotates appropriate motor until Ultrasonic Sensor 
	 * reports that the robot has moved desired amount.
	 * @param distanceToMove
	 * @throws RemoteException
	 */
	public boolean move(int distanceToMove) throws RemoteException {
		boolean moved = agent.move(distanceToMove, currentShooterAngle);
		currentShooterAngle = 0;
		return moved;
	}
	/**
	 * @return Invalid command response
	 */
	private AgentResponse invalidCommand() {
		AgentResponse name = new AgentResponse("Invalid command");
		return name;
	}
	/**
	 * @return Invalid number response
	 */
	private AgentResponse invalidNum(){
		AgentResponse name = new AgentResponse("Invalid number");
		return name;
	}
	/**
	 * @param currentAngle
	 * @return Invalid angle response
	 */
	private AgentResponse invalidAngle(int currentAngle){
		AgentResponse name = new AgentResponse("Invalid angle \nCurrent angle: "
			+ currentAngle + "\nNew angle must be 0-25");
		return name;
	}
	/**
	 * @param e
	 * @return Error message e
	 */
	private AgentResponse reportError(Exception e){
		AgentResponse name = new AgentResponse("Unable to execute command because of "
			+ e.getMessage());
		return name;
	}
	/**
	 * @return AgentResponse cannot connect 
	 */
	private AgentResponse cannotConnect(){
		AgentResponse name = new AgentResponse("Cannot connect to EV3. \nType \"quit\" "
			+ "and press 'Submit'. \nTry shutting down EV3"
			+ " and restarting. Then reconnect EV3 and run again.");
		return name;
	}
}