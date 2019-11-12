package com.example;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExceptionTestsController {

    @GetMapping("e1")
    public ResponseEntity exception1(){
        throw new Exception1("error 1");
    }

    @GetMapping("e2")
    public ResponseEntity exception2(){
        throw new Exception2("error 2");
    }


    @GetMapping("e3")
    public ResponseEntity exception3(){
        throw new Exception3("error 3");
    }


}


