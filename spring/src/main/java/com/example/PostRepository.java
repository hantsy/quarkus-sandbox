package com.example;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface PostRepository extends JpaRepository<Post, UUID>
, PostRepositoryCustom
//, JpaSpecificationExecutor<Post>
{

}
