package com.example.app.case1;

import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

  @Query("select a from Article a left join fetch a.comments where a.id = ?1")
  Optional<Article> findById_JoinFetch(Integer id);

  @Query("select a from Article a where a.id = ?1")
  @EntityGraph(attributePaths = "comments")
  Optional<Article> findById_EntityGraph(Integer id);

  @Query("select a from Article a join a.comments c on c.active = false where a.id = ?1")
  @EntityGraph(attributePaths = "comments")
  Optional<Article> findById_Inactive(Integer id);
}
