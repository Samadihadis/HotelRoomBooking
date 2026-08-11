package com.samadihadis.hotelroombooking.controller;


import com.samadihadis.hotelroombooking.entity.Review;
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
    public ResponseEntity<Review> createReview(@Valid @RequestBody Review review){
        return ResponseEntity.ok(reviewService.createReview(review));
    }

    @GetMapping
    public ResponseEntity<List<Review>> getAllReviews(){
        return ResponseEntity.ok(reviewService.getAllReviews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Review> getReviewById(@PathVariable Long id){
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
    public ResponseEntity<List<Review>> getReviewsByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(reviewService.getReviewsByHotelId(hotelId));
    }

    @GetMapping("/user-id/{userId}")
    public ResponseEntity<List<Review>> getReviewsByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(reviewService.getReviewsByUserId(userId));
    }

    @GetMapping("/hotel-id/{hotelId}/average-rat")
    public ResponseEntity<Double> getAverageRateByHotelId(@PathVariable Long hotelId){
        return ResponseEntity.ok(reviewService.getAverageRateByHotelId(hotelId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable Long reviewId,
                                                          @RequestBody Review updatedReview){
        return ResponseEntity.ok(reviewService.updateReview(reviewId,updatedReview));
    }

}
