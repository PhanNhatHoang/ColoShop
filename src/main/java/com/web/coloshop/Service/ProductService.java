package com.web.coloshop.Service;

import com.web.coloshop.Repository.ProductRepository;
import com.web.coloshop.model.Category;
import com.web.coloshop.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.security.PrivateKey;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;

    public List<Product> findAll(){
        return repo.findAll();
    }
    public Product save(Product product) {
        return repo.save(product);
    }

    public Product findById(Long id) {
        Optional<Product> optionalProduct = repo.findById(id);
        return optionalProduct.orElse(null);
    }
    public void deleteById(Long id) {
        Product product = repo.getById(id);
        product.set_activated(false);
        product.set_deleted(true);
        repo.save(product);
    }
    public void enabledById(Long id) {
        Product product = repo.getById(id);
        product.set_activated(true);
        product.set_deleted(false);
        repo.save(product);
    }

    public List<Product> getAllProducts() {
        return repo.getAllProducts();
    }

    public List<Product> findByCategoryId(Long id) {
        return repo.findByCategoryId(id);
    }
}
