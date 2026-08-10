package navigation.algorithms;

import navigation.graph.Edge;
import navigation.graph.Graph;
import navigation.graph.Node;

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
 * Breadth-first search implementation used for shortest route finding.
 * <p>
 * Since each graph movement has the same cost, BFS is suitable because it
 * reaches the destination using the fewest number of steps.
 */
public class BFS {

    /**
     * Finds the shortest path between two selected graph nodes.
     *
     * @param graph graph created from the processed settlement image
     * @param sourceId selected start node id
     * @param destId selected end node id
     * @return ordered list of nodes from start to end, or empty list if no path exists
     */
    public List<Node> findPath(Graph graph, int sourceId, int destId) {
        if (!graph.containsNode(sourceId) || !graph.containsNode(destId)) {
            return Collections.emptyList();
        }

        Map<Integer, Integer> parent = new HashMap<>();
        Set<Integer> visited = new HashSet<>();
        Queue<Integer> queue = new LinkedList<>();

        visited.add(sourceId);
        queue.add(sourceId);
        parent.put(sourceId, -1);

        while (!queue.isEmpty()) {
            int currentId = queue.poll();

            if (currentId == destId) {
                break;
            }

            Node currentNode = graph.getNode(currentId);
            for (Edge edge : graph.getEdges(currentId)) {
                int neighbourId = edge.getOther(currentNode).id;

                if (visited.add(neighbourId)) {
                    parent.put(neighbourId, currentId);
                    queue.add(neighbourId);
                }
            }
        }

        if (!parent.containsKey(destId)) {
            return Collections.emptyList();
        }

        return buildPath(graph, parent, destId);
    }

    /**
     * Rebuilds the final path by following parents backwards from the end node.
     */
    private List<Node> buildPath(Graph graph, Map<Integer, Integer> parent, int destId) {
        LinkedList<Node> path = new LinkedList<>();
        int currentId = destId;

        while (currentId != -1) {
            path.addFirst(graph.getNode(currentId));
            currentId = parent.getOrDefault(currentId, -1);
        }

        return new ArrayList<>(path);
    }
}
