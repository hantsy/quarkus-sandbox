package com.example.demo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Input;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Input
public class CreatePost {

    @NotEmpty// bean validation dose not work.
    @Length(min = 5)
    String title;

    String content;
}
