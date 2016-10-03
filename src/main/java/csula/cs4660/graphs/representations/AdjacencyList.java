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

import javax.swing.text.html.HTMLDocument.Iterator;

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
    		//read nodes and edges data from the file
    		ArrayList<String> arr = readFile(file);
    		//0:3:4 means from node 0 to node 3, weight is 4
    		Node fromNode;
			Node toNode;
			
    		for(int i=0;i<arr.size();i++){
    			String s[] = arr.get(i).split(":");
    			
    			try{
					fromNode = new Node(Integer.parseInt(s[0]));
					toNode = new Node(Integer.parseInt(s[1]));
				}catch(NumberFormatException e){
					fromNode = new Node(s[0]);
					toNode = new Node(s[1]);
				}

    			//for the first line of record
    			if(adjacencyList == null){
    				
    				
					Edge ed = new Edge(fromNode, toNode, Integer.parseInt(s[2]));
    				//System.out.println("test:" + fromNode.getData() + " " + toNode.getData() + " " + Integer.parseInt(s[2]));
    				ArrayList<Edge> edges = new ArrayList<>();
    				edges.add(ed);
    				adjacencyList.put(fromNode, edges);
    				continue;
    			}
    			//from the second record
    			
    			//check if a new node or not
    			//when it's a new node, add it to the map as a new key
    			if(!adjacencyList.containsKey(fromNode)){
    				//create new node if the key does not exist yet
    				
    				Edge ed = new Edge(fromNode, toNode, Integer.parseInt(s[2]));
    				//System.out.println("test:" + fromNode.getData() + " " + toNode.getData() + " " + Integer.parseInt(s[2]));
    				ArrayList<Edge> edges = new ArrayList<>();
    				edges.add(ed);
    				adjacencyList.put(fromNode, edges);
    				
    			//when the node is in the map already, find the key and just add value	
    			}else{
    				//add edges to existing node since the node exists already
    				
    				ArrayList<Edge> edges = (ArrayList)adjacencyList.get(fromNode);
    				Edge ed = new Edge(fromNode, toNode, Integer.parseInt(s[2]));
    				edges.add(ed);
    			}
    		}
    		//adjacencyList.forEach((k,v)->{
    			
    			//ArrayList<Edge> e = (ArrayList)v;
    			//System.out.println(e.toString());
    		//});
    		
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
    	//System.out.println("list");
    	ArrayList<String> strarr = new ArrayList<>();
        while(read.hasNext()){
            String line = read.nextLine().trim();
            //System.out.println(line);
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
    		ArrayList<Edge> edges = (ArrayList)adjacencyList.get(x);
    		ArrayList<Node> nodes = new ArrayList<Node>();
    		for(int i=0;i<adjacencyList.get(x).size();i++){
    			System.out.print("Neighbors: " + edges.get(i).getTo().toString());
    		    nodes.add(edges.get(i).getTo());
    		}
    		System.out.println("");
    		return nodes;    	
    	}
    	return new ArrayList<Node>();
        //return null;
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
    	
    	//remove x from the value of any of the key nodes
    	adjacencyList.forEach((k, v)->{
    		ArrayList<Edge> edarr = (ArrayList)v;
    		for(int i=0;i<edarr.size();i++){
    		   if(edarr.get(i).getTo().equals(x)){
    			   edarr.remove(i);
    		   }
    		}
    	});
    	
    	//when node x is a key, then remove it
    	
    	if(adjacencyList.containsKey(x)){
    		System.out.println("Found node and remove it");
    		return adjacencyList.remove(x, adjacencyList.get(x));
    		
    	}
    	
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
    		if(arr.contains(x)){
    			System.out.println("Error: This edge exists already!");
    			return false;
    		}else{
    			arr.add(x);
        		return true;
    		}
    		
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
    		return adjacencyList.get(fn).remove(x);
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
    	Node nd = new Node(index);
    	if(adjacencyList.containsKey(nd)){
    		System.out.println("Found " + nd.getData() );
    	}
        return null;
    }
}
