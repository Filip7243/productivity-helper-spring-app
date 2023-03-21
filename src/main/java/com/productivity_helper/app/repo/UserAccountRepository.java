package com.productivity_helper.app.repo;

import com.productivity_helper.app.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountInterface extends JpaRepository<UserAccount, Long> {
}
