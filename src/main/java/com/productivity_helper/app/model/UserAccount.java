package com.productivity_helper.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class UserAccount implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotBlank
    private String username;
    @Max(72)
    @NotNull
    private Character password;
    @Email
    private String email;
    @Enumerated(EnumType.STRING)
    @ManyToMany()
    @JoinTable(name = "role",
            joinColumns = {
                    @JoinColumn(name = "users")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "roles")
            })
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private Boolean isExpired;
    private Boolean isLocked;
    private Boolean isCredentialsExpired;
    private Boolean isEnabled;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
    }

    @Override
    public String getPassword() {
        return this.password.toString();
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.isLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !this.isCredentialsExpired;
    }

    public void setCredentialsExpired() {
        LocalDateTime dateOfAccountExpiration = getCreatedAt().plusMonths(24);
        LocalDateTime now = LocalDateTime.now();

        this.isCredentialsExpired = now.isAfter(dateOfAccountExpiration);
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(Character password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
