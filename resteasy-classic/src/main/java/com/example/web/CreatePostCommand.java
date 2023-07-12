package com.example.web;

import jakarta.validation.constraints.NotEmpty;

public record CreatePostCommand(@NotEmpty String title,
                                @NotEmpty String content) {
// ensure JSON-B works with Record.
// see: https://rmannibucau.metawerx.net/java-14-record-class-type-and-json-b.html
// and https://dev.to/cchacin/java-14-records-with-jakartaee-json-b-160n
// Update:: not a problem now.
//    @JsonbCreator()
//    public CreatePostCommand {
//    }
}
