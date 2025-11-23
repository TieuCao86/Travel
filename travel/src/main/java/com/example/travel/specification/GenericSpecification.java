package com.example.travel.specification;

import com.example.travel.model.Tour;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

public class GenericSpecification<T> {

    // Lọc chuỗi chứa (LIKE %value%)
    public Specification<T> stringContains(String field, String value) {
        return (root, query, cb) -> {
            if (value == null || value.isEmpty()) return cb.conjunction();
            return cb.like(cb.lower(root.get(field)), "%" + value.toLowerCase() + "%");
        };
    }

    // Lọc chuỗi bằng nhau
    public Specification<T> stringEqual(String field, String value) {
        return (root, query, cb) -> {
            if (value == null || value.isEmpty()) return cb.conjunction();
            return cb.equal(cb.lower(root.get(field)), value.toLowerCase());
        };
    }

    // Lọc số trong khoảng
    public Specification<T> numberBetween(String field, Number min, Number max) {
        return (root, query, cb) -> {
            var predicate = cb.conjunction();
            if (min != null) predicate = cb.and(predicate, cb.ge(root.get(field), min));
            if (max != null) predicate = cb.and(predicate, cb.le(root.get(field), max));
            return predicate;
        };
    }

    // Lọc quan hệ: entity liên kết và field trong entity đó
    public Specification<T> relatedEntityEqual(String relation, String field, String value) {
        return (root, query, cb) -> {
            if (value == null || value.isEmpty()) return cb.conjunction();
            Join<Object, Object> join = root.join(relation, JoinType.LEFT);
            return cb.equal(cb.lower(join.get(field)), value.toLowerCase());
        };
    }

    public Specification<Tour> fetchRelations() {
        return (root, query, cb) -> {
            root.fetch("danhGiaList", JoinType.LEFT);
            root.fetch("phuongTiens", JoinType.LEFT);
            root.fetch("hinhAnhTourList", JoinType.LEFT);
            query.distinct(true); // tránh duplicate khi join nhiều-to-many
            return cb.conjunction();
        };
    }

}
