package com.shyn9yskhan.user_service.service;

import com.shyn9yskhan.user_service.dto.*;
import com.shyn9yskhan.user_service.entity.UserEntity;
import com.shyn9yskhan.user_service.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Captor
    ArgumentCaptor<UserEntity> userEntityCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void createUser_success_generatesUniqueUsernameAndPassword() {
        CreateUserRequest req = new CreateUserRequest("John", "Doe");
        when(userRepository.existsByUsername("John.Doe")).thenReturn(false);

        UserEntity saved = new UserEntity("John", "Doe", "John.Doe", "pw12345678", true);
        saved.setId("id-1");
        when(userRepository.save(any())).thenReturn(saved);

        CreateUserResponse resp = userService.createUser(req);

        assertNotNull(resp);
        assertEquals("id-1", resp.userId());
        assertEquals("John.Doe", resp.username());
        assertNotNull(resp.password());
        verify(userRepository).save(userEntityCaptor.capture());
        UserEntity captured = userEntityCaptor.getValue();
        assertEquals("John", captured.getFirstname());
        assertEquals("Doe", captured.getLastname());
        assertTrue(captured.isActive());
    }

    @Test
    void createUser_whenBaseExists_appendsIncrementSuffix() {
        CreateUserRequest req = new CreateUserRequest("Jane", "Smith");
        when(userRepository.existsByUsername("Jane.Smith")).thenReturn(true);
        when(userRepository.findUsernameByUsernameStartingWith("Jane.Smith")).thenReturn(List.of("Jane.Smith", "Jane.Smith1", "Jane.Smith2"));

        UserEntity saved = new UserEntity("Jane", "Smith", "Jane.Smith3", "pw", true);
        saved.setId("id-2");
        when(userRepository.save(any())).thenReturn(saved);

        CreateUserResponse resp = userService.createUser(req);

        assertEquals("Jane.Smith3", resp.username());
        verify(userRepository).save(userEntityCaptor.capture());
        assertEquals("Jane", userEntityCaptor.getValue().getFirstname());
    }

    @Test
    void getUser_success() {
        UserEntity entity = new UserEntity("A", "B", "a.b", "pass", true);
        entity.setId("u-1");
        when(userRepository.findById("u-1")).thenReturn(Optional.of(entity));

        GetUserResponse resp = userService.getUser("u-1");

        assertEquals("A", resp.firstname());
        assertEquals("B", resp.lastname());
        assertEquals("a.b", resp.username());
        assertTrue(resp.isActive());
    }

    @Test
    void getUser_notFound_throws() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> userService.getUser("missing"));
    }

    @Test
    void getUserByUsername_success() {
        UserEntity entity = new UserEntity("X", "Y", "x.y", "pw", false);
        entity.setId("u-2");
        when(userRepository.findByUsername("x.y")).thenReturn(Optional.of(entity));

        GetUserByUsernameResponse resp = userService.getUserByUsername("x.y");

        assertEquals("u-2", resp.id());
        assertEquals("X", resp.firstname());
    }

    @Test
    void getUserIdByUsername_success() {
        when(userRepository.findIdByUsername("someone")).thenReturn(Optional.of("uid-123"));
        GetUserIdByUsernameResponse resp = userService.getUserIdByUsername("someone");
        assertEquals("uid-123", resp.userId());
    }

    @Test
    void getUsersByIds_returnsDtos() {
        UserEntity e1 = new UserEntity("A", "A", "a.a", "p1", true);
        e1.setId("id1");
        UserEntity e2 = new UserEntity("B", "B", "b.b", "p2", false);
        e2.setId("id2");
        when(userRepository.findByIdIn(List.of("id1","id2"))).thenReturn(List.of(e1,e2));

        var dtos = userService.getUsersByIds(List.of("id1","id2"));
        assertEquals(2, dtos.size());
        assertEquals("id1", dtos.get(0).id());
    }

    @Test
    void getAllActiveUsers_returnsOnlyActive() {
        UserEntity a = new UserEntity("A", "A", "a.a", "p", true);
        a.setId("ida");
        UserEntity b = new UserEntity("B", "B", "b.b", "p", false);
        b.setId("idb");
        when(userRepository.findByIsActiveTrue()).thenReturn(List.of(a));
        var dtos = userService.getAllActiveUsers();
        assertEquals(1, dtos.size());
        assertEquals("ida", dtos.get(0).id());
    }

    @Test
    void updateUser_success_changesUsernameAndNames() {
        UserEntity existing = new UserEntity("Old", "Name", "old.name", "pw", true);
        existing.setId("u10");
        when(userRepository.findById("u10")).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsername("New.Name")).thenReturn(false);
        when(userRepository.findUsernameByUsernameStartingWith("New.Name")).thenReturn(List.of());
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserRequest req = new UpdateUserRequest("New","Name", false);
        UpdateUserResponse resp = userService.updateUser("u10", req);

        assertEquals("New.Name", resp.username());
        assertEquals("New", resp.firstname());
        assertFalse(resp.isActive());
    }

    @Test
    void updateUserByUsername_success() {
        UserEntity existing = new UserEntity("First", "Last", "first.last", "pw", true);
        existing.setId("ux");
        when(userRepository.findByUsername("first.last")).thenReturn(Optional.of(existing));
        when(userRepository.existsByUsername("New.First")).thenReturn(false);
        when(userRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        UpdateUserRequest req = new UpdateUserRequest("New", "First", true);
        UpdateUserByUsernameResponse resp = userService.updateUserByUsername("first.last", req);

        assertEquals("ux", resp.id());
        assertEquals("New.First", resp.username());
    }

    @Test
    void deleteUser_notFound_throws() {
        when(userRepository.existsById("no")).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser("no"));
    }

    @Test
    void deleteUserByUsername_success() {
        UserEntity e = new UserEntity("A","B","a.b","pw",true);
        e.setId("del-1");
        when(userRepository.findByUsername("a.b")).thenReturn(Optional.of(e));
        when(userRepository.deleteByUsername("a.b")).thenReturn(1L);

        var resp = userService.deleteUserByUsername("a.b");
        assertEquals("del-1", resp.id());
    }

    @Test
    void changePasswordByUsername_successAndFailure() {
        UserEntity e = new UserEntity("AA","BB","aa.bb","oldpw",true);
        e.setId("p1");
        when(userRepository.findByUsername("aa.bb")).thenReturn(Optional.of(e));
        ChangePasswordRequest good = new ChangePasswordRequest("oldpw","newpw");
        userService.changePasswordByUsername("aa.bb", good);
        verify(userRepository).save(userEntityCaptor.capture());
        assertEquals("newpw", userEntityCaptor.getValue().getPassword());

        ChangePasswordRequest bad = new ChangePasswordRequest("wrong","x");
        when(userRepository.findByUsername("aa.bb")).thenReturn(Optional.of(e));
        assertThrows(IllegalArgumentException.class, () -> userService.changePasswordByUsername("aa.bb", bad));
    }

    @Test
    void updateUserActiveStatus_success() {
        UserEntity e = new UserEntity("FN","LN","f.l","pw", true);
        e.setId("act-1");
        when(userRepository.findByUsername("f.l")).thenReturn(Optional.of(e));
        UpdateUserActiveStatusRequest req = new UpdateUserActiveStatusRequest(false);
        userService.updateUserActiveStatus("f.l", req);
        verify(userRepository).save(userEntityCaptor.capture());
        assertFalse(userEntityCaptor.getValue().isActive());
    }
}
