package com.productivity_helper.app.service.user;

import com.productivity_helper.app.controller.user.RegisterRequest;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.repo.user.AppUserRepository;
import com.productivity_helper.app.security.CustomPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTests {

    @Mock
    private AppUserRepository userRepo;
    private CustomPasswordEncoder passwordEncoder;
    private AppUserService userService;

    @BeforeEach
    void setUp() {
        this.passwordEncoder = new CustomPasswordEncoder();
        this.userService = new AppUserService(userRepo, passwordEncoder);
    }

    @Test
    public void canLoadUserByUsername() {
        String email = anyString();

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(new AppUser()));

        UserDetails user = userService.loadUserByUsername(email);

        assertThat(user).isNotNull();
        assertThat(user).isInstanceOf(UserDetails.class);
    }

    @Test
    public void throwsExceptionWhenLoadUserByUsername() {
        String email = anyString();

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> userService.loadUserByUsername(email));

        assertThat(ex.getMessage()).isEqualTo("User not found!");
    }

    @Test
    public void canCreateUser() {
        String any = anyString();
        RegisterRequest registerRequest = new RegisterRequest(any, any, any, any);

        when(userRepo.existsByEmail(any)).thenReturn(false);

        String email = userService.createUser(registerRequest);

        assertThat(email).isEqualTo(registerRequest.username());
    }

    @Test
    public void throwsExceptionWhenCreateUser() {
        String any = anyString();
        RegisterRequest registerRequest = new RegisterRequest(any, any, any, any);

        when(userRepo.existsByEmail(any)).thenReturn(true);

        RuntimeException ex =
                assertThrows(RuntimeException.class, () -> userService.createUser(registerRequest));

        assertThat(ex.getMessage()).isEqualTo("User with email already exists!");
    }
}