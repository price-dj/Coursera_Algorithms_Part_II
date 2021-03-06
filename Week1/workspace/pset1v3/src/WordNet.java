import edu.princeton.cs.algs4.In;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.StdIn;

public class WordNet {

    private int V;                                             // number of vertices in this digraph
    private Map<Integer, String> synsetsMap;                   // map of synset IDs to their synset Strings 
    private Map<String, List<Integer>> wordIdMap;       // map of words to their synset IDs
    private Digraph graph;
    
    private SAP sap;
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException("Null argument(s) to WordNet constructor.");

        // initialise the SynsetsMap
        this.synsetsMap = initialiseSynsetsMap(synsets);
        
        // initialise wordIDs
        this.wordIdMap = initialiseWordIdMap(synsets);
        
        // initialise the hypernyms Adjacency List Array of Bags
        this.V = synsetsMap.size();
        
        //System.out.println(V);
        
        // build graph
        this.graph = new Digraph(V);
        
        In in = new In(hypernyms);

        while (in.hasNextLine()) {
            String currentLine = in.readLine();
            String[] numbers = currentLine.split(",");

            Integer idV = Integer.parseInt(numbers[0]);
            for (int i = 0; i < numbers.length; i++) {
                Integer n = Integer.parseInt(numbers[i]);
                if (n < 0 || n >= V)
                    throw new IndexOutOfBoundsException();

                if (i > 0) {
                    graph.addEdge(idV, n);
                }
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
        List<String> nouns = new ArrayList<String>(wordIdMap.keySet());

        return Collections.unmodifiableList(nouns);
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word.equals(null)) {
            throw new NullPointerException();
        }
        
        return wordIdMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException("Noun argument(s) to distance not in graph");
        }
        
        if (nounA.equals(null) || nounB.equals(null)) {
            throw new NullPointerException();
        }
        
        int IdForA = wordIdMap.get(nounA).get(0);
        int IdForB = wordIdMap.get(nounB).get(0);
        
        return sap.length(IdForA, IdForB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!this.isNoun(nounA) || !this.isNoun(nounB)) {
            throw new IllegalArgumentException("Noun argument(s) to sap not in graph");
        }
        
        int IdForA = wordIdMap.get(nounA).get(0);
        int IdForB = wordIdMap.get(nounB).get(0);
        
        int sapID = sap.ancestor(IdForA, IdForB);
        
        String ancestor = this.synsetsMap.get(sapID);
        return ancestor;
        
    }

    private Map<Integer, String> initialiseSynsetsMap(String synsets) {
        Map<Integer, String> results = new HashMap<Integer, String>();
        try (BufferedReader br = new BufferedReader(new FileReader(synsets))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                String[] fields = line.split(",");
                int synsetsID = Integer.parseInt(fields[0]);
                results.put(synsetsID, fields[1]);
            }
            
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }


    private Map<String, List<Integer>> initialiseWordIdMap(String synsets) {
        Map<String, List<Integer>> results = new HashMap<String, List<Integer>>();
        try (BufferedReader br = new BufferedReader(new FileReader(synsets))) {
            for(String line; (line = br.readLine()) != null; ) {
                // process the line.
                String[] fields = line.split(",");
                int synsetsID = Integer.parseInt(fields[0]);
                String[] synsetsArr = fields[1].split(" ");
                for (int i = 0; i < synsetsArr.length; i++) {
                    if (!results.containsKey(synsetsArr[i])) {
                        List<Integer> IdList = new ArrayList<Integer>();
                        IdList.add(synsetsID);
                        results.put(synsetsArr[i], IdList);
                    }
                    else {
                        List<Integer> synsetsIdList = results.get(synsetsArr[i]);
                        synsetsIdList.add(synsetsID);
                        results.put(synsetsArr[i], synsetsIdList);
                    }
                }
                
            }
            
        }

        catch (IOException e) {
            e.printStackTrace();
        }

        return results;
    }

    
    private boolean isRootedDAG(Digraph graph) {

        DirectedCycle cycleFinder = new DirectedCycle(graph);
        if (cycleFinder.hasCycle()) {
            return false;
        }

        int numberOfZeroOut = 0;
        for (int i = 0; i < graph.V(); i++) {
            int count = 0;
            for (int adjNode : graph.adj(i)) {
                count++;
            }

            if (count == 0) {
                numberOfZeroOut++;
            }
        }

        if (numberOfZeroOut != 1) {
            return false;
        }

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
