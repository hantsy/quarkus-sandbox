package com.example.demo;


import io.smallrye.common.constraint.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Input;
import org.eclipse.microprofile.graphql.NonNull;
import org.eclipse.microprofile.graphql.Type;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@Input
@NoArgsConstructor
@AllArgsConstructor
public class CreatePost {

    @NotEmpty// bean validation dose not work.
    @NonNull
    String title;

    String content;
}
