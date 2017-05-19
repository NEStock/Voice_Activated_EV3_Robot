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

public interface EV3AgentPlatform {
	
	public abstract void ev3Null();
	public abstract boolean ev3On();
	public abstract void setUpEV3() throws RemoteException, MalformedURLException, NotBoundException;
	public abstract void setUpPorts();
	public abstract void setUpShooter() throws RemoteException;
	public abstract void closePorts();
	public abstract void shoot() throws RemoteException;
	public abstract void stop() throws RemoteException;
	public abstract void resetMotorsSensors(int bAngleCurrent) throws RemoteException;
	public abstract double getBattery();
	public abstract int changeShooterAngle(int inputAngle, int bAngleCurrent) throws RemoteException;
	public abstract void turn(int endAngle) throws RemoteException;
	public abstract void touchSensor() throws RemoteException;
	public abstract void moveForward() throws RemoteException;
	public abstract void moveBackward() throws RemoteException;
	public abstract boolean move(int distanceToMove, int bAngleCurrent) throws RemoteException;
}
