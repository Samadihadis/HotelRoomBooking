package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.enumes.UserRole;
import com.samadihadis.hotelroombooking.enumes.UserState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findByUserState(UserState userState);

    boolean existsByEmail(String email);

    // اضافه کن:
    List<User> findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String fullName, String email);

    List<User> findByUserRole(UserRole userRole);
}
