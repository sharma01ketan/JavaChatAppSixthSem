# Group Chat
##### We have 3 classes:
* #### GroupServer
  Provides connection to the instances of users created in **User** class <br>
  It is running on port '2003'.
* #### User
  Provides an instance for creating a new user, and set its properties
* #### GroupFrame
  Here the messages of each user are displayed in real-time with their respective usernames.


## How to implement?
1. Run the GroupServer class
2. This initialises the ServerSocket to receive the messages on port 2003, from all the connected users
3. Now in the user class, we can create as many instances of users as we want. (I have used 3 for the sake of simplicity).
4. Click on te "Connect Group" button, it prompts a dialog box, asking to enter username, and has a checkbox to validate the authentication of the user.
5. User can connect to the host server, if it is authentic and a frame showing the group chat frame pops up, else it throws an exception stating - "User is not authentic".
6. As we send any message, it is displayed on the group chat frame, and a notification sound is made.


## Features:
* Client-server architecture
* Prompts user to enter username, and authenticate themselves
* Displays profile images
* Notification sound
* ScrollBar implementation to view previous messages
* Error handling in case of inauthentic users
* Real-time connection with time-stamps, and sender
* End-to-end connection in private messaging




# Private Chat

For this, we use two separate classes: Server and Client

#### Server:
It is used to establish the connection between the two users: server and client
It is hosted on port 2003, from where it accepts the messages

#### Client:
It sends the messages to port 2003, and a communication between the two is established