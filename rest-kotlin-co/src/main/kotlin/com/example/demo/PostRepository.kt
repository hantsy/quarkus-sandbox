package com.example.demo


import io.quarkus.hibernate.reactive.panache.common.WithSession
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheRepositoryBase
import io.quarkus.panache.common.Sort
import io.smallrye.mutiny.Uni
import jakarta.enterprise.context.ApplicationScoped
import java.util.*


@ApplicationScoped
//@WithSession
class PostRepository : PanacheRepositoryBase<Post, UUID> {
    fun findByKeyword(q: String?, offset: Int, limit: Int): Uni<List<Post>> {
        return if (q == null || q.trim { it <= ' ' }.isEmpty()) {
            this.findAll(Sort.descending("createdAt"))
                .page(offset / limit, limit)
                .list()
        } else {
            this.find(
                "title like ?1 or content like ?1", Sort.descending("createdAt"),
                "%$q%"
            )
                .page(offset / limit, limit)
                .list()
        }
    }

    fun countByKeyword(q: String?): Uni<Long> {
        return if (q == null || q.trim { it <= ' ' }.isEmpty()) {
            this.count()
        } else {
            this.count("title like ?1 or content like ?1", "%$q%")
        }
    }

    fun update(post: Post): Uni<Int> {
        return this.update(
            "update Post p set p.title=? and p.content=? where p.id=?",
            listOf(post.title, post.content, post.id)
        )
    }
}

