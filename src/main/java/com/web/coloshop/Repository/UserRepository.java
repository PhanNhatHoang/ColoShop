package com.web.coloshop.Repository;

import com.web.coloshop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    @Query("select u from User u where u.is_activated = true and u.is_deleted = false")
    List<User> findAllByActivated();
    User findByEmail(String email);
    User findByPass(String pass);
}
