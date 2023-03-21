package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Boolean removeRoleById(Long id);

    Boolean removeRoleByName(String name);

    @Modifying
    @Query("UPDATE Role r SET r.name = :name WHERE r.id = :id")
    // method for ADMIN for using only in extremely cases!!!
    Integer updateRoleWithId(@Param("id") Long id, @Param("name") String name);

    Boolean existsByName(String name);
}
