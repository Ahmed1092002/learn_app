package com.example.learn_app.learn_app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.learn_app.learn_app.dto.TodosDto;
import com.example.learn_app.learn_app.dto.UpdateTodoDto;
import com.example.learn_app.learn_app.entity.Todos;
import com.example.learn_app.learn_app.repository.TodosRepository;
import com.example.learn_app.learn_app.repository.UserRepository;

@Service
public class TodosService {
    private final TodosRepository todosRepository;
    private final UserRepository userRepository;

    public TodosService(TodosRepository todosRepository, UserRepository userRepository) {
        this.todosRepository = todosRepository;
        this.userRepository = userRepository;
    }

    private Long getCurrentUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            username = principal.toString();
            if ("anonymousUser".equals(username))
                throw new RuntimeException("No authenticated user");
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    public List<Todos> getTodosByUserId() {
        Long userId = getCurrentUserId();

        return todosRepository.findByUserId(userId);
    }

    public Todos createTodo(TodosDto todoDto) {
        Long userId = getCurrentUserId();

        Todos todo = new Todos();
        todo.setTitle(todoDto.title);
        todo.setDescription(todoDto.description);
        todo.setCompleted(todoDto.completed);
        todo.setUserId(userId);
        return todosRepository.save(todo);
    }

    public String deleteTodo(Long todoId) {

        todosRepository.deleteById(todoId);
        return "Todo deleted successfully";
    }

    public Todos updateTodo(UpdateTodoDto todoDto) {
        Long userId = getCurrentUserId();
        Todos existingTodo = todosRepository.findById(todoDto.getId())
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + todoDto.getId()));
        if (!existingTodo.getUserId().equals(userId)) {
            throw new RuntimeException("You are not allowed to update this todo");
        }
        existingTodo.setTitle(todoDto.getTitle());
        existingTodo.setDescription(todoDto.getDescription());
        existingTodo.setCompleted(todoDto.isCompleted());
        return todosRepository.save(existingTodo);
    }

    public Todos getTodoById(Long todoId) {
        return todosRepository.findById(todoId).orElseThrow(
                () -> new RuntimeException("Todo not found with id: " + todoId));
    }

}
