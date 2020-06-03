package com.example;

//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.util.StringUtils;
//
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Predicate;
//import javax.persistence.criteria.Root;
//import java.util.ArrayList;
//import java.util.List;

/**
 *
 * @author Hantsy Bai<hantsy@gmail.com>
 *
 */
//public class PostSpecifications {
//
//    private PostSpecifications() {
//    }
//
//    public static Specification<Post> filterByKeywordAndStatus(
//        final String keyword
//    ) {
//        return (Root<Post> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
//            List<Predicate> predicates = new ArrayList<>();
//            if (StringUtils.hasText(keyword)) {
//                predicates.add(
//                    cb.or(
//                        cb.like(root.get(Post_.title), "%" + keyword + "%"),
//                        cb.like(root.get(Post_.content), "%" + keyword + "%")
//                    )
//                );
//            }
//
//            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
//        };
//    }
//
//}
