package com.todo.todo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.repository.Repository;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Todo extends AuditingEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title;
    private String content;

}
