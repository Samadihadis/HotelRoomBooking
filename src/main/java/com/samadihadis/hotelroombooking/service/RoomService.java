package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.entity.Room;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import com.samadihadis.hotelroombooking.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    @Transactional
    public Room createRoom(Room room) {

        if (room.getHotel() == null || room.getHotel().getId() == null) {
            throw new RuntimeException("اطلاعات هتل برای ثبت اتاق الزامی است.");
        }

        Optional<Room> existingRoom = roomRepository
                .findByRoomNumberAndHotelId(room.getRoomNumber(), room.getHotel().getId());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("اتاق با این شماره در هتل مورد نظر قبلاً ثبت شده است.");
        }
        return roomRepository.save(room);
    }


    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public Room getRoomById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("اتاق با شناسه %d یافت نشد.", id)
                ));
    }

    public List<Room> getRoomsByType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType);
    }

    public List<Room> getRoomsByState(RoomState roomState) {
        return roomRepository.findByRoomState(roomState);
    }

    public Room getByRoomNumberAndHotelId(Long roomNumber, Long hotelId) {
        if (roomNumber == null) {
            throw new RuntimeException("شماره اتاق نمی‌تواند خالی باشد.");
        }

        return roomRepository.findByRoomNumberAndHotelId(roomNumber, hotelId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("اتاق با شماره %d یافت نشد.", roomNumber)
                ));
    }

    public List<Room> getRoomsByHotelIdAndRoomState(Long hotelId, RoomState roomState) {
        if (hotelId == null) {
            throw new RuntimeException("شماره هتل نمی‌تواند خالی باشد.");
        }
        return roomRepository.findByHotelIdAndRoomState(hotelId, roomState);
    }

    public List<Room> getRoomsByMaxCapacityGreaterThanEqualAndRoomState(Integer capacity, RoomState roomState) {
        return roomRepository.findByMaxCapacityGreaterThanEqualAndRoomState(capacity, roomState);
    }

    @Transactional
    public void deleteRoom(Long id){
        Room room = getRoomById(id);

        boolean hasActiveBookings = room.getBookings().stream()
                .anyMatch(b -> b.getBookingState() != BookingState.CANCELLED);

        if (hasActiveBookings) {
            throw new RuntimeException("اتاق دارای رزرو فعال است و نمی‌توان آن را حذف کرد.");
        }
        roomRepository.deleteById(id);
    }

    @Transactional
    public Room updateRoomState(Long roomId, RoomState newState) {
        Room room = getRoomById(roomId);
        room.setRoomState(newState);
        return roomRepository.save(room);
    }

    @Transactional
    public Room updateRoom(Long id, Room updatedRoom) {
        Room existingRoom = getRoomById(id);

        if (!existingRoom.getRoomNumber().equals(updatedRoom.getRoomNumber())) {
            Optional<Room> duplicateCheck = roomRepository
                    .findByRoomNumberAndHotelId(updatedRoom.getRoomNumber(), existingRoom.getHotel().getId());

            if (duplicateCheck.isPresent()) {
                throw new RuntimeException("اتاق با این شماره در هتل مورد نظر قبلاً ثبت شده است.");
            }
        }

        existingRoom.setRoomNumber(updatedRoom.getRoomNumber());
        existingRoom.setRoomType(updatedRoom.getRoomType());
        existingRoom.setBasePrice(updatedRoom.getBasePrice());
        existingRoom.setMaxCapacity(updatedRoom.getMaxCapacity());
        existingRoom.setDescription(updatedRoom.getDescription());

        return roomRepository.save(existingRoom);
    }
}
