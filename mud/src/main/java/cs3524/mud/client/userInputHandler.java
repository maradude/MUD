package cs3524.mud.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import cs3524.mud.server.ConnectionInterface;

/*
1. Player can make at least 1 move in at least 1 direction
2. Prints to player the infromation of the start locations
3. Prints location info after chaningin to new location
*/
public class userInputHandler {
    BufferedReader stdin;
    ConnectionInterface conn;
    Player player;

    public userInputHandler(ConnectionInterface connection, Player player) {
        stdin = new BufferedReader(new InputStreamReader(System.in));
        conn = connection;
        this.player = player;
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

    private void commandParser(String message) throws RemoteException {
        String parsed[] = message.split(" ", 2);
        String cmd = parsed[0];
        String arg = null;
        if (parsed.length > 1) {
            arg = parsed[1];
        }
        switch (cmd) {
            case "":
                player.lookAround();
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
                player.lookAround(player.getStartingLocation());
                break;
        }
        System.out.print("");
        System.out.print("> ");
    }

    private void printCommands() {
        String commands = "Look around by pressing enter with no input\n" +
        "Move by typing 'move' and a cardinal direction {north, east, west, south}\n" +
        "To pickup an item in your current location type 'get' and the items name\n" +
        "To look into your inventory type 'inventory'\n" +
        "To see this help text again, type 'help'"
        ;
        System.out.println(commands);
    }

    public void listenForUserInput() throws IOException, UserQuit {
        while (stdin.ready()) {
            sendUserInput(stdin);
        }
    }

    public void run() {
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
