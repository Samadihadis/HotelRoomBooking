package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.dto.BookingCreateRequest;
import com.samadihadis.hotelroombooking.dto.BookingResponse;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@Valid @RequestBody BookingCreateRequest request) {
        return ResponseEntity.ok(bookingService.createBooking(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(String.format("رزرو با شناسه %d حذف شد.", id));
    }

    @GetMapping("/state/{bookingState}")
    public ResponseEntity<List<BookingResponse>> getBookingsByState(@PathVariable BookingState bookingState) {
        return ResponseEntity.ok(bookingService.findBookingsByState(bookingState));
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(bookingService.findBookingsByUserId(userId));
    }

    @GetMapping("/room-id/{roomId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByRoomId(@PathVariable Long roomId) {
        return ResponseEntity.ok(bookingService.findBookingsByRoomId(roomId));
    }

    @GetMapping("/room-id-checkin-checkout/{roomId}/{checkinDate}/{checkoutDate}")
    public ResponseEntity<List<BookingResponse>> getConflictingBookings(
            @PathVariable Long roomId,
            @PathVariable LocalDate checkinDate,
            @PathVariable LocalDate checkoutDate) {
        return ResponseEntity.ok(bookingService.getConflictingBookings(roomId, checkinDate, checkoutDate));
    }

    @GetMapping("/date-range/{start}/{end}")
    public ResponseEntity<List<BookingResponse>> getBookingsByDateRange(
            @PathVariable LocalDate start,
            @PathVariable LocalDate end) {
        return ResponseEntity.ok(bookingService.getBookingsByDateRange(start, end));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BookingResponse> cancelBooking(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @PatchMapping("/{id}/checkin")
    public ResponseEntity<BookingResponse> checkIn(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkIn(id));
    }

    @PatchMapping("/{id}/checkout")
    public ResponseEntity<BookingResponse> checkOut(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.checkOut(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookingResponse> updateBooking(
            @PathVariable Long id,
            @RequestParam LocalDate checkinDate,
            @RequestParam LocalDate checkoutDate) {
        return ResponseEntity.ok(bookingService.updateBooking(id, checkinDate, checkoutDate));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<BookingResponse> updateBookingState(
            @PathVariable Long id,
            @RequestParam BookingState newState) {
        return ResponseEntity.ok(bookingService.updateBookingState(id, newState));
    }
}