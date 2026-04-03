package org.caihaolun.service;

import org.caihaolun.model.Staffpost;
import org.caihaolun.model.TrainingSample;
import org.caihaolun.repository.TrainingSampleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * Intelligent staff matching service.
 * - New staff: ID3 decision tree (trained from DB samples) → dept + level
 * - Old staff: weighted score → level adjustment
 */
@Service
public class MatchService {

    private static final Logger log = LoggerFactory.getLogger(MatchService.class);

    private static final String F_CATEGORY = "category";
    private static final String F_QUAL = "qual";
    private static final String F_MAJOR = "majorBucket";
    private static final String F_GRADUATE = "graduateTier";
    private static final Set<String> FEATURES = Set.of(F_CATEGORY, F_QUAL, F_MAJOR, F_GRADUATE);

    private final TrainingSampleRepository sampleRepo;
    private ID3DecisionTree tree;

    public MatchService(TrainingSampleRepository sampleRepo) {
        this.sampleRepo = sampleRepo;
    }

    @PostConstruct
    public void rebuildTree() {
        List<TrainingSample> rows = sampleRepo.findAll();
        if (rows.isEmpty()) {
            log.warn("No training samples in DB, tree not built");
            tree = null;
            return;
        }
        List<ID3DecisionTree.Sample> samples = new ArrayList<>();
        for (TrainingSample r : rows) {
            Map<String, String> f = new HashMap<>();
            f.put(F_CATEGORY, r.getCategory());
            f.put(F_QUAL, r.getQual());
            f.put(F_MAJOR, r.getMajorBucket());
            f.put(F_GRADUATE, r.getGraduateTier());
            samples.add(new ID3DecisionTree.Sample(f, r.getLabel()));
        }
        ID3DecisionTree t = new ID3DecisionTree();
        t.fit(samples, FEATURES);
        this.tree = t;
        log.info("ID3 tree rebuilt with {} samples", rows.size());
    }

    /**
     * Match a new staff member to dept + level.
     */
    public Map<String, String> matchNewStaff(String category, String qual, String major, String graduate) {
        String deptName = "售后服务部";
        int level = 1;

        if (tree != null) {
            try {
                Map<String, String> fs = new HashMap<>();
                fs.put(F_CATEGORY, safe(category));
                fs.put(F_QUAL, safe(qual));
                fs.put(F_MAJOR, normalizeMajor(major));
                fs.put(F_GRADUATE, normalizeGraduate(graduate));
                String label = tree.predict(fs);
                if (label != null && label.contains("|")) {
                    String[] parts = label.split("\\|");
                    if (parts.length == 2) {
                        deptName = parts[0];
                        level = Integer.parseInt(parts[1]);
                    }
                }
            } catch (Exception e) {
                log.warn("ID3 prediction failed, using fallback", e);
            }
        }

        if (level < 1) {
            level = fallbackLevel(qual);
        }

        Map<String, String> res = new LinkedHashMap<>();
        res.put("部门名称", deptName);
        res.put("级别", String.valueOf(level));
        return res;
    }

    /**
     * Evaluate an existing staff member based on scores.
     */
    public Map<String, String> evaluateOldStaff(int score1, int score2, int score3, Staffpost staffpost) {
        double w1 = 0.4, w2 = 0.3, w3 = 0.3;
        String deptName = "研发技术部";
        int level = 5;

        if (staffpost != null && staffpost.getPost() != null && staffpost.getRank() != null) {
            deptName = staffpost.getPost();
            String rank = staffpost.getRank();
            try {
                level = Integer.parseInt(rank.substring(rank.length() - 1));
            } catch (Exception ignored) {}
        }

        double totScore = w1 * score1 + w2 * score2 + w3 * score3;
        if (totScore >= 1 && totScore <= 5) {
            level--;
        } else if (totScore > 5 && totScore <= 8) {
            level++;
        } else {
            level += 2;
        }

        Map<String, String> res = new LinkedHashMap<>();
        res.put("部门名称", deptName);
        res.put("级别", String.valueOf(level));
        return res;
    }

    private int fallbackLevel(String qual) {
        switch (safe(qual)) {
            case "专科": return 1;
            case "本科": return 2;
            case "硕士": return 3;
            case "博士": return 4;
            default: return 1;
        }
    }

    String normalizeMajor(String major) {
        String s = safe(major).toLowerCase();
        if (s.contains("计算机") || s.contains("软件") || s.contains("数据") || s.contains("信息")) return "it";
        if (s.contains("会计") || s.contains("财务") || s.contains("审计") || s.contains("金融")) return "finance";
        if (s.contains("市场") || s.contains("营销") || s.contains("商务")) return "market";
        if (s.contains("人力") || s.contains("管理")) return "hr";
        return "other";
    }

    String normalizeGraduate(String graduate) {
        String s = safe(graduate);
        if ("清华大学".equals(s) || "北京大学".equals(s) || s.contains("复旦") || s.contains("上海交通")) return "top";
        return "normal";
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }
}
