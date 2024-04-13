package com.todo.todo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class Todo extends AuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    private String content;

    @NotNull(message = "시작일을 입력해주세요.")
    private LocalDate startDt;

    @NotNull(message = "종료일을 입력해주세요.")
    private LocalDate endDt;


}
