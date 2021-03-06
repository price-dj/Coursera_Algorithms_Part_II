/**
 * 
 */

/**
 * @author David Price
 *
 */

import edu.princeton.cs.algs4.In;





import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    
    private Digraph graph;                                  // the digraph
    
    private int minLength;                                  // for calculating the minLength
    private int ancestorID;                                 // ID of the common ancestor
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.graph = G;
        this.minLength = -1;
        this.ancestorID = -1;
        //System.out.println("here");
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        this.calculateBFSpath(v, w);
        
        int result = minLength;
        return result;
    }

    // a common ancestor of v and w that participates in a shortest ancestral
    // path; -1 if no such path
    public int ancestor(int v, int w) {
        this.calculateBFSpath(v, w);
        
        
        int result = ancestorID;
        return result;

    }

    // length of shortest ancestral path between any vertex in v and any vertex
    // in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        this.calculateBFSpath(v, w);
        
        int result = minLength;
        return result;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no
    // such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        this.calculateBFSpath(v, w);
        
        int result = ancestorID;
        return result;

    }
    
    private void calculateBFSpath(int v, int w) {
        
        //if (v == null || w == null) throw new NullPointerException();
        
        if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V()) {
            throw new IndexOutOfBoundsException();
        }
        
        if (v == w) {
            minLength = 0;
            ancestorID = v;
            return;
        }
        
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (minLength == -1) {
                    minLength = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorID = i;
                }
                else if (bfsV.distTo(i) + bfsW.distTo(i) < minLength) {
                    minLength = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorID = i;
                }
                else continue;
            }
        }
    }
    
    private void calculateBFSpath(Iterable<Integer> v, Iterable<Integer> w) {
        
        if (v == null || w == null) throw new NullPointerException();
        
        for (int a : v) {
            if (a < 0 || a >= graph.V()) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        for (int b : w) {
            if (b < 0 || b >= graph.V()) {
                throw new IndexOutOfBoundsException();
            }
        }
        
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        
        for (int i = 0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                if (minLength == -1) {
                    minLength = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorID = i;
                }
                else if (bfsV.distTo(i) + bfsW.distTo(i) < minLength) {
                    minLength = bfsV.distTo(i) + bfsW.distTo(i);
                    ancestorID = i;
                }
                else continue;
            }
        }
    }
    

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            //System.out.println("here");
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
