/*
* Martti Aukia 51657228
Handles user requests to the server related to moving the player from
one location to another.
*/
package cs3524.mud.client;

import java.rmi.RemoteException;
import java.util.Set;

import cs3524.mud.server.ConnectionInterface;

public class Legs {
    final private static Set<String> cardinalDirections = Set.of("north", "west", "south", "east");
    private ConnectionInterface conn;

    public Legs(ConnectionInterface conn) {
        this.conn = conn;
    }

    public void move(Player player, String direction) throws RemoteException {
        if (direction == null) {
            System.out.println("direction needed to move");
            return;
        }
        direction = direction.toLowerCase();
        if (Legs.cardinalDirections.contains(direction)) {
            String newLocation = conn.move(player.getCurrentLocation(), direction, player.getName());
            if (newLocation.equals(player.getCurrentLocation())) {
                System.out.println("Direction specified is not accessible");
            } else {
                player.setCurrentLocation(newLocation);
                player.lookAround(player.getCurrentLocation());
            }
        } else {
            System.out.println("Direction not recognized, please use on of {north, west, south, east}");
        }

    }
}