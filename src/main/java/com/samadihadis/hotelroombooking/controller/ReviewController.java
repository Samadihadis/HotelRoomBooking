package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.dto.reviewdto.ReviewCreateRequest;
import com.samadihadis.hotelroombooking.dto.reviewdto.ReviewResponse;
import com.samadihadis.hotelroombooking.dto.reviewdto.ReviewUpdateRequest;
import com.samadihadis.hotelroombooking.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(@Valid @RequestBody ReviewCreateRequest request) {
        return ResponseEntity.ok(reviewService.createReview(request));
    }

    @GetMapping
    public ResponseEntity<List<ReviewResponse>> getAllReviews() {
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.findReviewById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.ok(
                String.format("ارزیابی با شناسه %d حذف شد.", id)
        );
    }

    @GetMapping("/hotel-id/{hotelId}/reviews")
    public ResponseEntity<List<ReviewResponse>> getReviewsByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reviewService.getReviewsByHotelId(hotelId));
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    @GetMapping("/hotel-id/{hotelId}/average-rate")
    public ResponseEntity<Double> getAverageRateByHotelId(@PathVariable Long hotelId) {
        return ResponseEntity.ok(reviewService.getAverageRateByHotelId(hotelId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, request));
    }
}