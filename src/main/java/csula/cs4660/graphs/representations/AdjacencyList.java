package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/**
 * Adjacency list is probably the most common implementation to store the unknown
 * loose graph
 *
 * TODO: please implement the method body
 */
public class AdjacencyList implements Representation {
    private Map<Node, Collection<Edge>> adjacencyList;
    //use Map for AdjacencyList, node id as key, value is a ArrayList which contains
    //all edges direcly connected to this node and their weights
    
    public AdjacencyList(File file) {
    	try{
    		adjacencyList = new HashMap<Node, Collection<Edge>>();
    		PrintWriter writer = new PrintWriter("src/csula/cs4660/graphs/test/log");
    		//read nodes and edges data from the file
    		//String testfile = "src/csula/cs4660/graphs/test/test1";
    		//file = new File(testfile);
    		//Representation adjacencyList = new Representation(STRATEGY.ADJACENCY_LIST);
    		ArrayList<String> arr = readFile(file);
    		//0:3:4 means from node 0 to node 3, weight is 4
    		for(int i=0;i<arr.size();i++){
    			String s[] = arr.get(i).split(":");
    			
    			//check if a new node or not
    			//when it's a new node, add it to the map as a new key
    			
    			//for the first line of record
    			// NullPointException
    			if(adjacencyList == null){
    				Node fromNode = new Node(s[0]);
    				Node toNode = new Node(s[1]);
    				Edge ed = new Edge(fromNode, toNode, Integer.parseInt(s[2]));
    				//writer.println("test:" + fromNode + " " + toNode + " " + Integer.parseInt(s[2]));
    				ArrayList<Edge> edges = new ArrayList<>();
    				edges.add(ed);
    				adjacencyList.put(fromNode, edges);
    				continue;
    			}
    			//from the second record
    			if(!adjacencyList.containsKey(s[0])){
    				//create new node if the key does not exist yet
    				Node fromNode = new Node(s[0]);
    				Node toNode = new Node(s[1]);
    				Edge ed = new Edge(fromNode, toNode, Integer.parseInt(s[2]));
    				//writer.println("test:" + fromNode + " " + toNode + " " + Integer.parseInt(s[2]));
    				ArrayList<Edge> edges = new ArrayList<>();
    				edges.add(ed);
    				adjacencyList.put(fromNode, edges);
    				
    			//when the node is in the map already, find the key and just add value	
    			}else{
    				//add edges to existing node since the node exists already
    				Set<Node> keynodes = adjacencyList.keySet();
    				//loop through the map data structure
    				adjacencyList.forEach((k, v)->{
    					//find the node which matches the key, then just add the new
    					//edge to the value (which is the ArrayList)
    					if(k.equals(s[0])){
    	    				Node toNode = new Node(s[1]);
    	    				Edge ed = new Edge(k, toNode, Integer.parseInt(s[2]));
    						ArrayList<Edge> edges = (ArrayList)adjacencyList.get(s[0]);
    	    				edges.add(ed);
    					}
    				} );
    				
    			}
    		}
    		
    	}catch(IOException e){
    		System.out.println(e.getMessage());
    	}
    }

    public AdjacencyList() {

    }
    
    //read a file in and return a String array which contains each line of the file
    private static ArrayList<String> readFile(File file) throws IOException{
    	
    	Scanner read = new Scanner(file);
    	//read first line
    	//need check??
    	String firstLine = read.nextLine().trim();
    	//int NodeNo = Integer.parseInt(firstLine);
    	ArrayList<String> strarr = new ArrayList<>();
        while(read.hasNext()){
            String line = read.nextLine().trim();
            System.out.println(line);
            strarr.add(line);
        }
        read.close();
    	return strarr;
    }

    @Override
    public boolean adjacent(Node x, Node y) {
    	if(adjacencyList.containsKey(x)){
    		ArrayList<Edge> arr = (ArrayList)adjacencyList.get(x);
    		for(int i=0;i<arr.size();i++){
    			if(arr.get(i).getTo().equals(y)){
    				return true;
    			}
    		}
    	}
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
    	if(adjacencyList.containsKey(x)){
    		return (ArrayList)adjacencyList.get(x);    	
    	}
        return null;
    }

    @Override
    public boolean addNode(Node x) {
    	//if x is not a key, then add it as a key, while value as a empty ArrayList
    	if(!adjacencyList.containsKey(x)){
    		ArrayList<Edge> arr = new ArrayList<Edge>();
    		adjacencyList.put(x, arr);
    		return true;
    	}else{
    		System.out.println("Error: This node exists already!");
    	}
        return false;
    }

    @Override
    public boolean removeNode(Node x) {
    	//when node x is a key, then remove it
    	if(adjacencyList.containsKey(x)){
    		adjacencyList.remove(x);
    	}
    	//remove x from the value of any of the key nodes
    	adjacencyList.forEach((k, v)->{
    		if(v.contains(x)){
    			v.remove(x);
    		}
    	});
        return false;
    }

    @Override
    public boolean addEdge(Edge x) {
    	
    	//add  toNode to FromNode's value list, FromNode should exist already
    	Node fn = x.getFrom();
    	Node tn = x.getTo();
    	if(adjacencyList.containsKey(fn)){
    		//add the edge to the value ArrayList
    		ArrayList<Edge> arr = (ArrayList) adjacencyList.get(fn);
    		arr.add(x);
    		return true;
    	}else{
    		System.out.println("Error: fromNode doesn't exist!");
    	}
        return false;
    }

    @Override
    public boolean removeEdge(Edge x) {

    	Node fn = x.getFrom();
    	Node tn = x.getTo();
    	//if fromNode or toNode not existing, then gives error message
    	if(!adjacencyList.containsKey(fn) || !adjacencyList.containsKey(tn)){
    		System.out.println("Error: fromNode or toNode does not exist...");
    		return false;
    	}    	
    	//fromNode exists
    	if(adjacencyList.containsKey(fn)){
    		adjacencyList.get(fn).remove(x);
    		return true;
    	}
        return false;
    }

    @Override
    public int distance(Node from, Node to) {
    	//when from node doesn't exist, gives error out
    	if(!adjacencyList.containsKey(from)){
    		System.out.println("fromNode does not exist!");
    	}else{
    		ArrayList<Edge> arr = (ArrayList)adjacencyList.get(from);
    		for(int i=0;i<arr.size();i++){
    			if(arr.get(i).getTo().equals(to)){
    				return arr.get(i).getValue();
    			}
    		}
    	}
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
