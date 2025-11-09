package org.example.testcontainer.repository;

import org.example.testcontainer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
