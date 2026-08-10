package MazeSolver.algorithm;

import MazeSolver.graph.Graph;
import MazeSolver.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Breadth-first search for shortest path in an unweighted maze graph.
 */
public class BFS {
    public static List<Node> Pathfinding(Graph graph, Node start, Node end) {
        if (graph == null || start == null || end == null) {
            return Collections.emptyList();
        }
        
        // BFS Structures
        Queue<Node> queue = new LinkedList<>();
        Set<Node> visited = new HashSet<>();
        Map<Node, Node> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current.equals(end)) {
                return buildPath(parent, end);
            }

            for (Node neighbour : graph.getNeighbours(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    parent.put(neighbour, current);
                    queue.add(neighbour);
                }
            }
        }

        return Collections.emptyList();
    }

    private static List<Node> buildPath(Map<Node, Node> parent, Node end) {
        List<Node> path = new ArrayList<>();
        Node current = end;

        while (current != null) {
            path.add(current);
            current = parent.get(current);
        }

        Collections.reverse(path);
        return path;
    }
}
