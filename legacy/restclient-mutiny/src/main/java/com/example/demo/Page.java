package com.example.demo;

import java.util.List;

public record Page<T>(List<T> content, Long count) {
}
