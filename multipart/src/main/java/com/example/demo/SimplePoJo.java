package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.ws.rs.FormParam;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class SimplePoJo {
    String test;
    boolean checked = false;
    Choice choice = Choice.NO;
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt;
}
