package com.example.seller.dao;

import com.example.seller.domain.Product;
import com.example.seller.domain.User;
import com.example.seller.domain.Watchlist;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.*;

@Repository
public class WatchlistDAO extends AbstractHibernateDao<Watchlist>
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WatchlistDAO.class);

    public WatchlistDAO()
    {
        this.setClazz(Watchlist.class);
    }

    public Watchlist getWatchlistEntryById(int watchlistId)
    {
        return this.findById(watchlistId);
    }

    public List<Watchlist> getWatchlistByUser(User userId)
    {
        Session session = getCurrentSession();
        Query<Watchlist> query = session.createQuery("FROM Watchlist WHERE user = :userId AND inStock = true", Watchlist.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public Optional<Watchlist> getWatchlistsByProduct(User user, Product product)
    {
        Session session = getCurrentSession();
        Query<Watchlist> query = session.createQuery("FROM Watchlist WHERE user = :user AND product = :product", Watchlist.class);
        query.setParameter("user", user);
        query.setParameter("product", product);
        List<Watchlist> list = query.getResultList();

        if (list.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.ofNullable(list.get(0));
    }

    public void addToWatchlist(Watchlist watchlist)
    {
        this.add(watchlist);
    }

    public void removeFromWatchlist(Product product)
    {
        Session session = getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("DELETE FROM Watchlist WHERE product = :product");
        query.setParameter("product", product);
        query.executeUpdate();
        transaction.commit();;
    }

    public void updateWatchlistProduct(int watchlistId, boolean newInStock)
    {
        Session session = getCurrentSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaUpdate<Watchlist> update = builder.createCriteriaUpdate(Watchlist.class);
        Root<Watchlist> root = update.from(Watchlist.class);
        update.set(root.get("inStock"), newInStock).where(builder.equal(root.get("watchlistId"), Integer.valueOf(watchlistId)));

        Transaction transaction = session.beginTransaction();
        session.createQuery(update).executeUpdate();
        transaction.commit();
    }
}
