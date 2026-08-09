package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.entity.Payment;
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
    public ResponseEntity<Payment> createPayment(@Valid @RequestBody Payment payment) {
        Payment createdPayment = paymentService.createPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPayment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(
                String.format("پرداخت با شناسه %d حذف شد.", id)
        );
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Payment>> getPaymentByState(@PathVariable PaymentState paymentState) {
        return ResponseEntity.ok(paymentService.getPaymentByState(paymentState));
    }

    @GetMapping("/method/{method}")
    public ResponseEntity<List<Payment>> getPaymentByMethod(@PathVariable PaymentMethod paymentMethod) {
        return ResponseEntity.ok(paymentService.getPaymentByMethod(paymentMethod));
    }

    @GetMapping("/booking-id/{bookingId}")
    public ResponseEntity<Payment> getPaymentByBookingId(@PathVariable Long bookingId){
        return ResponseEntity.ok(paymentService.getPaymentByBookingId(bookingId));
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<Payment>> getSuccessfulPaymentsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(paymentService.getSuccessfulPaymentsByUserId(userId));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<Payment> updatePaymentState(
            @PathVariable Long id,
            @RequestParam PaymentState newState) {
        return ResponseEntity.ok(paymentService.updatePaymentState(id, newState));
    }

}

