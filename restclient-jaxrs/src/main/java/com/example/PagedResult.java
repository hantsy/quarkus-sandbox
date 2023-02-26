package com.example;

import java.io.Serializable;
import java.util.List;

public class PagedResult implements Serializable {

    private List<Post> content;
    private Long count;

    public static PagedResult of(List<Post> content, Long count) {
        PagedResult page = new PagedResult();
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
