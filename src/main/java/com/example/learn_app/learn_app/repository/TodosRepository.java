package com.example.learn_app.learn_app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learn_app.learn_app.entity.Todos;

@Repository
public interface TodosRepository extends JpaRepository<Todos, Long> {
    public List<Todos> findByUserId(Long userId);

    Page<Todos> findByUserId(Long userId, Pageable pageable);

    Page<Todos> findByUserIdAndTitleContainingIgnoreCase(Long userId, String q, Pageable pageable);

}
