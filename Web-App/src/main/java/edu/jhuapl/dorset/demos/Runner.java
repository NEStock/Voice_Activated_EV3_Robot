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

import java.net.URL;
import java.security.ProtectionDomain;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class Runner {
    public static void main(String[] args) throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        int port = 8888;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        }
        if (port == 80) {
            System.out.println("Dorset web demo running on http://localhost/");            
        } else {
            System.out.println("Dorset web demo running on http://localhost:" 
                            + String.valueOf(port) + "/");
        }
        Server server = new Server(port);

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");
        // turn off class loading from WEB-INF due to logging
        context.setParentLoaderPriority(true);

        ProtectionDomain protectionDomain = Runner.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setWar(location.toExternalForm());

        server.setHandler(context);
        server.start();
        server.join();
    }
}
