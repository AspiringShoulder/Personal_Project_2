package com.example.seller.service;

import com.example.seller.dao.OrderProductDAO;
import com.example.seller.domain.Order;
import com.example.seller.domain.OrderProduct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderProductService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProductService.class);
    private final OrderProductDAO orderProductDAO;

    @Autowired
    public OrderProductService(OrderProductDAO orderProductDAO)
    {
        LOGGER.info("orderProductService created");
        this.orderProductDAO = orderProductDAO;
    }

    public List<OrderProduct> getAllOrderProducts()
    {
        return orderProductDAO.getAllOrderProducts();
    }

    public List<OrderProduct> getOrderProductsByOrder(Order order)
    {
        return orderProductDAO.getOrderProductByOrder(order);
    }

    public void addOrderProduct(OrderProduct orderProduct)
    {
        orderProductDAO.addOrderProduct(orderProduct);
    }
}
