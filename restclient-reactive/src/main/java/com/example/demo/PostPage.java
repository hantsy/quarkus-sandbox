package com.example.demo;

import java.io.Serializable;
import java.util.List;

public class PostPage implements Serializable {
    private List<Post> content;
    private Long count;

    public static PostPage of(List<Post> content, Long count) {
        PostPage page = new PostPage();
        page.setContent(content);
        page.setCount(count);

        return page;
    }

    public List<Post> getContent() {
        return content;
    }

    public void setContent(List<Post> content) {
        this.content = content;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
