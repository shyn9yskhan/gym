package com.shyn9yskhan.user_service.repository;

import com.shyn9yskhan.user_service.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByIdIn(List<String> ids);
    List<UserEntity> findByUsernameIn(List<String> usernames);
    List<UserEntity> findByIsActiveTrue();
    long deleteByUsername(String username);
    boolean existsByUsername(String username);

    @Query("SELECT u.id FROM UserEntity u WHERE u.username = :username")
    Optional<String> findIdByUsername(@Param("username") String username);

    @Query("SELECT u.username FROM UserEntity u WHERE u.username LIKE CONCAT(:base, '%')")
    List<String> findUsernameByUsernameStartingWith(@Param("base") String base);

    @Modifying
    @Query("UPDATE UserEntity u SET u.username = :username, u.firstname = :firstname, u.lastname = :lastname, u.isActive = :isActive WHERE u.id = :id")
    int updateUserById(@Param("id") String id,
                       @Param("username") String username,
                       @Param("firstname") String firstname,
                       @Param("lastname") String lastname,
                       @Param("isActive") boolean isActive);

    @Modifying
    @Query("UPDATE UserEntity u SET u.username = :newUsername, u.firstname = :firstname, u.lastname = :lastname, u.isActive = :isActive WHERE u.username = :username")
    int updateUserByUsername(@Param("username") String username,
                             @Param("newUsername") String newUsername,
                             @Param("firstname") String firstname,
                             @Param("lastname") String lastname,
                             @Param("isActive") boolean isActive);

    @Modifying
    @Query("UPDATE UserEntity u SET u.password = :newPassword WHERE u.username = :username")
    int updatePasswordByUsername(@Param("username") String username, @Param("newPassword") String newPassword);
}
