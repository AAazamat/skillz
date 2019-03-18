package com.github.skillz.mailserver.repository;

import com.github.skillz.mailserver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
