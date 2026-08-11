package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.dto.room.RoomCreateRequest;
import com.samadihadis.hotelroombooking.dto.room.RoomResponse;
import com.samadihadis.hotelroombooking.dto.room.RoomUpdateRequest;
import com.samadihadis.hotelroombooking.entity.Hotel;
import com.samadihadis.hotelroombooking.entity.Room;
import com.samadihadis.hotelroombooking.enumes.BookingState;
import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import com.samadihadis.hotelroombooking.mapper.RoomMapper;
import com.samadihadis.hotelroombooking.repository.HotelRepository;
import com.samadihadis.hotelroombooking.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;

    @Transactional
    public RoomResponse createRoom(RoomCreateRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException(
                        String.format("هتل با شناسه %d یافت نشد.", request.getHotelId())
                ));

        Optional<Room> existingRoom = roomRepository
                .findByRoomNumberAndHotelId(request.getRoomNumber(), hotel.getId());

        if (existingRoom.isPresent()) {
            throw new RuntimeException("اتاق با این شماره در هتل مورد نظر قبلاً ثبت شده است.");
        }

        Room room = roomMapper.toEntity(request);
        room.setHotel(hotel);

        Room saved = roomRepository.save(room);
        return roomMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(Long id) {
        return roomMapper.toResponse(findRoomEntityById(id));
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByType(RoomType roomType) {
        return roomRepository.findByRoomType(roomType)
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByState(RoomState roomState) {
        return roomRepository.findByRoomState(roomState)
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public RoomResponse getByRoomNumberAndHotelId(Long roomNumber, Long hotelId) {
        if (roomNumber == null) {
            throw new RuntimeException("شماره اتاق نمی‌تواند خالی باشد.");
        }

        Room room = roomRepository.findByRoomNumberAndHotelId(roomNumber, hotelId)
                .orElseThrow(() -> new RuntimeException(
                        String.format("اتاق با شماره %d یافت نشد.", roomNumber)
                ));
        return roomMapper.toResponse(room);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByHotelIdAndRoomState(Long hotelId, RoomState roomState) {
        if (hotelId == null) {
            throw new RuntimeException("شناسه هتل نمی‌تواند خالی باشد.");
        }
        return roomRepository.findByHotelIdAndRoomState(hotelId, roomState)
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByMaxCapacityGreaterThanEqualAndRoomState(
            Integer capacity, RoomState roomState) {
        return roomRepository.findByMaxCapacityGreaterThanEqualAndRoomState(capacity, roomState)
                .stream()
                .map(roomMapper::toResponse)
                .toList();
    }

    @Transactional
    public void deleteRoom(Long id) {
        Room room = findRoomEntityById(id);

        boolean hasActiveBookings = room.getBookings() != null &&
                room.getBookings().stream()
                        .anyMatch(b -> b.getBookingState() != BookingState.CANCELLED);

        if (hasActiveBookings) {
            throw new RuntimeException("اتاق دارای رزرو فعال است و نمی‌توان آن را حذف کرد.");
        }
        roomRepository.deleteById(id);
    }

    @Transactional
    public RoomResponse updateRoomState(Long roomId, RoomState newState) {
        Room room = findRoomEntityById(roomId);
        room.setRoomState(newState);
        Room updated = roomRepository.save(room);
        return roomMapper.toResponse(updated);
    }

    @Transactional
    public RoomResponse updateRoom(Long id, RoomUpdateRequest request) {
        Room existingRoom = findRoomEntityById(id);

        if (request.getRoomNumber() != null &&
                !existingRoom.getRoomNumber().equals(request.getRoomNumber())) {

            Optional<Room> duplicateCheck = roomRepository
                    .findByRoomNumberAndHotelId(request.getRoomNumber(), existingRoom.getHotel().getId());

            if (duplicateCheck.isPresent()) {
                throw new RuntimeException("اتاق با این شماره در هتل مورد نظر قبلاً ثبت شده است.");
            }
        }

        roomMapper.updateEntityFromRequest(request, existingRoom);
        Room updated = roomRepository.save(existingRoom);
        return roomMapper.toResponse(updated);
    }

    // ---------- متد کمکی ----------
    private Room findRoomEntityById(Long id) {
        return roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("اتاق با شناسه %d یافت نشد.", id)
                ));
    }
}