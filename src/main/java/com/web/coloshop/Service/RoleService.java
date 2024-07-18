package com.web.coloshop.Service;

import com.web.coloshop.Repository.CategoryRepository;
import com.web.coloshop.Repository.RoleRepository;
import com.web.coloshop.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {
    @Autowired
    private RoleRepository repo;

    public Role save(Role role) {
          return repo.save(role);
    }
    public List<Role> findAll(){
        return repo.findAll();
    }
    public Role findByName(String name) {
        return repo.findByName(name);
    }
    public Role findById(Long id) {
        Optional<Role> optionalRole = repo.findById(id);
        return optionalRole.orElse(null);
    }

}
