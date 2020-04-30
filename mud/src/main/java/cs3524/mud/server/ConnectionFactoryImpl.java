/*******************************************************************
 * cs3515.examples.funnel.ConnectionFactoryImpl                    *
 *******************************************************************/

package cs3524.mud.server;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
// import java.util.Vector;

import cs3524.mud.server.game.MUD;

import java.rmi.server.UID;
import java.rmi.server.UnicastRemoteObject;

/**
 * Manages a pool of connections.
 *
 * <p>
 * This class simply provides a means to request a connection and a means to
 * release the connection (called by the generated connection itself). When a
 * ConnectionImpl instance is generated, a reference to it is passed back to the
 * client.
 *
 * @see ConnectionInterface
 * @see ConnectionImpl
 * @see ConnectionFactoryInterface
 *
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ConnectionFactoryImpl implements ConnectionFactoryInterface {
    private Set<String> conIDs;
    private int serverport;
    private int maxConnections;
    private MUD masterMud;
    // private MUD defaultMud;

    // public boolean setNewID(String newID, String oldID) {
    //     return conIDs.add(newID) && conIDs.remove(oldID);
    // }
    /**
     * Initialise the vector of connection identifiers.
     */

    public ConnectionFactoryImpl(int serverport, int maxConnections, MUD masterMud) throws RemoteException {
        conIDs = new HashSet<String>();
        this.serverport = serverport;
        this.maxConnections = maxConnections;
        this.masterMud = masterMud;
    }

    /**
     * Get an ID using java.rmi.server.UID, record it, generate a ConnectionImpl
     * object and return it to the client.
     */

    private synchronized boolean isFull() {
        return maxConnections <= conIDs.size();
    }

    public ConnectionInterface getConnection() throws RemoteException, MaxConnectionsException {

        if (isFull()) {
            throw new MaxConnectionsException();
        }
        String id = (new UID()).toString();

        synchronized (conIDs) {
            conIDs.add(id);
        }
        System.out.println("Connection with ID=" + id + " generated.");
        var conn = new ConnectionImpl(id, this, masterMud);
        var stub = (ConnectionInterface) UnicastRemoteObject.exportObject(conn, serverport);
        return stub;
    }

    /**
     * Used by Connection objects to tell the Factory that the client has released
     * the connection.
     *
     * @param id The identifier of the connection to be released.
     */

    public void releaseConnection(String id) throws RemoteException {
        boolean wasElement;
        synchronized (conIDs) {
            wasElement = conIDs.remove(id);
        }
        if (wasElement) {
            System.out.println("Connection with ID=" + id + " removed from conIDs.");
        } else {
            System.out.println("Runtime error: connection with ID=" + id + " does not exist.");
        }
    }
}
