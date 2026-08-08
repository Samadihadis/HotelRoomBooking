package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.entity.Hotel;
import com.samadihadis.hotelroombooking.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;

    @Transactional
    public Hotel createHotel(Hotel hotel) {

        List<Hotel> existingHotels = hotelRepository.findHotelsByNameContainingIgnoreCase(hotel.getName());

        if (!existingHotels.isEmpty()) {
            throw new RuntimeException("هتلی با این نام قبلاً ثبت شده است.");
        }

        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("هتل با شناسه %d یافت نشد.", id)
                ));
    }

    public List<Hotel> getHotelsByRate(Integer rate) {
        return hotelRepository.findHotelsByRate(rate);
    }

    public List<Hotel> getHotelsByStarRating(Integer starRating) {
        return hotelRepository.findHotelsByStarRating(starRating);
    }

    public List<Hotel> getHotelsByNameContainingIgnoreCase(String name){
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("نام هتل نمی‌تواند خالی باشد.");
        }
        return hotelRepository.findHotelsByNameContainingIgnoreCase(name);
    }

    public List<Hotel> getHotelsByStarRatingAndRateGreaterThanEqual(Integer starRating, Integer rate) {
        return hotelRepository.findHotelsByStarRatingAndRateGreaterThanEqual(starRating, rate);
    }

    @Transactional
    public void deleteHotel(Long id) {
        Hotel hotel = getHotelById(id);

        if (hotel.getRooms() != null && !hotel.getRooms().isEmpty()) {
            throw new RuntimeException("هتل دارای اتاق است و نمی‌توان آن را حذف کرد.");
        }
        hotelRepository.deleteById(id);
    }

    @Transactional
    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Hotel existingHotel = getHotelById(id);

        if (!existingHotel.getName().equals(updatedHotel.getName())) {
            List<Hotel> duplicateCheck = hotelRepository
                    .findHotelsByNameContainingIgnoreCase(updatedHotel.getName());
            if (!duplicateCheck.isEmpty()) {
                throw new RuntimeException("هتلی با این نام قبلاً ثبت شده است.");
            }
        }

        existingHotel.setName(updatedHotel.getName());
        existingHotel.setAddress(updatedHotel.getAddress());
        existingHotel.setDescription(updatedHotel.getDescription());
        existingHotel.setRate(updatedHotel.getRate());
        existingHotel.setStarRating(updatedHotel.getStarRating());

        return hotelRepository.save(existingHotel);
    }
}
