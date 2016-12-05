package Tron;
import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class TronBattle {
    
    private static String dir_self = "";
    private static String dir_other = "";
    private static int width = 30;
    private static int height = 20;

	private static Set<Node> visit = new HashSet<Node>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int self_X=0;
        int self_Y=0;
        int o_X=0;
        int o_Y=0;
        
        AdjacencyList graph = new AdjacencyList();
        initGraph(30, 20, graph);
        int count_self =0;
        int count_other=0;
        
        Node root = new Node(new Tile(5,17,true));
        Tile rt = (Tile)graph.getNode(root).get().getData();
        System.out.println("found root" + rt.getX() + " " + rt.getY() + " ");
        List<Node> neighbor = graph.neighbors(root);
        System.out.println("neighbors size: " + neighbor.size());
        for(Node n : neighbor){
        	Tile t = (Tile)graph.getNode(n).get().getData();
        	System.out.println("neighbors: " + t.getX() + " " + t.getY() + " " );
        }

		visit.add(root);
        //printGraph(graph, root);
        /**
        // game loop
        while (true) {
            int N = in.nextInt(); // total number of players (2 to 4).
            int P = in.nextInt(); // your player number (0 to 3).
            for (int i = 0; i < N; i++) {
                int X0 = in.nextInt(); // starting X coordinate of lightcycle (or -1)
                int Y0 = in.nextInt(); // starting Y coordinate of lightcycle (or -1)
                int X1 = in.nextInt(); // starting X coordinate of lightcycle (can be the same as X0 if you play before this player)
                int Y1 = in.nextInt(); // starting Y coordinate of lightcycle (can be the same as Y0 if you play before this player)
                
                if(i == P){
                    self_X = X1;   //self position x
                    self_Y = Y1;   //self position y
                    
                    count_self++;
                    Tile tile = new Tile(X1,Y1,true);
                    Node current = new Node(tile);
                    
                    
                }else{
                    o_X = X1;  //the other player's position x
                    o_Y = Y1;  //the other player's position y
                    
                    Tile tile = new Tile(X1,Y1,true);
                    Node current = new Node(tile);
                    
                    
                    //remove edges to that node, both direction
                    List neibs = graph.neighbors(current);
                    System.err.println("Debug remove other " + neibs.size());
                    Iterator<Node> iterator = neibs.iterator();
                    while(iterator.hasNext()){
                        Node n = iterator.next();
                        Edge ed = new Edge(n,current,1);
                        graph.removeEdge(ed);
                        Edge ed_o = new Edge(current,n,1);
                        graph.removeEdge(ed_o);
                    }
                }
            }
            
            

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println("Debug I'm the " + P );
            System.err.println("Debug " + self_X + " " + self_Y);
            System.err.println("Debug " + o_X + " " + o_Y);
            
            if(P==0 && count_self==0){
                String move = startFirst(self_X,self_Y, graph);
            }
            //Node source = new Node(new Tile(self_X,self_Y,true));
            //Node next = getBestMove(graph,source,3,Integer.MIN_VALUE,Integer.MAX_VALUE,true,0);
            //Edge edge = new Edge(source, next,1);
            ////String move = converEdgesToAction(nextEdge);
            Edge edge =floodfill(self_X,self_Y, graph);
            
            Tile tf = (Tile) edge.getFrom().getData();
            Tile tt = (Tile) edge.getTo().getData();
            System.err.println("Debug flood from" + tf.getX() + " " + tf.getY());
            System.err.println("Debug flood to" + tt.getX() + " " + tt.getY());
           
            
            //remove edges to current position so won't come back again
            
            Node current = new Node(new Tile(self_X,self_Y,true));
            List neibs = graph.neighbors(current);
            Iterator<Node> iterator = neibs.iterator();
            while(iterator.hasNext()){
                Node child = iterator.next();
                Tile t = (Tile)child.getData();
                System.err.println("Debug child" + t.getX() + " " + t.getY());
                Edge edge_c = new Edge(child,current,1);
                graph.removeEdge(edge_c);
                System.err.println("Debug remove " + t.getX() + " " + t.getY() + " to " + self_X + " " + self_Y );
            }
            
            String move = converEdgesToAction(edge);
            dir_self = move;
            System.out.println(move);
            
        }*/
    }
    public static void printGraph(AdjacencyList graph, Node source){
		
		Node root = graph.getNode(source).get();
		System.err.println("debug root: " + ((Tile)root.getData()).getX() + "," + ((Tile)root.getData()).getY() );
		
		List<Node> children = graph.neighbors(root);
		System.err.println("Debug children size: " + children.size());
		if(children.size() > 0){
			for(Node<Tile> n : children){
					if(!visit.contains(n)){
						visit.add(n);
						printGraph(graph,n);
					}
			}
		}
		
	}
    public static void initGraph(int x, int y, AdjacencyList graph){
        
        for(int j=0;j< y;j++){
            for(int i=0;i< x;i++){
                
                Tile tile = new Tile(i,j,true);
                Node current = new Node(tile);
                graph.addNode(current);
                
                if(i == 0 ){
                    addRightNode(i,j,graph,current);
                       
                    if(j==0){
                     //right, down
                        addDownNode(i,j,graph,current);
                    }
                      
                    if(j>0 && j < y-1){
                    // up, down, right
                        addUpNode(i,j,graph,current);
                        addDownNode(i,j,graph,current);
                    }
                      
                    if(j== y-1){
                     //right, up
                        addUpNode(i,j,graph,current);
                    }
                     
                }
                if(i < x-1 && i > 0 ){
                    //left,right
                    addLeftNode(i,j,graph,current);
                    addRightNode(i,j,graph,current);
                    
                    if(j==0) {
                        //left, right, down
                        addDownNode(i,j,graph,current);
                    }
                     
                    if(j>0 && j < y-1){
                        // left, right, up, down
                        addUpNode(i,j,graph,current);
                        addDownNode(i,j,graph,current);
                    }
                     
                    if(j== y-1){
                        //left, right, up
                        addUpNode(i,j,graph,current);
                    }
                }
                if(i == x-1){
                    addLeftNode(i,j,graph,current);
                    
                    if(j==0){
                        //left, down
                        addDownNode(i,j,graph,current);
                    }
                    if(j >0 && j < y-1){
                        //up, down, left
                        addUpNode(i,j,graph,current);
                        addDownNode(i,j,graph,current);
                    }
                    if(j == y-1){
                        //left, up
                        addUpNode(i,j,graph,current);
                    }
               }
            }
        }
    }
    public static void addUpNode(int x, int y, AdjacencyList graph, Node from){
        Tile tile = new Tile(x,y-1,true);
        Node node = new Node(tile);
        graph.addNode(node);
        graph.addEdge(new Edge(from, node, 1));
    }
    
    public static void addDownNode(int x, int y, AdjacencyList graph, Node from){
        Tile tile = new Tile(x,y+1,true);
        Node node = new Node(tile);
        graph.addNode(node);
        graph.addEdge(new Edge(from, node, 1));
    }
    public static void addLeftNode(int x, int y, AdjacencyList graph, Node from){
        Tile tile = new Tile(x-1,y,true);
        Node node = new Node(tile);
        graph.addNode(node);
        graph.addEdge(new Edge(from, node, 1));
    }
    public static void addRightNode(int x, int y, AdjacencyList graph, Node from){
        Tile tile = new Tile(x+1,y,true);
        Node node = new Node(tile);
        graph.addNode(node);
        graph.addEdge(new Edge(from, node, 1));
    }
    
    public static String startFirst(int x, int y, AdjacencyList graph){
        
        //find farther way to go
        Map<String,Integer> move = new HashMap<>();
        move.put("UP", y);
        move.put("LEFT", x);
        move.put("DOWN", height-y);
        move.put("RIGHT", width-x);
        int max = 0;
        String bestMove = "";
        for(String m : move.keySet()){
            if(move.get(m) > max){
                bestMove = m;
            }
        }
        return bestMove;
    }
    
    public static int findDepth(Node source,AdjacencyList graph, int count) {
    	
    	Node node = graph.getNode(source).get();
    	if(graph.neighbors(node).size() < 1){
    		return count;
    	}else{
    	
    		for(Node n : graph.neighbors(node)){
    			int depth = findDepth(n, graph, count+1);
    			return depth;
    		}
    	}
    	return 0;
    }
    
    public static Edge floodfill(int x, int y, AdjacencyList graph){
        
        ArrayList edges = new ArrayList<>();
        
        //check surrounding and go south,west,east,north if the neighbour nodes are valid

        Node current = new Node(new Tile(x,y,true));
        List neibs = graph.neighbors(current);
        Iterator<Node> iterator = neibs.iterator();
        while(iterator.hasNext()){
            Node nextNode = iterator.next();
            Tile tile = (Tile)nextNode.getData();
                Edge edge = new Edge(current,nextNode,1);
                edges.add(edge);

        }
        //which next node to choose?
        if(edges.size() == 1){
            return (Edge)edges.get(0);
        }else if(edges.size() > 1){
            //find previous direction
            String dir = dir_self;
            for(int i=0;i<edges.size();i++){
                Edge ed = (Edge)edges.get(i);
                String m = converEdgesToAction(ed);
                if(m.equals(dir)){
                    return ed;
                }
            }
            //if cannot continue the same direction then pick anyone
            return (Edge) edges.get(0);
            
        }
        
        return null;
    }
    
    
    public static Node getBestMove(AdjacencyList graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max, int count) {
        // TODO: implement your alpha beta pruning algorithm here
        
        int bestValue = alphaBeta(graph,source,depth,alpha,beta,max,count);
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
    
    public static int alphaBeta(AdjacencyList graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max, int count){
        
        Node<MiniMaxState> current = graph.getNode(source).get();
        if(graph.neighbors(current).size() < 1 || depth == 0){
            //System.out.println("leaf: " + count);
            return count;
        }
        
        if(max == true){
            
            Integer value = Integer.MIN_VALUE;
            
            for(Node<MiniMaxState> n : graph.neighbors(current)){
                value = Math.max(value, alphaBeta(graph, n, depth-1, alpha, beta, false, count+1));
                //update graph
                current.getData().setValue(value);
                if(value >= beta){
                    return value;
                }
                alpha = Math.max(alpha, value);
            }
            
            return value;
            
        }else{
            
            Integer value = Integer.MAX_VALUE;
            
            for(Node<MiniMaxState> n : graph.neighbors(current)){
                value = Math.min(value, alphaBeta(graph,n,depth-1, alpha,beta,true,count+1));
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
    
    public static String converEdgesToAction(Edge edge) {
        // TODO: convert a list of edges to a list of action
        String move = "";
        if(edge == null){
            return "edge is nulls";
        }
        
        Tile t_from = (Tile) edge.getFrom().getData();
        Tile t_to = (Tile) edge.getTo().getData();
        
        int x_from = t_from.getX();
        int y_from = t_from.getY();
        int x_to = t_to.getX();
        int y_to = t_to.getY();
        
        if(x_from - x_to == -1){
            move += "RIGHT";
        }else if(x_from - x_to == 1){
            move += "LEFT";
        }else if(y_from - y_to == -1){
            move += "DOWN";
        }else if(y_from - y_to == 1){
            move += "UP";
        }
        if(!move.equals("")){
            return move;
        }
        return "";
    }
}

class AdjacencyList {
    
    private Map<Node, Collection<Edge>> adjacencyList;
    
    public AdjacencyList() {
        
        adjacencyList = new HashMap<Node, Collection<Edge>>();
    }
    
    public boolean addNode(Node x) {
        //if x is not a key, then add it as a key, while value as a empty ArrayList
        if(adjacencyList != null){
            if(!adjacencyList.containsKey(x)){
                ArrayList<Edge> arr = new ArrayList<Edge>();
                adjacencyList.put(x, arr);
                return true;
            }else{
                //System.out.println("Error: This node exists already!");
            }
        }else{
            ArrayList<Edge> arr = new ArrayList<Edge>();
            adjacencyList.put(x, arr);
            return true;
        }
        
        return false;
    }
    
    public boolean addEdge(Edge x) {
        
        //add  toNode to FromNode's value list, FromNode should exist already
        Node fn = x.getFrom();
        Node tn = x.getTo();
        if(adjacencyList.containsKey(fn)){
            //add the edge to the value ArrayList
            ArrayList<Edge> arr = (ArrayList) adjacencyList.get(fn);
            if(arr.contains(x)){
                //System.out.println("Error: This edge exists already!");
                return false;
            }else{
                arr.add(x);
                return true;
            }
            
        }else{
            //System.out.println("Error: fromNode doesn't exist!");
        }
        return false;
    }
    
    public boolean adjacent(Node x, Node y) {
        if(adjacencyList.containsKey(x)){
            ArrayList<Edge> arr = (ArrayList)adjacencyList.get(x);
            for(int i=0;i<arr.size();i++){
                if(arr.get(i).getTo().equals(y)){
                    return true;
                }
            }
        }
        return false;
    }
    
    public List<Node> neighbors(Node x) {
        if(adjacencyList.containsKey(x)){
            ArrayList<Edge> edges = (ArrayList)adjacencyList.get(x);
            ArrayList<Node> nodes = new ArrayList<Node>();
            for(int i=0;i<adjacencyList.get(x).size();i++){
                //System.out.print("Neighbors: " + edges.get(i).getTo().toString());
                nodes.add(edges.get(i).getTo());
            }
            //System.out.println("");
            return nodes;       
        }
        return new ArrayList<Node>();
        //return null;
    }
    
    public boolean removeNode(Node x) {
        
        //remove x from the value of any of the key nodes
        adjacencyList.forEach((k, v)->{
            ArrayList<Edge> edarr = (ArrayList)v;
            for(int i=0;i<edarr.size();i++){
               if(edarr.get(i).getTo().equals(x)){
                   edarr.remove(i);
               }
            }
        });
        
        //when node x is a key, then remove it
        if(adjacencyList.containsKey(x)){
            //System.out.println("Found node and remove it");
            return adjacencyList.remove(x, adjacencyList.get(x));
            
        }
        
        return false;
    }
    public boolean removeEdge(Edge x) {

        Node fn = x.getFrom();
        Node tn = x.getTo();
        //if fromNode or toNode not existing, then gives error message
        if(!adjacencyList.containsKey(fn) || !adjacencyList.containsKey(tn)){
            //System.out.println("Error: fromNode or toNode does not exist...");
            return false;
        }       
        //fromNode exists
        if(adjacencyList.containsKey(fn)){
            return adjacencyList.get(fn).remove(x);
        }
        return false;
    }
    
    public Optional<Node> getNode(Node node) {
        Iterator<Node> iterator = adjacencyList.keySet().iterator();
        Optional<Node> result = Optional.empty();
        while (iterator.hasNext()) {
            Node next = iterator.next();
            if (next.equals(node)) {
                result = Optional.of(next);
            }
        }
        return result;
    }
    
}

class Tile {
    private final int x;
    private final int y;
    private final Boolean type;

    public Tile(int x, int y, Boolean type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Boolean getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tile)) return false;

        Tile tile = (Tile) o;

        if (getX() != tile.getX()) return false;
        if (getY() != tile.getY()) return false;
        return getType() != null ? getType().equals(tile.getType()) : tile.getType() == null;

    }

    @Override
    public int hashCode() {
        int result = getX();
        result = 31 * result + getY();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
}

class Edge {
    private Node from;
    private Node to;
    private int value;

    public Edge(Node from, Node to, int value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public Node getFrom() {
        return from;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getTo() {
        return to;
    }

    public void setTo(Node to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge = (Edge) o;

        if (getValue() != edge.getValue()) return false;
        if (getFrom() != null ? !getFrom().equals(edge.getFrom()) : edge.getFrom() != null)
            return false;
        return !(getTo() != null ? !getTo().equals(edge.getTo()) : edge.getTo() != null);

    }

    @Override
    public String toString() {
        return "Edge{" +
            "from=" + from +
            ", to=" + to +
            ", value=" + value +
            '}';
    }

    @Override
    public int hashCode() {
        int result = getFrom() != null ? getFrom().hashCode() : 0;
        result = 31 * result + (getTo() != null ? getTo().hashCode() : 0);
        result = 31 * result + getValue();
        return result;
    }
}

class Node<T> {
    private final T data;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Node{" +
            "data=" + data +
            '}';
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;

        Node<?> node = (Node<?>) o;

        return getData() != null ? getData().equals(node.getData()) : node.getData() == null;

    }

    public int hashCode() {
        return getData() != null ? getData().hashCode() : 0;
    }
}

class MiniMaxState {
    private final int index;
    private  int value;

    public MiniMaxState(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public int getValue() {
        return value;
    }
    
    public void setValue(int value){
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MiniMaxState)) return false;

        MiniMaxState that = (MiniMaxState) o;

        return getIndex() == that.getIndex();

    }

    @Override
    public int hashCode() {
        return getIndex();
    }
}