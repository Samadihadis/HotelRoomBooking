package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.entity.Hotel;
import com.samadihadis.hotelroombooking.entity.Review;
import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final HotelService hotelService;

    @Transactional
    public Review createReview (Review review) {
        validateReview(review);

        User user = userService.findUserById(review.getUser().getId());
        Hotel hotel = hotelService.getHotelById(review.getHotel().getId());

        if (reviewRepository.existsByUserIdAndHotelId(user.getId(), hotel.getId())) {
            throw new RuntimeException("شما قبلاً برای این هتل نظر داده‌اید.");
        }
        review.setCreateAt(LocalDate.now());
        return reviewRepository.save(review);
    }

    public List<Review> getAllReviews() {
     return reviewRepository.findAll();
    }

    public Review findReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("نظر با شناسه %d یافت نشد.", id)
                ));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        findReviewById(reviewId);
        reviewRepository.deleteById(reviewId);
    }

    public List<Review> getReviewsByHotelId(Long hotelId) {
        if (hotelId == null) {
            throw new RuntimeException("شناسه هتل نمی‌تواند خالی باشد.");
        }
        hotelService.getHotelById(hotelId);
        return reviewRepository.findByHotelId(hotelId);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("شناسه کاربر نمی‌تواند خالی باشد.");
        }
        userService.findUserById(userId);
        return reviewRepository.findByUserId(userId);
    }

    public Double getAverageRateByHotelId(Long hotelId) {
        if (hotelId == null) {
            throw new RuntimeException("شناسه هتل نمی‌تواند خالی باشد.");
        }
        hotelService.getHotelById(hotelId);
        return reviewRepository.getAverageRateByHotelId(hotelId);
    }

    @Transactional
    public Review updateReview(Long reviewId, Review updatedReview) {
        Review existingReview = findReviewById(reviewId);

        if (updatedReview.getRate() != null) {
            if (updatedReview.getRate() < 1 || updatedReview.getRate() > 5) {
                throw new RuntimeException("امتیاز باید بین 1 تا 5 باشد.");
            }
            existingReview.setRate(updatedReview.getRate());
        }

        if (updatedReview.getComment() != null && !updatedReview.getComment().trim().isEmpty()) {
            existingReview.setComment(updatedReview.getComment());
        }

        return reviewRepository.save(existingReview);
    }

    private void validateReview(Review review) {
        if (review.getUser() == null || review.getUser().getId() == null) {
            throw new RuntimeException("شناسه کاربر الزامی است.");
        }

        if (review.getHotel() == null || review.getHotel().getId() == null) {
            throw new RuntimeException("شناسه هتل الزامی است.");
        }

        if (review.getRate() == null) {
            throw new RuntimeException("امتیاز نمی‌تواند خالی باشد.");
        }

        if (review.getRate() < 1 || review.getRate() > 5) {
            throw new RuntimeException("امتیاز باید بین 1 تا 5 باشد.");
        }

        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            throw new RuntimeException("متن نظر نمی‌تواند خالی باشد.");
        }
    }
}
