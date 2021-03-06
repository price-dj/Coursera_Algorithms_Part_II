import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {

    private int V;                                             // number of vertices in this digraph
    private ST<Integer, String> synid;                          // Symbol table containing the synset id numbers and their associated nouns.
    private ST<String, Bag<Integer>> nouns;                    // Symbol table containing the nouns and their associated synsets in the WordNet.
    private Digraph graph;
    
    private SAP sap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException("Null argument(s) to WordNet constructor.");

       
        
        nouns = new ST<String, Bag<Integer>>();
        synid = new ST<Integer, String>();
        
        In in = new In(synsets);
        
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int id = Integer.parseInt(fields[0]);
            String[] synset = fields[1].split(" ");
            for (String noun : synset) {
                Bag<Integer> list = new Bag<Integer>();
                if(nouns.contains(noun)) list = nouns.get(noun);
                list.add(id);
                nouns.put(noun, list);
            }
            synid.put(id, fields[1]);
        }
        
        V = synid.size();
        graph = new Digraph(V);
        
        in = new In(hypernyms);
        while(!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split(",");
            int v = Integer.parseInt(fields[0]);
            for (int i = 1; i < fields.length; i++) {
                int w = Integer.parseInt(fields[i]);
                if (w < 0 || w >= V)
                    throw new IndexOutOfBoundsException();

                graph.addEdge(v, w);
                
            }
        }
        
        
        // a rooted DAG
        if (!isRootedDAG(graph)) {
            throw new IllegalArgumentException("The input does not correspond to a rooted DAG");
        }
        
        // construct the sap
        sap = new SAP(graph);
        
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        
        return nouns.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new NullPointerException();
        }
        
        return nouns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException("Noun argument(s) to distance not in graph");
        }
        
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        
        Bag<Integer> v = nouns.get(nounA);
        Bag<Integer> w = nouns.get(nounB);
        
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException("Noun argument(s) to sap not in graph");
        }
        
        Bag<Integer> v = nouns.get(nounA);
        Bag<Integer> w = nouns.get(nounB);
        
        int sapID = sap.ancestor(v, w);
        
        String ancestor = this.synid.get(sapID);
        return ancestor;
        
    }

    

    
    private boolean isRootedDAG(Digraph graph) {

        int roots = 0;
        
        for (int v = 0; v < graph.V(); v++) {
            if (!graph.adj(v).iterator().hasNext()) roots++;
        }
        
        if (roots != 1) return false;
        
        return true;
    }

    public static void main(String[] args) {
        /*// Auto-generated method stub
        WordNet wn = new WordNet("D:/Documents/Dropbox/Coursera/Algorithms/Part_II/Week1/workspace/pset1v3/wordnet/synsets100-subgraph.txt", 
                                    "D:/Documents/Dropbox/Coursera/Algorithms/Part_II/Week1/workspace/pset1v3/wordnet/hypernyms100-subgraph.txt");

        for (String s : wn.nouns()) {
            System.out.println(s);
        }*/

    }

}
