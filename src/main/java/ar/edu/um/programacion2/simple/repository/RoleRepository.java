package ar.edu.um.programacion2.simple.repository;

import java.util.Optional;

import ar.edu.um.programacion2.simple.model.Role;
import ar.edu.um.programacion2.simple.security.enums.RoleList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository <Role, Integer> {
    Optional<Role> findByRoleName(RoleList roleName);
    
}
