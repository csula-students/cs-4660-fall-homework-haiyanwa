package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eric on 9/24/16.
 */
public class DFS implements SearchStrategy {
	
	Map<Node, Integer> visited = new HashMap<>();
	// child-parent relation
	Map<Node, Node> relation = new HashMap<>();
	List<Edge> result = new ArrayList<>();
	
	public boolean visited(Node n){
		if(relation.get(n) != null ){
			return true;
		}
		return false;
	}
	
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	
    	Node current = source;
    	
    	List<Edge> list = search(graph, current, relation, dist);
    	if(list != null){
    		return list;
    	}
    	
        return null;
    }
    
    public List<Edge> search(Graph graph, Node source, Map relation, Node dist){
		
		Node current = source;
    	visited.put(current, 1);
    	

    	if(graph.neighbors(current) != null){
    		List<Node> children = graph.neighbors(current);
    		for(Node n : children){
    			if(!visited(n)){
    				relation.put(n, current);
    				if(!n.equals(dist)){
    					
    					List<Edge> list = search(graph, n, relation, dist);
    					if(list != null){
    						list.add(new Edge(n,current,1));
    					}
    					return list;
    				}else{
    					result.add(new Edge(n, current,1));
    					return result;
    				}
    			}
    		}
    	}
		
		return null;
	}
    
}
