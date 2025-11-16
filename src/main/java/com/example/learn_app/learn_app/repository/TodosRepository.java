package com.example.learn_app.learn_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learn_app.learn_app.entity.Todos;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long> {
    public List<Todos> findByUserId(Long userId);
    
}
