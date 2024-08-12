package com.app.book_network.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query(value = "select * from tbl_user where email=:email",nativeQuery = true)
    Optional<User> findByUserEmail(@Param("email")String email);
}
