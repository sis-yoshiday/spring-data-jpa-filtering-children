package com.example.app.case2;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "departments")
public class Department {

  @Id
  @GeneratedValue
  @Column(name = "id")
  private Integer id;

  @OneToMany(mappedBy = "department")
  private Set<User> users;
}
