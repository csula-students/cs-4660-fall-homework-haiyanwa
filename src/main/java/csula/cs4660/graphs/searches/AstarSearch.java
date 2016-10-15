package csula.cs4660.graphs.searches;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Perform A* search
 */
public class AstarSearch implements SearchStrategy {
	private Map<Node,Node> relation = new HashMap<>();
	private List<Node> pri_queue = new LinkedList<Node>();
	private Map<Node,Integer> distance = new HashMap<>();
	private ArrayList<Edge> result = new ArrayList<>();
	private Map<Node, Integer> visit = new HashMap<>(); 
	
	private static final float D = 1;

    @Override
    public List<Edge> search(Graph graph, Node source, Node dist) {
    	
    	pri_queue.add(source);
    	distance.put(source,0);
    	visit.put(source, 1);
    	System.out.print("source ");
    	printTile(source);

		//Tile goal = (Tile) dist.getData();
		
    	while(!pri_queue.isEmpty()){
    		
    		//sort queue
    		Collections.sort(pri_queue, new Comparator<Node>(){  //sort by distance
    			@Override
    			public int compare(Node n1, Node n2){
    				return distance.get(n1).compareTo(distance.get(n2));
    			}
    		});
    		
    		Node current = pri_queue.get(0);
    		if(current.equals(dist)){
    			return find_route(graph,source,dist);
    		}
    		if(graph.neighbors(current) != null){
    			//System.out.println("Found neibours");
    			for(Node n : graph.neighbors(current)){
    				int ds = distance.get(current) + graph.distance(current, n);  //distance from source
					int dg = heuristic(n,dist); //to goal
					int dist_h = ds + dg; //together
    				if((distance.get(n) != null)){
    					if((dist_h) < distance.get(n)){
    						distance.put(n, dist_h);  //update distance
    						relation.put(n, current);  //update relationship
    					}
    				}else{
    					distance.put(n, dist_h);
    					relation.put(n, current);
    				}
    				if(!visited(n)){
    					System.out.print("queue Node: ");
    					printTile(n);
        				pri_queue.add(n);
        				visit.put(n, 1);
        			}
    			}
    			//dequeue
    		    System.out.print("remove: ");
    		    printTile(current);
    			pri_queue.remove(current);
    		}
    	}
        return null;
    }
    
    public ArrayList<Edge> find_route(Graph graph, Node source, Node dist){
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
    		for(Edge ed : result){
    			Tile f = (Tile)ed.getFrom().getData();
    			Tile t = (Tile)ed.getTo().getData();
    			System.out.println("from " + f.getX() + "," + f.getY() + " To " + t.getX() + "," + t.getY());
    		}
    		return result;
    	}
    	return result;
    }
    
    public int heuristic(Node node, Node goal){
		int dist_h = 0;
		Tile n = (Tile) node.getData();
		Tile g = (Tile) goal.getData();
		
		int dx = Math.abs(n.getX() - g.getX());
		int dy = Math.abs(n.getY() - g.getY());
		dist_h = (int)D * (dx + dy);
	
		return dist_h;
	}
    public boolean visited(Node n){
		if(visit.get(n) != null){
			return true;
		}
		return false;
	}
    public void printTile(Node node){
    	int x = ((Tile)node.getData()).getX();
    	int y = ((Tile)node.getData()).getY();
    	System.out.println("node = " + x + " " + y);
    }
}
