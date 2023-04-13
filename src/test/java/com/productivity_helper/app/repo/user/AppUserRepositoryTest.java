package com.productivity_helper.app.repo.user;

import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.model.user.AppUserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository userRepo;
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

        AppUser john = new AppUser("John", "Doe", "john@mail.com", "password123");
        AppUser mark = new AppUser("Mark", "Doe", "mark@mail.com", "password123");
        AppUser ada = new AppUser("Ada", "Doe", "ada@mail.com", "password123");
        AppUser userWithNoRoles = new AppUser("Ada", "Doe", "unknown@mail.com", "password123");
        userRepo.save(john);
        userRepo.save(mark);
        userRepo.save(ada);
        userRepo.save(userWithNoRoles);

        john.addRoleToUser(admin);
        john.addRoleToUser(user);
        john.addRoleToUser(guest);

        mark.addRoleToUser(user);
        mark.addRoleToUser(guest);

        ada.addRoleToUser(guest);
    }

    @Test
    public void shouldFindAllUsers() {
        List<AppUser> all = userRepo.findAll();

        assertThat(all).isNotNull();
        assertThat(all.size()).isEqualTo(4);
    }

    @Test
    public void shouldFindUserByEmail() {
        String mail = "john@mail.com";

        Optional<AppUser> foundUser = userRepo.findByEmail(mail);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo(mail);
    }
}
