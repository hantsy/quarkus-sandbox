package com.example.demo;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record CreatePost(
        @NotEmpty// add hibernate-validator extension, else bean validation dose not work.
        @Length(min = 5)
        String title,
        String content
) {

}
