package com.example;

import java.util.List;

public interface PostRepositoryCustom {
    List<Post> findByKeyword(String q, int page, int size);
}
