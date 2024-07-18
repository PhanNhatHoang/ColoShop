package com.web.coloshop.Service;

import com.web.coloshop.Repository.ContactRepository;
import com.web.coloshop.model.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    @Autowired
    private ContactRepository repo;

    public Contact save(Contact contact) {
        return repo.save(contact);
    }
    public List<Contact> findAll(){
        return repo.findAll();
    }
    public void Delete( Long id){
        repo.deleteById(id);
    }
}
