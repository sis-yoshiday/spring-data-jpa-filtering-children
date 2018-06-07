package com.example.app.case1;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import org.hibernate.annotations.Where;

@Getter
@Entity
@Table(name = "articles")
public class Article {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer id;

  @Where(clause = "active = true")
  @OneToMany(mappedBy = "article")
  private Set<Comment> comments;
}
