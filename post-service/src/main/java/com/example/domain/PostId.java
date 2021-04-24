package com.example.domain;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class PostId implements Serializable {
    private String id;

    public PostId() {
    }

    public PostId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
