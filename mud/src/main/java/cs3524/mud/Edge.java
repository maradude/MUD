/***********************************************************************
 * cs3524.mud.Edge
 ***********************************************************************/

package src.main.java.cs3524.mud;

// Represents an path in the MUD (an edge in a graph).
class Edge
{
    public Vertex _dest;   // Your destination if you walk down this path
    public String _view;   // What you see if you look down this path

    public Edge( Vertex d, String v )
    {
        _dest = d;
	_view = v;
    }
}

