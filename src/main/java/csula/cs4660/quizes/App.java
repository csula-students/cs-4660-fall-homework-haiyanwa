package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.Event;
import csula.cs4660.quizes.models.State;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyList;

/**
 * Here is your quiz entry point and your app
 */
public class App {
	
    private static String startId = "10a5461773e8fd60940a56d2e9ef7bf4";
    private static String distId = "e577aa79473673f6158cc73e0e5dc122";
    
    public static void main(String[] args) {
        // to get a state, you can simply call `Client.getState with the id`
        State initialState = Client.getState("10a5461773e8fd60940a56d2e9ef7bf4").get();
        System.out.println("Initial State: " + initialState);
        // to get an edge between state to its neighbor, you can call stateTransition
        System.out.println("\ntest " + Client.stateTransition(initialState.getId(), initialState.getNeighbors()[2].getId()));
        System.out.println("\ntest " + Client.stateTransition(initialState.getId(), initialState.getNeighbors()[3].getId()));
        
        /**State state = initialState;
        String init_id = state.getId();
        System.out.println(state.getId());
        System.out.println(state.getLocation());
        State neighbors[] = state.getNeighbors();
        for(int i=0;i<neighbors.length;i++){
        	System.out.println("neighbor: " + neighbors[i].getId());
        	System.out.println("          " + neighbors[i].getLocation());
        	
        	DTO act = getAction(init_id,neighbors[i].getId());
        	if(act != null){
        		System.out.println("action: " + act.getAction());
        		Event evt = act.getEvent();
        		System.out.println("event: " + evt.getName() + " : " + evt.getEffect());
        	}
        	
        	System.out.println();
        }*/
        
        List<Edge> result = BFS(startId, distId);
        Iterator<Edge> iterator = result.iterator();
    	while(iterator.hasNext()){
    		Edge ed = iterator.next();
    		Node from = ed.getFrom();
    		Node to = ed.getTo();
    		State sFrom = (State)from.getData();
    		State sTo = (State)to.getData();
    		int cost = ed.getValue();
    		System.out.println("id: " + sFrom.getId() + "loc: " + sFrom.getLocation() + " cost: "  + cost );
    	}
        
    }
    
    public static State getState(String id){
    	State state = Client.getState(id).get();
    	return state;
    }
    public static DTO getAction(String fromId, String toId){
    	
    	Optional<DTO> opt =  Client.stateTransition(fromId, toId); 
    	if(opt.isPresent()){
    		DTO dto = opt.get();
    		System.out.println("got DTO");
    		return dto;
    	}
    	return null;
    }
    
    public static boolean visited(Node s, Map visit){
    	if(visit.get(s) != null){
    		return true;
    	}
    	
    	return false;
    }
    
    public static List<Edge> BFS(String start, String dist){
    	AdjacencyList graph = new AdjacencyList();
    	Queue<Node> queue = new LinkedList<Node>();
        List<Edge> result = new ArrayList<>();
        Map<Node, Integer> visit = new HashMap<>();
        
        Map<Node,Node> relations = new HashMap<>();
        Map<Node,Integer> distances = new HashMap<>();
        Map<Node[],Integer> edges = new HashMap<>();
        
        Node endNode= null;
        
        State initState = getState(start);
        Node initNode = new Node(initState);
        queue.add(initNode);
        visit.put(initNode, 1);
        distances.put(initNode, 0);
        System.out.println("start " + initState.getId() + " " + initState.getLocation());
        
        while(!queue.isEmpty()){
        	Node curNode = queue.poll();
        	State curState = (State)curNode.getData();
        	State neighbors[] = curState.getNeighbors();
        	for(int i=0;i<neighbors.length;i++){
        		State neibState = neighbors[i];
        		Node neibNode = new Node(neibState);
        		System.out.println("next state: " + neibState.getId() + " " + neibState.getLocation().getName());
        		if(!visited(neibNode, visit)){
        			DTO act = getAction(curState.getId(), neibState.getId());
        			int dis = 0;
        			if(act != null){
        				Event event = act.getEvent();
        				dis = distances.get(curNode) + event.getEffect();
        			}else{
        				dis = distances.get(curNode) + 1;
        				System.out.println(neibState + "has no effect");
        			}
        			distances.put(neibNode,dis);
        			relations.put(neibNode, curNode);
        			Node[] pair = {curNode,neibNode};
        			edges.put(pair, act.getEvent().getEffect());
        			
        			if(neibState.getId().equals(distId)){
        				endNode = neibNode;
        			}
        			queue.add(neibNode);
        		}
        	}
        }
        
        while(relations.get(endNode) != null){
    		Node parent = relations.get(endNode);
    		Node[] pair = {parent,endNode};
    		result.add(new Edge(parent, endNode, edges.get(pair)));
    		//
    		endNode = parent;
    	}
        if(result != null){
    		
    		Collections.reverse(result);
    		return result;
    	}
    	
    	return null;
    }
}
