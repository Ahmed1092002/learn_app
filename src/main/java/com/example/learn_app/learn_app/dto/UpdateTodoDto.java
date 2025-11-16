package com.example.learn_app.learn_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTodoDto {
    @NotEmpty(message = "Id cannot be empty")
    @NotBlank(message = "Id cannot be blank")
    @NotNull(message = "Id cannot be null")
    @Min(value = 1, message = "Id must be greater than 0")

    public Long id;

    @NotEmpty(message = "Title cannot be empty")
    public String title;
    public String description;
    public boolean completed;

}
