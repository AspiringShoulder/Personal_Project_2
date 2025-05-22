package com.example.seller.service;

import com.example.seller.dao.ProductDAO;
import com.example.seller.domain.Product;
import com.example.seller.domain.User;
import com.example.seller.domain.Watchlist;
import com.example.seller.dao.WatchlistDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WatchlistService
{
    private final WatchlistDAO watchlistDAO;
    private final ProductDAO productDAO;

    @Autowired
    public WatchlistService(WatchlistDAO watchlistDAO, ProductDAO productDAO)
    {
        this.watchlistDAO = watchlistDAO;
        this.productDAO = productDAO;
    }

    public List<Watchlist> getAllWatchListProducts(User user)
    {
        List<Watchlist> watchlists = watchlistDAO.getWatchlistByUser(user);
        for (Watchlist watchlist: watchlists)
        {
            Product product = watchlist.getProduct();
            watchlist.setProduct(productDAO.convertToUserProduct(product));
        }

        return watchlists;
    }

    public void addToWatchlist(Watchlist watchlist)
    {
        watchlistDAO.addToWatchlist(watchlist);
    }

    public Optional<Watchlist> getWatchlistsFromProduct(User user, Product product)
    {
        return watchlistDAO.getWatchlistsByProduct(user, product);
    }

    public void removeFromWatchlist(Product product)
    {
        watchlistDAO.removeFromWatchlist(product);
    }
}
