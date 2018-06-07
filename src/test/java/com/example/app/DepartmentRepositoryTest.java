package com.example.app;

import com.example.app.case2.Department;
import com.example.app.case2.DepartmentRepository;
import java.util.Optional;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DepartmentRepositoryTest {

  @Autowired
  private DepartmentRepository target;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  /**
   * Hibernate:
   *     select
   *         department0_.id as id1_2_
   *     from
   *         departments department0_
   *     left outer join
   *         users users1_
   *             on department0_.id=users1_.department_id
   *             and (
   *                 users1_.active=1
   *             )
   * Hibernate:
   *     select
   *         users0_.department_id as departme3_3_0_,
   *         users0_.id as id1_3_0_,
   *         users0_.id as id1_3_1_,
   *         users0_.active as active2_3_1_,
   *         users0_.department_id as departme3_3_1_
   *     from
   *         users users0_
   *     where
   *         users0_.department_id=?
   */
  @Sql(statements = {
      "INSERT INTO departments(id) VALUES (1);",
      "INSERT INTO users(id, department_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_join() {

    Optional<Department> actual = target.findById_Join(1);

    assertThat(actual.get().getUsers(), hasSize(2));// not work
  }

  /**
   * Hibernate:
   *     select
   *         department0_.id as id1_2_0_,
   *         users1_.id as id1_3_1_,
   *         users1_.active as active2_3_1_,
   *         users1_.department_id as departme3_3_1_,
   *         users1_.department_id as departme3_3_0__,
   *         users1_.id as id1_3_0__
   *     from
   *         departments department0_
   *     inner join
   *         users users1_
   *             on department0_.id=users1_.department_id
   *             and (
   *                 users1_.active=1
   *             )
   */
  @Sql(statements = {
      "INSERT INTO departments(id) VALUES (1);",
      "INSERT INTO users(id, department_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_join_entity_graph() {

    Optional<Department> actual = target.findById_EntityGraph(1);

    assertThat(actual.get().getUsers(), hasSize(2));
  }

  /**
   * Hibernate:
   *     select
   *         department0_.id as id1_2_0_,
   *         users1_.id as id1_3_1_,
   *         users1_.active as active2_3_1_,
   *         users1_.department_id as departme3_3_1_,
   *         users1_.department_id as departme3_3_0__,
   *         users1_.id as id1_3_0__
   *     from
   *         departments department0_
   *     inner join
   *         users users1_
   *             on department0_.id=users1_.department_id
   *             and (
   *                 users1_.active=0
   *             )
   */
  @Sql(statements = {
      "INSERT INTO departments(id) VALUES (1);",
      "INSERT INTO users(id, department_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_inactive() {

    Optional<Department> actual = target.findById_Inactive(1);

    assertThat(actual.get().getUsers(), hasSize(1));
  }
}
