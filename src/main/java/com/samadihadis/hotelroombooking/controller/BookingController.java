package com.samadihadis.hotelroombooking.controller;


import com.samadihadis.hotelroombooking.entity.Booking;
import com.samadihadis.hotelroombooking.entity.Room;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking){
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings(){
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingById(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return ResponseEntity.ok(
                String.format("رزرو با شناسه %d حذف شد.", id)
        );
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Booking>> getBookingsByState(@PathVariable BookingState bookingState){
        return ResponseEntity.ok(bookingService.findBookingsByState(bookingState));
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<Booking>> getBookingsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(bookingService.findBookingsByUserId(userId));
    }

    @GetMapping("/room-id/{roomId}")
    public ResponseEntity<List<Booking>> getBookingsByRoomId(@PathVariable Long roomId){
        return ResponseEntity.ok(bookingService.findBookingsByRoomId(roomId));
    }


    @GetMapping("/room-id-checkin-checkout/{roomId}/{checkinDate}/{checkoutDate}")
    public ResponseEntity<List<Booking>> getConflictingBookings(@PathVariable Long roomId,
                                                                @PathVariable LocalDate checkinDate,
                                                                @PathVariable LocalDate checkoutDate){
        return ResponseEntity.ok(bookingService.getConflictingBookings(roomId, checkinDate, checkoutDate));
    }

    @GetMapping("/date-range/{start}/{end}")
    public ResponseEntity<List<Booking>> getBookingsByDateRange(@PathVariable LocalDate start,
                                                                @PathVariable LocalDate end){
        return ResponseEntity.ok(bookingService.getBookingsByDateRange(start, end));
    }

    @GetMapping("/cancel/{id}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.cancelBooking(id));
    }

    @GetMapping("/checkin/{id}")
    public ResponseEntity<Booking> checkIn(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.checkIn(id));
    }

    @GetMapping("/checkout/{id}")
    public ResponseEntity<Booking> checkOut(@PathVariable Long id){
        return ResponseEntity.ok(bookingService.checkOut(id));
    }

    @PutMapping("{id}/{checkin}/{checkout}")
    public ResponseEntity<Booking> updateBooking(@PathVariable Long bookingId,
                                                                 @PathVariable LocalDate checkinDate,
                                                                 @PathVariable LocalDate checkoutDate){
        return ResponseEntity.ok(bookingService.updateBooking(bookingId, checkinDate, checkoutDate));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<Booking> updateBookingState(
            @PathVariable Long id,
            @RequestParam BookingState newState) {
        return ResponseEntity.ok(bookingService.updateBookingState(id, newState));
    }

}
