package com.example.learn_app.learn_app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
@Data
public class TodosDto {
    @NotEmpty(message = "Title cannot be empty")
    public String title;
    public String description;
    public boolean completed;
    
}
