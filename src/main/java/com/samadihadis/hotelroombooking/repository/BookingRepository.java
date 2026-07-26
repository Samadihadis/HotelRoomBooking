package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.Booking;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookingState(BookingState bookingState);

    // رزروهای یک کاربر خاص
    List<Booking> findByUserId(Long userId);

    // رزروهای یک اتاق خاص
    List<Booking> findByRoomId(Long roomId);

    // چک کردن تداخل تاریخ برای یک اتاق
    @Query("SELECT b FROM Booking b WHERE b.room.id = :roomId " +
            "AND b.bookingState != 'CANCELLED' " +
            "AND ((b.checkinDate <= :checkoutDate AND b.checkoutDate >= :checkinDate))")
    List<Booking> findConflictingBookings(@Param("roomId") Long roomId,
                                          @Param("checkinDate") LocalDate checkinDate,
                                          @Param("checkoutDate") LocalDate checkoutDate);

    // رزروهای فعال در یک بازه زمانی
    List<Booking> findByCheckinDateBetweenOrCheckoutDateBetween(LocalDate start1, LocalDate end1,
                                                                LocalDate start2, LocalDate end2);
}
