package com.example.seller.controller;

import com.example.seller.domain.Product;
import com.example.seller.dto.request.ProductRequest;
import com.example.seller.dto.response.AllProductsResponse;
import com.example.seller.dto.response.ProductResponse;
import com.example.seller.exception.ForbiddenException;
import com.example.seller.exception.ProductNotFoundException;
import com.example.seller.exception.UserNotFoundException;
import com.example.seller.security.AuthUserDetails;
import com.example.seller.security.JWTProvider;
import com.example.seller.service.ProductService;
import com.example.seller.dto.response.ResponseStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
@CrossOrigin("origins = http://localhost:4200")
public class ProductController
{
    private final ProductService productService;
    private final JWTProvider jwtProvider;

    @Autowired
    public ProductController(ProductService productService, JWTProvider jwtProvider)
    {
        this.productService = productService;
        this.jwtProvider = jwtProvider;
    }

    @GetMapping("/all")
    public ResponseEntity<AllProductsResponse> getAllProducts(HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AllProductsResponse response;
        AuthUserDetails authUserDetails = authUserDetailsOptional.get();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("admin");
        if (authUserDetails.getAuthorities().contains(auth))
        {
            response = AllProductsResponse.builder()
                    .status(
                            ResponseStatus.builder()
                                    .success(true)
                                    .message("Returning all products...")
                                    .build()
                    )
                    .products(productService.getAllProducts())
                    .build();
        }

        else
        {
            response = AllProductsResponse.builder()
                    .status(
                            ResponseStatus.builder()
                                    .success(true)
                                    .message("Returning all in-stock products...")
                                    .build()
                    )
                    .products(productService.getAllInStockProducts())
                    .build();
        }

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable int productId, HttpServletRequest request)
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

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();
        Product product = productOptional.get();
        ProductResponse response;

        if (authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            response = ProductResponse.builder()
                    .status(
                            ResponseStatus.builder()
                                    .success(true)
                                    .message("Product was found")
                                    .build()
                    )
                    .product(product)
                    .build();
        }

        else
        {
            response = ProductResponse.builder()
                    .status(
                            ResponseStatus.builder()
                                    .success(true)
                                    .message("Product was found")
                                    .build()
                    )
                    .product(productService.convertToUserProduct(product))
                    .build();
        }

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable int productId,
                                                         @RequestBody ProductRequest productRequest,
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

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        productService.updateProductDescription(productId, productRequest.getDescription());
        productService.updateProductQuantity(productId, productRequest.getQuantity());
        productService.updateProductWholesalePrice(productId, productRequest.getWholesalePrice());
        productService.updateProductRetailPrice(productId, productRequest.getRetailPrice());
        productService.updateProductName(productId, productRequest.getName());

        Product product = productService.getProductById(productId).get();

        ProductResponse response = ProductResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("product updated")
                                .build()
                )
                .product(product)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .wholesalePrice(productRequest.getWholesalePrice())
                .retailPrice(productRequest.getRetailPrice())
                .quantity(productRequest.getQuantity())
                .build();

        productService.addProduct(newProduct);
        ProductResponse response = ProductResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("successfully added product")
                                .build()
                )
                .product(newProduct)
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/frequent/{limit}")
    public ResponseEntity<AllProductsResponse> mostFrequentlyPurchased(@PathVariable int limit, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("nonAdmin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        List<Product> mostFreq = productService.getMostFrequentlyPurchased(authUserDetails.getUser(), limit);
        AllProductsResponse response = AllProductsResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Returning " + limit + " most frequently purchased products...")
                                .build()
                )
                .products(mostFreq)
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/recent/{limit}")
    public ResponseEntity<AllProductsResponse> mostRecentlyPurchased(@PathVariable int limit, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("nonAdmin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        List<Product> mostRecent = productService.getMostRecentlyPurchased(authUserDetails.getUser(), limit);
        AllProductsResponse response = AllProductsResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Returning " + limit + " most recently purchased products...").build()
                )
                .products(mostRecent)
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/profit/{limit}")
    public ResponseEntity<AllProductsResponse> mostProfitableProducts(@PathVariable int limit, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        List<Product> mostProfits = productService.getMostProfitable(limit);
        AllProductsResponse response = AllProductsResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Returning most profitable products...")
                                .build()
                )
                .products(mostProfits)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/popular/{limit}")
    public ResponseEntity<AllProductsResponse> mostPopularProducts(@PathVariable int limit, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        List<Product> mostPopular = productService.getMostPopular(limit);
        AllProductsResponse response = AllProductsResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Returning most popular products...")
                                .build()
                )
                .products(mostPopular)
                .build();
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/sell_success")
    public ResponseEntity<ResponseStatus> successfullySoldProducts(HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();

        if (!authUserDetails.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            throw new ForbiddenException("Access denied.");
        }

        Long totalSold = productService.getTotalSold();

        ResponseStatus response = ResponseStatus.builder().success(true).message("Total number of items sold: " + totalSold).build();
        return ResponseEntity.status(200).body(response);
    }
}
