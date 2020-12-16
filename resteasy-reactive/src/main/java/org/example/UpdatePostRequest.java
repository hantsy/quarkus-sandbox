package org.example;

import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Value
public class UpdatePostRequest implements Serializable {

    @NotBlank
    @Size(max = 100)
    String title;

    @NotBlank
    String content;
}
