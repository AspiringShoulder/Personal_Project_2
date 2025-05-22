package com.example.seller.controller;

import com.example.seller.domain.OrderProduct;
import com.example.seller.domain.Product;
import com.example.seller.domain.User;
import com.example.seller.domain.Order;
import com.example.seller.dto.request.OrderDTO;
import com.example.seller.dto.request.OrderRequest;
import com.example.seller.dto.response.AllOrdersResponse;
import com.example.seller.dto.response.OrderProductsResponse;
import com.example.seller.dto.response.OrderResponse;
import com.example.seller.dto.response.ResponseStatus;
import com.example.seller.exception.*;
import com.example.seller.security.AuthUserDetails;
import com.example.seller.security.JWTProvider;
import com.example.seller.service.OrderProductService;
import com.example.seller.service.OrderService;
import com.example.seller.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("orders")
public class OrderController
{
    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final JWTProvider jwtProvider;
    private final ProductService productService;

    @Autowired
    public OrderController(OrderService orderService, OrderProductService orderProductService, JWTProvider jwtProvider, ProductService productService)
    {
        this.orderService = orderService;
        this.orderProductService = orderProductService;
        this.jwtProvider = jwtProvider;
        this.productService = productService;
    }

    @GetMapping("/all")
    public ResponseEntity<AllOrdersResponse> getAllOrders(HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);
        AllOrdersResponse response;

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails user = authUserDetailsOptional.get();
        if (user.getAuthorities().contains(new SimpleGrantedAuthority("admin")))
        {
            response = AllOrdersResponse.builder()
                    .status(
                            ResponseStatus.builder()
                                    .success(true)
                                    .message("Returning all orders...")
                                    .build()
                    )
                    .orders(orderService.getAllOrders())
                    .build();
        }

        else
        {
            response = AllOrdersResponse.builder()
                    .status(
                            ResponseStatus.builder()
                                    .success(true)
                                    .message("Returning all orders you have made...")
                                    .build()
                    )
                    .orders(orderService.getAllOrdersByUser(user.getUser()))
                    .build();

        }

        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderProductsResponse> getOrder(@PathVariable int orderId, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();
        User user = authUserDetails.getUser();
        Optional<Order> orderOptional = orderService.getOrder(orderId);

        if (!orderOptional.isPresent())
        {
            throw new OrderNotFoundException();
        }

        Order order = orderOptional.get();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("admin");

        if (!order.getUserId().equals(user) && !authUserDetails.getAuthorities().contains(auth))
        {
            throw new ForbiddenException("This order does not belong to you.");
        }
        
        OrderProductsResponse response = OrderProductsResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Order found.")
                                .build()
                )
                .products(orderProductService.getOrderProductsByOrder(order))
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @PatchMapping("/{orderId}/{newStatus}")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable int orderId,
                                                           @PathVariable String newStatus,
                                                           HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        Optional<Order> orderOptional = orderService.getOrder(orderId);

        if (!orderOptional.isPresent())
        {
            throw new OrderNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();
        SimpleGrantedAuthority auth = new SimpleGrantedAuthority("admin");
        Order order = orderOptional.get();

        if (newStatus.equals("completed") && !authUserDetails.getAuthorities().contains(auth))
        {
            throw new ForbiddenException("Only admins can complete orders.");
        }

        if (order.getStatus().equals("completed") && newStatus.equals("canceled"))
        {
            throw new OrderStatusException("Cannot cancel a completed order.");
        }

        if (order.getStatus().equals("canceled") && newStatus.equals("completed"))
        {
            throw new OrderStatusException("Cannot complete a canceled order.");
        }

        if (order.getStatus().equals(newStatus))
        {
            throw new OrderStatusException("The order is already at this status.");
        }

        orderService.setOrderStatus(order, newStatus);
        OrderResponse response = OrderResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Updated status. Returning old order...")
                                .build()
                )
                .order(order)
                .build();

        return ResponseEntity.status(200).body(response);
    }

    @PostMapping()
    public ResponseEntity<OrderResponse> addOrder(@RequestBody OrderRequest orderRequest, HttpServletRequest request)
    {
        Optional<AuthUserDetails> authUserDetailsOptional = jwtProvider.resolveToken(request);

        if (!authUserDetailsOptional.isPresent())
        {
            throw new UserNotFoundException();
        }

        AuthUserDetails authUserDetails = authUserDetailsOptional.get();
        User user = authUserDetails.getUser();
        Order order = Order.builder()
                .userId(user)
                .status("processing")
                .timeCreated(Date.valueOf(LocalDate.now()))
                .build();

        orderService.addOrder(order);

        OrderDTO[] products = orderRequest.getOrder();
        for (OrderDTO product: products)
        {
            Optional<Product> productInStockOptional = productService.getProductById(product.getProductId());

            if (!productInStockOptional.isPresent())
            {
                throw new ProductNotFoundException("No product with this ID was found.");
            }

            Product productInStock = productInStockOptional.get();
            int orderQuantity = product.getQuantity();
            int inStockQuantity = productInStock.getQuantity();

            if (inStockQuantity == 0)
            {
                throw new NotEnoughInventoryException("This product is out of stock.");
            }

            if (orderQuantity > inStockQuantity)
            {
                throw new NotEnoughInventoryException("Your order quantity exceeds the in-stock quantity for " + productInStock.getName() + ".");
            }

            OrderProduct orderProduct = OrderProduct.builder()
                    .orderId(order)
                    .productId(productInStock)
                    .retailPriceAtTimeOfPurchase(productInStock.getRetailPrice())
                    .wholesalePriceAtTimeOfPurchase(productInStock.getWholesalePrice())
                    .orderQuantity(orderQuantity)
                    .build();

            orderProductService.addOrderProduct(orderProduct);
            productService.updateProductQuantity(product.getProductId(), inStockQuantity - orderQuantity);
        }

        OrderResponse response = OrderResponse.builder()
                .status(
                        ResponseStatus.builder()
                                .success(true)
                                .message("Order successfully created. Returning order...")
                                .build()
                )
                .order(order)
                .build();
        return ResponseEntity.status(200).body(response);
    }
}
