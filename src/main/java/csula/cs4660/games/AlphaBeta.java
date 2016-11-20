package csula.cs4660.games;

import java.util.List;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class AlphaBeta {
    public static Node getBestMove(Graph graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max) {
        // TODO: implement your alpha beta pruning algorithm here
        
    	int bestValue = alphaBeta(graph,source,depth,alpha,beta,max);
    	for(Node<MiniMaxState> n : graph.neighbors(source)){
    		Node<MiniMaxState> childNode = graph.getNode(n).get();
    		int index = ((MiniMaxState)childNode.getData()).getIndex();
    		int value = ((MiniMaxState)childNode.getData()).getValue();
    		if(value == bestValue){
    			return childNode;
    		}
    	}
    	return null;
    }
    
    public static int alphaBeta(Graph graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max){
   	
    	Node<MiniMaxState> root = graph.getNode(source).get();
    	int value = maxValue(graph,root,alpha,beta);
    	
    	return value;
    }
    
    public static int maxValue(Graph graph, Node node, int alpha, int beta){
    	
    	Node<MiniMaxState> current = graph.getNode(node).get();
    	
    	if(graph.neighbors(current).size() < 1){
    		return current.getData().getValue();
    	}
    	Integer value = Integer.MIN_VALUE;
    	
    	for(Node<MiniMaxState> n : graph.neighbors(current)){
    		value = Math.max(value, minValue(graph, n, alpha, beta));
    		//update graph
    		current.getData().setValue(value);
    		if(value >= beta){
    			return value;
    		}
    		alpha = Math.max(alpha, value);
    	}
    	
    	return value;
    }
    
    public static int minValue(Graph graph, Node node, int alpha, int beta){
    	Node<MiniMaxState> current = graph.getNode(node).get();
    	if(graph.neighbors(current).size() < 1){
    		return current.getData().getValue();
    	}
    	Integer value = Integer.MAX_VALUE;
    	
    	for(Node<MiniMaxState> n : graph.neighbors(current)){
    		value = Math.min(value, maxValue(graph,n,alpha,beta));
    		//update graph
    		current.getData().setValue(value);
    		if(value <= alpha){
    			return value;
    		}
    		beta = Math.min(beta, value);
    	}
    	
    	return value;
    }
}
