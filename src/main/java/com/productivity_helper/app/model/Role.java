package com.productivity_helper.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @ManyToMany(mappedBy = "roles")
    private Set<UserAccount> users;

    public void addRoleToUser(UserAccount account) {
        this.users.add(account);
        //TODO: add role to user
    }

    public void removeRoleFromUser(UserAccount account) {
        this.users.remove(account);
        account.getAuthorities().remove(this);
    }
}
