package com.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.status;

@RestControllerAdvice
public class PostExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity notFound(PostNotFoundException ex/*, WebRequest req*/) {
        Map<String, String> errors = new HashMap<>();
        errors.put("entity", "POST");
        errors.put("id", "" + ex.getSlug());
        errors.put("code", "not_found");
        errors.put("message", ex.getMessage());

        return status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(Exception1.class)
    public ResponseEntity handleException1(Exception1 e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

//    @ExceptionHandler(Exception2.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public void handleException2(Exception2 e){
////    public void handleException2(Exception2 e, HttpServletResponse response) throws IOException {
////        response.setHeader("Content-Type", "application/json");
////        response.getWriter().write(e.getMessage());
////        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
//    }

    @ExceptionHandler(Exception2.class)
    public void handleException2(Exception2 e, HttpServletResponse response) throws IOException {
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(e.getMessage());
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @ExceptionHandler(Exception3.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map handleException3(Exception3 e) {
        Map map= new HashMap();
        map.put("exception", e.getMessage());
        return map;
    }


}
