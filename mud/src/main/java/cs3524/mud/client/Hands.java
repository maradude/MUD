package cs3524.mud.client;

import java.rmi.RemoteException;

import cs3524.mud.server.ConnectionInterface;

public class Hands {
    private ConnectionInterface conn;

    public Hands(ConnectionInterface conn) {
        this.conn = conn;
    }

    public void pickUp(Player player, String thing) throws RemoteException {
        if (thing == null) {
            System.out.println("item not specified for picking up");
            return;
        }
        thing = thing.toLowerCase();
        boolean success = conn.pickUpThing(player.getCurrentLocation(), thing);
        if (success) {
            player.getInventory().add(thing);
            System.out.println("You have picked up the " + thing);
        } else {
            System.out.println("There is no " + thing + " here");
        }
    }
}