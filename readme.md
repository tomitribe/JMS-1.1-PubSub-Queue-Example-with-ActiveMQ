## Overview

This is an example of how to use the Java JMS api with ActiveMQ executing both Pub/Sub and 
Queue based messaging.

## Prereqs

- Install Java SDK
- Install [Maven](http://maven.apache.org/download.html) 

## Building

Run:

    mvn install

## Running the Examples

There are two Java classes. example.Listener and example.Publisher.  They are designed to 
exchange messages using either a Queue or a Topic (pub/sub).  To take full advantage of 
the example launch at least one Publisher and one Listener. Than type in your messages 
in the Publisher console where it says to do so at the prompt as like below 

Enter message: Hi, how are you? 

To shutdown the Publisher and Listener enter "shutdown" as follows or Ctrl+c

Enter message: shutdown

Note: When using the queue system the "shutdown" command will only shutdown the Publisher 
which its executed and one other Lstener.  

Both the Publisher and the Listener have to be using the same kind of message system. So 
they must both be using Queue or they must both be using Topic (pub/sub). The commands to 
start instances of each program are shown below.

You can run multiple Publishers and/or multiple Listeners as long as they specify the same 
kind of messaging system the will will work together with Publishers sending messages to 
the Listeners.

In individual terminal windows for Topic (pub/sub) based messaging:

    java -cp target/jms-example-SNAPSHOT.jar example.Listener pub/sub
    java -cp target/jms-example-SNAPSHOT.jar example.Publisher pub/sub

In individual terminal windows for Queue based messaging:

    java -cp target/jms-example-SNAPSHOT.jar example.Listener queue
    java -cp target/jms-example-SNAPSHOT.jar example.Publisher queue

