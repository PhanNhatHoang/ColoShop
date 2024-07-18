package com.web.coloshop.Service;

import com.web.coloshop.Repository.CategoryRepository;
import com.web.coloshop.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public Category save(Category category) {
          return repo.save(category);
    }
    public List<Category> findAll(){
        return repo.findAll();
    }
    public Category findByName(String name) {
        return repo.findByName(name);
    }
    public Category findById(Long id) {
        Optional<Category> optionalCategory = repo.findById(id);
        return optionalCategory.orElse(null);
    }
    public void deleteById(Long id) {
        Category category = repo.getById(id);
        category.set_activated(false);
        category.set_deleted(true);
        repo.save(category);
    }
    public void enabledById(Long id) {
        Category category = repo.getById(id);
        category.set_activated(true);
        category.set_deleted(false);
        repo.save(category);
    }
   // Product
    public List<Category> findAllByActivated() {
        return repo.findAllByActivated();
    }

}
