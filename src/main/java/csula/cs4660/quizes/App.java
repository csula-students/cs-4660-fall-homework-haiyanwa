package csula.cs4660.quizes;

import csula.cs4660.quizes.models.DTO;
import csula.cs4660.quizes.models.Event;
import csula.cs4660.quizes.models.State;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.representations.AdjacencyList;
import csula.cs4660.graphs.representations.AdjacencyMatrix;

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
        
        List<Edge> result_BFS = BFS(startId, distId);
        List<Edge> result_Dij = DijstraSearch(startId, distId);
        
        System.out.println("BFS:");
        printPath(result_BFS);
        System.out.println("DijstraSearch:");
        printPath(result_Dij);
    }
    
    public static void printPath(List path){
        if(path != null){
        	
        }else{
        	System.out.println("result is null");
        }
        Iterator<Edge> iterator = path.iterator();
    	while(iterator.hasNext()){
    		Edge ed = iterator.next();
    		Node from = ed.getFrom();
    		Node to = ed.getTo();
    		State sFrom = (State)from.getData();
    		State sTo = (State)to.getData();
    		int cost = ed.getValue();
    		System.out.println("from: " + sFrom.getLocation().getName() + " to: " + sTo.getLocation().getName() + " cost: "  + cost );
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
    		return dto;
    	}
    	return null;
    }
    
    public static boolean visited(String s, Map visit){
    	if(visit.get(s) != null){
    		return true;
    	}
    	
    	return false;
    }
    
    public static List<Edge> BFS(String start, String dist){
    	
    	Queue<State> queue = new LinkedList<State>();
        List<Edge> result = new ArrayList<>();
        Set visit = new HashSet<>();
        
        Map<State,State> relations = new HashMap<>();
        Map<State,Integer> distances = new HashMap<>();
        Map<String,Integer> edges = new HashMap<>();
        
        State endState= null;
        
        State initState = getState(start);
        queue.add(initState);
        visit.add(initState);
        System.out.println("start " + initState);
        int loop = 0;
        
        try{
        	PrintWriter writer = new PrintWriter("src/main/java/csula/cs4660/quizes/log", "UTF-8");
        	
        	while(!queue.isEmpty()){
            	State curState = queue.poll();
            	State neighbors[] = curState.getNeighbors();
            	for(int i=0;i<neighbors.length;i++){
            		String nid = neighbors[i].getId();
            		State nState = getState(nid);
            		//Node nNode = new Node(nState);
            		
            		if(!visit.contains(nState)){
            			DTO act = getAction(curState.getId(), nState.getId());
            			String key = curState.getId() + "" + nState.getId();
            			edges.put(key, act.getEvent().getEffect());
            			//System.out.println("add edges: " + act.getEvent().getEffect());
            			if(nid.equals(dist)){
            				endState = nState;
            				//System.out.println("Found end node!!!!" + loop + " "  + dist);
            				//writer.println("Found endNode!!!" + loop + " " + dist);
            				relations.put(nState, curState);
            				return getPath(endState,relations,edges);
            			}
            			relations.put(nState, curState);
            			visit.add(nState);
        
            			queue.add(nState);
            		}
            	}
            }
        	
        	writer.close();
        }catch(IOException e){
        	System.out.println(e.getMessage());
        	
        }
        return null;
    }
    
    public static List<Edge> DijstraSearch(String start, String dist){
    	
    	List<State> pri_queue = new LinkedList<State>();
        List<Edge> result = new ArrayList<>();
        Set visit = new HashSet<>();
        
        Map<State,State> relations = new HashMap<>();
        Map<State,Integer> distances = new HashMap<>();
        Map<String,Integer> edges = new HashMap<>();
        
        State endState= null;
        
        State initState = getState(start);
        //Node initNode = new Node(initState);
        pri_queue.add(initState);
        visit.add(initState);
        distances.put(initState,0);
        
        int loop = 0;
        while(!pri_queue.isEmpty()){
        	loop++;
        	Collections.sort(pri_queue, new Comparator<State>(){  //sort by distance
    			@Override
    			public int compare(State n1, State n2){
    				return distances.get(n1).compareTo(distances.get(n2));
    			}
    		});
        	
        	State curState = pri_queue.get(0);
        	
        	State neighbors[] = curState.getNeighbors();
        	for(int i=0;i<neighbors.length;i++){
        		String nid = neighbors[i].getId();
        		State nState = getState(nid);
        		
        		if(!visit.contains(nState)){
        			
        			int d = distances.get(curState) + 1;
        			if(distances.get(nState) != null){
        				if(d < distances.get(nState)){
        					distances.put(nState, d);
        				}
        			}else{
        				distances.put(nState, d);
        			}
        			
        			DTO act = getAction(curState.getId(), nState.getId());
        			String key = curState.getId() + "" + nState.getId();
        			edges.put(key, act.getEvent().getEffect());
        			
        			if(nid.equals(dist)){
        				endState = nState;
        				//System.out.println("Found end node!!!!" + loop + " "  + dist);
        				relations.put(nState, curState);
        				return getPath(endState,relations,edges);
        			}
        			relations.put(nState, curState);
        			visit.add(nState);
    
        			pri_queue.add(nState);
        		}
        		
        	}
        	pri_queue.remove(0);
        }
        
        return null;
    }
    
    public static List<Edge> getPath(State endState, Map<State,State> relations, Map<String,Integer> edges){
    	ArrayList<Edge> result = new ArrayList<>();
    	while(relations.get(endState) != null){
        	//System.out.println("endNode relation" + endState);
    		State parent = relations.get(endState);
    		//System.out.println("parent " + parent);
    		String key = parent.getId()+ "" + endState.getId();
    		int cost=999;
    		if(edges.containsKey(key)){
    			cost = edges.get(key);
    		}else{
    			System.out.println("edges is null");
    		}
    		result.add(new Edge(new Node(parent), new Node(endState), cost));
    		endState = parent;
    	}
    	
        if(result != null){
    		
    		Collections.reverse(result);
    		return result;
    	}
    	
    	return null;
    }
}


