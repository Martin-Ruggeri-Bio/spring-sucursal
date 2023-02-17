package ar.edu.um.programacion2.simple.service;

import java.util.Optional;

import ar.edu.um.programacion2.simple.model.Role;
import ar.edu.um.programacion2.simple.security.enums.RoleList;
import ar.edu.um.programacion2.simple.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> getByRoleName(RoleList roleName){
        return this.roleRepository.findByRoleName(roleName);
    }
    
    
}
