package com.web.coloshop.Repository;

import com.web.coloshop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {
    List<CartItem> findByCartId(Long cartId);
    Optional<CartItem> findByProductIdAndCartId(Long productId, Long cartId);

    int countByCartId(Long cartId);

    @Modifying
    @Query("DELETE FROM CartItem c WHERE c.id = :cartItemId")
    void deleteById(@Param("cartItemId") Long cartItemId);

    void deleteByCartId(Long cartId);
}
