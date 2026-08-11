package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.dto.room.RoomCreateRequest;
import com.samadihadis.hotelroombooking.dto.room.RoomResponse;
import com.samadihadis.hotelroombooking.dto.room.RoomUpdateRequest;
import com.samadihadis.hotelroombooking.enumes.RoomState;
import com.samadihadis.hotelroombooking.enumes.RoomType;
import com.samadihadis.hotelroombooking.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@Valid @RequestBody RoomCreateRequest request) {
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRoom() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/type/{roomType}")
    public ResponseEntity<List<RoomResponse>> getRoomByType(@PathVariable RoomType roomType) {
        return ResponseEntity.ok(roomService.getRoomsByType(roomType));
    }

    @GetMapping("/state/{roomState}")
    public ResponseEntity<List<RoomResponse>> getRoomByState(@PathVariable RoomState roomState) {
        return ResponseEntity.ok(roomService.getRoomsByState(roomState));
    }

    @GetMapping("/room-id-hotel-id/{roomNumber}/{hotelId}")
    public ResponseEntity<RoomResponse> getRoomByRoomNumberAndHotelId(
            @PathVariable Long roomNumber,
            @PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getByRoomNumberAndHotelId(roomNumber, hotelId));
    }

    @GetMapping("/hotel-id-room-state/{hotelId}/{roomState}")
    public ResponseEntity<List<RoomResponse>> getRoomByHotelIdAndRoomState(
            @PathVariable Long hotelId,
            @PathVariable RoomState roomState) {
        return ResponseEntity.ok(roomService.getRoomsByHotelIdAndRoomState(hotelId, roomState));
    }

    @GetMapping("/capacity-roomState/{capacity}/{roomState}")
    public ResponseEntity<List<RoomResponse>> getRoomByMaxCapacityGreaterThanEqualAndRoomState(
            @PathVariable Integer capacity,
            @PathVariable RoomState roomState) {
        return ResponseEntity.ok(
                roomService.getRoomsByMaxCapacityGreaterThanEqualAndRoomState(capacity, roomState)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(
                String.format("اتاق با شناسه %d حذف شد.", id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @Valid @RequestBody RoomUpdateRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<RoomResponse> updateRoomState(
            @PathVariable Long id,
            @RequestParam RoomState newState) {
        return ResponseEntity.ok(roomService.updateRoomState(id, newState));
    }
}