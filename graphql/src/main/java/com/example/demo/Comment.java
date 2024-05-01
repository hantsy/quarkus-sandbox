package com.example.demo;

import org.eclipse.microprofile.graphql.Id;

public record Comment(
        @Id
        String id,
        String content
) {
}
