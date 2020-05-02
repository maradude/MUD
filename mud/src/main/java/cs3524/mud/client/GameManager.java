/*
* Martti Aukia 51657228
* Main class for handling the game state clientside
* keeps track of user created characters
* handles game creation, ending, changing
*/
package cs3524.mud.client;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3524.mud.server.ConnectionInterface;

public class GameManager {
    private ConnectionInterface conn; // connection to server
    private Player activeCharacter; // current user "focus" character
    private Map<String, Player> characters; // (server name => player instance), stores users characters
    private String currentGame; // current user "focus" game
    private String userName; // user specified name used for all characters
    private String startingLocation;

    public GameManager(ConnectionInterface conn, String userName) throws RemoteException {
        this.conn = conn;
        this.characters = new HashMap<>();
        this.userName = userName;
        this.startingLocation = conn.getStartLocation();
    }

    public void getCurrentGameName() throws RemoteException {
        System.out.println(currentGame);
    }

    public String getUserName() {
        return this.userName;
    }

    public String getStartLocation() {
        return this.startingLocation;
    }

    /*
     * create a new player instance and add it to the specified server if the user
     * already had a character on the server instead return null also register that
     * character and server relationship
     */
    private Player createPlayer(String server) throws RemoteException {
        if (characters.containsKey(server)) {
            return null;
        }
        var character = new Player(this, conn);
        characters.put(server, character);
        return character;
    }

    private void setActiveCharacter(Player player) {
        this.activeCharacter = player;
    }

    public Player getPlayer() {
        return this.activeCharacter;
    }

    public List<String> listServers() throws RemoteException {
        return conn.listGames();
    }

    /*
     * if a name of a mud game where the player has a character is given, remove
     * that character. Essentially removes the stored location and inventory of the
     * character
     */
    public void leaveGame(String game) throws RemoteException {
        Player player = this.characters.get(game);
        if (validateServerName(game) && player != null) {
            this.characters.remove(game);
        }
    }

    /*
     * Check common problems in user inputted server names
     */
    private boolean validateServerName(String serverName) throws RemoteException {
        List<String> servers;
        boolean valid = false;
        if (serverName == null) {
            System.out.println("Name of game required to join");
        } else if (serverName.equals(currentGame)) {
            System.out.println("Can't use active game name");
        } else if (!(servers = listServers()).contains(serverName)) {
            System.out.println("Unkown game name: " + serverName);
            System.out.println("current games: " + String.join(", ", servers));
        } else {
            valid = true;
        }
        return valid;
    }

    /*
     * if the mud game name provided by the player is valid, create a new character
     * or load an existing character if the user has already joined the server
     * before. Leave the current
     */
    public boolean joinGame(String serverName) throws RemoteException {
        boolean success = false;
        if (validateServerName(serverName)) {
            Player player;
            if ((player = characters.get(serverName)) == null) {
                player = createPlayer(serverName);
            }
            success = joinServer(player, serverName);
            if (success) {
                setActiveCharacter(player);
                this.currentGame = serverName;
                System.out.println("Welcome to " + serverName);
                activeCharacter.lookAround(activeCharacter.getCurrentLocation());
            } else {
                System.out.println(serverName + " is full, can't join at the moment");
            }
        }
        return success;
    }

    public boolean joinServer(Player player, String serverName) throws RemoteException {
        return conn.joinServer(player.getName(), player.getLocation(), serverName);
    }

    public void createNewGame(String serverName) throws RemoteException {
        if (serverName == null) {
            System.out.println("Missing name for new MUD");
        } else if (conn.listGames().contains(serverName)) {
            System.out.println("MUD with that name already exists");
        } else {
            if (conn.createNewGame(serverName)) {
                System.out.println(
                        "New game MUD: " + serverName + " created,\n" + "please join by typing: join " + serverName);
            } else {
                System.out.println("Too many MUDs at the moment");
            }
        }

    }

    public void listPlayers() throws RemoteException {
        System.out.println(String.join(", ", conn.listPlayers()));
    }
}