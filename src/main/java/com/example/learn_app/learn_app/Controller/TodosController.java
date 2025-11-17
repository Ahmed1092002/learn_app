package com.example.learn_app.learn_app.Controller;

import java.net.Authenticator;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learn_app.learn_app.dto.TodosDto;
import com.example.learn_app.learn_app.dto.UpdateTodoDto;
import com.example.learn_app.learn_app.entity.Todos;
import com.example.learn_app.learn_app.service.TodosService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/todos")
public class TodosController {
    private final TodosService todosService;

    public TodosController(TodosService todosService) {
        this.todosService = todosService;
    }

    @GetMapping("/listTodos")
    public ResponseEntity<List<Todos>> listTodos() {
        return ResponseEntity.ok(todosService.getTodosByUserId());
    }

    @PutMapping("/updateTodos")
    public ResponseEntity<Todos> updateTodo(@RequestBody UpdateTodoDto todo) {
        return ResponseEntity.ok(todosService.updateTodo(todo));
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Todos> getTodoById(@PathVariable Long id) {
        return ResponseEntity.ok(todosService.getTodoById(id));
    }

    @DeleteMapping("/deleteTodo/{id}")
    public String deleteTodo(@PathVariable Long id) {

        todosService.deleteTodo(id);
        return "Todo deleted successfully";
    }

    @PostMapping("/CreateTodos")
    public ResponseEntity<Todos> createTodoString(@RequestBody TodosDto entity) {
        Todos createdTodo = todosService.createTodo(entity);
        return ResponseEntity.ok(createdTodo);
    }

    @GetMapping("/paginatedTodos")
    public ResponseEntity<?> getPaginatedTodos(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending,
            @RequestParam(defaultValue = "") String title) {

        return ResponseEntity
                .ok(todosService.getTodosByUserIdWithPaginationWithAscending(page, size, sortBy, title, ascending));
    }

}
