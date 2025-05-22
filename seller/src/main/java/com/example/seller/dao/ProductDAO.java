package com.example.seller.dao;

import com.example.seller.domain.Product;

import com.example.seller.domain.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductDAO extends AbstractHibernateDao<Product>
{
    public ProductDAO()
    {
        this.setClazz(Product.class);
    }

    public Product getProductById(int productId)
    {
        return this.findById(productId);
    }

    public List<Product> getAllProducts()
    {
        return this.getAll();
    }

    public Long getTotalSold()
    {
        Session session = getCurrentSession();
        Query<Long> query = session.createQuery("SELECT SUM(op.orderQuantity) "+
                "FROM Order o JOIN OrderProduct op ON o = op.orderId " +
                "WHERE o.status = 'completed'", Long.class);
        return query.getSingleResult();
    }

    public List<Product> getMostProfitable(int limit)
    {
        Session session = getCurrentSession();
        Query<Product> query = session.createQuery("SELECT op.productId " +
                "FROM Order o JOIN OrderProduct op ON o = op.orderId " +
                "WHERE o.status = 'completed'" +
                "GROUP BY op.productId " +
                "ORDER BY SUM(op.profit) DESC", Product.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Product> getMostPopular(int limit)
    {
        Session session = getCurrentSession();
        Query<Product> query = session.createQuery("SELECT op.productId " +
                "FROM Order o JOIN OrderProduct op ON o = op.orderId " +
                "WHERE o.status = 'completed'" +
                "GROUP BY op.productId " +
                "ORDER BY COUNT(op.productId) DESC", Product.class);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Product> getMostFrequentlyPurchased(User user, int limit)
    {
        Session session = getCurrentSession();
        Query<Product> query = session.createQuery("SELECT op.productId " +
                "FROM Order o JOIN OrderProduct op ON o = op.orderId " +
                "WHERE o.userId = :user AND o.status != 'canceled'" +
                "GROUP BY op.productId " +
                "ORDER BY COUNT(op.productId) DESC", Product.class);
        query.setParameter("user", user);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Product> getMostRecentlyPurchased(User user, int limit)
    {
        Session session = getCurrentSession();
        Query<Product> query = session.createQuery("SELECT op.productId " +
                "FROM Order o JOIN OrderProduct op ON o = op.orderId " +
                "WHERE o.userId = :user " +
                "ORDER BY o.timeCreated DESC", Product.class);
        query.setParameter("user", user);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public List<Product> getAllInStockProducts()
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = builder.createQuery(clazz);
        Root<Product> root = criteria.from(Product.class);
        criteria.select(root).where(builder.greaterThan(root.get("quantity"), 0));
        return session.createQuery(criteria).getResultList();
    }

    public void updateProductQuantity(int productId, int newQuantity)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Product> update = builder.createCriteriaUpdate(Product.class);
        Root<Product> root = update.from(Product.class);
        update.set(root.get("quantity"), newQuantity).where(builder.equal(root.get("productId"), Integer.valueOf(productId)));

        Transaction transaction = session.beginTransaction();
        session.createQuery(update).executeUpdate();
        transaction.commit();
    }

    public void updateProductWholesalePrice(int productId, double newWholesalePrice)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Product> update = builder.createCriteriaUpdate(Product.class);
        Root<Product> root = update.from(Product.class);
        update.set(root.get("wholesalePrice"), newWholesalePrice).where(builder.equal(root.get("productId"), Integer.valueOf(productId)));

        Transaction transaction = session.beginTransaction();
        session.createQuery(update).executeUpdate();
        transaction.commit();
    }

    public void updateProductRetailPrice(int productId, double newRetailPrice)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Product> update = builder.createCriteriaUpdate(Product.class);
        Root<Product> root = update.from(Product.class);
        update.set(root.get("retailPrice"), newRetailPrice).where(builder.equal(root.get("productId"), Integer.valueOf(productId)));

        Transaction transaction = session.beginTransaction();
        session.createQuery(update).executeUpdate();
        transaction.commit();
    }

    public void updateProductDescription(int productId, String newDescription)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Product> update = builder.createCriteriaUpdate(Product.class);
        Root<Product> root = update.from(Product.class);
        update.set(root.get("description"), newDescription).where(builder.equal(root.get("productId"), Integer.valueOf(productId)));

        Transaction transaction = session.beginTransaction();
        session.createQuery(update).executeUpdate();
        transaction.commit();
    }

    public void updateProductName(int productId, String newName)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Product> update = builder.createCriteriaUpdate(Product.class);
        Root<Product> root = update.from(Product.class);
        update.set(root.get("name"), newName).where(builder.equal(root.get("productId"), Integer.valueOf(productId)));

        Transaction transaction = session.beginTransaction();
        session.createQuery(update).executeUpdate();
        transaction.commit();
    }

    public void addProduct(Product product)
    {
        this.add(product);
    }

    public Product convertToUserProduct(Product product)
    {
        return Product.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .retailPrice(product.getRetailPrice())
                .build();
    }
}
