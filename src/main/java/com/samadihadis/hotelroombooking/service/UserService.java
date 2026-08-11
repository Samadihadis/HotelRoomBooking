package com.samadihadis.hotelroombooking.service;

import com.samadihadis.hotelroombooking.dto.UserRegisterRequest;
import com.samadihadis.hotelroombooking.dto.UserResponseDTO;
import com.samadihadis.hotelroombooking.dto.UserUpdateRequestDTO;
import com.samadihadis.hotelroombooking.entity.User;
import com.samadihadis.hotelroombooking.enumes.UserRole;
import com.samadihadis.hotelroombooking.enumes.UserState;
import com.samadihadis.hotelroombooking.mapper.UserMapper;
import com.samadihadis.hotelroombooking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO registerUser(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("این ایمیل قبلاً ثبت شده است.");
        }

        User user = userMapper.toEntity(request);
        user.setUserState(UserState.ACTIVE);
        user.setUserRole(UserRole.GUEST);

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با شناسه %d یافت نشد.", id)
                ));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("ایمیل نمی‌تواند خالی باشد.");
        }

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با ایمیل %s یافت نشد.", email)
                ));
        return userMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByState(UserState userState) {
        if (userState == null) {
            throw new RuntimeException("وضعیت کاربر نمی‌تواند خالی باشد.");
        }
        return userRepository.findByUserState(userState)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveUsers() {
        return userRepository.findByUserState(UserState.ACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UserUpdateRequestDTO request) {
        User existingUser = findUserEntityById(id);
        userMapper.updateEntityFromRequest(request, existingUser);
        User updated = userRepository.save(existingUser);
        return userMapper.toResponse(updated);
    }

    @Transactional
    public UserResponseDTO changeUserState(Long userId, UserState newState) {
        User user = findUserEntityById(userId);

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
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public UserResponseDTO changeUserRole(Long userId, UserRole newRole) {
        User user = findUserEntityById(userId);
        user.setUserRole(newRole);
        return userMapper.toResponse(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = findUserEntityById(id);

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
    public List<UserResponseDTO> searchUsers(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllUsers();
        }
        return userRepository.findByFullNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    private User findUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        String.format("کاربر با شناسه %d یافت نشد.", id)
                ));
    }
}