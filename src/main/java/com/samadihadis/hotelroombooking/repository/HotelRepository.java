package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findByRate(Integer rate);

    List<Hotel> findByStarRate(Integer starRating);

    // جستجوی هتل‌ها با امتیاز بالاتر از مقدار مشخص
    List<Hotel> findByRateGreaterThanEqual(Integer rate);

    // جستجو بر اساس نام (برای سرچ)
    List<Hotel> findByNameContainingIgnoreCase(String name);

    // جستجوی ترکیبی
    List<Hotel> findByStarRatingAndRateGreaterThanEqual(Integer starRating, Integer rate);
}
