package org.caihaolun.repository;

import org.caihaolun.model.Dept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeptRepository extends JpaRepository<Dept, String> {
}
