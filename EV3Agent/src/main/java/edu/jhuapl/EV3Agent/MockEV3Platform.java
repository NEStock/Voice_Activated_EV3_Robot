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

public class MockEV3Platform implements EV3AgentPlatform {
	
	public MockEV3Platform(){
	}
	public void ev3Null(){
	}
	public boolean ev3On(){
		return true;
	}
	public void setUpEV3(){	
	}
	public void setUpPorts(){	
	}
	public void setUpShooter(){	
	}
	public void closePorts(){	
	}
	public void shoot(){
	}
	public void stop(){
	}
	public void resetMotorsSensors(int bAngleCurrent){
	}
	public double getBattery(){
		return 0;
	}
	public int changeShooterAngle(int inputAngle, int bAngleCurrent){
		bAngleCurrent += inputAngle;
		return bAngleCurrent;
	}
	public void turn(int endAngle){
	}
	public void touchSensor(){
	}
	public void moveForward(){
	}
	public void moveBackward(){
	}
	public boolean move(int distanceToMove, int bAngleCurrent){
		return true;
	}
}
