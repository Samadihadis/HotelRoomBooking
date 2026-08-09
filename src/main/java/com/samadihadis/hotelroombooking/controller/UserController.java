package com.samadihadis.hotelroombooking.controller;

import com.samadihadis.hotelroombooking.entity.User;
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
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.registerUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/state")
    public ResponseEntity<List<User>> getUserByState(@PathVariable UserState userState) {
        return ResponseEntity.ok(userService.getUsersByState(userState));
    }

    @GetMapping("/active-user")
    public ResponseEntity<List<User>> getActiveUsers() {
        return ResponseEntity.ok(userService.getActiveUsers());
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<User> changeUserState(@PathVariable Long id,
                                                @PathVariable UserState userState) {
        return ResponseEntity.ok(userService.changeUserState(id, userState));
    }

    @PatchMapping("/{id}/role")
    public ResponseEntity<User> changeUserRole(@PathVariable Long id,
                                               @PathVariable UserRole userRole) {
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
    public ResponseEntity<List<User>> searchUsers(String keyword) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> existsByEmail(String email) {
        return ResponseEntity.ok(userService.existsByEmail(email));
    }

}
