package analysis;
import MazeSolver.graph.Graph;
import MazeSolver.graph.Node;
import MazeSolver.algorithm.BFS;

import java.util.List;

public class SimilarityFinder {

	// Method to compute similarity
    public static double computeSimilarity(Graph g1, Graph g2,
                                           Node start1, Node end1,
                                           Node start2, Node end2) {

        int nodeDiff = computeNodeDifference(g1, g2);
        int edgeDiff = computeEdgeDifference(g1, g2);
        int pathDiff = computePathDifference(g1, g2, start1, end1, start2, end2);

        double similarity = 100 - (nodeDiff + edgeDiff + pathDiff);

        //Prevents negative similarity
        return Math.max(0, similarity);
    }

    //Node difference
    public static int computeNodeDifference(Graph g1, Graph g2) {
        return Math.abs(g1.getNodeCount() - g2.getNodeCount());
    }

    //Edge difference
    public static int computeEdgeDifference(Graph g1, Graph g2) {
        return Math.abs(g1.getEdgeCount() - g2.getEdgeCount());
    }

    //Path difference using BFS
    public static int computePathDifference(Graph g1, Graph g2,
                                            Node start1, Node end1,
                                            Node start2, Node end2) {

        List<Node> path1 = BFS.Pathfinding(g1, start1, end1);
        List<Node> path2 = BFS.Pathfinding(g2, start2, end2);

        int length1 = (path1 == null) ? Integer.MAX_VALUE : path1.size();
        int length2 = (path2 == null) ? Integer.MAX_VALUE : path2.size();

        return Math.abs(length1 - length2);
    }

    //Analysis report
    public static void printAnalysis(Graph g1, Graph g2,
                                     Node start1, Node end1,
                                     Node start2, Node end2) {

        int nodeDiff = computeNodeDifference(g1, g2);
        int edgeDiff = computeEdgeDifference(g1, g2);
        int pathDiff = computePathDifference(g1, g2, start1, end1, start2, end2);

        double similarity = Math.max(0, 100 - (nodeDiff + edgeDiff + pathDiff));

        System.out.println("=== MAZE SIMILARITY ANALYSIS ===");
        System.out.println("Node Difference: " + nodeDiff);
        System.out.println("Edge Difference: " + edgeDiff);
        System.out.println("Path Difference: " + pathDiff);
        System.out.println("Similarity Score: " + similarity + "%");
} 
}
