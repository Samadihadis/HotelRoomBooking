package com.samadihadis.hotelroombooking.controller;


import com.samadihadis.hotelroombooking.entity.Room;
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
    public ResponseEntity<Room> createRoom(@Valid @RequestBody Room room) {
        return ResponseEntity.ok(roomService.createRoom(room));
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRoom() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Room>> getRoomByType(@PathVariable RoomType roomType) {
        return ResponseEntity.ok(roomService.getRoomsByType(roomType));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<Room>> getRoomByState(@PathVariable RoomState roomState) {
        return ResponseEntity.ok(roomService.getRoomsByState(roomState));
    }

    @GetMapping("/room-id-hotel-id/{roomNumber}/{hotelId}")
    public ResponseEntity<Room> getRoomByRoomNumberAndHotelId(@PathVariable Long roomNumber,
                                                              @PathVariable Long hotelId) {
        return ResponseEntity.ok(roomService.getByRoomNumberAndHotelId(roomNumber, hotelId));
    }

    @GetMapping("/hotel-id-room-state/{hotelId}/{roomState}")
    public ResponseEntity<List<Room>> getRoomByRHotelIdAndRoomState(@PathVariable Long hotelId,
                                                                    @PathVariable RoomState roomState) {
        return ResponseEntity.ok(roomService.getRoomsByHotelIdAndRoomState(hotelId, roomState));
    }

    @GetMapping("/capacity-roomState/{capacity}/{roomState}")
    public ResponseEntity<List<Room>> getRoomByMaxCapacityGreaterThanEqualAndRoomState(@PathVariable Integer capacity,
                                                                                       @PathVariable RoomState roomState) {
        return ResponseEntity.ok(roomService.
                getRoomsByMaxCapacityGreaterThanEqualAndRoomState(capacity, roomState));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRoomById(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok(
                String.format("اتاق با شناسه %d حذف شد.", id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom (@PathVariable Long id , @Valid@RequestBody Room room){
        return ResponseEntity.ok(roomService.updateRoom(id, room));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<Room> updateRoomState(
            @PathVariable Long id,
            @RequestParam RoomState newState) {
        return ResponseEntity.ok(roomService.updateRoomState(id, newState));
    }
}
