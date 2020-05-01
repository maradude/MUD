/***********************************************************************
 * cs3524.mud.Vertex
 * aka location in game,
 ***********************************************************************/

package cs3524.mud.server.game;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;
import java.util.Map.Entry;

// Represents a location in the MUD (a vertex in the graph).
class Vertex {
	public String name; // Vertex name
	public String message = ""; // Message about this location
	public Map<String, Edge> routes; // Association between direction
	// (e.g. "north") and a path
	// (Edge)
	public List<String> things; // The things (e.g. players) at
	// this location

	public Vertex(String nm) {
		name = nm;
		routes = new HashMap<String, Edge>(); // Not synchronised
		things = new Vector<String>(); // Synchronised
	}

	public String toString(String playerName) {
		var summary = new StringBuilder();
		summary.append("\n");
		summary.append(message + "\n");
		for (Entry<String, Edge> route : routes.entrySet()) {
			summary.append("To the ");
			summary.append(route.getKey());
			summary.append(" there is ");
			summary.append(route.getValue().view);
			summary.append("\n");
		}
		summary.append("You can see: ");
		for (var thing : things) {
			// don't print the player themself
			if (!thing.equals(playerName)) {
				summary.append(' ' + thing);
			}
		}
		summary.append("\n\n");
		return summary.toString();
	}
}
