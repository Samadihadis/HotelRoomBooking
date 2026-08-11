package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.dto.hoteldto.HotelCreateRequest;
import com.samadihadis.hotelroombooking.dto.hoteldto.HotelResponse;
import com.samadihadis.hotelroombooking.dto.hoteldto.HotelUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Hotel;
import com.samadihadis.hotelroombooking.mapper.HotelMapper;
import com.samadihadis.hotelroombooking.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Transactional
    public HotelResponse createHotel(HotelCreateRequest request) {
        List<Hotel> existingHotels = hotelRepository.findHotelsByNameContainingIgnoreCase(request.getName());

        if (!existingHotels.isEmpty()) {
            throw new RuntimeException("هتلی با این نام قبلاً ثبت شده است.");
        }

        Hotel hotel = hotelMapper.toEntity(request);
        Hotel saved = hotelRepository.save(hotel);
        return hotelMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotelMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public HotelResponse getHotelById(Long id) {
        return hotelMapper.toResponse(findHotelEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> getHotelsByRate(Integer rate) {
        return hotelRepository.findHotelsByRate(rate)
                .stream()
                .map(hotelMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> getHotelsByStarRating(Integer starRating) {
        return hotelRepository.findHotelsByStarRating(starRating)
                .stream()
                .map(hotelMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> getHotelsByNameContainingIgnoreCase(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("نام هتل نمی‌تواند خالی باشد.");
        }
        return hotelRepository.findHotelsByNameContainingIgnoreCase(name)
                .stream()
                .map(hotelMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<HotelResponse> getHotelsByStarRatingAndRateGreaterThanEqual(Integer starRating, Integer rate) {
        return hotelRepository.findHotelsByStarRatingAndRateGreaterThanEqual(starRating, rate)
                .stream()
                .map(hotelMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = findHotelEntityById(id);

        if (hotel.getRooms() != null && !hotel.getRooms().isEmpty()) {
            throw new RuntimeException("هتل دارای اتاق است و نمی‌توان آن را حذف کرد.");
        }
        hotelRepository.deleteById(id);
    }

    @Transactional
    public HotelResponse updateHotel(Long id, HotelUpdateRequest request) {
        Hotel existingHotel = findHotelEntityById(id);

        if (request.getName() != null && !existingHotel.getName().equals(request.getName())) {
            List<Hotel> duplicateCheck = hotelRepository
                    .findHotelsByNameContainingIgnoreCase(request.getName());
            if (!duplicateCheck.isEmpty()) {
                throw new RuntimeException("هتلی با این نام قبلاً ثبت شده است.");
            }
        }

        hotelMapper.updateEntityFromRequest(request, existingHotel);
        Hotel updated = hotelRepository.save(existingHotel);
        return hotelMapper.toResponse(updated);
    }

    private Hotel findHotelEntityById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("هتل با شناسه %d یافت نشد.", id)
                ));
    }
}