package com.example.seller.service;

import com.example.seller.domain.Product;
import com.example.seller.dao.ProductDAO;
import com.example.seller.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService
{
    private final ProductDAO productDAO;

    @Autowired
    public ProductService(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    public List<Product> getAllInStockProducts()
    {
        List<Product> inStock = productDAO.getAllInStockProducts();
        inStock.replaceAll(this::convertToUserProduct);
        return inStock;
    }

    public void addProduct(Product product)
    {
        productDAO.addProduct(product);
    }

    public Optional<Product> getProductById(int productId)
    {
        return Optional.ofNullable(productDAO.getProductById(productId));
    }

    public void updateProductQuantity(int productId, int newQuantity)
    {
        productDAO.updateProductQuantity(productId, newQuantity);
    }

    public void updateProductWholesalePrice(int productId, double newWholesalePrice)
    {
        productDAO.updateProductWholesalePrice(productId, newWholesalePrice);
    }

    public void updateProductRetailPrice(int productId, double newRetailPrice)
    {
        productDAO.updateProductRetailPrice(productId, newRetailPrice);
    }

    public void updateProductDescription(int productId, String newDescription)
    {
        productDAO.updateProductDescription(productId, newDescription);
    }

    public void updateProductName(int productId, String newName)
    {
        productDAO.updateProductName(productId, newName);
    }

    public List<Product> getAllProducts()
    {
        return productDAO.getAllProducts();
    }

    public List<Product> getMostFrequentlyPurchased(User user, int limit)
    {
        List<Product> mostFreq = productDAO.getMostFrequentlyPurchased(user, limit);
        mostFreq.replaceAll(this::convertToUserProduct);
        return mostFreq;
    }

    public List<Product> getMostRecentlyPurchased(User user, int limit)
    {
        List<Product> mostRecent = productDAO.getMostRecentlyPurchased(user, limit);
        mostRecent.replaceAll(this::convertToUserProduct);
        return mostRecent;
    }

    public List<Product> getMostProfitable(int limit)
    {
        return productDAO.getMostProfitable(limit);
    }

    public List<Product> getMostPopular(int limit)
    {
        return productDAO.getMostPopular(limit);
    }

    public Product convertToUserProduct(Product product)
    {
        return productDAO.convertToUserProduct(product);
    }

    public Long getTotalSold()
    {
        return productDAO.getTotalSold();
    }
}
