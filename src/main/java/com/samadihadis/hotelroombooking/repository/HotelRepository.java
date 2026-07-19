package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelsByRate(Integer rate);

    List<Hotel> findHotelsByStarRating(Integer starRating);

    // جستجوی هتل‌ها با امتیاز بالاتر از مقدار مشخص
    List<Hotel> findHotelsByRateGreaterThanEqual(Integer rate);

    // جستجو بر اساس نام (برای سرچ)
    List<Hotel> findHotelsByNameContainingIgnoreCase(String name);

    // جستجوی ترکیبی
    List<Hotel> findHotelsByStarRatingAndRateGreaterThanEqual(Integer starRating, Integer rate);
}
