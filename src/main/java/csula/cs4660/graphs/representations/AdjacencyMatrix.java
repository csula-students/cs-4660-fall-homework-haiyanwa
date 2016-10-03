package csula.cs4660.graphs.representations;

import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Adjacency matrix in a sense store the nodes in two dimensional array
 *
 * TODO: please fill the method body of this class
 */
public class AdjacencyMatrix implements Representation {
    private Node[] nodes;
    private int[][] adjacencyMatrix;

    public AdjacencyMatrix(File file) {
    	try{
    		ArrayList<String> arr = readFile(file);
    		Node fromNode;
    		Node toNode;
    		
    		//first record is total node number
    		try{
    			int NodeNum = Integer.parseInt(arr.get(0));
    			nodes = new Node[NodeNum];
    			adjacencyMatrix = new int[NodeNum][NodeNum];
    			
    			int node_index=0;
    			//process data for nodes
    			//read from second record
        		for(int i=1;i<arr.size();i++){
        			String s[] = arr.get(i).split(":");
        			
    				fromNode = new Node(Integer.parseInt(s[0]));
    				toNode = new Node(Integer.parseInt(s[1]));
    				
    				//System.out.println("[" + fromNode.getData() + "][" + toNode.getData() + "]" +  Integer.parseInt(s[2]));
        			
        			//node not exists yet, then add to nodes
        			int fnode_index = GetNodeIndex(fromNode);
        			int tnode_index = GetNodeIndex(toNode);
        			if( fnode_index == -1){
        				//add the new node to the end of nodes
        				nodes[node_index] = new Node(fromNode.getData());
        				node_index++;
        			}
        			if( tnode_index == -1){
        				//add the new node to the end of nodes
        				nodes[node_index] = new Node(toNode.getData());
        				node_index++;
        			}
        		}
        		//for(int i=0;i<nodes.length;i++){
        			//System.out.print(nodes[i].getData() + " ");
        		//}
        		//System.out.println();
        		//process again for the edges
        		for(int i=1;i<arr.size();i++){
                    String s[] = arr.get(i).split(":");
        			
    				fromNode = new Node(Integer.parseInt(s[0]));
    				toNode = new Node(Integer.parseInt(s[1]));
    				
    				//System.out.println("[" + fromNode.getData() + "][" + toNode.getData() + "]" +  Integer.parseInt(s[2]));
        			
    				//get node index
        			int fnode_index = GetNodeIndex(fromNode);
        			int tnode_index = GetNodeIndex(toNode);
        			adjacencyMatrix[fnode_index][tnode_index] = 1;
        		}
        		
        		//System.out.println("matrix: ");
        		//for(int j=0;j<NodeNum;j++){
    				//for(int k=0;k<NodeNum;k++){
    					//System.out.print( adjacencyMatrix[j][k] + " ");
    				//}
    				//System.out.println();
    			//}
    		}catch(NumberFormatException e){
    			System.out.println("First line of the file " + file + " needs to be a numeric number");
    		}
    		
    		
    	}catch(IOException e){
    		System.out.println(e.getMessage());
    	}
    }

    public AdjacencyMatrix() {

    }
    public int GetNodeIndex(Node node){
    	//when return -1 means this node is not found in nodes
    	int index = -1;
    	if(nodes.length != 0){
    		for(int i=0; i< nodes.length; i++){
    			//System.out.println("i=" + i);
    			if(nodes[i] == null){
    				return -1;
    			}else if(nodes[i].equals(node)){
        			return i;
        		}
        	}
    	}
    	
    	return -1;
    }
   
    public static ArrayList<String> readFile(File file) throws IOException{
    	
    	Scanner read = new Scanner(file);
    	//read first line
    	//String firstLine = read.nextLine().trim();
    	//int NodeNo = Integer.parseInt(firstLine);
    	ArrayList<String> strarr = new ArrayList<>();
        while(read.hasNext()){
            String line = read.nextLine().trim();
            //System.out.println(line);
            strarr.add(line);
        }
        read.close();
    	return strarr;
    }
    @Override
    public boolean adjacent(Node x, Node y) {
    	int index_x = GetNodeIndex(x);
    	int index_y = GetNodeIndex(y);
    	if(adjacencyMatrix[index_x][index_y] > 0){
    		return true;
    	}
    	
        return false;
    }

    @Override
    public List<Node> neighbors(Node x) {
    	int index_x = GetNodeIndex(x);
    	ArrayList<Node> node_arr = new ArrayList<>();
    	for(int i=0;i<adjacencyMatrix[index_x].length;i++){
    		if(adjacencyMatrix[index_x][i] > 0){
    			
    			node_arr.add(nodes[i]);
    		}
    	}
    	
    	if(node_arr != null){
    		Collections.sort(node_arr, new Comparator<Node>(){
    			@Override
    			public int compare(Node n1, Node n2){
    				return n1.getData().toString().compareTo(n2.getData().toString());
    			}
    		});
    		return node_arr;
    	}
    	
        return null;
    }
    
    
    
    @Override
    public boolean addNode(Node x) {
    	//new matrix length
    	int num = nodes.length + 1;
    	//node exists
    	if(GetNodeIndex(x) != -1){
    		System.out.println("Error: this node exists already");
    		
    	}else{
    		
    		//add x to nodes
        	Node[] newNode = new Node[num];
    		int[][] newMatrix = new int[num][num];
    		
    		for(int i=0;i<nodes.length;i++){
    			newNode[i] = nodes[i];
    		}
    		
    		//add node x to the last element
    		newNode[num-1] = new Node(x.getData());
    		nodes = new Node[num];
    		System.arraycopy(newNode, 0, nodes, 0, num);
    		//for(int i=0;i<nodes.length;i++){
    			//System.out.println(i + " " + nodes[i].getData());
    		//}
    		
    		//get new index
    		int index = GetNodeIndex(x);
    		
    		//add to matrix
    		for(int i=0; i<adjacencyMatrix.length; i++){
    			for(int j=0; j<adjacencyMatrix.length; j++){
    				//copy from old matrix to new
        			newMatrix[i][j] = adjacencyMatrix[i][j];
        		}
    			//new col (n-1) is the index of the last element
    			newMatrix[i][num-1] = 0;
    		}
    		for(int i=0;i<num;i++){
    			newMatrix[num-1][i] = 0;
    		}
    		adjacencyMatrix = new int[num][num];
    		//copy back to adjacencyMatrix
    		System.arraycopy(newMatrix, 0, adjacencyMatrix, 0, num);
    		//debug
    		//System.out.println("matrix: ");
    		//for(int j=0;j<num;j++){
				//for(int k=0;k<num;k++){
					//System.out.print( adjacencyMatrix[j][k] + " ");
				//}
				//System.out.println();
			//}
    		return true;
    	}
        return false;
    }

    @Override
    public boolean removeNode(Node x) {
    	
    	int index = 0;
    	int num = nodes.length - 1;
    	//create new node array with current length -1
    	Node[] newNode = new Node[num];
    	//remove node x from nodes
    	
    	index = GetNodeIndex(x);
    	//node not found
    	if(index != -1){
    		int j = 0;
        	for(int i=0;i<nodes.length;i++){
        		if(nodes[i].equals(x)){
        			continue;
        		}else{
        			newNode[j] = nodes[i];
        			j++;
        		}
        	}
    		//copy from newNode to nodes
    		nodes = new Node[num];
    		System.arraycopy(newNode, 0, nodes, 0, num);
    		
    		//delete from matrix
    		int m=0;
    		int n=0;
    		int[][] newMatrix = new int[num][num];
    		for(int i=0;i< adjacencyMatrix.length;i++){
    			if(i == index){
    				continue;
    			}else{
    				n = 0;
    				for(int k=0;k<adjacencyMatrix.length;k++){
        				if(k != index){
            				newMatrix[m][n] = adjacencyMatrix[i][k];
            			}else{
            				continue;
            			}
        				n++;
        			}
    			}
    			m++;
    		}
    		adjacencyMatrix = new int[num][num];
    		System.arraycopy(newMatrix, 0, adjacencyMatrix, 0, num);
    		//debug
    		//System.out.println("matrix: after removenode");
    		//for(int i=0;i<num;i++){
				//for(int k=0;k<num;k++){
					//System.out.print( adjacencyMatrix[i][k] + " ");
				//}
				//System.out.println();
			//}
    		return true;

    	}else{
    		System.out.println("Error: this node does not exist");
    	}
        return false;
    }

    @Override
    public boolean addEdge(Edge x) {
    	
        int index_f = GetNodeIndex(x.getFrom());
        int index_t = GetNodeIndex(x.getTo());
    	
        //check if the fromNode exists or not, if not return error
        if((adjacencyMatrix[index_f] != null)||(adjacencyMatrix[index_f][0]==-1)){
        	//check if toNode exists or not
        	//if exists then add 1 to the matrix
            if(adjacencyMatrix[index_t] != null){
            	if(adjacencyMatrix[index_f][index_t] == 0){
            	    adjacencyMatrix[index_f][index_t] = 1;
            	    return true;
            	}else{
            		System.out.println("Error: this edge exists already!");
            	}
            	
            }else{
            	//when toNode not existing, then add toNode and 1 to the matrix
            	this.addNode(x.getTo());
            	index_t = GetNodeIndex(x.getTo());
            	adjacencyMatrix[index_f][index_t] = 1;
            	return true;
            }
        }else{
        	System.out.println("Error: fromNode does not exists or has been removed!");
        }
        
        return false;
    }

    @Override
    public boolean removeEdge(Edge x) {
    	int f = GetNodeIndex(x.getFrom());
        int t = GetNodeIndex(x.getTo());
        
    	if((adjacencyMatrix[f] == null) ||(adjacencyMatrix[t] == null) ){
    		System.out.println("Error: fromNode or toNode doesn't exist");
    	}else if(adjacencyMatrix[f][t] == 0){
    		System.out.println("Error: This edge does not exist");
    		return false;
    	}else{
    		adjacencyMatrix[f][t] = 0;
    		return true;
    	}
    	
        return false;
    }

    @Override
    public int distance(Node from, Node to) {
    	int f = (int)from.getData();
    	int t = (int)to.getData();
    	if((adjacencyMatrix[f] == null) ||(adjacencyMatrix[t] == null) ){
    		System.out.println("Error: fromNode or toNode doesn't exist");
    	}else{
    		return 1;
    	}
        return 0;
    }

    @Override
    public Optional<Node> getNode(int index) {
        return null;
    }
}
