package ar.edu.um.programacion2.simple.security.services;

import java.util.Optional;

import javax.transaction.Transactional;

import ar.edu.um.programacion2.simple.model.Role;
import ar.edu.um.programacion2.simple.security.enums.RoleList;
import ar.edu.um.programacion2.simple.security.respositories.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    public Optional<Role> getByRoleName(RoleList roleName){
        return roleRepository.findByRoleName(roleName);
    }
    
    
}
