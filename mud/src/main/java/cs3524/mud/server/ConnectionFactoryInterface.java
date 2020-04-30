/*******************************************************************
 * cs3515.examples.factory.ConnectionFactoryInterface              *
 *******************************************************************/

package cs3524.mud.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote persona of a ConnectionFactory object.
 *
 * <p>
 * Any implementation of this interface must implement at least these methods.
 *
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public interface ConnectionFactoryInterface extends Remote {
    /**
     * Attempt to obtain a connection to the remote resource.
     */
    public ConnectionInterface getConnection() throws RemoteException, MaxConnectionsException;

    // public boolean setNewID(String newID, String oldID);
    /**
     * Release connection. Should only be called by the connection object.
     */
    public void releaseConnection(String id) throws RemoteException;
}
