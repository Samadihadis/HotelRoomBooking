package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.dto.ReviewCreateRequest;
import com.samadihadis.hotelroombooking.dto.ReviewResponse;
import com.samadihadis.hotelroombooking.dto.ReviewUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Hotel;
import com.samadihadis.hotelroombooking.entity.Review;
import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.mapper.ReviewMapper;
import com.samadihadis.hotelroombooking.repository.HotelRepository;
import com.samadihadis.hotelroombooking.repository.ReviewRepository;
import com.samadihadis.hotelroombooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;
    private final ReviewMapper reviewMapper;

    @Transactional
    public ReviewResponse createReview(ReviewCreateRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با شناسه %d یافت نشد.", request.getUserId())
                ));

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("هتل با شناسه %d یافت نشد.", request.getHotelId())
                ));

        if (reviewRepository.existsByUserIdAndHotelId(user.getId(), hotel.getId())) {
            throw new RuntimeException("شما قبلاً برای این هتل نظر داده‌اید.");
        }

        Review review = reviewMapper.toEntity(request);
        review.setUser(user);
        review.setHotel(hotel);
        review.setCreateAt(LocalDate.now());

        Review saved = reviewRepository.save(review);
        return reviewMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReviewResponse findReviewById(Long id) {
        return reviewMapper.toResponse(findReviewEntityById(id));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        findReviewEntityById(reviewId);
        reviewRepository.deleteById(reviewId);
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByHotelId(Long hotelId) {
        if (hotelId == null) {
            throw new RuntimeException("شناسه هتل نمی‌تواند خالی باشد.");
        }
        // اطمینان از وجود هتل
        hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("هتل با شناسه %d یافت نشد.", hotelId)
                ));

        return reviewRepository.findByHotelId(hotelId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getReviewsByUserId(Long userId) {
        if (userId == null) {
            throw new RuntimeException("شناسه کاربر نمی‌تواند خالی باشد.");
        }
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با شناسه %d یافت نشد.", userId)
                ));

        return reviewRepository.findByUserId(userId)
                .stream()
                .map(reviewMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public Double getAverageRateByHotelId(Long hotelId) {
        if (hotelId == null) {
            throw new RuntimeException("شناسه هتل نمی‌تواند خالی باشد.");
        }
        hotelRepository.findById(hotelId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("هتل با شناسه %d یافت نشد.", hotelId)
                ));

        Double average = reviewRepository.getAverageRateByHotelId(hotelId);
        return average != null ? average : 0.0;
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, ReviewUpdateRequest request) {
        Review existingReview = findReviewEntityById(reviewId);

        reviewMapper.updateEntityFromRequest(request, existingReview);

        // اعتبارسنجی امتیاز بعد از آپدیت
        if (existingReview.getRate() != null &&
                (existingReview.getRate() < 1 || existingReview.getRate() > 5)) {
            throw new RuntimeException("امتیاز باید بین ۱ تا ۵ باشد.");
        }

        Review updated = reviewRepository.save(existingReview);
        return reviewMapper.toResponse(updated);
    }

    // ---------- متد کمکی ----------
    private Review findReviewEntityById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("نظر با شناسه %d یافت نشد.", id)
                ));
    }
}