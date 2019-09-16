package com.example;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, String>//, JpaSpecificationExecutor<Post>
{

}
