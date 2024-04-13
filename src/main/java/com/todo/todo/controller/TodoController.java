
package com.todo.todo.controller;

import com.todo.todo.entity.Todo;
import com.todo.todo.repository.TodoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        return todoRepository.findAll(PageRequest.of(page, size, Sort.by("modifiedDate").descending()));
    }

    @GetMapping("/api/todo/search")
    public Page<Todo> getSearchTodoList(
        @RequestParam(required = true) String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        return todoRepository.findByTitleContainingIgnoreCase(
                keyword,
                PageRequest.of(page, size, Sort.by("modifiedDate").descending())
        );
    }

    @PostMapping("/api/todo/create")
    public ResponseEntity<Todo> createTodo(@RequestBody Todo todo){
        Todo savedTodo = todoRepository.save(todo);
        return new ResponseEntity<>(savedTodo, HttpStatus.CREATED);
    }

    @PostMapping("/api/todo/create/test")
    public void createTestTodo(){
        for (int i = 0; i < 100; i++) {
            Todo todo = new Todo();
            todo.setTitle("Todo " + (i + 1));
            todo.setContent("Content " + (i + 1));
            todo.setStartDt(LocalDate.now());
            todo.setEndDt(LocalDate.now().plusDays(2));
            todoRepository.save(todo);
        }
        System.out.println("Test data generated successfully.");
    }

    @DeleteMapping("/api/todo/delete/{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable Long id){
        todoRepository.deleteById(id);
        return new ResponseEntity<>(new String("삭제 성공".getBytes(), StandardCharsets.UTF_8), HttpStatus.OK);
    }

    @GetMapping("/api/todo/detail/{id}")
    public ResponseEntity<Todo> detailTodo(@PathVariable Long id){
        try {
            Todo todo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("찾을 수 없는 id"));
            return new ResponseEntity<>(todo, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/api/todo/modify/{id}")
    public ResponseEntity<?> modifyTodo(@PathVariable Long id, @RequestBody @Valid Todo todo, BindingResult result){

        if(result.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();

            for(FieldError error : result.getFieldErrors()) {
                errorMap.put(error.getField(), error.getDefaultMessage());
            }

            return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
        }

        try {
            Todo existTodo = todoRepository.findById(id).orElseThrow(() -> new RuntimeException("찾을 수 없는 ID : " + id));

            existTodo.setTitle(todo.getTitle());
            existTodo.setContent(todo.getContent());
            existTodo.setStartDt(todo.getStartDt());
            existTodo.setEndDt(todo.getEndDt());

            todoRepository.save(existTodo);

            return new ResponseEntity<>(existTodo, HttpStatus.OK);
        }catch (RuntimeException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}