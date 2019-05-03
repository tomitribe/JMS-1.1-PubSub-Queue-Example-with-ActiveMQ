/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import org.apache.qpid.jms.*;
import javax.jms.*;

class Listener {

    public static void main(String[] args) throws JMSException {

        JmsConnectionFactory factory = new JmsConnectionFactory("amqp://localhost:5672");

        Connection connection = factory.createConnection("admin", "password");
        connection.start();
        
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        Destination destination = null;
        if(args.length > 0 && args[0].equalsIgnoreCase("QUEUE")) {        	
        	destination = session.createQueue("MyQueue");	
        }else if(args.length > 0 && args[0].equalsIgnoreCase("PUB/SUB"))  {        	
        	destination = session.createTopic("MyChannel");        	
        }else {
        	System.out.println("Error: You must specify Queue or Pub/Sub");
        	connection.close();
        	System.exit(1);
        }
        
        MessageConsumer consumer = session.createConsumer(destination);
        
        String body;
        do {
        	
            Message msg = consumer.receive();
            body = ((TextMessage) msg).getText();
            
            System.out.println("Received = "+body);
            
        }while (!body.equalsIgnoreCase("SHUTDOWN"));
        
        connection.close();
        System.exit(1);
    }
}