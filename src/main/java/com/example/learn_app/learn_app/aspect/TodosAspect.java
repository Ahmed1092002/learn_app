package com.example.learn_app.learn_app.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.learn_app.learn_app.entity.Todos;
import com.example.learn_app.learn_app.repository.TodosRepository;
import com.example.learn_app.learn_app.repository.UserRepository;

@Component
@Aspect
public class TodosAspect {

    private final UserRepository userRepository;
    private final TodosRepository todosRepository;

    public TodosAspect(UserRepository userRepository, TodosRepository todosRepository) {
        this.userRepository = userRepository;
        this.todosRepository = todosRepository;
    }

    // Ensure only the owner can delete a todo
    @Before("execution(* com.example.learn_app.learn_app.service.TodosService.deleteTodo(..)) && args(todoId)")
    public void checkDeleteTodoOwnership(JoinPoint joinPoint, Long todoId) {
        requireTodoOwnedByCurrentUser(todoId);
    }

    // Ensure only the owner can update a todo; also force owner id on payload
    @Before("execution(* com.example.learn_app.learn_app.service.TodosService.updateTodo(..)) && args(todoDto)")
    public void checkUpdateTodoOwnership(JoinPoint joinPoint, Todos todo) {
        if (todo == null || todo.getId() == null) {
            throw new RuntimeException("Todo id is required for update");
        }
        Long currentUserId = getCurrentUserId();
        requireTodoOwnedByCurrentUser(todo.getId());
        // prevent changing ownership via payload
        todo.setUserId(currentUserId);
    }

    // Guard reading a single todo by id
    @Before("execution(* com.example.learn_app.learn_app.service.TodosService.getTodoById(..)) && args(todoId)")
    public void checkGetTodoOwnership(JoinPoint joinPoint, Long todoId) {
        requireTodoOwnedByCurrentUser(todoId);
    }

   
    private Long getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("No authenticated user");
        }

        Object principal = auth.getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            String p = (String) principal;
            if ("anonymousUser".equalsIgnoreCase(p)) {
                throw new RuntimeException("No authenticated user");
            }
            username = p;
        } else {
            username = principal.toString();
        }

        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new RuntimeException("User not found"))
                .getId();
    }

    private Todos requireTodoOwnedByCurrentUser(Long todoId) {
        Long currentUserId = getCurrentUserId();
        Todos existing = todosRepository.findById(todoId)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + todoId));
        if (existing.getUserId() == null || !existing.getUserId().equals(currentUserId)) {
            throw new RuntimeException("You are not allowed to access this todo");
        }
        return existing;
    }

}
