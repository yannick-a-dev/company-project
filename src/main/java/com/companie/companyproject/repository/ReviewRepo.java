package com.companie.companyproject.repository;

import com.companie.companyproject.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends JpaRepository<Review,Long> {
    List<Review> findAllByAdId(Long adId);
}
