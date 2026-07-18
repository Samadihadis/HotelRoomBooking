package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.Room;
import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room , Long> {

    List<Room> findByRoomType(RoomType roomType);

    List<Room> findByRoomState(RoomState roomState);

    Optional<Room> findByRoomNumberAndHotelId(Long roomNumber, Long hotelId);

    //  پیدا کردن اتاق‌های موجود در یک هتل خاص
    List<Room> findByHotelIdAndRoomState(Long hotelId, RoomState roomState);

    // پیدا کردن اتاق‌های موجود با ظرفیت مشخص
    List<Room> findByMaxCapacityGreaterThanEqualAndRoomState(Integer capacity, RoomState roomState);
}
