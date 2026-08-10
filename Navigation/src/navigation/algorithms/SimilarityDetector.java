package navigation.algorithms;

import navigation.graph.Graph;

/**
 * Cosine similarity on a 4-feature structural vector extracted from each graph.
 */
public class SimilarityDetector {

    public double compare(Graph a, Graph b) {
        double[] fa = features(a);
        double[] fb = features(b);
        return cosine(fa, fb);
    }

    public double[] features(Graph g) {
        return new double[]{
            g.getNodeCount()     / 10000.0,
            g.getEdgeCount()     / 50000.0,
            g.getDensity()       * 100.0,
            g.getAverageDegree() / 8.0
        };
    }

    public String[] featureNames() {
        return new String[]{
            "Walkable Area (nodes)",
            "Connections (edges)",
            "Graph Density",
            "Avg Connectivity (degree)"
        };
    }

    public String interpret(double score) {
        if (score >= 0.90) return "Very High Similarity";
        if (score >= 0.70) return "High Similarity";
        if (score >= 0.50) return "Moderate Similarity";
        if (score >= 0.30) return "Low Similarity";
        return "Very Low Similarity";
    }

    private double cosine(double[] a, double[] b) {
        double dot = 0, na = 0, nb = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            na  += a[i] * a[i];
            nb  += b[i] * b[i];
        }
        if (na == 0 || nb == 0) return 0;
        return Math.max(0, Math.min(1, dot / (Math.sqrt(na) * Math.sqrt(nb))));
    }
}
