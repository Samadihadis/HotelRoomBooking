package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.Payment;
import com.samadihadis.hotelroombooking.enumes.PaymentMethod;
import com.samadihadis.hotelroombooking.enumes.PaymentState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByPaymentState(PaymentState paymentState);

    List<Payment> findByPaymentMethod(PaymentMethod paymentMethod);

    // پرداخت‌های یک رزرو
    Optional<Payment> findByBookingId(Long bookingId);

    // پرداخت‌های موفق یک کاربر (از طریق Booking)
    @Query("SELECT p FROM Payment p WHERE p.booking.user.id = :userId AND p.paymentState = 'SUCCESS'")
    List<Payment> findSuccessfulPaymentsByUserId(@Param("userId") Long userId);

    boolean existsByBookingId(Long bookingId);
}
