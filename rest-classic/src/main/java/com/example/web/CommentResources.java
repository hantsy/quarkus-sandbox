package com.example.web;


import com.example.domain.Comment;
import com.example.repository.CommentRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@Path("/comments")
@ApplicationScoped
@RequiredArgsConstructor
public class CommentResources {
    private final CommentRepository commentRepository;

    @GET
    @Path("{id}")
    public Response all() {
        List<Comment> list = this.commentRepository.findAll().list();
        return Response.ok(list).build();
    }

    @GET
    @Path("{id}")
    public Response getById(@PathParam("id") UUID id) {
        return commentRepository.findByIdOptional(id)
                .map(c -> Response.ok(c).build())
                .orElse(Response.status(404).build());

    }
}
