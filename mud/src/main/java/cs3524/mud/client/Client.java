/*
* Martti Aukia 51657228
Code is adapted from cs3518.examples.factory.ClientSimulator/ClientThread

Class registers user connection to the server via RMI,
inits the local information for game playing,
inits user input handling
*/

package cs3524.mud.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import cs3524.mud.server.MaxConnectionsException;
import cs3524.mud.server.ConnectionInterface;
import cs3524.mud.PolicyReader;
import cs3524.mud.server.ConnectionFactoryInterface;

public class Client {
    userInputHandler cli;
    String hostname;
    int port;
    int id;

    public Client(String args[]) {
        if (args.length < 2) {
            System.err.println("Usage:\njava cs3515.mud.client.Client <host> <port>");
            return;
        }
        hostname = args[0];
        port = Integer.parseInt(args[1]);
    }

    public void registerClient() {
        PolicyReader.registerPolicy("mud.policy");
        String regURL = "rmi://" + hostname + ":" + port + "/MUD";
        try {
            var man = (ConnectionFactoryInterface) Naming.lookup(regURL);
            ConnectionInterface conn = man.getConnection();
            cli = new userInputHandler();
            String userName = cli.getName();
            var gameManager = new GameManager(conn, userName);
            String server;
            boolean success = false;
            while (!success) {
                server = cli.selectServer(conn.listGames());
                success = gameManager.joinGame(server);
            }
            cli.runGame(gameManager);

        } catch (NotBoundException e) {
            System.err.println(regURL + " not bound in rmiregistry.");
        } catch (MalformedURLException e) {
            System.err.println(regURL + " not a correctly formed URL.");
        } catch (RemoteException e) {
            System.err.println("Error contacting remote objects on host " + hostname);
            e.printStackTrace(System.err);
        } catch (MaxConnectionsException e) {
            System.err.println("Too many clients connected, try again alter");
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

    /*
     * args[0] = The DNS entry for the host on which the server is running.
     *
     * args[1] = The port on the remote host on which the rmiregistry is listening.
     */
    public static void main(String[] args) {
        Client client = new Client(args);
        client.registerClient();
    }
}