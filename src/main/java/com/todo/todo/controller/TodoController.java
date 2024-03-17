
package com.todo.todo.controller;

import com.todo.todo.entity.Todo;
import com.todo.todo.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @GetMapping("/test")
    public String test(){
        return "test";
    }

    @GetMapping("/api/todo/list")
    public Page<Todo> getTodoList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return todoRepository.findAll(PageRequest.of(page, size));
    }

    @PostMapping("/api/todo/create")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo){
        Todo savedTodo = todoRepository.save(todo);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    @PostMapping("/api/todo/create/test")
    public void createTestTodo(){
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            Todo todo = new Todo();
            todo.setTitle("Todo " + (i + 1));
            todo.setContent("Content " + (i + 1));
            todoRepository.save(todo);
        }
        System.out.println("Test data generated successfully.");
    }
}