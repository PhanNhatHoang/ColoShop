package com.web.coloshop.Service;

import com.web.coloshop.Repository.CartItemRepository;
import com.web.coloshop.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    @Autowired
    private CartItemRepository cartItemRepository;

    public CartItem save(CartItem cartItem){
        return cartItemRepository.save(cartItem);
    }
    public List<CartItem> findByCartID(Long cartId){
        return cartItemRepository.findByCartId(cartId);
    }
    public void deleteById(Long CartItemID){
        cartItemRepository.deleteById(CartItemID);
    }
    public CartItem findById(Long id){
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
        return optionalCartItem.orElse(null);
    }
    public CartItem getProductQuantityInCart(Long productId, Long cartId) {
        Optional<CartItem> optionalcartItems = cartItemRepository.findByProductIdAndCartId(productId, cartId);
        return optionalcartItems.orElse(null);
    }
    public int getCountByCartId(Long cartId) {
        return cartItemRepository.countByCartId(cartId);
    }

}
