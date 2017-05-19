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
package edu.jhuapl.EV3Agent.app;
/**
 * EV3.java
 * Runs EV3 Command Line `Demo
 */

import java.util.Properties;
import java.util.Scanner;

import edu.jhuapl.EV3Agent.EV3Agent;
import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.Request;
import edu.jhuapl.dorset.Response;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.routing.Router;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

public class EV3 {

    public static void main(String[] args) {

    	Properties prop = new Properties();
        Agent agent = new EV3Agent(prop);
        Router router = new SingleAgentRouter(agent);
        Application app = new Application(router);

        System.out.println("Welcome to the Dorset EV3 Demo. "
        		+ "Enter command for robot or type \"quit\" to end this session.");
        System.out.println("**To see all commands type \"commands\" \n");
        String input = "";
        Scanner in = new Scanner(System.in);
        
        System.out.println("Format(option 1): \"<command> <number>\"");
    	System.out.println("\tCommands: angle, turn, move");
    	System.out.println("\t<number>: Angle of movement (angle: 1-25)(turn: 1-360)");
    	System.out.println("\t\t Unit of Measure: (angle, turn: degrees)(move: centimeters)");
    	System.out.println("Format(option 2): \"<command>");
     	System.out.println("\tCommands: fire, stop, quit, battery, manual, forward, backward");

        while (true) {
            System.out.print("\nCommand > ");
            input = in.nextLine();
            if (input.equalsIgnoreCase("quit")){ //q quits program 
                break;
            }
            Request request = new Request(input);
            Response response = app.process(request);		//sends to Agent
            System.out.println(response.getText());		//print Agent response
            	
            if (response.getText().equals("Invalid command, Stopping all motion")) {
        		System.out.println("To see all commands type \"commands\" ");
            }
        }
        input = "quit";
        Request request = new Request(input);
        Response response = app.process(request);	//tells Agent to stop EV3
    	System.out.println(response.getText());		//prints response
        
        in.close();
    }
}
