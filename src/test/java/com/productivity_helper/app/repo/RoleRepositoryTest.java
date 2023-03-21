package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        Role admin = new Role(null, "ROLE_ADMIN", new HashSet<>());
        Role user = new Role(null, "ROLE_USER", new HashSet<>());
        Role moderator = new Role(null, "ROLE_MODERATOR", new HashSet<>());

        roleRepository.save(admin);
        roleRepository.save(user);
        roleRepository.save(moderator);
    }

    @Test
    public void itShouldFindRoleByName() {
        Optional<Role> foundRole = roleRepository.findByName("ROLE_ADMIN");

        makeSureThatRoleWasFound(foundRole);
    }

    @Test
    public void itShouldNotFindRoleByName() {
        Optional<Role> foundRole = roleRepository.findByName("ROLE_NON_EXISTING");

        makeSureThatRoleWasNotFound(foundRole);
    }

    @Test
    public void itShouldFindRoleByNameAfterSave() {
        Role newRole = new Role(null, "NEW_ROLE", new HashSet<>());

        roleRepository.save(newRole);
        Optional<Role> foundRole = roleRepository.findByName(newRole.getName());

        makeSureThatRoleWasFound(foundRole);
    }

    @Test
    public void itShouldNotSaveRoleBecauseRoleAlreadyExists() {
        Role newRole = new Role(null, "ROLE_ADMIN", new HashSet<>()); // role already exists

        assertThrows(DataIntegrityViolationException.class, () -> roleRepository.save(newRole));
        makeSureThatRoleWasFound(Optional.of(newRole));
    }

    @Test
    public void itShouldUpdateRoleWithId() {
        Optional<Role> foundRole = roleRepository.findById(1L);
        String oldName = foundRole.get().getName();
        String newName = "ROLE_TEST";

        int updatedRows = roleRepository.updateRoleWithId(foundRole.get().getId(), newName);
        Optional<Role> oldRole = roleRepository.findByName(oldName);
        Optional<Role> newRole = roleRepository.findByName(newName);

        assertEquals(updatedRows, 1); // 1 row was updated
        makeSureThatRoleWasNotFound(oldRole);
        makeSureThatRoleWasFound(newRole);
    }

    @Test
    public void itShouldNotUpdateRoleWithIdBecauseDoesntExist() {
        String newName = "ROLE_TEST";

        int updatedRows = roleRepository.updateRoleWithId(0L, newName);

        assertEquals(updatedRows, 0); // 1 row was updated
    }

    private static void makeSureThatRoleWasFound(Optional<Role> foundRole) {
        assertThat(foundRole).isPresent();
        assertThat(foundRole).isNotNull();
        assertThat(foundRole.get()).isInstanceOf(Role.class);
    }

    private static void makeSureThatRoleWasNotFound(Optional<Role> foundRole) {
        assertThat(foundRole).isNotPresent();
        assertThat(foundRole).isNotNull();
    }
}
