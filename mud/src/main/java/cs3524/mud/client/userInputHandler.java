package cs3524.mud.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.List;

/*
1. Player can make at least 1 move in at least 1 direction
2. Prints to player the infromation of the start locations
3. Prints location info after chaningin to new location
*/
public class userInputHandler {
    private BufferedReader stdin;
    private GameManager games;

    public userInputHandler() {
        stdin = new BufferedReader(new InputStreamReader(System.in));
    }

    private void sendUserInput(BufferedReader stdin) throws IOException {
        String message = stdin.readLine();
        commandParser(message);
    }

    public String getName() {
        System.out.print("Please name yourself\n> ");
        String name = "";
        try {
            while (name.trim().equals(""))
                name = stdin.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return name;
    }

    public void printGames(List<String> servers) throws RemoteException {
        System.out.println("current games: " + String.join(", ", servers));
    }

    public String selectServer(List<String> servers) throws RemoteException {
        var formattedServers = "current games: " + String.join(", ", servers);
        String chosenServer = null;
        try {
            while (true) {
                System.out.print("Please select from the list of available games\n" + formattedServers + "\n> ");
                chosenServer = stdin.readLine();
                if (servers.contains(chosenServer.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        return chosenServer;
    }

    private void commandParser(String message) throws RemoteException {
        String parsed[] = message.split(" ", 2);
        String cmd = parsed[0];
        String arg = null;
        if (parsed.length > 1) {
            arg = parsed[1];
        }
        Player player = games.getPlayer();
        switch (cmd) {
            case "":
                player.lookAround(player.getCurrentLocation());
                break;
            case "move":
                player.move(arg);
                break;
            case "get":
                player.pickUp(arg);
                break;
            case "inventory":
                player.showInventory();
                break;
            case "help":
                printCommands();
                break;
            case "origin":
                player.lookAround(games.getStartLocation());
                break;
            case "games":
                var servers = games.listServers();
                System.out.println("current games: " + String.join(", ", servers));
                break;
            case "join":
                games.joinGame(arg);
                break;
            case "end":
                games.leaveGame(arg);
                break;
            case "create":
                games.createNewGame(arg);
                break;
            case "where":
                games.getCurrentGameName();
                break;
            case "players":
                games.listPlayers();
                break;
            default:
                System.out.println("unkown command");
        }
        System.out.print("");
        System.out.print("> ");
    }

    private void printCommands() {
        String commands = "[GAME CONTROLS]\n" + "- Look around by pressing enter with no input\n"
                + "- Move by typing 'move' and a cardinal direction {north, east, west, south}\n"
                + "- To pickup an item in your current location type 'get' and the items name\n"
                + "- To look into your inventory type 'inventory'\n" + "\n[GAME MANAGER]\n"
                + "- To see this help text again, type 'help'\n" + "- To see currently existing MUDs, type 'games'\n"
                + "- To see what's in the starting location, type 'origin'\n"
                + "- To join a new game from existing MUDs, type 'join' followed by the games name\n"
                + ">> This will create a new character in that game for you (don't worry your old character is still safe but hidden)"
                + "- To create a new MUD game, type 'create' followed by a unique name for the game\n"
                + "- To permanently delete your character in another game, type 'end' followed by the games name"
                + "- To see active players in your current game, type 'players'"
                + "- To see in which game you are, type 'where'";
        System.out.println(commands);
    }

    public void listenForUserInput() throws IOException, UserQuit {
        while (stdin.ready()) {
            sendUserInput(stdin);
        }
    }

    public void runGame(GameManager game) {
        this.games = game;
        System.out.print("> ");
        try {
            while (true) {
                listenForUserInput();
            }
        } catch (UserQuit e) {
            System.out.print("Goodbye");
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public static void main(String[] args) {
        System.out.println("Welcome to MUD");
        System.out.print("> ");
        // new userInputHandler().run();
    }
}
