package com.example;

public class Exception3 extends RuntimeException{
    public Exception3(String message) {
        super("Exception 3:" + message);
    }
}
