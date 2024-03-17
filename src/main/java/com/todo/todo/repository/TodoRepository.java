package com.todo.todo.repository;

import com.todo.todo.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}
