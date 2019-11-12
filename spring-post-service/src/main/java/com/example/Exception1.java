package com.example;

public class Exception1 extends RuntimeException{
    public Exception1(String message) {
        super("Exception 1 :" + message);
    }
}
