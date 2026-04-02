package org.caihaolun.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Administrator on 2017/3/22.
 * 构造决策树算法
 */

public class CalculateNewStaff {

    private static final String FEATURE_CATEGORY = "category";
    private static final String FEATURE_QUAL = "qual";
    private static final String FEATURE_MAJOR = "majorBucket";
    private static final String FEATURE_GRADUATE = "graduateTier";
    private static final ID3DecisionTree TREE = buildTree();

    /**
     * 根据四个评价维度，得到DeptName、Level的值
     *
     * @param category 学历类别
     * @param qual     学历
     * @param major    专业
     * @param graduate 毕业院校
     * @return
     */
    public Map<String, String> calcRes(String category, String qual, String major, String graduate) {
        System.out.println(category + " " + qual + " " + major + " " + graduate);

        String deptName = "售后服务部";
        int level = 1;

        try {
            Map<String, String> fs = new HashMap<>();
            fs.put(FEATURE_CATEGORY, safe(category));
            fs.put(FEATURE_QUAL, safe(qual));
            fs.put(FEATURE_MAJOR, normalizeMajor(major));
            fs.put(FEATURE_GRADUATE, normalizeGraduate(graduate));
            String label = TREE.predict(fs);
            if (label != null && label.contains("|")) {
                String[] parts = label.split("\\|");
                if (parts.length == 2) {
                    deptName = parts[0];
                    level = Integer.parseInt(parts[1]);
                }
            }
        } catch (Exception ignore) {
            // 决策树异常时回退到兜底规则，保证线上可用。
        }

        if (level < 1) {
            level = fallbackLevel(qual);
        }

        Map<String, String> res = new HashMap<>();
        res.put("部门名称", deptName);
        res.put("级别", String.valueOf(level));
        return res;
    }

    private static ID3DecisionTree buildTree() {
        List<ID3DecisionTree.Sample> samples = new ArrayList<>();
        // 理工类：优先研发
        add(samples, "理工类", "专科", "it", "normal", "研发部|1");
        add(samples, "理工类", "本科", "it", "normal", "研发部|3");
        add(samples, "理工类", "硕士", "it", "top", "研发部|5");
        add(samples, "理工类", "博士", "it", "top", "研发部|6");
        add(samples, "理工类", "本科", "other", "normal", "研发部|2");
        add(samples, "理工类", "硕士", "other", "normal", "研发部|3");
        // 经管类：财务/营销/人资分流
        add(samples, "经济管理类", "专科", "finance", "normal", "财务审计部|1");
        add(samples, "经济管理类", "本科", "finance", "normal", "财务审计部|2");
        add(samples, "经济管理类", "硕士", "finance", "top", "财务审计部|4");
        add(samples, "经济管理类", "本科", "market", "normal", "市场营销部|2");
        add(samples, "经济管理类", "硕士", "market", "normal", "市场营销部|3");
        add(samples, "经济管理类", "本科", "hr", "normal", "人力资源部|2");
        add(samples, "经济管理类", "硕士", "hr", "top", "人力资源部|4");
        // 其他类别：售后/运营兜底
        add(samples, "其他", "专科", "other", "normal", "售后服务部|1");
        add(samples, "其他", "本科", "other", "normal", "售后服务部|2");
        add(samples, "其他", "硕士", "other", "normal", "售后服务部|3");
        add(samples, "其他", "博士", "other", "top", "售后服务部|4");

        Set<String> features = new HashSet<>();
        features.add(FEATURE_CATEGORY);
        features.add(FEATURE_QUAL);
        features.add(FEATURE_MAJOR);
        features.add(FEATURE_GRADUATE);

        ID3DecisionTree tree = new ID3DecisionTree();
        tree.fit(samples, features);
        return tree;
    }

    private static void add(List<ID3DecisionTree.Sample> samples,
                            String category, String qual, String majorBucket, String graduateTier, String label) {
        Map<String, String> f = new HashMap<>();
        f.put(FEATURE_CATEGORY, category);
        f.put(FEATURE_QUAL, qual);
        f.put(FEATURE_MAJOR, majorBucket);
        f.put(FEATURE_GRADUATE, graduateTier);
        samples.add(new ID3DecisionTree.Sample(f, label));
    }

    private int fallbackLevel(String qual) {
        switch (safe(qual)) {
            case "专科":
                return 1;
            case "本科":
                return 2;
            case "硕士":
                return 3;
            case "博士":
                return 4;
            default:
                return 1;
        }
    }

    private String normalizeMajor(String major) {
        String s = safe(major).toLowerCase();
        if (s.contains("计算机") || s.contains("软件") || s.contains("数据") || s.contains("信息")) {
            return "it";
        }
        if (s.contains("会计") || s.contains("财务") || s.contains("审计") || s.contains("金融")) {
            return "finance";
        }
        if (s.contains("市场") || s.contains("营销") || s.contains("商务")) {
            return "market";
        }
        if (s.contains("人力") || s.contains("管理")) {
            return "hr";
        }
        return "other";
    }

    private String normalizeGraduate(String graduate) {
        String s = safe(graduate);
        if ("清华大学".equals(s) || "北京大学".equals(s) || s.contains("复旦") || s.contains("上海交通")) {
            return "top";
        }
        return "normal";
    }

    private String safe(String s) {
        return s == null ? "" : s.trim();
    }

}
