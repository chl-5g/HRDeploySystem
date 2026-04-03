package org.caihaolun.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ID3DecisionTreeTest {

    private ID3DecisionTree tree;

    @BeforeEach
    void setUp() {
        tree = new ID3DecisionTree();
        List<ID3DecisionTree.Sample> samples = new ArrayList<>();
        samples.add(sample("理工类", "本科", "it", "normal", "研发部|3"));
        samples.add(sample("理工类", "硕士", "it", "top", "研发部|5"));
        samples.add(sample("经济管理类", "本科", "finance", "normal", "财务审计部|2"));
        samples.add(sample("其他", "专科", "other", "normal", "售后服务部|1"));
        tree.fit(samples, Set.of("category", "qual", "majorBucket", "graduateTier"));
    }

    @Test
    void predictKnownSample() {
        Map<String, String> fs = Map.of(
                "category", "理工类", "qual", "本科", "majorBucket", "it", "graduateTier", "normal");
        assertEquals("研发部|3", tree.predict(fs));
    }

    @Test
    void predictUnseenFallsBackToMajority() {
        Map<String, String> fs = Map.of(
                "category", "文史类", "qual", "本科", "majorBucket", "art", "graduateTier", "normal");
        String result = tree.predict(fs);
        assertNotNull(result);
    }

    @Test
    void emptyThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new ID3DecisionTree().fit(Collections.emptyList(), Set.of("a")));
    }

    @Test
    void untrainedThrows() {
        assertThrows(IllegalStateException.class,
                () -> new ID3DecisionTree().predict(Map.of("a", "b")));
    }

    private ID3DecisionTree.Sample sample(String cat, String qual, String major, String grad, String label) {
        Map<String, String> f = new HashMap<>();
        f.put("category", cat);
        f.put("qual", qual);
        f.put("majorBucket", major);
        f.put("graduateTier", grad);
        return new ID3DecisionTree.Sample(f, label);
    }
}
