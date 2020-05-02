package cs3524.mud.client;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cs3524.mud.server.ConnectionInterface;

public class GameManager {
    private ConnectionInterface conn;
    private Player activeCharacter;
    private Map<String, Player> characters;
    private String currentGame;
    private String userName;
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

    private Player createPlayer(String server) throws RemoteException {
        var character = new Player(this, conn);
        if (characters.containsKey(server)) {
            return null;
        }
        characters.put(server, character);
        return character;
    }

    public void addPlayerToServer(String server, Player player) throws RemoteException {
        conn.createPlayer(userName, startingLocation);
    }

    public void joinNewGame(Player player) throws RemoteException {
        conn.createPlayer(userName, player.getLocation());
        player.lookAround(player.getCurrentLocation());
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

    public void leaveGame(String game) throws RemoteException {
        Player player = this.characters.get(game);
        if (validateServerName(game) && player != null) {
            int status = conn.leaveGame(player.getName(), player.getCurrentLocation(), game);
            switch (status) {
                case 0:
                    System.out.println("Successfully left " + game);
                    this.characters.remove(game);
                    break;
                case 1:
                    System.out.println("Game " + game + " not found");
                    break;
                case 2:
                    System.out.println("You are not registed in " + game);
                    break;
                default:
                    System.out.println("Unsuccessfully in leaving " + game);
            }
        }

    }

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