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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.agents.AgentRequest;
import edu.jhuapl.dorset.agents.AgentResponse;


public class EV3AgentTest {

	private static Agent agent;
	
	@BeforeClass
	public static void runBeforeTests(){
		Properties prop = new Properties();
		prop.setProperty("mockEV3", "true");
		agent = new EV3Agent(prop);
	}	
	@AfterClass
	public static void runAfterTests(){
		AgentRequest request = new AgentRequest("quit");
        agent.process(request);
	}	
	@Test
	public void testFireGood(){
        AgentRequest request = new AgentRequest("fire");
        AgentResponse response = agent.process(request);

        assertEquals("Firing", response.getText());
	}	
	@Test
	public void testStopGood(){
        AgentRequest request = new AgentRequest("stop");
        AgentResponse response = agent.process(request);

        assertEquals("Stopped", response.getText());
	}	
	@Test
	public void testManualGood(){
        AgentRequest request = new AgentRequest("manual");
        AgentResponse response = agent.process(request);

        assertEquals("Firing in response to button", response.getText());
	}	
	@Test
	public void testForwardGood(){
        AgentRequest request = new AgentRequest("forward");
        AgentResponse response = agent.process(request);

        assertEquals("Moving forward", response.getText());
        
        request = new AgentRequest("stop");
        response = agent.process(request);
	}	
	@Test
	public void testBackwardGood(){
        AgentRequest request = new AgentRequest("backward");
        AgentResponse response = agent.process(request);

        assertEquals("Moving backward", response.getText());

        request = new AgentRequest("stop");
        response = agent.process(request);
	}	
	@Test
	public void testBatteryGood(){
        AgentRequest request = new AgentRequest("battery");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Battery Voltage: "));
	}	
	@Test
	public void testCommandsGood() {
        AgentRequest request = new AgentRequest("commands");
        AgentResponse response = agent.process(request);

        assertEquals("\t\"fire\"   \"stop\"   \"quit\"   \"battery\" "
				+ "\n\t \"forward\"   \"backward\"   \"manual\"   \n\t\"angle <number>\"   "
				+ "\"turn <number>\"   \"move <number>\""
				+ "\n\tTo learn about a specific command, type \"help <first word of command>\"", 
				response.getText());
	}	    
	@Test
	public void testOneWordCommandBad(){
        AgentRequest request = new AgentRequest("fire 10");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid command", response.getText());
	}	
	@Test
    public void testTurnOneWordBad() {
        AgentRequest request = new AgentRequest("turn");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid command", response.getText());
    }	
    @Test
    public void testTurnPositiveGood() {
        AgentRequest request = new AgentRequest("turn 20");
        AgentResponse response = agent.process(request);

        assertEquals("Turning", response.getText());
    }    
    @Test
    public void testTurnPositiveBad() {
        AgentRequest request = new AgentRequest("turn 370");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void testTurnNegativeGood() {
        AgentRequest request = new AgentRequest("turn -20");
        AgentResponse response = agent.process(request);

        assertEquals("Turning", response.getText());
    }
    @Test
    public void testTurnNegativeBad() {
        AgentRequest request = new AgentRequest("turn -370");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }
    @Test
    public void testTurnNumberBad() {
        AgentRequest request = new AgentRequest("turn t");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void testTurnSignBad() {
        AgentRequest request = new AgentRequest("turn #50");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void testAngleOneWordBad() {
        AgentRequest request = new AgentRequest("angle");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid command", response.getText());
    }    
    @Test
    public void testAnglePositiveGood() {
        AgentRequest request = new AgentRequest("angle 20");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Changing angle by 20 degrees"));
    }    
    @Test
    public void testAnglePositiveBad() {
        AgentRequest request = new AgentRequest("angle 30");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Invalid angle"));
        assertTrue(response.getText().contains("New angle must be 0-25"));
    }
    //TODO only good after "angle 10+" has been called
    /*@Test
    public void testAngleNagativeGood() {
    	AgentRequest request = new AgentRequest("angle -10");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Changing angle by -10 degrees"));
    }*/
    @Test
    public void testAngleNegativeBad() {
        AgentRequest request = new AgentRequest("angle -30");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Invalid angle"));
        assertTrue(response.getText().contains("New angle must be 0-25"));
    }    
    @Test
    public void testAngleNumberBad() {
        AgentRequest request = new AgentRequest("angle t");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void testAngleSignBad() {
        AgentRequest request = new AgentRequest("angle #10");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void testMoveOneWordBad() {
        AgentRequest request = new AgentRequest("move");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid command", response.getText());
    }    
    @Test
    public void testMovePositiveGood() {
        AgentRequest request = new AgentRequest("move 4");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Moving"));
    }    
    @Test
    public void testMoveNegativeGood() {
        AgentRequest request = new AgentRequest("move -4");
        AgentResponse response = agent.process(request);

        assertTrue(response.getText().contains("Moving"));
    }    
    @Test
    public void testMoveNumberBad() {
        AgentRequest request = new AgentRequest("move t");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void tesMoveSignBad() {
        AgentRequest request = new AgentRequest("move #50");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid number", response.getText());
    }    
    @Test
    public void testInvalidCommand() {
        AgentRequest request = new AgentRequest("bad");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid command", response.getText());
    }    
    @Test
    public void testHelpFireGood() {
        AgentRequest request = new AgentRequest("help fire");
        AgentResponse response = agent.process(request);

        assertEquals("\"fire\" \nTells EV3 to fire once", response.getText());
    }    
    @Test
    public void testHelpStopGood() { 
        AgentRequest request = new AgentRequest("help stop");
        AgentResponse response = agent.process(request);

        assertEquals("\"stop\" \nTells EV3 to stop all motion", response.getText());
    }    
    @Test
    public void testHelpQuitGood() {
        AgentRequest request = new AgentRequest("help quit");
        AgentResponse response = agent.process(request);

        assertEquals("\"quit\" \nEnds program", response.getText());
    }    
    @Test
    public void testHelpBatteryGood() { 
        AgentRequest request = new AgentRequest("help battery");
        AgentResponse response = agent.process(request);

        assertEquals("\"battery\" \nReturns EV3 battery level", response.getText());
    }    
    @Test
    public void testHelpManualGood() {
        AgentRequest request = new AgentRequest("help manual");
        AgentResponse response = agent.process(request);

        assertEquals("\"button\" \nWaits for button on robot to be pressed "
				+ "and fires once", response.getText());
    }    
    @Test
    public void testHelpForwardGood() { 
        AgentRequest request = new AgentRequest("help forward");
        AgentResponse response = agent.process(request);

        assertEquals("\"forward\" \nMoves EV3 forward until \"stop\" command "
				+ "is called", response.getText());
    }    
    @Test
    public void testHelpBackwardGood() {
        AgentRequest request = new AgentRequest("help backward");
        AgentResponse response = agent.process(request);

        assertEquals("\"backward\" \nMoves EV3 backward until \"stop\" command "
				+ "is called", response.getText());
    }    
    @Test
    public void testHelpAngleGood() {
        AgentRequest request = new AgentRequest("help angle");
        AgentResponse response = agent.process(request);

        assertEquals("\"angle <number>\" \nChanges the angle of the shooter"
				+ "\nNumber should be from 1-25", response.getText());
    }
    @Test
    public void testHelpTurnGood() { 
        AgentRequest request = new AgentRequest("help turn");
        AgentResponse response = agent.process(request);

        assertEquals("\"turn <number>\" \nChanges the angle the EV3 is facing"
				+ "\nNumber should be from 1-360", response.getText());
    }    
    @Test
    public void testHelpMoveGood() { 
        AgentRequest request = new AgentRequest("help move");
        AgentResponse response = agent.process(request);

        assertEquals("\"move <number>\" \nMoves EV3 \nNumber should be in centimeters", response.getText());
    }       
    @Test
    public void testHelpBad() {
        AgentRequest request = new AgentRequest("help bad");
        AgentResponse response = agent.process(request);

        assertEquals("Invalid command", response.getText());
    }    
}