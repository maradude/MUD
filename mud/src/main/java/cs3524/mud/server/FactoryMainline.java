/*******************************************************************
 * cs3515.examples.factory.FactoryMainline                         *
 *******************************************************************/
/* updated to Java 8: RMISecurityManager is deprecated
 * MK, 2016-01-27
 */

package cs3524.mud.server;

import java.net.InetAddress;

import java.rmi.Naming;
import java.rmi.server.UnicastRemoteObject;

import cs3524.mud.PolicyReader;
import cs3524.mud.server.game.MUD;

/**
 * The server mainline for the connection factory.
 *
 * <p>
 * There are two tasks to be performed here:
 * <ol>
 * <li>Generate a connection factory remote object instance.
 * <li>Register its remote reference with the rmiregistry.
 * </ol>
 *
 * <p>
 * Of course, we need to obtain and check the integrity of the run-time
 * parameters and set up a security manager.
 *
 * The run-time parameters are:
 * <ul>
 * <li>args[0] = the port at which the rmiregistry is listening.
 * <li>args[1] = the port on which to export objects.
 *
 * @see ConnectionFactoryInterface
 * @see ConnectionFactoryImpl
 *
 * @author Tim Norman, University of Aberdeen
 * @version 1.0
 */

public class FactoryMainline {
	/**
	 * Generate the factory object and register it with the rmiregistry.
	 */
	 private String  edgesfile, messagesfile, thingsfile;
	 private Integer registryport, serverport, maxConnections;

	 public FactoryMainline(String args[]){
		registryport = Integer.parseInt(args[0]);
		serverport = Integer.parseInt(args[1]);
		maxConnections = Integer.parseInt(args[2]);
		edgesfile = args[3];
		messagesfile = args[4];
		thingsfile = args[5];
		PolicyReader.registerPolicy("mud.policy");
	 }
	public static void main(String args[]) {
		if (args.length < 6) {
			System.err.println(
					"Usage:\njava cs3515.examples.factory.FactoryMainline <registryport> <serverport> <maxConnections> <edgesfile> <messagesfile> <thingsfile>");
			return;
		}
		var server = new FactoryMainline(args);
        MUD mudMaster = new MUD(server.edgesfile, server.messagesfile, server.thingsfile);
        System.out.println(mudMaster.toString());

		try {
			// rmi stuff
			String hostname = (InetAddress.getLocalHost()).getCanonicalHostName();

			// factory stuff

			var serv = new ConnectionFactoryImpl(server.serverport, server.maxConnections, mudMaster);
			var stub = (ConnectionFactoryInterface) UnicastRemoteObject.exportObject(serv, server.serverport);
			Naming.rebind("rmi://" + hostname + ":" + server.registryport + "/MUD", stub);

		} catch (java.net.UnknownHostException e) {
			System.err.println(e.getMessage());
		} catch (java.io.IOException e) {
			System.err.println(e.getMessage());
		}
	}
}