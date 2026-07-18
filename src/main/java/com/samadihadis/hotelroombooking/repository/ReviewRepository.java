package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface ReviewRepository extends JpaRepository<Review, Long> {

    // نظرات یک هتل
    List<Review> findByHotelId(Long hotelId);

    // نظرات یک کاربر
    List<Review> findByUserId(Long userId);

    // محاسبه میانگین امتیاز یک هتل
    @Query("SELECT AVG(r.rate) FROM Review r WHERE r.hotel.id = :hotelId")
    Double getAverageRateByHotelId(@Param("hotelId") Long hotelId);
}
