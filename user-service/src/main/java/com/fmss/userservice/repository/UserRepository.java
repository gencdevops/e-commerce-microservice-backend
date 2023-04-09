package com.fmss.userservice.repository;

import com.fmss.userservice.model.entity.User;
import com.fmss.userservice.model.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    Optional<User> findUser_AdvisorByUsername(String username);

    @Modifying
    @Query(value = "UPDATE amy_customer ac SET ac.password = :password where ac.user_id =:id   ", nativeQuery = true)
    void updateUserPassword(@Param("id") Long id, @Param("password") String password);

    User findByEmailAndUserStatus(String email, UserStatus userStatus);

    @Modifying
    @Query(value = " update User u set u.email= :email where u.id =:id ")
    void updateEmail(@Param("id") Long id, @Param("email") String email);

    @Modifying
    @Query("update User a set a.password=:password where a.id=:userId")
    void updatePassword(@Param("userId") Long advisorId, @Param("password") String password);

    @Query(value = """
            SELECT a.username
            FROM User a
            WHERE a.id=:userId""")
    String findUserName(@Param("userId") Long userId);
}
