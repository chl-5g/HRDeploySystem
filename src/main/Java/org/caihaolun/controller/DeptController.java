package org.caihaolun.controller;

import org.caihaolun.model.Dept;
import org.caihaolun.repository.DeptRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dept")
public class DeptController {

    private final DeptRepository deptRepo;

    public DeptController(DeptRepository deptRepo) {
        this.deptRepo = deptRepo;
    }

    @GetMapping
    public List<Dept> listAll() {
        return deptRepo.findAll();
    }

    @PostMapping
    public Dept create(@RequestBody Dept dept) {
        return deptRepo.save(dept);
    }
}
