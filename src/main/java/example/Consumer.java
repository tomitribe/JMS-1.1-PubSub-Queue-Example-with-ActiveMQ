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

// ActiveMQ JMS Provider
import org.apache.qpid.jms.JmsConnectionFactory;

// JMS API types
import javax.jms.Connection;
import javax.jms.Session;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import javax.jms.*;

class Consumer {

    public static void main(String[] args) throws JMSException {

    	/*  Every JMS provider (every library that implements the JMS API) 
    	 *  will have its own implementation of the javax.jms.ConnectionFactory. 
    	 * 
    	 *  The purpose of the ConnectionFactory is to create a network connection 
    	 *  to a specific JMS broker, such as ActiveMQ, or a specific protocol,
    	 *  such as AMQP.  This allows the JMS library to send and receive messages
    	 *  over a network from the broker.
    	 * 
    	 *  In this case we are using the Apache Qpid JMS library which is specific 
    	 *  to the protocol, AMQP. AMQP is only one of ten protocols currently supported by
    	 *  ActiveMQ.
    	 */
        JmsConnectionFactory factory = new JmsConnectionFactory("amqp://localhost:5672");
        Connection connection = factory.createConnection("admin", "password");
        connection.start();
        
        /*  Every JMS Connection can have multiple sessions which manage things like
         *  transactions and message persistence separately.  In practice multiple sessions
         *  are not used much by developers but may be used in more sophisticated
         *  application servers to conserve resources.
         */
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        /*  A Destination is an address of a specific Topic or Queue hosted by the 
         *  JMS broker. The only difference between using JMS for a Topic (Pub/Sub) 
         *  and Queue (P2P) is this bit of code here - at least in the simplest 
         *  cases.  
         *  
         *  That said, there are significant differences between Topic- and 
         *  Queue-based messaging and understanding those differences is key to 
         *  understanding JMS and messaging systems in general. This is discussed in 
         *  more detail the blog post, "5 Minutes or Less: ActiveMQ with JMS Queues and Topics".
         */
        Destination destination = null;
        if(args.length > 0 && args[0].equalsIgnoreCase("QUEUE")) {        	
        	destination = session.createQueue("MyQueue");	
        }else if(args.length > 0 && args[0].equalsIgnoreCase("TOPIC"))  {        	
        	destination = session.createTopic("MyTopic");        	
        }else {
        	System.out.println("Error: You must specify Queue or Topic");
        	connection.close();
        	System.exit(1);
        }
        
        /*  A MessageConsumer is specific to a destination - it can only
         *  receive messages from a specific Topic or Queue.
		 */
        MessageConsumer consumer = session.createConsumer(destination);
        

        String body;
        do {
        	
        	/* To consume messages from Topics or Queues you call the receive() method
        	 * on the MessageConsumer. That method will pause the application (block the 
        	 * thread) until a message is delivered from the Topic or Queue.
        	 * 
        	 * As soon as the API detects that a new message has reached a Topic or Queue, 
        	 * it will alert the JMS consumers and deliver the message. 
        	 * 
        	 * In the case of a Topic, every JMS client listening to the same
        	 * Topic gets a copy of the message. In the case of a Queue, the JMS clients 
        	 * wait in a queue (thus the name). When a message is delivered to the Queue
        	 * the JMS client in the front gets the message and moves to the back of the 
        	 * queue so that the next JMS client in line gets the next message.
        	 */
            Message msg = consumer.receive();
            body = ((TextMessage) msg).getText();
            
            System.out.println("Received = "+body);
            
        }while (!body.equalsIgnoreCase("SHUTDOWN"));
        
        /* As is the case with most enterprise resources, you want to shut a JMS connection
         * down when you are done using it.  This tells the JMS broker that it can free
         * up the network resources used for that connection making the whole system more 
         * scalable.
         */
        connection.close();
        System.exit(1);
    }
}