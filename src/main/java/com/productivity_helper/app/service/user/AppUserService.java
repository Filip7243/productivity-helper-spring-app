package com.productivity_helper.app.service.user;

import com.productivity_helper.app.controller.user.RegisterRequest;
import com.productivity_helper.app.model.user.AppUser;
import com.productivity_helper.app.repo.user.AppUserRepository;
import com.productivity_helper.app.security.CustomPasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository userRepo;
    private final CustomPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public String createUser(RegisterRequest registerRequest) {
        String username = registerRequest.username();
        if (userRepo.existsByEmail(username)) {
            throw new RuntimeException("User with email already exists!");
        }

        String encodedPassword = passwordEncoder.passwordEncoder().encode(registerRequest.password());
        AppUser user = new AppUser(registerRequest.firstName(),
                registerRequest.lastName(),
                registerRequest.username(),
                encodedPassword
        );
        // TODO: add basic role
        userRepo.save(user);

        return user.getUsername();
    }
}
