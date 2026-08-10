package MazeSolver.gui;

import MazeSolver.algorithm.BFS;
import MazeSolver.graph.Graph;
import MazeSolver.graph.GraphBuilder;
import MazeSolver.graph.Node;

import java.util.ArrayList;
import java.util.List;

//Finding the path using BFS 
public class BFSPathFinder {

    public List<Node> solve(Maze maze, Node selectedStart, Node selectedEnd) {

        if (maze == null || selectedStart == null || selectedEnd == null) {
            return new ArrayList<>();
        }

        int[][] grid = new int[maze.getRows()][maze.getCols()];

        for (int r = 0; r < maze.getRows(); r++) {
            for (int c = 0; c < maze.getCols(); c++) {
                grid[r][c] = maze.getCell(r, c);
            }
        }

        GraphBuilder builder = new GraphBuilder();
        Graph graph = builder.buildGraph(grid);

        Node start = graph.getNode(selectedStart.getRow(), selectedStart.getCol());
        Node end = graph.getNode(selectedEnd.getRow(), selectedEnd.getCol());

        System.out.println("Selected start: " + start);
        System.out.println("Selected end: " + end);
        System.out.println("Graph nodes: " + graph.getNodeCount());
        System.out.println("Graph edges: " + graph.getEdgeCount());

        if (start == null || end == null) {
            return new ArrayList<>();
        }

        return BFS.Pathfinding(graph, start, end);
    }
}
