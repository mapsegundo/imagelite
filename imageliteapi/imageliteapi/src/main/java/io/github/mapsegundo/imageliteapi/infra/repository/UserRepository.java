package io.github.mapsegundo.imageliteapi.infra.repository;

import io.github.mapsegundo.imageliteapi.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByEmail(String email);
}
