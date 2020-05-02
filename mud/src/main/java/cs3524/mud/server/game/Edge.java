/***********************************************************************
* Martti Aukia 51657228
 * cs3524.mud.Edge
 * aka path you can walk through or peek at
 *
 * Untouched by student, apart from stylistic refactoring
 ***********************************************************************/

package cs3524.mud.server.game;

// Represents an path in the MUD (an edge in a graph).
class Edge {
    public Vertex destination; // Your destination if you walk down this path
    public String view; // What you see if you look down this path

    public Edge(Vertex d, String v) {
        destination = d;
        view = v;
    }
}
