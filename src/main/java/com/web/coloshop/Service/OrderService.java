package com.web.coloshop.Service;

import com.web.coloshop.Repository.OrderRepository;
import com.web.coloshop.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;

    public Order save(Order order){
        return repo.save(order);
    }
    public Order findById(Long id){
        Optional<Order> optinalOrder = repo.findById(id);
        return  optinalOrder.orElse(null);
    }
    public Order findFirstByCode(String codeOrder){
        return repo.findByCodeOrder(codeOrder);
    }
    //Customer
    public List<Order> findByUserId(Long UserId){
        return repo.findByUserId(UserId);
    }
    //Admin
    public  List<Order> findAll(){
        return repo.findAll();
    }
}
