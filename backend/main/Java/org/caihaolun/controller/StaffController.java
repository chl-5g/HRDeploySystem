package org.caihaolun.controller;

import org.caihaolun.model.Staff;
import org.caihaolun.model.Staffedu;
import org.caihaolun.model.Staffpost;
import org.caihaolun.repository.StaffRepository;
import org.caihaolun.repository.StaffeduRepository;
import org.caihaolun.repository.StaffpostRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    private final StaffRepository staffRepo;
    private final StaffeduRepository eduRepo;
    private final StaffpostRepository postRepo;

    public StaffController(StaffRepository staffRepo, StaffeduRepository eduRepo, StaffpostRepository postRepo) {
        this.staffRepo = staffRepo;
        this.eduRepo = eduRepo;
        this.postRepo = postRepo;
    }

    // ---- Staff CRUD ----

    @GetMapping
    public List<Staff> listAll() {
        return staffRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        Optional<Staff> staff = staffRepo.findById(id);
        if (staff.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Map<String, Object> detail = new LinkedHashMap<>();
        detail.put("staff", staff.get());
        eduRepo.findById(id).ifPresent(e -> detail.put("education", e));
        postRepo.findById(id).ifPresent(p -> detail.put("position", p));
        return ResponseEntity.ok(detail);
    }

    @PostMapping
    public Staff create(@RequestBody Staff staff) {
        return staffRepo.save(staff);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Staff> update(@PathVariable String id, @RequestBody Staff staff) {
        if (!staffRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        staff.setId(id);
        return ResponseEntity.ok(staffRepo.save(staff));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (!staffRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        staffRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Education ----

    @GetMapping("/edu")
    public List<Staffedu> listAllEdu() {
        return eduRepo.findAll();
    }

    @PostMapping("/edu")
    public Staffedu createEdu(@RequestBody Staffedu edu) {
        return eduRepo.save(edu);
    }

    // ---- Position ----

    @GetMapping("/post")
    public List<Staffpost> listAllPost() {
        return postRepo.findAll();
    }

    @PostMapping("/post")
    public Staffpost createPost(@RequestBody Staffpost post) {
        return postRepo.save(post);
    }
}
