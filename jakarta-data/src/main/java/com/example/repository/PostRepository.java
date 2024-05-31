package com.example.repository;

import java.util.UUID;

import com.example.domain.Post;

import jakarta.data.repository.CrudRepository;
import jakarta.data.repository.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, UUID> {

}
