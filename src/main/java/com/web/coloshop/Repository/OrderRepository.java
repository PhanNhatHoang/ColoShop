package com.web.coloshop.Repository;

import com.web.coloshop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    @Query("SELECT o FROM Order o WHERE o.Code = ?1")
    Order findByCodeOrder(String codeOrder);

    List<Order> findByUserId(Long UserId);
}
