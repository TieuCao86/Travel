package com.example.travel.specification;

import com.example.travel.model.Tour;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class TourSpecification {

    public static Specification<Tour> tenTourContains(String tenTour) {
        return (root, query, cb) -> {
            if (tenTour == null || tenTour.isEmpty()) return null;
            return cb.like(cb.lower(root.get("tenTour")), "%" + tenTour.toLowerCase() + "%");
        };
    }

    public static Specification<Tour> loaiTourEqual(String loaiTour) {
        return (root, query, cb) -> {
            if (loaiTour == null || loaiTour.isEmpty()) return null;
            return cb.equal(cb.lower(root.get("loaiTour")), loaiTour.toLowerCase());
        };
    }

    public static Specification<Tour> thanhPhoEqual(String thanhPho) {
        return (root, query, cb) -> {
            if (thanhPho == null || thanhPho.isEmpty()) return null;

            Join<Object, Object> tp = root.join("thanhPhos", JoinType.LEFT);
            return cb.equal(cb.lower(tp.get("tenThanhPho")), thanhPho.toLowerCase());
        };
    }

    public static Specification<Tour> giaTrongKhoang(Double minGia, Double maxGia) {
        return (root, query, cb) -> {
            Join<Object, Object> lk = root.join("lichKhoiHanhs", JoinType.LEFT);
            Join<Object, Object> g = lk.join("giaLichKhoiHanhs", JoinType.LEFT);

            var predicate = cb.conjunction();

            if (minGia != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(g.get("gia"), minGia));
            }

            if (maxGia != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(g.get("gia"), maxGia));
            }

            return predicate;
        };
    }

    public static Specification<Tour> diaDiemEqual(String loaiTour, String thanhPho) {
        return (root, query, cb) -> {
            if (thanhPho == null || thanhPho.isEmpty()) return null;

            Join<Object, Object> tp = root.join("thanhPhos", JoinType.LEFT);

            if ("noidia".equalsIgnoreCase(loaiTour)) {
                return cb.equal(cb.lower(tp.get("tenThanhPho")), thanhPho.toLowerCase());
            } else if ("nuocngoai".equalsIgnoreCase(loaiTour)) {
                return cb.equal(cb.lower(tp.join("quocGia", JoinType.LEFT).get("tenQuocGia")), thanhPho.toLowerCase());
            }

            return null; // không lọc nếu loaiTour khác
        };
    }

}
