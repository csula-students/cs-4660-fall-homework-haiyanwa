package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by eric on 9/24/16.
 */
public class DijkstraSearch implements SearchStrategy {
	//Map<child, parent> 
	private Map<Node, Node> relation = new HashMap<>();
	private Map<Node, Integer> distance = new HashMap<>();
	private List<Node> pri_queue = new LinkedList<Node>(); 
	private List<Edge> result = new LinkedList<>();
	private Map<Node, Integer> visit = new HashMap<>(); 
	
	public boolean visited(Node n){
		if(visit.get(n) != null){
			return true;
		}
		return false;
	}
	
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	distance.put(source, 0);
    	pri_queue.add(source);
    	while (!pri_queue.isEmpty()){
    		//get the smallest distance
    		Collections.sort(pri_queue, new Comparator<Node>(){  //sort by distance
    			@Override
    			public int compare(Node n1, Node n2){
    				return distance.get(n1).compareTo(distance.get(n2));
    			}
    		});
    		//get the first one which should be the smallest
    		Node current = pri_queue.get(0);
    		
    		for(Node n : graph.neighbors(current)){
    			
    			int d = distance.get(current) + graph.distance(current, n);
    			if(distance.get(n) != null){
    				if(d < distance.get(n)){
    					distance.put(n, d);
    					relation.put(n, current);
    				}
    			}else{
    				distance.put(n, d);
    				relation.put(n, current);
    			}
    			if(!visited(n)){
    				pri_queue.add(n);
    				visit.put(n, 1);
    			}
    		}
    		pri_queue.remove(0);
    	}
    	
    	//backtrack
    	//put the shortest path from source to dist in to result
    	Node child = dist;
    	while(!child.equals(source)){
    		Node parent = relation.get(child);
    		if(parent != null){
    			Edge ed = new Edge(parent,child,graph.distance(parent,child));
    			result.add(ed);
    			
    			child = parent;
    		}else{
    			break;
    		}
    	}
    	
    	//reverse the list
    	Collections.reverse(result);
    	
    	if(result != null){
    		return result;
    	}
    	
        return null;
    }
}
