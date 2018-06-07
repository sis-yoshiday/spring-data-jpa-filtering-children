package com.example.app;

import com.example.app.case1.Article;
import com.example.app.case1.ArticleRepository;
import java.util.Optional;
import org.junit.Test;
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
public class ArticleRepositoryTest {

  @Autowired
  private ArticleRepository target;

  /**
   * Hibernate:
   *     select
   *         article0_.id as id1_0_0_
   *     from
   *         articles article0_
   *     where
   *         article0_.id=?
   * Hibernate:
   *     select
   *         comments0_.article_id as article_3_1_0_,
   *         comments0_.id as id1_1_0_,
   *         comments0_.id as id1_1_1_,
   *         comments0_.active as active2_1_1_,
   *         comments0_.article_id as article_3_1_1_
   *     from
   *         comments comments0_
   *     where
   *         (
   *             comments0_.active = 1
   *         )
   *         and comments0_.article_id=?
   */
  @Sql(statements = {
      "INSERT INTO articles(id) VALUES (1);",
      "INSERT INTO comments(id, article_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_plain() {

    Optional<Article> actual = target.findById(1);

    assertThat(actual.get().getComments(), hasSize(2));
  }

  /**
   * Hibernate:
   *     select
   *         article0_.id as id1_0_0_,
   *         comments1_.id as id1_1_1_,
   *         comments1_.active as active2_1_1_,
   *         comments1_.article_id as article_3_1_1_,
   *         comments1_.article_id as article_3_1_0__,
   *         comments1_.id as id1_1_0__
   *     from
   *         articles article0_
   *     left outer join
   *         comments comments1_
   *             on article0_.id=comments1_.article_id
   *             and (
   *                 comments1_.active = 1
   *             )
   *     where
   *         article0_.id=?
   */
  @Sql(statements = {
      "INSERT INTO articles(id) VALUES (1);",
      "INSERT INTO comments(id, article_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_join_fetch() {

    Optional<Article> actual = target.findById_JoinFetch(1);

    assertThat(actual.get().getComments(), hasSize(2));
  }

  /**
   * Hibernate:
   *     select
   *         article0_.id as id1_0_0_,
   *         comments1_.id as id1_1_1_,
   *         comments1_.active as active2_1_1_,
   *         comments1_.article_id as article_3_1_1_,
   *         comments1_.article_id as article_3_1_0__,
   *         comments1_.id as id1_1_0__
   *     from
   *         articles article0_
   *     left outer join
   *         comments comments1_
   *             on article0_.id=comments1_.article_id
   *             and (
   *                 comments1_.active = 1
   *             )
   *     where
   *         article0_.id=?
   */
  @Sql(statements = {
      "INSERT INTO articles(id) VALUES (1);",
      "INSERT INTO comments(id, article_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_entity_graph() {

    Optional<Article> actual = target.findById_EntityGraph(1);

    assertThat(actual.get().getComments(), hasSize(2));
  }

  /**
   * Hibernate:
   *     select
   *         article0_.id as id1_0_0_,
   *         comments1_.id as id1_1_1_,
   *         comments1_.active as active2_1_1_,
   *         comments1_.article_id as article_3_1_1_,
   *         comments1_.article_id as article_3_1_0__,
   *         comments1_.id as id1_1_0__
   *     from
   *         articles article0_
   *     inner join
   *         comments comments1_
   *             on article0_.id=comments1_.article_id
   *             and (
   *                 comments1_.active = 1
   *             )  
   *             and (
   *                 comments1_.active=0
   *             )
   *     where
   *         article0_.id=?
   */
  @Sql(statements = {
      "INSERT INTO articles(id) VALUES (1);",
      "INSERT INTO comments(id, article_id, active) VALUES (11, 1, 1), (12, 1, 1), (13, 1, 0);"
  })
  @Transactional(readOnly = true)
  @Test
  public void test_inactive() {

    Optional<Article> actual = target.findById_Inactive(1);

    assertThat(actual.get().getComments(), hasSize(1));// not work
  }
}
