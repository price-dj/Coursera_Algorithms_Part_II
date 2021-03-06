import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    
    private final Digraph graph;            // Private immutable copy of the digraph.

    private ST<String, int[]> cache;        // Cache of previously determined ancestors and SAPs (speed optimisation).
    
    private int minLength;                  // length of shortest ancestral path between v and w
    
    private int ancestor;                   // common ancestor id for v and w
    
    
    /**
     * Constructor takes a digraph (not necessarily a DAG).
     * @param digraph to analyse
     */
    
    
    
    public SAP(final Digraph digraph) {
        // Make a deep copy of the digraph
        graph = new Digraph(digraph.V());
        for (int v = 0; v < digraph.V(); v++) {
            for (int w : digraph.adj(v)) {
                graph.addEdge(v, w);
            }
        }

        // Create the cache
        cache = new ST<String, int[]>();
    }

    /**
     * Returns the length of the shortest ancestral path between v and w;
     * -1 if no such path.
     * @param v - a vertex in the digraph
     * @param w - a vertex in the digraph
     * @return the shortest ancestral path length
     */
    public int length(final int v, final int w) { 
        calcBfsPath(v, w);
        
        return minLength;
        
    }

    /**
     * Returns a common ancestor of v and w that participates in a shortest ancestral
     * path; -1 if no such path.
     * @param v - a vertex in the digraph
     * @param w - a vertex in the digraph
     * @return the common ancestor of v and w, or -1 if no such path exists
     */
    public int ancestor(final int v, final int w) { 
        calcBfsPath(v, w);
        
        return ancestor;
        
    }

    /**
     * Returns the length of the shortest ancestral path between any vertex in v and
     * any vertex in w; -1 if no such path.
     * @param v - list of vertices in the digraph
     * @param w - list of vertices in the digraph
     * @return the shortest ancestral path length between any vertex in v and w
     */
    public int length(final Iterable<Integer> v, final Iterable<Integer> w) { 
        calcBfsPath(v, w);
        
        return minLength;
        
    }

    /**
     * Returns a common ancestor that participates in the shortest ancestral path;
     * -1 if no such path.
     * @param v - list of vertices in the digraph
     * @param w - list of vertices in the digraph
     * @return the common ancestor with shortest path between any vertex in v and w
     */
    public int ancestor(final Iterable<Integer> v, final Iterable<Integer> w) { 
        calcBfsPath(v, w); 
        
        return ancestor;
        
    }

    /**
     * Finds the shortest ancestral path and common ancestor between v and w.
     * @param v - vertex in the digraph
     * @param w - vertex in the digraph
     * @return an integer array containing the common ancestor and the path length
     */
    private void calcBfsPath(final int v, final int w) {
        // Check that v and w are valid
        validateVertices(v, w);

        // Return the cached common ancestor, if previously determined
        if (cache.contains(keyOf(v, w))) {
            ancestor = cache.get(keyOf(v, w))[0];
            minLength = cache.get(keyOf(v, w))[1];
            return;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);

        ancestor = -1;
        minLength = Integer.MAX_VALUE;

        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int length = bfsV.distTo(i) + bfsW.distTo(i);
                if (length < minLength) {
                    ancestor = i;
                    minLength = length;
                }
            }
        }

        if (ancestor == -1) minLength = -1;

        int[] value = new int[2];
        value[0] = ancestor;
        value[1] = minLength;

        // Cache the common ancestor and shortest ancestral path
        cache.put(keyOf(v, w), value);
        cache.put(keyOf(w, v), value);
        
        return;
    }

    /**
     * Finds the shortest ancestral path and common ancestor between any vertex in v
     * and any vertex in w.
     * @param v - list of vertices in the digraph
     * @param w - list of vertices in the digraph
     * @return an integer array containing the common ancestor and the path length
     */
    private void calcBfsPath(final Iterable<Integer> v, final Iterable<Integer> w) {
        // Check that v and w are valid
        validateVertices(v, w);

        // Return the cached common ancestor, if previously determined
        if (cache.contains(keyOf(v, w))) {
            ancestor = cache.get(keyOf(v, w))[0];
            minLength = cache.get(keyOf(v, w))[1];
            return;
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);

        ancestor = -1;
        minLength = Integer.MAX_VALUE;

        for (int i = 0; i < graph.V(); i++)
        {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i))
            {
                int length = bfsV.distTo(i) + bfsW.distTo(i);
                if (length < minLength)
                {
                    ancestor = i;
                    minLength = length;
                }
            }
        }

        if (ancestor == -1) minLength = -1;

        int[] value = new int[2];
        value[0] = ancestor;
        value[1] = minLength;

        // Cache the common ancestor and shortest ancestral path
        cache.put(keyOf(v, w), value);
        cache.put(keyOf(w, v), value);

        return;
    }

    /**
     * Checks that the vertices v and w are valid; that is, they are both between
     * zero and the total number of vertices in the digraph.
     * @param v - a vertex in the digraph
     * @param w - a vertex in the digraph
     */
    private void validateVertices(final int v, final int w) {
        if (v < 0 || v >= graph.V())
            throw new java.lang.IndexOutOfBoundsException("v must be >= 0 and < V");
        if (w < 0 || w >= graph.V())
            throw new java.lang.IndexOutOfBoundsException("w must be >= 0 and < V");
    }

    /**
     * Checks that all vertices in the lists v and w are valid; that is, they are all
     * between zero and the total number of vertices in the digraph.
     * @param v - list of vertices in the digraph
     * @param w - list of vertices in the digraph
     */
    private void validateVertices(final Iterable<Integer> v, final Iterable<Integer> w) {
        
        for (int a : v)
            if (a < 0 || a >= graph.V())
                throw new java.lang.IndexOutOfBoundsException(
                        "v[" + a + "] must be >= 0 and < V");
        for (int b : w)
            if (b < 0 || b >= graph.V())
                throw new java.lang.IndexOutOfBoundsException(
                        "w[" + b + "] must be >= 0 and < V");
    }

    /**
     * Returns the corresponding cache key string for the two vertices v and w.
     * @param v - a vertex in the digraph
     * @param w - a vertex in the digraph
     * @return the key string for the two vertices
     */
    private String keyOf(final int v, final int w)
    { return String.valueOf(v) + "-" + String.valueOf(w); }

    /**
     * Returns the corresponding cache key string for two lists of vertices v and w.
     * @param v - list of vertices in the digraph
     * @param w - list of vertices in the digraph
     * @return the key string for the two lists of vertices
     */
    private String keyOf(final Iterable<Integer> v, final Iterable<Integer> w)
    {
        String key = "";
        for (int i: v) key += String.valueOf(i) + ",";
        key += "-";
        for (int i: w) key += String.valueOf(i) + ",";
        return key;
    }

    /**
     * Unit tests.
     * @param args - not used
     */
    public static void main(final String[] args) {
        /*In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }*/
    }
}