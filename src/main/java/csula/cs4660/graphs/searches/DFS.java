package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eric on 9/24/16.
 */
public class DFS implements SearchStrategy {
	
	Map<Node, Integer> visit = new HashMap<>();
	
	public boolean visited(Node n){
		if(visit.get(n) != null ){
			return true;
		}
		return false;
	}
	
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	
    	Node current = source;
    	
    	List<Edge> list = search_r(graph, current, dist);
    	if(list != null){
    		Collections.reverse(list);
    		return list;
    	}
    	
        return null;
    }
    
    public List<Edge> search_r(Graph graph, Node source, Node dist){
		
		Node current = source;
    	visit.put(current, 1);
    	
    	if(graph.neighbors(current) != null){
    		
    		List<Node> children = graph.neighbors(current);
    		for(Node n : children){
    			if(!visited(n)){
    				if(!n.equals(dist)){
    					List<Edge> list = search_r(graph, n, dist);
    					if(list != null){
    						list.add(new Edge(current,n,graph.distance(current, n)));
    					}else{
    						continue;
    					}
    					return list;
    				}else{
    					List<Edge> result = new ArrayList<>();
    					result.add(new Edge(current, n,graph.distance(current, n)));
    					return result;
    				}
    			}
    		}
    	}
		
		return null;
	}
    
}
