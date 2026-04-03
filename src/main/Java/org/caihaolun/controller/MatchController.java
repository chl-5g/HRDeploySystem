package org.caihaolun.controller;

import org.caihaolun.model.Staffpost;
import org.caihaolun.model.TrainingSample;
import org.caihaolun.repository.StaffpostRepository;
import org.caihaolun.repository.TrainingSampleRepository;
import org.caihaolun.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
public class MatchController {

    private final MatchService matchService;
    private final StaffpostRepository postRepo;
    private final TrainingSampleRepository sampleRepo;

    public MatchController(MatchService matchService, StaffpostRepository postRepo, TrainingSampleRepository sampleRepo) {
        this.matchService = matchService;
        this.postRepo = postRepo;
        this.sampleRepo = sampleRepo;
    }

    @PostMapping("/new")
    public ResponseEntity<?> matchNewStaff(@RequestBody NewStaffRequest req) {
        Map<String, String> result = matchService.matchNewStaff(req.category, req.qual, req.major, req.graduate);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("deptName", result.get("部门名称"));
        res.put("level", Integer.parseInt(result.get("级别")));
        res.put("raw", result);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/old")
    public ResponseEntity<?> evaluateOldStaff(@RequestBody OldStaffRequest req) {
        Staffpost staffpost = null;
        if (req.staffId != null) {
            staffpost = postRepo.findById(req.staffId).orElse(null);
        }
        if (staffpost == null && req.post != null) {
            staffpost = new Staffpost();
            staffpost.setPost(req.post);
            staffpost.setRank(req.rank);
        }
        Map<String, String> result = matchService.evaluateOldStaff(
                safeInt(req.score1), safeInt(req.score2), safeInt(req.score3), staffpost);
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("deptName", result.get("部门名称"));
        res.put("level", Integer.parseInt(result.get("级别")));
        res.put("raw", result);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/samples")
    public List<TrainingSample> listSamples() {
        return sampleRepo.findAll();
    }

    @PostMapping("/samples")
    public ResponseEntity<?> addSample(@RequestBody TrainingSample sample) {
        sampleRepo.save(sample);
        matchService.rebuildTree();
        return ResponseEntity.ok(Map.of("message", "样本已添加，决策树已重建"));
    }

    @PostMapping("/rebuild")
    public ResponseEntity<?> rebuildTree() {
        matchService.rebuildTree();
        return ResponseEntity.ok(Map.of("message", "决策树已重建"));
    }

    private int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); }
        catch (Exception e) { return 0; }
    }

    public static class NewStaffRequest {
        public String category;
        public String qual;
        public String major;
        public String graduate;
    }

    public static class OldStaffRequest {
        public String staffId;
        public String score1;
        public String score2;
        public String score3;
        public String post;
        public String rank;
    }
}
