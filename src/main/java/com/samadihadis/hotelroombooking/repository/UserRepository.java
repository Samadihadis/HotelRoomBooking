package com.samadihadis.hotelroombooking.repository;

import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.enumes.UserState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);  // برای لاگین

    List<User> findByUserState(UserState userState);

    boolean existsByEmail(String email);  //برای ثبت نام
}
