package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.dto.HotelCreateRequest;
import com.samadihadis.hotelroombooking.dto.HotelResponse;
import com.samadihadis.hotelroombooking.dto.HotelUpdateRequest;
import com.samadihadis.hotelroombooking.service.HotelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody HotelCreateRequest request) {
        return ResponseEntity.ok(hotelService.createHotel(request));
    }

    @GetMapping
    public ResponseEntity<List<HotelResponse>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelResponse> getHotelById(@PathVariable Long id) {
        return ResponseEntity.ok(hotelService.getHotelById(id));
    }

    @GetMapping("/rate/{rate}")
    public ResponseEntity<List<HotelResponse>> getHotelsByRate(@PathVariable Integer rate) {
        return ResponseEntity.ok(hotelService.getHotelsByRate(rate));
    }

    @GetMapping("/star-rating/{starRating}")
    public ResponseEntity<List<HotelResponse>> getHotelsByStarRating(@PathVariable Integer starRating) {
        return ResponseEntity.ok(hotelService.getHotelsByStarRating(starRating));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<List<HotelResponse>> getHotelsByNameContainingIgnoreCase(@PathVariable String name) {
        return ResponseEntity.ok(hotelService.getHotelsByNameContainingIgnoreCase(name));
    }

    @GetMapping("/by-star-and-rate/{starRating}/{rate}")
    public ResponseEntity<List<HotelResponse>> getHotelsByStarRatingAndRateGreaterThanEqual(
            @PathVariable Integer starRating,
            @PathVariable Integer rate) {
        return ResponseEntity.ok(
                hotelService.getHotelsByStarRatingAndRateGreaterThanEqual(starRating, rate)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHotel(@PathVariable Long id) {
        hotelService.deleteHotel(id);
        return ResponseEntity.ok(
                String.format("هتل با شناسه %d حذف شد.", id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<HotelResponse> updateHotel(
            @PathVariable Long id,
            @Valid @RequestBody HotelUpdateRequest request) {
        return ResponseEntity.ok(hotelService.updateHotel(id, request));
    }
}