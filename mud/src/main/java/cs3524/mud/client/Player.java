package cs3524.mud.client;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cs3524.mud.server.ConnectionInterface;

public class Player {
    private ConnectionInterface conn;
    private String startingLocation;
    private String currentLocation;
    private Set<String> cardinalDirections = Set.of("north", "west", "south", "east");
    private String name;
    private List<String> inventory;

    public Player(ConnectionInterface conn) throws RemoteException {
        this.conn = conn;
        startingLocation = conn.getStartLocation();
        currentLocation = startingLocation;
        inventory = new ArrayList<>();
    }

    public String getStartingLocation() {
        return this.startingLocation;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void joinGame(String name) throws RemoteException{
        conn.createPlayer(name);
        this.name = name;
        System.out.println("Welcome " + name);
        lookAround();
    }

    public void lookAround() throws RemoteException {
        String info = conn.getLocationInfo(name, currentLocation);
        System.out.println(info);
    }

    public void lookAround(String location) throws RemoteException {
        String info = conn.getLocationInfo(name, location);
        System.out.println(info);
    }

    public void move(String direction) throws RemoteException {
        if (direction == null) {
            System.out.println("direction needed to move");
            return;
        }
        direction = direction.toLowerCase();
        if (this.cardinalDirections.contains(direction)) {
            String newLocation = conn.move(currentLocation, direction, name);
            if (newLocation.equals(currentLocation)) {
                System.out.println("Direction specified is not accessible");
            } else {
                currentLocation = newLocation;
                lookAround();
            }
        } else {
            System.out.println("Direction not recognized, please use on of {north, west, south, east}");
        }

    }

	public void pickUp(String thing) throws RemoteException {
        if (thing == null) {
            System.out.println("item not specified for picking up");
            return;
        }

        thing = thing.toLowerCase();
        boolean success= conn.pickUpThing(currentLocation, thing);
        if (success){
            inventory.add(thing);
            System.out.println("You have picked up the " + thing);
        } else {
            System.out.println("There is no " + thing + " here");
        }
    }

    public void showInventory() {
        if (inventory.size() > 0 ){
            System.out.println("Your current inventory: " + String.join(", ", inventory));
        } else {
            System.out.println("Your inventory is empty");
        }
    }

}
