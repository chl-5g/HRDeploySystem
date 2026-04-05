package org.caihaolun.service;

import java.util.*;

/**
 * ID3 Decision Tree — pure algorithm, no Spring dependency.
 * Trains on discrete features and predicts a label string.
 */
public class ID3DecisionTree {

    public static class Sample {
        private final Map<String, String> features;
        private final String label;

        public Sample(Map<String, String> features, String label) {
            this.features = features;
            this.label = label;
        }

        public Map<String, String> getFeatures() { return features; }
        public String getLabel() { return label; }
    }

    private static class Node {
        String splitFeature;
        String label;
        String majorityLabel;
        Map<String, Node> children = new HashMap<>();
        boolean isLeaf;
    }

    private Node root;

    public void fit(List<Sample> samples, Set<String> features) {
        if (samples == null || samples.isEmpty()) {
            throw new IllegalArgumentException("samples is empty");
        }
        root = build(samples, new HashSet<>(features));
    }

    public String predict(Map<String, String> features) {
        if (root == null) {
            throw new IllegalStateException("tree not trained");
        }
        Node cur = root;
        while (!cur.isLeaf) {
            String val = safe(features.get(cur.splitFeature));
            Node next = cur.children.get(val);
            if (next == null) {
                return cur.majorityLabel;
            }
            cur = next;
        }
        return cur.label;
    }

    private Node build(List<Sample> samples, Set<String> features) {
        Node node = new Node();
        node.majorityLabel = majorityLabel(samples);

        if (allSameLabel(samples)) {
            node.isLeaf = true;
            node.label = samples.get(0).getLabel();
            return node;
        }
        if (features.isEmpty()) {
            node.isLeaf = true;
            node.label = node.majorityLabel;
            return node;
        }

        String best = bestFeature(samples, features);
        if (best == null) {
            node.isLeaf = true;
            node.label = node.majorityLabel;
            return node;
        }

        node.splitFeature = best;
        node.isLeaf = false;

        Map<String, List<Sample>> groups = new HashMap<>();
        for (Sample s : samples) {
            String v = safe(s.getFeatures().get(best));
            groups.computeIfAbsent(v, k -> new ArrayList<>()).add(s);
        }

        Set<String> left = new HashSet<>(features);
        left.remove(best);
        for (Map.Entry<String, List<Sample>> e : groups.entrySet()) {
            node.children.put(e.getKey(), build(e.getValue(), left));
        }
        return node;
    }

    private String bestFeature(List<Sample> samples, Set<String> features) {
        double base = entropy(samples);
        String best = null;
        double bestGain = -1.0;
        for (String f : features) {
            double cond = conditionalEntropy(samples, f);
            double gain = base - cond;
            if (gain > bestGain) {
                bestGain = gain;
                best = f;
            }
        }
        return best;
    }

    private double entropy(List<Sample> samples) {
        Map<String, Integer> cnt = new HashMap<>();
        for (Sample s : samples) {
            cnt.merge(s.getLabel(), 1, Integer::sum);
        }
        double sum = samples.size();
        double h = 0.0;
        for (Integer c : cnt.values()) {
            double p = c / sum;
            h -= p * (Math.log(p) / Math.log(2));
        }
        return h;
    }

    private double conditionalEntropy(List<Sample> samples, String feature) {
        Map<String, List<Sample>> groups = new HashMap<>();
        for (Sample s : samples) {
            String v = safe(s.getFeatures().get(feature));
            groups.computeIfAbsent(v, k -> new ArrayList<>()).add(s);
        }
        double sum = samples.size();
        double h = 0.0;
        for (List<Sample> group : groups.values()) {
            h += (group.size() / sum) * entropy(group);
        }
        return h;
    }

    private boolean allSameLabel(List<Sample> samples) {
        String first = samples.get(0).getLabel();
        for (Sample s : samples) {
            if (!Objects.equals(first, s.getLabel())) return false;
        }
        return true;
    }

    private String majorityLabel(List<Sample> samples) {
        Map<String, Integer> cnt = new HashMap<>();
        for (Sample s : samples) {
            cnt.merge(s.getLabel(), 1, Integer::sum);
        }
        return cnt.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
