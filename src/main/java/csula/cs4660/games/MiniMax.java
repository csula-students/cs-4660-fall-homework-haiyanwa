package csula.cs4660.games;

import java.util.List;

import csula.cs4660.games.models.MiniMaxState;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;

public class MiniMax {
    public static Node getBestMove(Graph graph, Node root, Integer depth, Boolean max) {
        // TODO: implement minimax to retrieve best move
        // NOTE: you should mutate graph and node as you traverse and update value
    	Node<MiniMaxState> node = getMiniMax(graph, root, depth, max);
    	
    	for(Node n : graph.neighbors(node)){
    		
    		Node<MiniMaxState> childNode = graph.getNode(n).get();
    		int index = ((MiniMaxState)childNode.getData()).getIndex();
    		int value = ((MiniMaxState)childNode.getData()).getValue();
    		//System.out.println("n: " + index + " " + value + " state "  + node.getData().getValue());

    		if(value == node.getData().getValue()){
    			return childNode;
    		}
    	}
        return null;
    }
    
    public static Node<MiniMaxState> getMiniMax(Graph graph, Node root, Integer depth, Boolean max){
    	
    	Node<MiniMaxState> current = graph.getNode(root).get();
    	//Node<MiniMaxState> current = root;
    	//System.out.println("current: " + current.getData().getIndex() + " " + current.getData().getValue());
    	
    	List<Node> children = graph.neighbors(current);
    	//leaf
    	if(children.size() == 0 || depth == 0 ){
    		
    		return current;
    	}
    	//max player, maximizing node value
    	if(max == true){
    		Integer bestValue = Integer.MIN_VALUE;
    		
    		for(Node<MiniMaxState> n : graph.neighbors(current)){
    			Node<MiniMaxState> nextMove = getMiniMax(graph, n, depth-1, false);
    			bestValue = Math.max(nextMove.getData().getValue(),bestValue);
    			current.getData().setValue(bestValue);
				}

    		return current;
    	//min player, minimizing value
    	}else{
    		Integer bestValue = Integer.MAX_VALUE;
    		
    		for(Node<MiniMaxState> n : graph.neighbors(current)){
    			Node<MiniMaxState> nextMove = getMiniMax(graph, n, depth-1, true);
    			bestValue = Math.min(nextMove.getData().getValue(), bestValue);
    			current.getData().setValue(bestValue);
    		}
    		
    		return current;
    	}
    }
}
