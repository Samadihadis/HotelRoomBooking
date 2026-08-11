package com.samadihadis.hotelroombooking.controller;


import com.samadihadis.hotelroombooking.dto.paymentdto.PaymentCreateRequest;
import com.samadihadis.hotelroombooking.dto.paymentdto.PaymentResponse;
import com.samadihadis.hotelroombooking.enumes.PaymentMethod;
import com.samadihadis.hotelroombooking.enumes.PaymentState;
import com.samadihadis.hotelroombooking.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@Valid @RequestBody PaymentCreateRequest request) {
        PaymentResponse created = paymentService.createPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(
                String.format("پرداخت با شناسه %d حذف شد.", id)
        );
    }

    @GetMapping("/state/{paymentState}")
    public ResponseEntity<List<PaymentResponse>> getPaymentByState(@PathVariable PaymentState paymentState) {
        return ResponseEntity.ok(paymentService.getPaymentByState(paymentState));
    }

    @GetMapping("/method/{paymentMethod}")
    public ResponseEntity<List<PaymentResponse>> getPaymentByMethod(@PathVariable PaymentMethod paymentMethod) {
        return ResponseEntity.ok(paymentService.getPaymentByMethod(paymentMethod));
    }

    @GetMapping("/booking-id/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<PaymentResponse>> getSuccessfulPaymentsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(paymentService.getSuccessfulPaymentsByUserId(userId));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<PaymentResponse> updatePaymentState(
            @PathVariable Long id,
            @RequestParam PaymentState newState) {
        return ResponseEntity.ok(paymentService.updatePaymentState(id, newState));
    }
}