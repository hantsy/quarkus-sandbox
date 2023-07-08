/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example;

import java.util.UUID;

/**
 *
 * @author hantsy
 */
public class PostNotFoundException extends RuntimeException {

    UUID id;
    public PostNotFoundException(UUID id) {
        super("Post #" + id + " was not found");
    }


    public UUID getId() {
        return id;
    }
}
