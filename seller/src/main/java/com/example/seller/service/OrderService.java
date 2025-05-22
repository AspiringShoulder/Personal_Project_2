package com.example.seller.service;

import com.example.seller.dao.OrderDAO;
import com.example.seller.dao.UserDAO;
import com.example.seller.domain.Order;
import com.example.seller.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);
    private final OrderDAO orderDAO;
    private final UserDAO userDAO;

    @Autowired
    public OrderService(OrderDAO orderDAO, UserDAO userDAO)
    {
        LOGGER.info("OrderService created");
        this.orderDAO = orderDAO;
        this.userDAO = userDAO;
    }

    public List<Order> getAllOrders()
    {
        return orderDAO.getAllOrders();
    }

    public Optional<Order> getOrder(int orderId)
    {
        return orderDAO.getOrderById(orderId);
    }

    public void setOrderStatus(Order order, String status)
    {
        orderDAO.setOrderStatus(order, status);
    }

    public void addOrder(Order order)
    {
        orderDAO.addOrder(order);
    }

    public List<Order> getAllOrdersByUser(User user)
    {
        return orderDAO.getOrdersByUser(userDAO.getUserById(user.getUserId()));
    }
}
