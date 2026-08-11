package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.dto.UserRegisterRequest;
import com.samadihadis.hotelroombooking.dto.UserResponseDTO;
import com.samadihadis.hotelroombooking.dto.UserUpdateRequestDTO;
import com.samadihadis.hotelroombooking.enumes.UserRole;
import com.samadihadis.hotelroombooking.enumes.UserState;
import com.samadihadis.hotelroombooking.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(userService.registerUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/state")
    public ResponseEntity<List<UserResponseDTO>> getUserByState(@RequestParam UserState userState) {
        return ResponseEntity.ok(userService.getUsersByState(userState));
    }

    @GetMapping("/active-user")
    public ResponseEntity<List<UserResponseDTO>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id,
                                                   @Valid @RequestBody UserUpdateRequestDTO request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<UserResponseDTO> changeUserState(@PathVariable Long id,
                                                        @RequestParam UserState userState) {
        return ResponseEntity.ok(userService.changeUserState(id, userState));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<UserResponseDTO> changeUserRole(@PathVariable Long id,
                                                       @RequestParam UserRole userRole) {
        return ResponseEntity.ok(userService.changeUserRole(id, userRole));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(
                String.format("کاربر با شناسه %d حذف شد.", id)
        );
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> existsByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }
}