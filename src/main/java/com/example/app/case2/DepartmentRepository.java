package com.example.app.case2;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {

  @Query("select d from Department d left join d.users u on u.active = true")
  Optional<Department> findById_Join(Integer id);

  @Query("select d from Department d left join d.users u on u.active = true")
  @EntityGraph(attributePaths = "users")
  Optional<Department> findById_EntityGraph(Integer id);

  @Query("select d from Department d left join d.users u on u.active = false")
  @EntityGraph(attributePaths = "users")
  Optional<Department> findById_Inactive(Integer id);
}
