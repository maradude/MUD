/*******************************************************************
 * cs3515.examples.factory.ConnectionInterface                      *
 *******************************************************************/

package cs3524.mud.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * The remote persona of a connection.
 *
 * <p>
 * Any implementation of this interface must implement at least these methods.
 *
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public interface ConnectionInterface extends Remote {

    /**
     * Obtain the identifier of this connection.
     */
    public String getID() throws RemoteException;

    /**
     * Obtain location info
     */

    public void createPlayer(String name) throws RemoteException;

    // public void setName(String loc, String thing, String Name);

    public String getLocationInfo(String playerName, String loc) throws RemoteException;

    public String getStartLocation() throws RemoteException;

    public String move(String loc, String dir, String name) throws RemoteException;

    public boolean pickUpThing(String loc, String thing) throws RemoteException;
    /**
     * Release the connection.
     */
    public void release() throws RemoteException;
}
