package org.example;

import lombok.Value;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

@Value
public class UpdatePostRequest implements Serializable {

    @NotBlank
    @Size(max = 100)
    String title;

    @NotBlank
    String content;
}
