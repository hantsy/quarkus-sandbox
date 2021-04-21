package com.example.repository;

import com.example.domain.Post;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, String> {

    public List<Post> findAllPosts() {
        return this.listAll(Sort.descending("createdAt"));
    }

//    public Stream<Post> findByKeyword(String q, int offset, int limit) {
//        if (q == null || q.trim().isEmpty()) {
//            return this.streamAll(Sort.descending("createdAt"))
//                    .skip(offset)
//                    .limit(limit);
//        } else {
//            return this.streamAll(Sort.descending("createdAt"))
//                    .filter(p -> p.title.contains(q) || p.content.contains(q))
//                    .skip(offset)
//                    .limit(limit);
//        }
//    }

    public List<Post> findByKeyword(String q, int offset, int limit) {
        if (q == null || q.trim().isEmpty()) {
            return this.streamAll(Sort.descending("createdAt"))
                    .skip(offset)
                    .limit(limit)
                    .toList();
        } else {
            return this.find("title like ?1 or content like ?1", Sort.descending("createdAt"), '%' + q + '%')
                    .page(offset / limit, limit)
                    .list();
        }
    }

    public Long countByKeyword(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this.count();
        } else {
            return this.count("title like ?1 or content like ?1", '%' + q + '%');
        }
    }


    @Transactional
    public Post save(Post post) {
        this.persist(post);
        return post;
    }

}
