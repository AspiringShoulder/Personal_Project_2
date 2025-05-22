package com.example.seller.controller;

import com.example.seller.domain.Product;
import com.example.seller.domain.User;
import com.example.seller.domain.Watchlist;
import com.example.seller.dto.response.AllWatchlistResponse;
import com.example.seller.dto.response.ResponseStatus;
import com.example.seller.dto.response.WatchlistResponse;
import com.example.seller.exception.ProductNotFoundException;
import com.example.seller.exception.UserNotFoundException;
import com.example.seller.security.AuthUserDetails;
import com.example.seller.security.JWTProvider;
import com.example.seller.service.ProductService;
import com.example.seller.service.WatchlistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("watchlist")
public class WatchlistController
{
    private final WatchlistService watchlistService;
    private final JWTProvider jwtProvider;
    private final ProductService productService;

    @Autowired
    public WatchlistController(WatchlistService watchlistService, JWTProvider jwtProvider, ProductService productService)
    {
        this.watchlistService = watchlistService;
        this.jwtProvider = jwtProvider;
        this.productService = productService;
    }

    @GetMapping("/products/all")
    public ResponseEntity<AllWatchlistResponse> getAllWatchlistProducts(HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        User user = authUserDetailsOptional.get().getUser();
        List<Watchlist> watchlists = watchlistService.getAllWatchListProducts(user);
        AllWatchlistResponse response = AllWatchlistResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("watchlist items retrieved")
                                .build()
                )
                .watchlists(watchlists)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<WatchlistResponse> addToWatchlist(@PathVariable int productId,
                                                            HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        Optional<Product> productOptional = productService.getProductById(productId);

        if (!productOptional.isPresent())
        {
            throw new ProductNotFoundException("Cannot find product with this ID.");
        }

        User user = authUserDetailsOptional.get().getUser();
        Product product = productOptional.get();
        Optional<Watchlist> watchlistsOptional = watchlistService.getWatchlistsFromProduct(user, product);

        if (watchlistsOptional.isPresent())
        {
            throw new ProductNotFoundException("This product already exists in this watchlist.");
        }

        Boolean inStock = product.getQuantity() > 0;

        Watchlist watchlist = Watchlist.builder()
                        .user(user)
                        .product(product)
                        .inStock(inStock)
                        .build();

        watchlistService.addToWatchlist(watchlist);
        WatchlistResponse response = WatchlistResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("adding item...")
                                .build()
                )
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @DeleteMapping("/product/{productId}")
    public ResponseEntity<WatchlistResponse> removeFromWatchlist(@PathVariable int productId,
                                                                 HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        Optional<Product> productOptional = productService.getProductById(productId);

        if (!productOptional.isPresent())
        {
            throw new ProductNotFoundException("Cannot find product with this ID.");
        }

        User user = authUserDetailsOptional.get().getUser();
        Product product = productOptional.get();
        Optional<Watchlist> watchlistOptional = watchlistService.getWatchlistsFromProduct(user, product);

        if (!watchlistOptional.isPresent())
        {
            throw new ProductNotFoundException("This product is not in this watchlist.");
        }

        watchlistService.removeFromWatchlist(product);

        WatchlistResponse response = WatchlistResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Removed item.")
                                .build()
                )
                .build();

        return ResponseEntity.status(200).body(response);
    }
}
