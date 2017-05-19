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
package edu.jhuapl.dorset.demos;

import java.util.HashMap; //new
import java.util.Map; //new
import java.util.Properties;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import edu.jhuapl.EV3Agent.EV3Agent;
import edu.jhuapl.dorset.Application;
import edu.jhuapl.dorset.agents.Agent;
import edu.jhuapl.dorset.filters.AliasRequestFilter; //new
import edu.jhuapl.dorset.filters.RequestFilter; //new
import edu.jhuapl.dorset.routing.Router;
import edu.jhuapl.dorset.routing.SingleAgentRouter;

/**
 * Initialize resources for the Dorset api
 * 
 * This uses Jersey's default dependency injection framework.
 */
public class AppInitializer extends ResourceConfig {
    private final Application app;

    /**
     * Create the app and bind it for injection
     */
    public AppInitializer() {

        app = new Application(initializeRouter());
        
        Map<String, String> aliasMap = new HashMap<String, String>();
        
        aliasMap.put("one", "1");
        aliasMap.put("two", "2");
        aliasMap.put("to", "2");
		aliasMap.put("too", "2"); 
        aliasMap.put("three", "3");
        aliasMap.put("four", "4");
		aliasMap.put("for", "4");
		aliasMap.put("five", "5");
		aliasMap.put("six", "6");
		aliasMap.put("seven", "7");
		aliasMap.put("eight", "8");
		aliasMap.put("ate", "8");
		aliasMap.put("ternate", "turn 8");
		aliasMap.put("nine", "9");
		aliasMap.put("ten", "10");
		aliasMap.put("ingle", "angle");
		aliasMap.put("mingle", "angle");
		aliasMap.put("single", "angle");
		aliasMap.put("negative ", "-");
		aliasMap.put("minus ", "-");
		aliasMap.put("for word", "forward");
		aliasMap.put("four word", "forward");
		aliasMap.put("4 word", "forward");
		aliasMap.put("for words", "forward");
		aliasMap.put("four words", "forward");
		aliasMap.put("4 words", "forward");
		aliasMap.put("forwards", "forward");
		aliasMap.put("back word", "backward");
		aliasMap.put("back words", "backward");
		aliasMap.put("backwards", "backward");
		
		RequestFilter aliasRequestFilter = new AliasRequestFilter(aliasMap);
		app.addRequestFilter(aliasRequestFilter);
	
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(app).to(Application.class);
            }
        });

        // uncomment for logging requests and responses at the INFO level
        // registerInstances(new
        // LoggingFilter(Logger.getLogger("org.glassfish.jersey"), true));
    }

    private Router initializeRouter() {
        /*Agent timeAgent = new DateTimeAgent();
        MultiValuedMap timeAgentParams = new MultiValuedMap();
        timeAgentParams.addString(KeywordRouter.KEYWORDS, "time");
        timeAgentParams.addString(KeywordRouter.KEYWORDS, "date");
        timeAgentParams.addString(KeywordRouter.KEYWORDS, "day");
        RouterAgentConfig kwConfig = RouterAgentConfig.create();
        kwConfig.add(timeAgent, timeAgentParams);
        Router kwRouter = new KeywordRouter(kwConfig);

        Agent wikiAgent = new DuckDuckGoAgent(new ApacheHttpClient());
        Router wikiRouter = new SingleAgentRouter(wikiAgent);

        Router mainRouter = new ChainedRouter(kwRouter, wikiRouter);
        return mainRouter;
        */
    	
    	Properties prop = new Properties();
    	Agent agent = new EV3Agent(prop);
        Router router = new SingleAgentRouter(agent);
        
        return router;
    	
    }
}