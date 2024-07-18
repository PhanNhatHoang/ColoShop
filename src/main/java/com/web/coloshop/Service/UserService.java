package com.web.coloshop.Service;

import com.web.coloshop.Repository.UserRepository;
import com.web.coloshop.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repo;
    @Autowired
    public List<User> findAll() {
        return repo.findAll();
    }
    public User save(User user){
        return repo.save(user);
    }
    public User findByEmail(String email) {
        return repo.findByEmail(email);
    }
    public User findByPass(String pass) {
        return repo.findByPass(pass);
    }
    public void deleteById(Long id) {
        User user = repo.getById(id);
        user.set_activated(false);
        user.set_deleted(true);
        repo.save(user);
    }
    public void enabledById(Long id) {
        User user = repo.getById(id);
        user.set_activated(true);
        user.set_deleted(false);
        repo.save(user);
    }
    public User findById(Long id) {
        Optional<User> optionalUser = repo.findById(id);
        return optionalUser.orElse(null);
    }

}
