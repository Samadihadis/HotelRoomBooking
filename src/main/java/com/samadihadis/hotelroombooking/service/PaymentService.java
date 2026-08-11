package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.dto.paymentdto.PaymentCreateRequest;
import com.samadihadis.hotelroombooking.dto.paymentdto.PaymentResponse;
import com.samadihadis.hotelroombooking.entity.Booking;
import com.samadihadis.hotelroombooking.entity.Payment;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.enumes.PaymentMethod;
import com.samadihadis.hotelroombooking.enumes.PaymentState;
import com.samadihadis.hotelroombooking.mapper.PaymentMapper;
import com.samadihadis.hotelroombooking.repository.BookingRepository;
import com.samadihadis.hotelroombooking.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final PaymentMapper paymentMapper;
    private final BookingService bookingService;

    @Transactional
    public PaymentResponse createPayment(PaymentCreateRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("رزرو با شناسه %d یافت نشد.", request.getBookingId())
                ));

        if (booking.getBookingState() != BookingState.PENDING) {
            throw new RuntimeException("فقط رزروهای در انتظار قابلیت پرداخت دارند.");
        }

        if (paymentRepository.findByBookingId(booking.getId()).isPresent()) {
            throw new RuntimeException("برای این رزرو قبلاً پرداختی ثبت شده است.");
        }

        Payment payment = paymentMapper.toEntity(request);
        payment.setBooking(booking);
        payment.setPaymentDate(LocalDate.now());

        Payment savedPayment = paymentRepository.save(payment);

        if (savedPayment.getPaymentState() == PaymentState.SUCCESS) {
            bookingService.updateBookingState(booking.getId(), BookingState.CONFIRMED);
        }

        return paymentMapper.toResponse(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponse findPaymentById(Long id) {
        return paymentMapper.toResponse(findPaymentEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deletePayment(Long id) {
        Payment payment = findPaymentEntityById(id);

        if (payment.getPaymentState() == PaymentState.SUCCESS) {
            throw new RuntimeException("امکان حذف پرداخت‌های موفق وجود ندارد.");
        }
        paymentRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentByState(PaymentState paymentState) {
        if (paymentState == null) {
            throw new RuntimeException("وضعیت پرداخت نمی‌تواند خالی باشد.");
        }
        return paymentRepository.findByPaymentState(paymentState)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentByMethod(PaymentMethod paymentMethod) {
        if (paymentMethod == null) {
            throw new RuntimeException("روش پرداخت نمی‌تواند خالی باشد.");
        }
        return paymentRepository.findByPaymentMethod(paymentMethod)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaymentResponse getPaymentByBookingId(Long bookingId) {
        if (bookingId == null) {
            throw new RuntimeException("شناسه رزرو نمی‌تواند خالی باشد.");
        }
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("پرداختی برای رزرو با شناسه %d یافت نشد.", bookingId)
                ));
        return paymentMapper.toResponse(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getSuccessfulPaymentsByUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("شناسه کاربر نمی‌تواند خالی باشد.");
        }
        return paymentRepository.findSuccessfulPaymentsByUserId(userId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional
    public PaymentResponse updatePaymentState(Long paymentId, PaymentState newState) {
        Payment payment = findPaymentEntityById(paymentId);

        if (payment.getPaymentState() == PaymentState.SUCCESS) {
            throw new RuntimeException("پرداخت موفق قابل تغییر نیست.");
        }

        payment.setPaymentState(newState);

        if (newState == PaymentState.SUCCESS) {
            bookingService.updateBookingState(payment.getBooking().getId(), BookingState.CONFIRMED);
        }

        Payment updated = paymentRepository.save(payment);
        return paymentMapper.toResponse(updated);
    }

    private Payment findPaymentEntityById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("پرداخت با شناسه %d یافت نشد.", id)
                ));
    }
}