/*
* Martti Aukia 51657228
Handles user request to the server related to manipulating items,
i.e. adding an item from the world into the player's
inventory.
*/
package cs3524.mud.client;

import java.rmi.RemoteException;

import cs3524.mud.server.ConnectionInterface;

public class Hands {
    private ConnectionInterface conn;

    public Hands(ConnectionInterface conn) {
        this.conn = conn;
    }

    /*
    * Pickup any thing on the map (player or item)
    * (note: if you pick up a player, it will just show up in the player inventory
    * and players can't see the player at the location until they move or leave and
    * join the game again)
    */
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