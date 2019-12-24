package com.example;

import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.Serializable;
import java.time.LocalDateTime;

@RegisterForReflection
public class Activity implements Serializable {
    private String type;
    private Object data;
    private LocalDateTime occurred;

    public static Activity of(String type, Object data) {
        var activity = new Activity();
        activity.setData(data);
        activity.setType(type);

        return activity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public LocalDateTime getOccurred() {
        return occurred;
    }

    public void setOccurred(LocalDateTime occurred) {
        this.occurred = occurred;
    }
}
