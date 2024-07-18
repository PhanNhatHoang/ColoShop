package com.web.coloshop.Service;

import com.web.coloshop.Repository.BrandRepository;
import com.web.coloshop.model.Brand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BrandService {
    @Autowired
    private BrandRepository repo;

    public Brand save(Brand brand) {
          return repo.save(brand);
    }
    public List<Brand> findAll(){
        return repo.findAll();
    }
    public Brand findByName(String name) {
        return repo.findByName(name);
    }
    public Brand findById(Long id) {
        Optional<Brand> optionalBrand = repo.findById(id);
        return optionalBrand.orElse(null);
    }
    public void deleteById(Long id) {
        Brand brand = repo.getById(id);
        brand.set_deleted(true);
        brand.set_activated(false);
        repo.save(brand);
    }
    public void enabledById(Long id) {
        Brand brand = repo.getById(id);
        brand.set_activated(true);
        brand.set_deleted(false);
        repo.save(brand);
    }
    public List<Brand> findAllByActivated() {
        return repo.findAllByActivated();
    }
}
