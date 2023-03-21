package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.Role;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    private Set<Role> roles;

    @BeforeEach
    public void setUp() {
        Role admin = new Role(null, "ROLE_ADMIN", new HashSet<>());
        Role user = new Role(null, "ROLE_USER", new HashSet<>());
        Role moderator = new Role(null, "ROLE_MODERATOR", new HashSet<>());

        roles = new HashSet<>(){{
            this.add(admin);
            this.add(user);
            this.add(moderator);
        }};

        roleRepository.save(admin);
        roleRepository.save(user);
        roleRepository.save(moderator);
    }

}
