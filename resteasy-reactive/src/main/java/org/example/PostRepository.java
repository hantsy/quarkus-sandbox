package org.example;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class PostRepository implements PanacheRepositoryBase<Post, UUID> {

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

    public Uni<List<Post>> findByKeyword(String q, int offset, int limit) {
        if (q == null || q.trim().isEmpty()) {
            return this.findAll(Sort.descending("createdAt"))
                    .page(offset / limit, limit)
                    .list();
        } else {
            return this.find("title like ?1 or content like ?1", Sort.descending("createdAt"), '%' + q + '%')
                    .page(offset / limit, limit)
                    .list();
        }
    }

    public Uni<Long> countByKeyword(String q) {
        if (q == null || q.trim().isEmpty()) {
            return this.count();
        } else {
            return this.count("title like ?1 or content like ?1", '%' + q + '%');
        }
    }


    @Transactional
    public Uni<Post> save(Post post) {
        return this.persist(post)
                .map(v -> post);
    }

    @Transactional
    public Uni<Integer> update(Post post) {
        return this.update("update Post p set p.title=? and p.content=? where p.id=?", List.of(post.getTitle(), post.getContent(), post.getId()));
    }

}
