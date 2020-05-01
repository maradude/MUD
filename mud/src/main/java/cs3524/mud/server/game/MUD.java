/***********************************************************************
 * cs3524.mud.MUD
 ***********************************************************************/

package cs3524.mud.server.game;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.StringTokenizer;

import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;

/**
 * A class that can be used to represent a MUD; essenially, this is a graph.
 */

public class MUD {
    private static String edgeFile, messageFile, thingFile;
    private static Vector<MUD> muds = new Vector<>();
    private static MUD defaultMUD;
    /**
     * Private stuff
     */

    // A record of all the vertices in the MUD graph. HashMaps are not
    // synchronized, but we don't really need this to be synchronised.
    private Map<String, Vertex> vertexMap = new HashMap<String, Vertex>();
    private Vector<String> activePlayers = new Vector<>();
    private String name;

    private String startLocation = "";

    /**
     * Add a new edge to the graph.
     */
    private void addEdge(String sourceName, String destName, String direction, String view) {
        Vertex v = getOrCreateVertex(sourceName);
        Vertex w = getOrCreateVertex(destName);
        v.routes.put(direction, new Edge(w, view));
    }

    public static MUD getDefaultMUD() {
        return defaultMUD;
    }

    public static void setDefaultMUD(MUD defaultMUD) {
        MUD.defaultMUD = defaultMUD;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Create a new thing at a location.
     */
    // private void createThing(String loc, String thing) {
    // Vertex v = getOrCreateVertex(loc);
    // v.things.add(thing);
    // }

    /**
     * Change the message associated with a location.
     */
    private void changeMessage(String loc, String msg) {
        Vertex v = getOrCreateVertex(loc);
        v.message = msg;
    }

    /**
     * If vertexName is not present, add it to vertexMap. In either case, return the
     * Vertex. Used only for creating the MUD.
     */
    private Vertex getOrCreateVertex(String vertexName) {
        Vertex v = vertexMap.get(vertexName);
        if (v == null) {
            v = new Vertex(vertexName);
            vertexMap.put(vertexName, v);
        }
        return v;
    }

    /**
     *
     */
    private Vertex getVertex(String vertexName) {
        return vertexMap.get(vertexName);
    }

    /**
     * Creates the edges of the graph on the basis of a file with the following
     * fromat: source direction destination message
     */
    private void createEdges(String edgesfile) {
        try {
            FileReader fin = new FileReader(edgesfile);
            BufferedReader edges = new BufferedReader(fin);
            String line;
            while ((line = edges.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() < 3) {
                    System.err.println("Skipping ill-formatted line " + line);
                    continue;
                }
                String source = st.nextToken();
                String dir = st.nextToken();
                String dest = st.nextToken();
                String msg = "";
                while (st.hasMoreTokens()) {
                    msg = msg + st.nextToken() + " ";
                }
                addEdge(source, dest, dir, msg);
            }
            edges.close();
        } catch (IOException e) {
            System.err.println("Graph.createEdges( String " + edgesfile + ")\n" + e.getMessage());
        }
    }

    /**
     * Records the messages assocated with vertices in the graph on the basis of a
     * file with the following format: location message The first location is
     * assumed to be the starting point for users joining the MUD.
     */
    private void recordMessages(String messagesfile) {
        try {
            FileReader fin = new FileReader(messagesfile);
            BufferedReader messages = new BufferedReader(fin);
            String line;
            boolean first = true; // For recording the start location.
            while ((line = messages.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() < 2) {
                    System.err.println("Skipping ill-formatted line " + line);
                    continue;
                }
                String loc = st.nextToken();
                String msg = "";
                while (st.hasMoreTokens()) {
                    msg = msg + st.nextToken() + " ";
                }
                changeMessage(loc, msg);
                if (first) { // Record the start location.
                    startLocation = loc;
                    first = false;
                }
            }
            messages.close();
        } catch (IOException e) {
            System.err.println("Graph.recordMessages( String " + messagesfile + ")\n" + e.getMessage());
        }
    }

    /**
     * Records the things assocated with vertices in the graph on the basis of a
     * file with the following format: location thing1 thing2 ...
     */
    private void recordThings(String thingsfile) {
        try {
            FileReader fin = new FileReader(thingsfile);
            BufferedReader things = new BufferedReader(fin);
            String line;
            while ((line = things.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.countTokens() < 2) {
                    System.err.println("Skipping ill-formatted line " + line);
                    continue;
                }
                String loc = st.nextToken();
                while (st.hasMoreTokens()) {
                    addThing(loc, st.nextToken());
                }
            }
            things.close();
        } catch (IOException e) {
            System.err.println("Graph.recordThings( String " + thingsfile + ")\n" + e.getMessage());
        }
    }

    /**
     * All the public stuff. These methods are designed to hide the internal
     * structure of the MUD. Could declare these on an interface and have external
     * objects interact with the MUD via the interface.
     */

    /**
     * A constructor that creates the MUD.
     */
    public MUD(String name, String edgesfile, String messagesfile, String thingsfile) {
        createEdges(edgesfile);
        recordMessages(messagesfile);
        recordThings(thingsfile);
        this.name = name;
        MUD.muds.add(this);

        System.out.println("Files read...");
        System.out.println(vertexMap.size() + " vertices\n");
    }

    public static MUD createDefaultMUD(String name){
        var defaultMUD = new MUD(name, MUD.edgeFile, MUD.messageFile, MUD.thingFile);

        System.out.println("default MUD generated");
        return defaultMUD;
    }

    // This method enables us to display the entire MUD (mostly used
    // for testing purposes so that we can check that the structure
    // defined has been successfully parsed.
    public String toString() {
        StringBuilder summary = new StringBuilder();
        for (Entry<String, Vertex> loc : vertexMap.entrySet()) {
            summary.append("Node: ");
            summary.append(loc.getKey());
            summary.append(loc.getValue().toString(""));
        }
        summary.append("Start location = " + startLocation);
        return summary.toString();
    }

    /**
     * A method to provide a string describing a particular location.
     */
    public String locationInfo(String playerName, String loc) {
        return getVertex(loc).toString(playerName);
    }

    /**
     * Get the start location for new MUD users.
     */
    public String startLocation() {
        return startLocation;
    }

    /**
     * Add a thing to a location; used to enable us to add new users.
     */
    public boolean addThing(String loc, String thing) {
        Vertex v = getVertex(loc);
        return v.things.add(thing);
    }

    public List<String> listPlayers() {
        return this.activePlayers;
    }

    public boolean removePlayer(String player) {
        return this.activePlayers.remove(player);
    }

    /**
     * Remove a thing from a location.
     *
     * Returns true if thing existed
     */
    public boolean delThing(String loc, String thing) {
        Vertex v = getVertex(loc);
        return v.things.remove(thing);
    }

    public void addPlayer(String name, String loc) {
        addThing(loc, name);
        activePlayers.add(name);
    }

    /**
     * A method to enable a player to move through the MUD (a player is a thing).
     * Checks that there is a route to travel on. Returns the location moved to.
     */
    public String moveThing(String loc, String dir, String thing) {
        Vertex v = getVertex(loc);
        Edge e = v.routes.get(dir);
        if (e == null) // if there is no route in that direction
            return loc; // no move is made; return current location.
        v.things.remove(thing);
        e.destination.things.add(thing);
        return e.destination.name;
    }

    /**
     * A main method that can be used to testing purposes to ensure that the MUD is
     * specified correctly.
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: java Graph <edgesfile> <messagesfile> <thingsfile>");
            return;
        }
        MUD m = new MUD("main-server",args[0], args[1], args[2]);
        System.out.println(m.toString());
    }

    public static void setDefaultConfigFiles(String edgeFile, String messageFile, String thingFile) {
        MUD.edgeFile = edgeFile;
        MUD.messageFile = messageFile;
        MUD.thingFile = thingFile;
    }

    public static void initialMUDs(int n) {
        for (int i = 1; i <= n; i++) {
            MUD.createDefaultMUD(String.format("mud%d", i));
        }
    }

    public static Vector<MUD> MUDList() {
        return MUD.muds;
    }

	public static MUD getMUD(String game) {
        return MUD.muds.stream().filter(x -> x.getName().equals(game)).findAny().orElse(null);
	}

	public void remove(String player) {
	}

	// public boolean join(String player) {

	// }

}
