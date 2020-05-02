/*
* Martti Aukia 51657228
Handles requests to the server about getting current location info
e.g. acting as eyes
*/
package cs3524.mud.client;

import java.rmi.RemoteException;

import cs3524.mud.server.ConnectionInterface;

public class Eyes {
    private ConnectionInterface conn;

    public Eyes(ConnectionInterface conn) {
        this.conn = conn;
    }

    public String lookAround(String name, String location) throws RemoteException {
        return conn.getLocationInfo(name, location);
    }
}