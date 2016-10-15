package csula.cs4660.graphs.representations;

import java.awt.List;
import java.io.File;

import csula.cs4660.games.models.Tile;
import csula.cs4660.graphs.Edge;
import csula.cs4660.graphs.Graph;
import csula.cs4660.graphs.Node;
import csula.cs4660.graphs.searches.AstarSearch;
import csula.cs4660.graphs.utils.Parser;

public class TestAstar {
	
	public static void main(String[] args) {
		
		String file1 = "src/csula/cs4660/graphs/test/grid-1.txt";
		String file2 = "src/csula/cs4660/graphs/test/grid-2.txt";
		
		
		File file_1 = new File(file1);
		File file_2 = new File(file2);
		

        Graph[] graph1s = buildGraphs(file_1);
        Graph[] graph2s = buildGraphs(file_2);
        
        AstarSearch astar = new AstarSearch();
        String route = Parser.converEdgesToAction( astar.search(graph1s[1], new Node<>(new Tile(3,0,"@1")),new Node<>(new Tile(4,4,"@6"))));
        //System.out.println(route);
        
        //String route = Parser.converEdgesToAction( astar.search(graph2s[1], new Node<>(new Tile(3,0,"@1")),new Node<>(new Tile(13,0,"@8"))));
        //System.out.println(route);
	}

    private static Graph[] buildGraphs(File file) {
        Graph[] graphs = new Graph[3];

        /**graphs[0] = Parser.readRectangularGridFile(
            Representation.STRATEGY.ADJACENCY_LIST,
            file
        );*/

        graphs[1] = Parser.readRectangularGridFile(
            Representation.STRATEGY.ADJACENCY_MATRIX,
            file
        );

        /**graphs[2] = Parser.readRectangularGridFile(
            Representation.STRATEGY.OBJECT_ORIENTED,
            file
        );*/

        return graphs;
    }

}
