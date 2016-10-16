package csula.cs4660.quizes;

import csula.cs4660.quizes.models.State;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyList;

/**
 * Here is your quiz entry point and your app
 */
public class App {
	
	
    private String startId = "10a5461773e8fd60940a56d2e9ef7bf4";
    private String distId = "e577aa79473673f6158cc73e0e5dc122";
    
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
        State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        System.out.println("Initial State: " + initialState);
        // to get an edge between state to its neighbor, you can call stateTransition
        System.out.println("\ntest " + Client.stateTransition(initialState.getId(), initialState.getNeighbors()[0].getId()));
        System.out.println("\ntest " + Client.stateTransition(initialState.getId(), initialState.getNeighbors()[1].getId()));
        
        
    }
    
    public Graph buildGraph(){
    	AdjacencyList graph = new AdjacencyList<>();
    }
    
    public List<Edge> BFS(Graph graph, Node source, Node dist){
    	
    	List<Node> queue = new LinkedList<Node>(); 
        List<Edge> result = new ArrayList<>();
        
        Map<Node,Node> relation = new HashMap<>();
        Map<Node,Integer> distance = new HashMap<>();
        
        
    	
    	return null;
    }
}

