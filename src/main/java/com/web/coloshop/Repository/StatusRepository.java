package com.web.coloshop.Repository;

import com.web.coloshop.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends JpaRepository<Status,Long> {

    Status findByName(String name);
}
