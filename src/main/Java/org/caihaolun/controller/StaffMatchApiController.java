package org.caihaolun.controller;

import org.caihaolun.model.Staffpost;
import org.caihaolun.service.CalculateNewStaff;
import org.caihaolun.service.CalculateOldStaff;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 新员工智能匹配 API（为前后端分离预留）。
 */
@Controller
@RequestMapping({"/staff/match", "/api/staff/match"})
public class StaffMatchApiController {

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseBody
    public NewStaffMatchResponse matchNewStaff(@RequestBody NewStaffMatchRequest req) {
        CalculateNewStaff service = new CalculateNewStaff();
        Map<String, String> result = service.calcRes(
                req.getCategory(),
                req.getQual(),
                req.getMajor(),
                req.getGraduate()
        );

        NewStaffMatchResponse res = new NewStaffMatchResponse();
        res.setDeptName(result.get("部门名称"));
        res.setLevel(safeParseInt(result.get("级别"), 1));
        res.setRaw(result);
        return res;
    }

    @RequestMapping(value = "/old", method = RequestMethod.POST)
    @ResponseBody
    public OldStaffMatchResponse matchOldStaff(@RequestBody OldStaffMatchRequest req) {
        Staffpost staffpost = buildStaffPost(req);
        Map<String, String> result;
        try {
            result = new CalculateOldStaff().calcRes2(
                    safeScore(req.getScore1()),
                    safeScore(req.getScore2()),
                    safeScore(req.getScore3()),
                    staffpost
            );
        } catch (Exception ex) {
            result = new CalculateOldStaff().calcRes2("0", "0", "0", staffpost);
        }

        OldStaffMatchResponse res = new OldStaffMatchResponse();
        res.setDeptName(result.get("部门名称"));
        res.setLevel(safeParseInt(result.get("级别"), 1));
        res.setRaw(result);
        return res;
    }

    private Staffpost buildStaffPost(OldStaffMatchRequest req) {
        // 优先使用请求体显式传入的岗位信息，避免 API 强依赖数据库。
        if (!isBlank(req.getPost()) || !isBlank(req.getRank())) {
            Staffpost sp = new Staffpost();
            sp.setPost(req.getPost());
            sp.setRank(req.getRank());
            return sp;
        }

        return null;
    }

    private String safeScore(String s) {
        if (s == null) {
            return "0";
        }
        try {
            Integer.parseInt(s.trim());
            return s.trim();
        } catch (NumberFormatException ex) {
            return "0";
        }
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private int safeParseInt(String s, int fallback) {
        if (s == null) {
            return fallback;
        }
        try {
            return Integer.parseInt(s.trim());
        } catch (NumberFormatException ex) {
            return fallback;
        }
    }

    public static class NewStaffMatchRequest {
        private String category;
        private String qual;
        private String major;
        private String graduate;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getQual() {
            return qual;
        }

        public void setQual(String qual) {
            this.qual = qual;
        }

        public String getMajor() {
            return major;
        }

        public void setMajor(String major) {
            this.major = major;
        }

        public String getGraduate() {
            return graduate;
        }

        public void setGraduate(String graduate) {
            this.graduate = graduate;
        }
    }

    public static class OldStaffMatchRequest {
        private String id;
        private String score1;
        private String score2;
        private String score3;
        private String post;
        private String rank;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getScore1() {
            return score1;
        }

        public void setScore1(String score1) {
            this.score1 = score1;
        }

        public String getScore2() {
            return score2;
        }

        public void setScore2(String score2) {
            this.score2 = score2;
        }

        public String getScore3() {
            return score3;
        }

        public void setScore3(String score3) {
            this.score3 = score3;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class NewStaffMatchResponse {
        private String deptName;
        private int level;
        private Map<String, String> raw;

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Map<String, String> getRaw() {
            return raw;
        }

        public void setRaw(Map<String, String> raw) {
            this.raw = raw;
        }
    }

    public static class OldStaffMatchResponse {
        private String deptName;
        private int level;
        private Map<String, String> raw;

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Map<String, String> getRaw() {
            return raw;
        }

        public void setRaw(Map<String, String> raw) {
            this.raw = raw;
        }
    }
}
