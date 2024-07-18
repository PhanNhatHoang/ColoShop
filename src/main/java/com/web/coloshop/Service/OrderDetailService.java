package com.web.coloshop.Service;

import com.web.coloshop.Repository.OrderDetailRepository;
import com.web.coloshop.model.OrderDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailService{
    @Autowired
    private OrderDetailRepository repo;

    public OrderDetail save(OrderDetail orderDetail){
        return repo.save(orderDetail);
    }
    public List<OrderDetail> findByOrderId(Long OrderId){
        return repo.findByOrderId(OrderId);
    }
}
