# MUD requirements

## MUST

1. Use Java RMI
2. Attribute code you didn't write yourself (e.g. practical code)
3. Use `vertex` class to represent "locations"
4. Use `edge` class to reprsent "paths"
5. Use `MUD` as main class
6. Create edges from `mymud.edg`
7. Create messages for locations using `mymud.msg`
8. Create things for location with `mymud.thg`
   - Things are players or items that can be picked up
9. **Instructions for installation, setup, running and playing**
10. Document submission ==// what does actually mean?==
    1. pdf to explain: what implemented and how implemented
    2. how implemented each requirements
11. Include name and userid at top of all files
12. Include config files etc. for compiling and running submission
13. Report
    1. describe submission
    2. how to use the application
14. Submission should be in a single zip file

## D

1. Implement MUD game server class.
   1. Needs to implement an interface that extends `java.rmi.Remote`.
   2. Needs to be registered with `rmiregistry`
2. Implement client.
   1. Player can make at least 1 move in at least 1 direction
   2. Prints to player the infromation of the start locations
   3. Prints location info after chaningin to new location
3. correct setup of a security manager
   1. Include a security policy file
   2. correct setup and use of RMI

## C

1. Multiple user support
2. Player can move in any direction
3. Players can see other users in MUD world
4. **Users can pickup things in the MUD**
5. Command set
   1. make moves
   2. lists players at current location
   3. list things in current location
   4. pickup a thing ==// should thing be item in these cases?==, can disallow picking up players
   5. **show players inventory**
   6. **help to show command list**
   7. Allow player to set their own name
   8. **Manually print start locations information**
   9. **when player presses `ENTER`, print current location information**
      1. no need to clear screen before hand
6. **On item pickup remove item from location**

## B

1. Implement multiple games support
   1. all MUDs can share the same initial setup `mymud.*` files
2. On server startup create multiple MUD games
3. Players can list running MUDs
4. Players can join a specific MUD
5. Players can leave a MUD and end playing the MUD game ==// what does end playing the MUD game mean?==
   1. acceptable interpretation is end game after all players leave
6. Players can switch MUD games (new command)

## A5

1. Player can create a new MUD game
2. Clients can have multiple games open
3. Players can hot swap between MUDs, only focus changes
4. Servers should limit number of MUDs that can run at any time and total number of users logged on to the MUDs
5. CLI should make game creation, joining and leaving easy

## A1

1. Clients can be aborted, restarted and reconned to the server
2. Reponsiveness – automatic refresh of player consoles:
   - auto update all player consoles in a specific MUD game if a change occurs, such as a player arriving at a location, or a player collecting an item
   - Use a callback implementation to push changes to clients of a game
3. Robustness – make server tolerate clients leaving a game or client app abortion
   - Handle users leaving MUD correctly, or closing or aborting client app
     - server should clean up clients logins, unregister any remote objects in rmiregistry if necessary
   - clients handle exit and abort corectly ==// vague?==
     - add handling for client and server disconnect (clean up)
   - ==// Should client connections be remembered? e.g. store game state between aborts?==
     - no need to remember players
4. Communication:
   1. Allow players to send messages to each other ==// private or public messages or both?==
      1. probably fine for either

