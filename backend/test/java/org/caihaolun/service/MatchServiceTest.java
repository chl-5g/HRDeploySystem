package org.caihaolun.service;

import org.caihaolun.model.Staffpost;
import org.caihaolun.model.TrainingSample;
import org.caihaolun.repository.TrainingSampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceTest {

    private MatchService service;

    @BeforeEach
    void setUp() {
        TrainingSampleRepository repo = mock(TrainingSampleRepository.class);
        when(repo.findAll()).thenReturn(List.of(
                makeSample("理工类", "本科", "it", "normal", "研发部|3"),
                makeSample("理工类", "硕士", "it", "top", "研发部|5"),
                makeSample("经济管理类", "本科", "finance", "normal", "财务审计部|2"),
                makeSample("其他", "专科", "other", "normal", "售后服务部|1")
        ));
        service = new MatchService(repo);
        service.rebuildTree();
    }

    @Test
    void matchNewStaffIT() {
        Map<String, String> res = service.matchNewStaff("理工类", "本科", "计算机科学", "南京邮电大学");
        assertEquals("研发部", res.get("部门名称"));
        assertEquals("3", res.get("级别"));
    }

    @Test
    void matchNewStaffTopSchool() {
        Map<String, String> res = service.matchNewStaff("理工类", "硕士", "软件工程", "清华大学");
        assertEquals("研发部", res.get("部门名称"));
        assertEquals("5", res.get("级别"));
    }

    @Test
    void evaluateOldStaffHighScore() {
        Staffpost sp = new Staffpost();
        sp.setPost("研发部");
        sp.setRank("P3");
        Map<String, String> res = service.evaluateOldStaff(9, 9, 9, sp);
        assertEquals("研发部", res.get("部门名称"));
        assertEquals("5", res.get("级别")); // 3 + 2
    }

    @Test
    void normalizeMajorIT() {
        assertEquals("it", service.normalizeMajor("计算机科学与技术"));
        assertEquals("it", service.normalizeMajor("软件工程"));
        assertEquals("finance", service.normalizeMajor("会计学"));
        assertEquals("market", service.normalizeMajor("市场营销"));
        assertEquals("hr", service.normalizeMajor("人力资源管理"));
        assertEquals("other", service.normalizeMajor("哲学"));
    }

    @Test
    void normalizeGraduate() {
        assertEquals("top", service.normalizeGraduate("清华大学"));
        assertEquals("top", service.normalizeGraduate("北京大学"));
        assertEquals("normal", service.normalizeGraduate("南京邮电大学"));
    }

    private TrainingSample makeSample(String cat, String qual, String major, String grad, String label) {
        TrainingSample s = new TrainingSample();
        s.setCategory(cat);
        s.setQual(qual);
        s.setMajorBucket(major);
        s.setGraduateTier(grad);
        s.setLabel(label);
        return s;
    }
}
