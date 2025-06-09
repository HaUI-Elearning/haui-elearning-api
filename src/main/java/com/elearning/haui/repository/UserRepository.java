package com.elearning.haui.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.elearning.haui.domain.dto.UserDTO;
import com.elearning.haui.domain.dto.UserRespone;
import com.elearning.haui.domain.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmail(String email);
    @Query("select u from User u where u.email=:email")
    User findUserByEmail(@Param("email") String email);
    User findById(long id);

    Void deleteById(long id);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    User findByName(String name);

    boolean existsByUsernameOrEmail(String username, String email);

    boolean existsByUsernameOrEmailAndUserIdNot(String username, String email, Long userId);

    @Query(
    """
           select count(u) from User u
           where u.role.Id=3
            """)
    int toTalTeacher();

    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    long countNewUsersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role.name = 'TEACHER' AND u.createdAt BETWEEN :startDate AND :endDate")
    long countNewTeachersBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT new com.elearning.haui.domain.dto.UserRespone(" +
           "u.userId, u.name, u.email, u.role.name, u.createdAt, u.emailVerified) " +
           "FROM User u JOIN u.role "+
           "ORDER BY u.userId ASC")
    Page<UserRespone> findUserSummaries(Pageable pageable);

    @Query("""
        SELECT new com.elearning.haui.domain.dto.UserRespone(
            u.userId, u.name, u.email, r.name, u.createdAt, u.emailVerified)
        FROM User u JOIN u.role r
        WHERE r.id = :roleId
        """)
    Page<UserRespone> findUsersByRole(Pageable pageable, @Param("roleId") Long roleId);

    @Query("""
        SELECT u
        FROM User u JOIN u.role
        WHERE u.userId = :userId
        """)
    User getUserDetailByAdmin(@Param("userId") Long userId);
}
