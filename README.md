![Build Status](https://travis-ci.org/aparra/follower_maze.svg?branch=master)

### The problem
Build a system which acts as a socket server, reading events from an 
*event source* and forwarding them when appropriate to *user clients*.

Clients will connect through TCP and use the simple protocol described in a
section below. There will be two types of clients connecting to your server:

- **One** *event source*: It will send you a stream of events which may or may
not require clients to be notified
- **Many** *user clients*: Each one representing a specific user,
these wait for notifications for events which would be relevant to the
user they represent

### The Protocol
The protocol used by the clients is string-based (i.e. a `CRLF` control
character terminates each message). All strings are encoded in `UTF-8`.

The *event source* **connects on port 9090** and will start sending
events as soon as the connection is accepted.

The many *user clients* will **connect on port 9099**. As soon
as the connection is accepted, they will send to the server the ID of
the represented user, so that the server knows which events to
inform them of. For example, once connected a *user client* may send down:
`2932\r\n`, indicating that they are representing user 2932.

After the identification is sent, the *user client* starts waiting for
events to be sent to them. Events coming from *event source* should be
sent to relevant *user clients* exactly like read, no modification is
required or allowed.

### The Events
There are five possible events. The table below describe payloads
sent by the *event source* and what they represent:

| Payload    | Sequence #| Type         | From User Id | To User Id |
|------------|-----------|--------------|--------------|------------|
|666|F|60|50 | 666       | Follow       | 60           | 50         |
|1|U|12|9    | 1         | Unfollow     | 12           | 9          |
|542532|B    | 542532    | Broadcast    | -            | -          |
|43|P|32|56  | 43        | Private Msg  | 32           | 56         |
|634|S|32    | 634       | Status Update| 32           | -          |

**The events will arrive out of order** and should delivery in order.

Events may generate notifications for *user clients*. **If there is a
*user client* ** connected for them, these are the users to be
informed for different event types:

* **Follow**: Only the `To User Id` should be notified
* **Unfollow**: No clients should be notified
* **Broadcast**: All connected *user clients* should be notified
* **Private Message**: Only the `To User Id` should be notified
* **Status Update**: All current followers of the `From User ID` should be notified

If there are no *user client* connected for a user, any notifications
for them must be silently ignored. *user clients* expect to be notified of
events **in the correct order**, regardless of the order in which the
*event source* sent them.

### Running
In the command line interface, you need go to follower_maze directory and you can execute the commands bellow:

* **sbt eclipse** to build an eclipse project
* **sbt test** to execute the tests cases 

To start the server, you can use the sbt tool as well. There are two optional parameters: **USER_CLIENT_PORT** and **EVENT_SOURCE_PORT**. The default value is **9099** and **9090** respectively.

> sbt "run-main com.soundcloud.followermaze.Server [USER_CLIENT_PORT] [EVENT_SOURCE_PORT]"

To execute using default settings, you can run:

> sbt "run-main com.soundcloud.followermaze.Server"

### The solution
![Components](http://s21.postimg.org/3w0ofko2f/follower_maze.jpg)

* Input is handled by UserClientHandler and EventClientHandler.
  * UserClientHandler receives users connections and for each user opens a connection in Router component. 
  * EventClientHandler receives the events and stores it in a buffer inside Router component.
* Router has a list of routes. Each route is responsible to handle an unique event. According the event, a Route should update the user information, if required. Also a Route returns the users that should notified by event.
* Delivery process (of routing process) happens in order of sequence events. If an event is received but cannot be delivered, it waits in the buffer until your predecessor event is received.
 
### Design

###### Buffer of events
The events will arrive out of order and should be delivered in order. In order to handle this efficiently, a hashtable is used to store events working like a buffer where the **event sequence** is used to identify the event in the hashtable. Admitting that there is not sequence duplications, we don't have collisions in hashtable. The event will be kept in the buffer until the next to delivery in sequence (predecessor) arrives. The hashtable ensures constant-time (*O(1)*) stores & lookups, and removes the overhead of sorting or searching in the buffer.

###### Threads
The input is handled by two main threads *UserClientHandler* and *EventClientHandler*. There is not race condition between these threads because they handle different kind of events.
In UserClientHandler and EventClientHandler run in parallell but the connections received by each handler runs in a single-thread. It causes slower performance but not generates race condition in handlers and it is easier to test. This solution have supported the input, but it can be improved using a multi-thread handler implemented using a thread-pool (if all request are handle by a new thread, there is a problem to manage the threads).

###### Improvements
* Implement a thread-pool in each handlers to process to provide parallel processing
* Change the solution to use actors using [akka](http://akka.io/)
