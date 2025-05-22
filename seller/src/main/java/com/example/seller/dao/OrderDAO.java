package com.example.seller.dao;

import com.example.seller.domain.Order;
import com.example.seller.domain.User;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderDAO extends AbstractHibernateDao<Order>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderDAO.class);

    public OrderDAO()
    {
        setClazz(Order.class);
    }

    public Optional<Order> getOrderById(int orderId)
    {
        return Optional.of(this.findById(orderId));
    }

    public List<Order> getAllOrders()
    {
        return this.getAll();
    }

    public List<Order> getOrdersByUser(User user)
    {
        Session session = getCurrentSession();
        Query<Order> query = session.createQuery("FROM Order o WHERE o.userId = :id", Order.class);
        query.setParameter("id", user);
        return query.getResultList();
    }

    public void addOrder(Order order)
    {
        this.add(order);
    }

    public void setOrderStatus(Order order, String newStatus)
    {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("UPDATE Order o SET o.status = :status WHERE o.orderId = :id")
                .setParameter("status", newStatus)
                .setParameter("id", order.getOrderId())
                .executeUpdate();

        transaction.commit();
    }
}
