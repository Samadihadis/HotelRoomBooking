package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.enumes.UserRole;
import com.samadihadis.hotelroombooking.enumes.UserState;
import com.samadihadis.hotelroombooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User registerUser(User user) {
        validateUser(user);

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("این ایمیل قبلاً ثبت شده است.");
        }

        user.setUserState(UserState.ACTIVE);
        user.setUserRole(UserRole.GUEST);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با شناسه %d یافت نشد.", id)
                ));
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("ایمیل نمی‌تواند خالی باشد.");
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با ایمیل %s یافت نشد.", email)
                ));
    }

    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<User> getUsersByState(UserState userState) {
        if (userState == null) {
            throw new RuntimeException("وضعیت کاربر نمی‌تواند خالی باشد.");
        }
        return userRepository.findByUserState(userState);
    }

    @Transactional(readOnly = true)
    public List<User> getActiveUsers() {
        return userRepository.findByUserState(UserState.ACTIVE);
    }

    @Transactional
    public User updateUser(Long id, User updatedUser) {
        User existingUser = findUserById(id);

        if (updatedUser.getFullName() != null && !updatedUser.getFullName().trim().isEmpty()) {
            existingUser.setFullName(updatedUser.getFullName());
        }

        if (updatedUser.getPhone() != null && !updatedUser.getPhone().trim().isEmpty()) {
            existingUser.setPhone(updatedUser.getPhone());
        }

        return userRepository.save(existingUser);
    }

    @Transactional
    public User changeUserState(Long userId, UserState newState) {
        User user = findUserById(userId);

        if (user.getUserRole() == UserRole.ADMIN && newState == UserState.INACTIVE) {
            long adminCount = userRepository.findByUserState(UserState.ACTIVE)
                    .stream()
                    .filter(u -> u.getUserRole() == UserRole.ADMIN)
                    .count();

            if (adminCount <= 1) {
                throw new RuntimeException("حداقل یک ادمین فعال باید وجود داشته باشد.");
            }
        }

        user.setUserState(newState);
        return userRepository.save(user);
    }

    @Transactional
    public User changeUserRole(Long userId, UserRole newRole) {
        User user = findUserById(userId);

        user.setUserRole(newRole);
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserById(id);

        if (user.getUserRole() == UserRole.ADMIN) {
            long adminCount = userRepository.findByUserState(UserState.ACTIVE)
                    .stream()
                    .filter(u -> u.getUserRole() == UserRole.ADMIN)
                    .count();

            if (adminCount <= 1) {
                throw new RuntimeException("نمی‌توان آخرین ادمین را حذف کرد.");
            }
        }

        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<User> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword);
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("ایمیل نمی‌تواند خالی باشد.");
        }

        if (user.getPassword() == null || user.getPassword().length() < 6) {
            throw new RuntimeException("رمز عبور باید حداقل 6 کاراکتر باشد.");
        }

        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) {
            throw new RuntimeException("نام کامل نمی‌تواند خالی باشد.");
        }
    }
}
