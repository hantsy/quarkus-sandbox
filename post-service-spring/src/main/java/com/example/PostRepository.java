package com.example;

import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<Post, String>
, PostRepositoryCustom
//, JpaSpecificationExecutor<Post>
{

}
