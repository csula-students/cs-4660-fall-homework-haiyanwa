package csula.cs4660.graphs.searches;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Created by eric on 9/24/16.
 */
public class BFS implements SearchStrategy {
	
	private Queue<Node> queue = new LinkedList<Node>();
	private List<Edge> result = new ArrayList<>();
	private Node endTile = null;
	//Map<childNode, parentNode>
	private Map<Node,Node> relation = new HashMap<>();
	private Map<Node,Integer> distance = new HashMap<>();
	ArrayList<Node> visitlist = new ArrayList<>();
	
	public boolean visited(Node n){
		for(int i=0;i<visitlist.size();i++){
			if(visitlist.get(i).equals(n)){
				return true;
			}
		}
		
		return false;
	}
	
	public boolean visit(Node n){
		if(distance.get(n) != null ){
			return true;
		}
		return false;
	}
	
    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	
    	
    	//AdjacencyList
    	distance.put(source, 0);
    	queue.add(source);
    	
    	while(!queue.isEmpty()){
    		Node current = queue.poll();
    		
    		for(Node n : graph.neighbors(current)){
    			
    			visitlist.add(n);
    			
    			int dis = distance.get(current) + graph.distance(current, n);
    			//count distance from source
    			distance.put(n, dis);
    			relation.put(n, current);
    			if(n.equals(dist)){
    				endTile = n;
    				//break;
    			}
    			
    			queue.add(n);
    		}
    		
    	}
    	while(relation.get(endTile) != null){
    		Node parent = relation.get(endTile);
    		result.add(new Edge(parent, endTile, graph.distance(parent,endTile)));
    	}
    	
        return null;
    }
}
