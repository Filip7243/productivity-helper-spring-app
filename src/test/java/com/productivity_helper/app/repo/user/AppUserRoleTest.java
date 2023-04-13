package com.productivity_helper.app.repo.user;

import com.productivity_helper.app.model.user.AppUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AppUserRoleTest {

    @Autowired
    private AppUserRoleRepository userRoleRepo;

    @BeforeEach
    void setUp() {
        AppUserRole admin = new AppUserRole("ADMIN");
        AppUserRole user = new AppUserRole("USER");
        AppUserRole guest = new AppUserRole("GUEST");

        userRoleRepo.save(admin);
        userRoleRepo.save(user);
        userRoleRepo.save(guest);
    }

    @Test
    public void shouldFindAllRoles() {
        List<AppUserRole> all = userRoleRepo.findAll();

        assertThat(all).isNotNull();
        assertThat(all.size()).isEqualTo(3);
    }

    @Test
    public void shouldFindRoleByName() {
        Optional<AppUserRole> admin = userRoleRepo.findByName("ADMIN");

        assertThat(admin).isPresent();
        assertThat(admin.get()).isNotNull();
        assertThat(admin.get().getName()).isEqualTo("ADMIN");

    }
}
