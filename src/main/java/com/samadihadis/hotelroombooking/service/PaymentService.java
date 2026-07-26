package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.entity.Booking;
import com.samadihadis.hotelroombooking.entity.Payment;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.enumes.PaymentMethod;
import com.samadihadis.hotelroombooking.enumes.PaymentState;
import com.samadihadis.hotelroombooking.repository.BookingRepository;
import com.samadihadis.hotelroombooking.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;

    @Transactional
    public Payment createPayment(Payment payment) {

        validatePayment(payment);

        Booking booking = bookingService.findBookingById(payment.getBooking().getId());

        if (booking.getBookingState() != BookingState.PENDING) {
            throw new RuntimeException("فقط رزروهای در انتظار قابلیت پرداخت دارند.");
        }

        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new RuntimeException("برای این رزرو قبلاً پرداختی ثبت شده است.");
        }

        payment.setPaymentDate(LocalDate.now());
        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getPaymentState() == PaymentState.SUCCESS) {
            Booking newBooking = savedPayment.getBooking();
            newBooking.setBookingState(BookingState.CONFIRMED);
            bookingService.updateBookingState(newBooking.getId(), BookingState.CONFIRMED);
        }
        return savedPayment;
    }

    public Payment getPaymentById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("پرداخت با شناسه %d یافت نشد.", id)
                ));
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = getPaymentById(id);

        if (payment.getPaymentState() == PaymentState.SUCCESS) {
            throw new RuntimeException("امکان حذف پرداخت‌های موفق وجود ندارد.");
        }
        paymentRepository.deleteById(id);
    }

    public List<Payment> getPaymentByState(PaymentState paymentState) {
        if (paymentState == null) {
            throw new RuntimeException("وضعیت پرداخت نمی‌تواند خالی باشد.");
        }
        return paymentRepository.findByPaymentState(paymentState);
    }

    public List<Payment> getPaymentByMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new RuntimeException("روش پرداخت نمی‌تواند خالی باشد.");
        }
        return paymentRepository.findByPaymentMethod(paymentMethod);
    }

    public Payment getPaymentByBookingId(Long bookingId) {
        if (bookingId == null) {
            throw new RuntimeException("شناسه رزرو نمی‌تواند خالی باشد.");
        }
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("پرداختی برای رزرو با شناسه %d یافت نشد.", bookingId)));
    }

    public List<Payment> getSuccessfulPaymentsByUserId(Long userId) {

        if (userId == null) {
            throw new RuntimeException("کاربر یافت نشد.");
        }
        return paymentRepository.findSuccessfulPaymentsByUserId(userId);
    }

    @Transactional
    public Payment updatePaymentState(Long paymentId, PaymentState newState) {
        Payment payment = getPaymentById(paymentId);

        if (payment.getPaymentState() == PaymentState.SUCCESS) {
            throw new RuntimeException("پرداخت موفق قابل تغییر نیست.");
        }

        payment.setPaymentState(newState);

        if (newState == PaymentState.SUCCESS) {
            Booking booking = payment.getBooking();
             booking.setBookingState(BookingState.CONFIRMED);
        }

        if (newState == PaymentState.SUCCESS) {
            Booking booking = payment.getBooking();
            booking.setBookingState(BookingState.CONFIRMED);
            bookingRepository.save(booking);
        }

        return paymentRepository.save(payment);
    }

    private void validatePayment(Payment payment) {
        if (payment.getBooking() == null || payment.getBooking().getId() == null) {
            throw new RuntimeException("شناسه رزرو الزامی است.");
        }

        if (payment.getAmount() == null) {
            throw new RuntimeException("مبلغ پرداخت نمی‌تواند خالی باشد.");
        }

        if (payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("مبلغ پرداخت باید بیشتر از صفر باشد.");
        }

        if (payment.getPaymentMethod() == null) {
            throw new RuntimeException("روش پرداخت نمی‌تواند خالی باشد.");
        }

        if (payment.getPaymentState() == null) {
            throw new RuntimeException("وضعیت پرداخت نمی‌تواند خالی باشد.");
        }
    }
}
