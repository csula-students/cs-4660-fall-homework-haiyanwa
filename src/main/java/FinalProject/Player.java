package FinalProject;

import java.util.*;
import java.io.*;
import java.math.*;
//newest version
/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {
    private static String dir_self = "";
    private static String dir_other = "";
    private static int width = 30;
    private static int height =20;

	private static Set<Node> visit = new HashSet<Node>();

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int self_X=0;
        int self_Y=0;
        int o_X=0;
        int o_Y=0;
        int o_X0=0;
        int o_Y0=0;
        
        AdjacencyList graph = new AdjacencyList();
        initGraph(width, height, graph);
        int count_self =0;
        int count_other=0;
        
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
                    Node current = new Node(new Tile(X1,Y1,true));
                    
                    
                }else{
                    o_X = X1;  //the other player's position x
                    o_Y = Y1;  //the other player's position y
                    Node current = new Node(new Tile(o_X,o_Y,true));
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
            
            //remember one previous location
            o_X0 = o_X;
            o_Y0 = o_Y;
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println("Debug I'm the " + P );
            System.err.println("Debug self " + self_X + " " + self_Y);
            System.err.println("Debug enemy " + o_X + " " + o_Y);
            
            //set current location as root
            Node root = graph.getNode(new Node(new Tile(self_X, self_Y,true))).get();
            Set<Node> visit = new HashSet<Node>();
            visit.add(root);
            Queue<Node> queue = new LinkedList<Node>();
        	markNodes(root,graph,0,queue,visit);
        	System.err.println("marked nodes" );
        
        	//Set<Node> visit2 = new HashSet<Node>();
        	//int d = floodfill(root, graph, 1, visit2);
        	//System.err.println("d= " + d);
        	String move = "";
        	
        	int[] dist = new int[4];
        	
        	dist[0] = getDistance(root, graph, "UP",10);
        	System.err.println("get distancd up"+ dist[0]  );
        	
        	dist[1] = getDistance(root, graph, "DOWN",10);
        	System.err.println("get distancd down"+ dist[1] );
        	
        	dist[3] = getDistance(root, graph, "LEFT",10);
        	System.err.println("get distancd left " + dist[3]  );
        	
        	long startTime = System.currentTimeMillis();
        	dist[2] = getDistance(root, graph, "RIGHT",10);
        	long stopTime = System.currentTimeMillis();
        	System.err.println("get distancd right " + dist[2]  + " " + (stopTime- startTime));
        	
        	
        	System.err.println("debug distance " + dist[0] + " " + dist[1] + " " + dist[2] + " " + dist[3]);
        	//go get bigger space towards the enemy
            int maxDist =0;
            int bestDir =0;
            for(int i=0;i<dist.length;i++){
                if(dist[i] > maxDist){
                    bestDir = i;
                }
            }
        	
        
        	int frontLine = getEnemyDistance(graph, o_X, o_Y);
        	System.err.println("frontline" + frontLine);
            
            //when starting, find the further direction
            if(count_self < 20){
                Edge edge =startFirst(self_X,self_Y, graph);
                move = convertEdgesToAction(edge);
            }
            
            //when trapped in a narraw area
            //Set<Node> visit_f = new HashSet<Node>();
            //int d = floodfill( root, graph, 1, visit_f);
            //move = convertIndexToDirection(d);
            
            //if(((dist[0] < 3) && (dist[1] < 3)) || (dist[2] < 3) && (dist[3] < 3)){
            //    System.err.println("floodfill");
            //	int d = floodfill( root, graph, 1, visit_f);
            //	move = convertIndexToDirection(d);
            
            //if near enemy then alphaBeta and enemy is reachable
            //}else if(frontLine < 5 && frontLine != 0){
            //    System.err.println("alphabeta");
            //	getBestMove(graph, root, 5, Integer.MIN_VALUE,Integer.MAX_VALUE, true);
            	
            //else go for a wider area
        	//}else{
        	 //   System.err.println("go far------------------!");
            	//move = convertIndexToDirection(bestDir);
            //}
            //enemy direction
              
             //String e_dir = getEnemyDirection(graph, o_X, o_Y, o_X0, o_Y0 );
             
            //Node source = new Node(new Tile(self_X,self_Y,true));
            //Node next = getBestMove(graph,source,3,Integer.MIN_VALUE,Integer.MAX_VALUE,true,0);
            //Edge edge = new Edge(source, next,1);
            ////String move = convertEdgesToAction(nextEdge);
            Edge edge =startFirst(self_X,self_Y, graph);
            move = convertEdgesToAction(edge);
            
            
            //remove edges to current position so won't come back again
            
            Node current = new Node(new Tile(self_X,self_Y,true));
            List neibs = graph.neighbors(current);
            Iterator<Node> iterator = neibs.iterator();
            while(iterator.hasNext()){
                Node child = iterator.next();
                Tile t = (Tile)child.getData();
                //System.err.println("Debug child" + t.getX() + " " + t.getY());
                Edge edge_c = new Edge(child,current,1);
                graph.removeEdge(edge_c);
                //System.err.println("Debug remove " + t.getX() + " " + t.getY() + " to " + self_X + " " + self_Y );
            }
            
            System.err.println("next move" + move);
            
            dir_self = move;
            System.out.println(move);
            
        }  //*/
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
    
    public static void markNodes(Node source, AdjacencyList graph, int count, Queue queue, Set visit){
    	Node node = graph.getNode(source).get();
    	node.setHeight(0); //set root hight back to 0
    	Tile t = (Tile) node.getData();
    	//System.err.println("root " + t.getX() + " " + t.getY());
    	visit.add(node);
    	queue.add(node);
    	while(!queue.isEmpty()){
    		Node current = (Node) queue.poll();
    		Tile cc = (Tile) current.getData();
        	//System.err.println("current " + cc.getX() + " " + cc.getY());
    		for(Node n : graph.neighbors(current)){
    			if(!visit.contains(n)){
    				visit.add(n);
    				Node child = graph.getNode(n).get();
    				Tile ct = (Tile) child.getData();
    				child.setHeight(current.getHeight()+ 1 );
    				//System.out.println("child " + ct.getX() + " " + ct.getY() + " h " + child.getHeight() );
    				queue.add(child);
    			}
    		}
    	}
    }
    
    /**
    public static int getDirection(Node source, AdjacencyList graph, String dir){
    	
    	Node root = graph.getNode(source).get();
        List<Node> neighbors = graph.neighbors(root);
        Tile t = (Tile) root.getData();
        System.out.println("Root "+ t.getX() + " " + t.getY() + " " + t.getType());
        Node dChild = null;
        if(dir.equals("UP")){
        	dChild = new Node(new Tile(t.getX(), t.getY()-1, true));
        }else if(dir.equals("DOWN")){
        	dChild = new Node(new Tile(t.getX(), t.getY()+1, true));
        }else if(dir.equals("LEFT")){
        	dChild = new Node(new Tile(t.getX()-1, t.getY(), true));
        }else if(dir.equals("RIGHT")){
        	dChild = new Node(new Tile(t.getX()+1, t.getY(), true));
        }
        
        Tile ct = (Tile) dChild.getData();
        System.out.println("Looking for "+ dir + " " + ct.getX() + " " + ct.getY() + " " + ct.getType());
        
        Node Child = null;
        Optional<Node> tmpChild = graph.getNode(dChild);
        if (!tmpChild.isPresent()){
        	System.out.println("tmpchild " + dirHeight);
        	return dirHeight;
        }else{
        	Child = tmpChild.get();
        	if(neighbors.contains(Child)){
    			System.out.println("Found Child");
    			//height number keeps go up then keep following that direction
    			//if the height change to smaller number or stops before a wall then return
    			
    			if(Child.getHeight() > dirHeight){
    				System.out.println("height "+ Child.getHeight() + " " + dirHeight);
    				dirHeight = Child.getHeight();
    				getDirection(Child,graph,dir);
    			
    			}else{
    				return dirHeight;
    			}
    		}
        }
        System.out.println("test");
		return dirHeight;
    }*/
    
    
    
    public static int getDistance(Node source, AdjacencyList graph, String dir, int limit){
    	
    	Node root = graph.getNode(source).get();
    	int distance = 0;
    	int count =0 ;
        while(graph.neighbors(root).size() > 0){
            if(count == limit){
                return limit;    
            }
        	Tile t = (Tile) root.getData();
        	Node dChild = null;
        	if(dir.equals("UP")){
            	dChild = new Node(new Tile(t.getX(), t.getY()-1, true));
            }else if(dir.equals("DOWN")){
            	dChild = new Node(new Tile(t.getX(), t.getY()+1, true));
            }else if(dir.equals("LEFT")){
            	dChild = new Node(new Tile(t.getX()-1, t.getY(), true));
            }else if(dir.equals("RIGHT")){
            	dChild = new Node(new Tile(t.getX()+1, t.getY(), true));
            }
        	Node Child = null;
            Optional<Node> tmpChild = graph.getNode(dChild);
            //if cannot find the node in graph then return hight
            if (!tmpChild.isPresent()){
            	return distance;
            }else{
            	Child = tmpChild.get();
            	List<Node> neighbors = graph.neighbors(root);
            	if(neighbors.contains(Child)){
        			//height number keeps go up then keep following that direction
        			//if the height change to smaller number or stops before a wall then return
        			
        			if(Child.getHeight() > distance){
        				distance = Child.getHeight();
        				//look for next child
        			    root = Child;
        			}else{
        				return distance;
        			}
        		}
            }
            count++;
        }
    	return distance;
    }
    
    //find how far the enemy is
    public static int getEnemyDistance(AdjacencyList graph, int x, int y){
    	//enemy's 4 children
    	Node position = graph.getNode(new Node(new Tile(x,y,true))).get();
    	Node[] neig = new Node[4];
    	int frontLine[] = new int[4];
    	neig[0] = new Node(new Tile(x, y-1, true)); //up
    	neig[1] = new Node(new Tile(x, y+1, true)); //down
    	neig[2] = new Node(new Tile(x+1, y, true)); //right
    	neig[3] = new Node(new Tile(x-1, y, true)); //left
    	
    	for (int i=0;i<neig.length;i++){
    		Optional<Node> tmpChild = graph.getNode(neig[i]);
    		if (tmpChild.isPresent()){
    			Node Child = tmpChild.get();
    			frontLine[i] = Child.getHeight();
    		}else{
    			frontLine[i] = -1; //means a wall
    		}
    	}
		//find minimum height
    	int distance = Integer.MAX_VALUE;
    	for(int j=0;j<frontLine.length;j++){
    		if(frontLine[j] > 0){
    			if(frontLine[j] < distance){
    				distance = frontLine[j];
    			}
    		}
    	}
    	
    	return distance;
    }
    //enemy's moving direction
    public static String getEnemyDirection(AdjacencyList graph, int x, int y, int x0, int y0 ){
    	
    	if((x - x0) < 0) return "RIGHT";
    	if((x - x0) > 0) return "LEFT";
    	if((y - y0) < 0) return "DOWN";
    	if((y - y0) > 0) return "UP";
    	
    	return null;
    }
    
    
    public static int floodfill(Node source, AdjacencyList graph, int count, Set visit) {
    	
    	Node node = graph.getNode(source).get();
    	visit.add(node);
		List<Node> neighbor = graph.neighbors(node);
    	
		//edge
    	if(neighbor.size() < 1){
    		System.out.println("return count" + count);
    		graph.getNode(node).get().setHeight(count);
    		return count;
    	}else{
    		//System.out.println("neig size" + neighbor.size());
    		int max =0;
    		int maxNode = 0;
    		Tile t = (Tile) node.getData();
    		Node up = new Node(new Tile(t.getX(), t.getY()-1, true));
    		Node down = new Node(new Tile(t.getX(), t.getY()+1, true));
    		Node right = new Node(new Tile(t.getX()+1, t.getY(), true));
    		Node left = new Node(new Tile(t.getX()-1, t.getY(), true));
    		Node[] nodes = {up,down,right,left};
    		Integer[] steps = new Integer[4];
    		//System.err.println("root " + t.getX() + " " + t.getY() + "-------------");
    		
    		for(int i=0;i<nodes.length;i++){
    			if(neighbor.contains(nodes[i])){
        			Node nChild = graph.getNode(nodes[i]).get();
        			if(!visit.contains(nChild)){
        				//System.out.println("child: ");
        				visit.add(nChild);
        				Tile ct = (Tile) nChild.getData();
        				//System.out.println("child: " + ct.getX() + " " + ct.getY() + " " + nChild.getHeight());
        				steps[i] = floodfill(nChild, graph, count+1, visit);
        				if(steps[i] > max){
        					max = steps[i];
        					//System.out.println("max: " + max);
        					maxNode = i;
        				}
        			}
        		}
    		}
    		
    		return maxNode;
    	}
    }
    
    
    
    public static Edge startFirst(int x, int y, AdjacencyList graph){
        
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
                String m = convertEdgesToAction(ed);
                if(m.equals(dir)){
                    return ed;
                }
            }
            //if cannot continue the same direction then pick anyone
            return (Edge) edges.get(0);
            
        }
        
        return null;
    }
    
    
    public static Node getBestMove(AdjacencyList graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max) {
        // TODO: implement your alpha beta pruning algorithm here
        
        int bestValue = alphaBeta(graph,source,depth,alpha,beta,max);
        for(Node n : graph.neighbors(source)){
            Node childNode = graph.getNode(n).get();
            int value = childNode.getValue();
            if(value == bestValue){
                return childNode;
            }
        }
        return null;
    }
    
    public static int getMiniMax(AdjacencyList graph, Node source, Integer depth, Boolean max){
    	
    	Node current = graph.getNode(source).get();
    	//Node<MiniMaxState> current = root;
    	//System.out.println("current: " + current.getData().getIndex() + " " + current.getData().getValue());
    	
    	List<Node> children = graph.neighbors(current);
    	//leaf
    	if(children.size() == 0 || depth == 0 ){
    		
    		return current.getHeight();
    	}
    	//max player, maximizing node value
    	//if(max == true){
    		Integer bestValue = Integer.MIN_VALUE;
    		
    		for(Node n : graph.neighbors(current)){
    			int nextMove = getMiniMax(graph, n, depth-1, true);
    			bestValue = Math.max(nextMove,bestValue);
    			current.setValue(bestValue);
				}

    		return bestValue;
    	//min player, minimizing value
    	//}
    	//eleminite min because enemy might not minimize the value
    	/**else{
    		Integer bestValue = Integer.MAX_VALUE;
    		
    		for(Node<MiniMaxState> n : graph.neighbors(current)){
    			Node<MiniMaxState> nextMove = getMiniMax(graph, n, depth-1, true);
    			bestValue = Math.min(nextMove.getData().getValue(), bestValue);
    			current.getData().setValue(bestValue);
    		}
    		
    		return bestValue;
    	}*/
    }
    
    public static int alphaBeta(AdjacencyList graph, Node source, Integer depth, Integer alpha, Integer beta, Boolean max){
        
        Node current = graph.getNode(source).get();
        if(graph.neighbors(current).size() < 1 || depth == 0){
            //System.out.println("leaf: " + count);
            return current.getHeight();
        }
        
        if(max == true){
            
            Integer value = Integer.MIN_VALUE;
            
            for(Node<MiniMaxState> n : graph.neighbors(current)){
                value = Math.max(value, alphaBeta(graph, n, depth-1, alpha, beta, false));
                //update graph
                current.setValue(value);
                if(value >= beta){
                    return value;
                }
                alpha = Math.max(alpha, value);
            }
            
            return value;
            
        }else{
            
            Integer value = Integer.MAX_VALUE;
            
            for(Node<MiniMaxState> n : graph.neighbors(current)){
                value = Math.min(value, alphaBeta(graph,n,depth-1, alpha,beta,true));
                //update graph
                current.setValue(value);
                if(value <= alpha){
                    return value;
                }
                beta = Math.min(beta, value);
            }
            
            return value;
        }
    }
    
    public static String convertEdgesToAction(Edge edge) {
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
    
    public static String convertIndexToDirection(int index){
    	String direction = "";
    	
    	if(index == 0){
        	direction = "UP";
        }else if(index == 1){
        	direction = "DOWN";
        }else if(index == 2){
        	direction = "RIGHT";
        }else if(index == 3){
        	direction = "LEFT";
        }
    	return direction;
    }
}


class AdjacencyList {
    
    private Map<Node, Collection<Edge>> adjacencyList;
    
    public AdjacencyList() {
        
        adjacencyList = new HashMap<Node, Collection<Edge>>();
    }
    //method for copying the graph
    public AdjacencyList(AdjacencyList graph) {
        
        adjacencyList = new HashMap<Node, Collection<Edge>>(graph.adjacencyList);
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
    private int height;
    private int value;

    public Node(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }
    public int getHeight(){
    	return this.height;
    }
    public void setHeight(int height){
    	this.height = height;
    }
    public int getValue(){
    	return this.value;
    }
    public void setValue(int value){
    	this.value = value;
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