package com.example.web;

import jakarta.ws.rs.core.Response;

import java.util.Map;

public record ProblemDetail(int status, String message, Map<String, Object> properties) {

    public static ProblemDetail of(int statusCode, String message) {
        return new ProblemDetail(statusCode, message, null);
    }

    public static ProblemDetail of(Response.Status status, String message) {
        return new ProblemDetail(status.getStatusCode(), message, null);
    }
}

