package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);

    Boolean removeRoleById(Long id);

    Boolean removeRoleByName(String name);

    void updateRoleWithId(Long id, Role updatedRole);

    Boolean existsByName(String name);
}
