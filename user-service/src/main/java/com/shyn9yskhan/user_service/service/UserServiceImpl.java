package com.shyn9yskhan.user_service.service;

import com.shyn9yskhan.user_service.dto.*;
import com.shyn9yskhan.user_service.entity.Role;
import com.shyn9yskhan.user_service.entity.UserEntity;
import com.shyn9yskhan.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public CreateUserResponse createUser(CreateUserRequest createUserRequest) {
        validateCreateRequest(createUserRequest);

        String firstname = createUserRequest.firstname();
        String lastname = createUserRequest.lastname();
        Role role = createUserRequest.role();
        String username = makeUniqueUsername(firstname, lastname);
        String password = generatePassword();
        String encodedPassword = passwordEncoder.encode(password);

        UserEntity userEntity = new UserEntity(firstname, lastname, username, encodedPassword, true, role);
        UserEntity saved = userRepository.save(userEntity);

        return new CreateUserResponse(saved.getId(), saved.getUsername(), password);
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserResponse getUser(String userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        return new GetUserResponse(
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isActive(),
                userEntity.getRole()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserByUsernameResponse getUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        return new GetUserByUsernameResponse(
                userEntity.getId(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isActive(),
                userEntity.getRole()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public GetUserIdByUsernameResponse getUserIdByUsername(String username) {
        String userId = userRepository.findIdByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("UserID not found with username: " + username));
        return new GetUserIdByUsernameResponse(userId);
    }

    @Override
    public List<UserDto> getUsersByIds(List<String> ids) {
        List<UserEntity> userEntities = userRepository.findByIdIn(ids);
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userDtos.add(new UserDto(
                    userEntity.getId(),
                    userEntity.getFirstname(),
                    userEntity.getLastname(),
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.isActive(),
                    userEntity.getRole()
            ));
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getUsersByUsernames(List<String> usernames) {
        List<UserEntity> userEntities = userRepository.findByUsernameIn(usernames);
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userDtos.add(new UserDto(
                    userEntity.getId(),
                    userEntity.getFirstname(),
                    userEntity.getLastname(),
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.isActive(),
                    userEntity.getRole()
            ));
        }
        return userDtos;
    }

    @Override
    public List<UserDto> getAllActiveUsers() {
        List<UserEntity> userEntities = userRepository.findByIsActiveTrue();
        List<UserDto> userDtos = new ArrayList<>();
        for (UserEntity userEntity : userEntities) {
            userDtos.add(new UserDto(
                    userEntity.getId(),
                    userEntity.getFirstname(),
                    userEntity.getLastname(),
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.isActive(),
                    userEntity.getRole()
            ));
        }
        return userDtos;
    }

    @Override
    @Transactional
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest updateUserRequest) {
        validateUpdateRequest(updateUserRequest);

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        userEntity.setFirstname(updateUserRequest.firstname());
        userEntity.setLastname(updateUserRequest.lastname());
        String newUsername = makeUniqueUsername(updateUserRequest.firstname(), updateUserRequest.lastname());
        userEntity.setUsername(newUsername);
        userEntity.setActive(updateUserRequest.isActive());

        UserEntity updated = userRepository.save(userEntity);
        return new UpdateUserResponse(updated.getUsername(), updated.getFirstname(), updated.getLastname(), updated.isActive());
    }

    @Override
    @Transactional
    public UpdateUserByUsernameResponse updateUserByUsername(String username, UpdateUserRequest updateUserRequest) {
        validateUpdateRequest(updateUserRequest);

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        userEntity.setFirstname(updateUserRequest.firstname());
        userEntity.setLastname(updateUserRequest.lastname());
        String newUsername = makeUniqueUsername(updateUserRequest.firstname(), updateUserRequest.lastname());
        userEntity.setUsername(newUsername);
        userEntity.setActive(updateUserRequest.isActive());

        UserEntity updated = userRepository.save(userEntity);
        return new UpdateUserByUsernameResponse(userEntity.getId(), userEntity.getUsername(), updated.getFirstname(), updated.getLastname(), updated.isActive());
    }

    @Override
    @Transactional
    public void deleteUser(String userId) {
        if (!userRepository.existsById(userId)) throw new EntityNotFoundException("User not found with ID: " + userId);
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional
    public DeleteUserByUsernameResponse deleteUserByUsername(String username) {
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        long deleted = userRepository.deleteByUsername(username);
        if (deleted == 0) throw new EntityNotFoundException("User not found with username: " + username);
        return new DeleteUserByUsernameResponse(userEntity.getId());
    }

    @Override
    @Transactional
    public void changePasswordByUsername(String username, ChangePasswordRequest changePasswordRequest) {
        validateChangePasswordRequest(changePasswordRequest);

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));

        String storedEncodedPassword = userEntity.getPassword();
        String oldRawPassword = changePasswordRequest.oldPassword();
        String newRawPassword = changePasswordRequest.newPassword();

        if (!passwordEncoder.matches(oldRawPassword, storedEncodedPassword)) {
            throw new IllegalArgumentException("Old password does not match");
        }
        String encodedNewPassword = passwordEncoder.encode(newRawPassword);
        userEntity.setPassword(encodedNewPassword);
        userRepository.save(userEntity);
    }

    @Override
    @Transactional
    public void updateUserActiveStatus(String username, UpdateUserActiveStatusRequest updateUserActiveStatusRequest) {
        validateUpdateActiveStatusRequest(updateUserActiveStatusRequest);
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found with username: " + username));
        userEntity.setActive(updateUserActiveStatusRequest.isActive());
        userRepository.save(userEntity);
    }

    private String makeUniqueUsername(String firstname, String lastname) {
        String base = firstname + "." + lastname;
        if (!userRepository.existsByUsername(base)) {
            return base;
        }

        List<String> similar = userRepository.findUsernameByUsernameStartingWith(base);
        int max = similar.stream()
                .map(u -> u.substring(base.length()))
                .map(s -> {
                    if (s.isEmpty()) return 0;
                    try { return Integer.parseInt(s); }
                    catch (NumberFormatException ex) { return 0; }
                })
                .max(Integer::compareTo)
                .orElse(0);

        return base + (max + 1);
    }

    private String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

    private void validateCreateRequest(CreateUserRequest createUserRequest) {
        if (createUserRequest == null || createUserRequest.firstname() == null || createUserRequest.firstname().trim().isEmpty() ||
                createUserRequest.lastname() == null || createUserRequest.lastname().trim().isEmpty()) {
            throw new IllegalArgumentException("Firstname and lastname are required and cannot be empty");
        }
    }

    private void validateUpdateRequest(UpdateUserRequest updateUserRequest) {
        if (updateUserRequest == null || updateUserRequest.firstname() == null || updateUserRequest.firstname().trim().isEmpty() ||
                updateUserRequest.lastname() == null || updateUserRequest.lastname().trim().isEmpty()) {
            throw new IllegalArgumentException("Firstname and lastname are required and cannot be empty");
        }
    }

    private void validateChangePasswordRequest(ChangePasswordRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("ChangePasswordRequest cannot be null");
        }
        if (request.oldPassword() == null || request.oldPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Old password is required");
        }
        validatePassword(request.newPassword());
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
    }

    private void validateUpdateActiveStatusRequest(UpdateUserActiveStatusRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("UpdateUserActiveStatusRequest cannot be null");
        }
    }
}
