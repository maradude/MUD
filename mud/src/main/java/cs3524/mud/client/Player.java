package cs3524.mud.client;

import java.rmi.RemoteException;
import java.util.List;

import cs3524.mud.server.ConnectionInterface;

public class Player {
    private String currentLocation;
    private String name;
    private List<String> inventory;
    private Eyes eyes;
    private Legs legs;
    private Hands hands;

    public Player(GameManager game, ConnectionInterface conn) throws RemoteException {
        this.eyes = new Eyes(conn);
        this.legs = new Legs(conn);
        this.hands = new Hands(conn);
        this.name = game.getUserName();
        this.setCurrentLocation(game.getStartLocation());
        this.setInventory(new Inventory());
    }

    public void setCurrentLocation(String loc) {
        this.currentLocation = loc;
    }

    public String getCurrentLocation() {
        return this.currentLocation;
    }

    public List<String> getInventory() {
        return inventory;
    }

    public void setInventory(List<String> inventory) {
        this.inventory = inventory;
    }

    public String getName() {
        return this.name;
    }

    public String getLocation() throws RemoteException {
        return this.currentLocation;
    }

    public void lookAround(String location) throws RemoteException {
        System.out.println(eyes.lookAround(name, location));
    }

    public void move(String direction) throws RemoteException {
        legs.move(this, direction);
    }

    public void pickUp(String thing) throws RemoteException {
        hands.pickUp(this, thing);
    }

    public void showInventory() {
        System.out.println(getInventory().toString());
    }

}
