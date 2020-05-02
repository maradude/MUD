/*******************************************************************
* Martti Aukia 51657228
 * cs3515.examples.funnel.ConnectionFactoryImpl                    *
 * from factory practical of the course
 * changes made by student:
 * apply auto formatting
 * doesn't create a new connection if limit reached
 *******************************************************************/

package cs3524.mud.server;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
// import java.util.Vector;


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
 * @see Connection
 * @see ConnectionFactoryInterface
 *
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class ConnectionFactory implements ConnectionFactoryInterface {
    private Set<String> conIDs;
    private int serverport;
    private int maxConnections;

    // public boolean setNewID(String newID, String oldID) {
    // return conIDs.add(newID) && conIDs.remove(oldID);
    // }
    /**
     * Initialise the vector of connection identifiers.
     */

    public ConnectionFactory(int serverport, int maxConnections) throws RemoteException {
        conIDs = new HashSet<String>();
        this.serverport = serverport;
        this.maxConnections = maxConnections;
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
        var conn = new Connection(id, this);
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
