package com.example.demo;

import jakarta.ws.rs.FormParam;
import lombok.Data;
import org.jboss.resteasy.reactive.PartType;

import java.io.File;

@Data
public class MultipartBody {
    @FormParam("file")
    @PartType("text/csv")
    File part;

    @FormParam("test")
    String test;

    @FormParam("checked")
    boolean checked = false;

    @FormParam("choice")
    Choice choice = Choice.NO;

}
