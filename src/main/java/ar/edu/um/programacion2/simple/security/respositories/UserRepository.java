package ar.edu.um.programacion2.simple.security.respositories;

import java.util.Optional;

import ar.edu.um.programacion2.simple.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends
        JpaRepository<User, String> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String userName);
    
}
