package com.app.book_network.roles;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles,Long> {

    @Query(value = "select * from tbl_roles where name=:name", nativeQuery = true)
    Optional<Roles> findByRoleName(@Param("name")String name);
}
