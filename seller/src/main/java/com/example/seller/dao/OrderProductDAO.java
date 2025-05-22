package com.example.seller.dao;

import com.example.seller.domain.Order;
import com.example.seller.domain.OrderProduct;
import com.example.seller.domain.Product;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class OrderProductDAO extends AbstractHibernateDao<OrderProduct>
{
    public OrderProductDAO()
    {
        this.setClazz(OrderProduct.class);
    }

    public List<OrderProduct> getAllOrderProducts()
    {
        return this.getAll();
    }

    public OrderProduct getOrderProductById(int orderProductId)
    {
        return this.findById(orderProductId);
    }

    public List<OrderProduct> getOrderProductByOrder(Order orderId)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<OrderProduct> criteria = builder.createQuery(clazz);
        Root<OrderProduct> root = criteria.from(OrderProduct.class);
        criteria.select(root).where(builder.equal(root.get("orderId"), orderId));
        return session.createQuery(criteria).getResultList();
    }

    public List<OrderProduct> getOrderProductByProduct(Product productId)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<OrderProduct> criteria = builder.createQuery(clazz);
        Root<OrderProduct> root = criteria.from(OrderProduct.class);
        criteria.select(root).where(builder.equal(root.get("productId"), productId));
        return session.createQuery(criteria).getResultList();
    }

    public void addOrderProduct(OrderProduct orderProduct)
    {
        this.add(orderProduct);
    }
}
