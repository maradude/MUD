/*******************************************************************
 * cs3515.examples.factory.ConnectionImpl                          *
 *******************************************************************/

package cs3524.mud.server;

import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import cs3524.mud.server.game.MUD;

/**
 * Represents a connection.
 *
 * <p>
 * These remote objects are temporary, session objects, and hence are not
 * registered with the rmiregistry. A client object requests a connection by
 * calling method getConnection() on a ConnectionFactoryManager remote object.
 * Once no longer required, the client should call the method release() on this
 * object.
 *
 * <p>
 * In this example, the unique identifier of the connection can be obtained by
 * the client. Normally, this would be kept private between the Connection and
 * the Manager so that release requests can be authenticated.
 *
 * <p>
 * The performance of a task by the scarce resource is simulated by the client
 * calling method doWork(). This simply causes the connection thread to sleep
 * for a number of milliseconds between minDelay and maxDelay (two of the
 * parameters to the constructor of this class.
 *
 * @see ConnectionInterface
 * @see ConnectionFactory#getConnection()
 * @see ConnectionFactory#releaseConnection( String )
 *
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class Connection implements ConnectionInterface {
    private MUD game;
    private String id;
    private ConnectionFactoryInterface factory;

    /**
     * Initialises member variables and seeds a random number generator with the
     * current time.
     */

    public Connection(String id, ConnectionFactoryInterface factory) throws RemoteException {
        this.id = id;
        this.factory = factory;
        this.game = MUD.getDefaultMUD();
    }


    public String getGameName() throws RemoteException{
        return this.game.getName();
    }
    /**
     * Allows the client to obtain the identifier of this connection. In a practical
     * application this should be kept private between the funnel manager and the
     * connection object.
     */

    // public void setName(String loc, String thing, String Name) {
    // game.delThing(loc, thing);
    // game.addThing(loc, Name);
    // }

    public String getID() throws RemoteException {
        return id;
    }

    public List<String> listGames() throws RemoteException {
        var names = MUD.MUDList().stream()
                            .map(MUD::getName)
                            .collect(Collectors.toList());
        return names;
    }

	public boolean joinServer(String player, String loc, String game) throws RemoteException {
        var newMud = MUD.getMUD(game);
        if (newMud == null) {
            return false;
        }
        this.game.removePlayer(player);
        this.game.delThing(loc, player);
        newMud.addPlayer(player, loc);
        this.game = newMud;
        return true;
    }

    /**
     * Causes the thread in which this connection is running to sleep for a number
     * of milliseconds.
     *
     * <p>
     s* Note that although this remote method is void, the client thread that called
     * this method will suspend until the method returns; RMI is an example of a
     * synchronous communication mechanism.
     */

    public void createPlayer(String name, String loc) throws RemoteException {
        game.addPlayer(name, loc);
    }

    public boolean pickUpThing(String loc, String thing) throws RemoteException {
        return game.delThing(loc, thing);
    }

    public String getLocationInfo(String playerName, String loc) throws RemoteException {
        return game.locationInfo(playerName, loc);
    }

    public String getStartLocation() throws RemoteException {
        return game.startLocation();
    }

    public String move(String loc, String dir, String name) throws RemoteException {
        return game.moveThing(loc, dir, name);
    }

    /**
     * Client uses this method to release its connection to the scarce resource.
     *
     * <p>
     * For the factory to operate correctly, it is imperative that the client does
     * not attempt to access the connection after calling this method. It should
     * allow the reference to the remote object to go out of scope and leave the JRE
     * to garbage collect this instance.
     */

    public void release() throws RemoteException {
        factory.releaseConnection(id);
    }

}