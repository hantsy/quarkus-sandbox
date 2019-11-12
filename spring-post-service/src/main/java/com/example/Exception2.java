package com.example;

public class Exception2 extends RuntimeException{
    public Exception2(String message) {
        super("Exception 2:" + message);
    }
}
