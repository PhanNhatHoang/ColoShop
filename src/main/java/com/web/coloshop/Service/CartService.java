package com.web.coloshop.Service;

import com.web.coloshop.Repository.CartRepository;
import com.web.coloshop.model.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {
    @Autowired
    private CartRepository repo;

    public Cart save(Cart cart){
        return  repo.save(cart);
    }

    public Cart findbyUserId(Long UserId){
        return repo.findByUserId(UserId);
    }
}
