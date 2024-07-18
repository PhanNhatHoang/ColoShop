package com.web.coloshop.Service;

import com.web.coloshop.Repository.StatusRepository;
import com.web.coloshop.model.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusService {
    @Autowired
    private StatusRepository repo;

    public Status save(Status status) {
        return repo.save(status);
    }

    public List<Status> findAll() {
        return repo.findAll();
    }
    public Status findByName(String name){
        return repo.findByName(name);
    }
    public Status findById(Long id){
        Optional<Status> optionalStatus = repo.findById(id);
        return optionalStatus.orElse(null);
    }
}
