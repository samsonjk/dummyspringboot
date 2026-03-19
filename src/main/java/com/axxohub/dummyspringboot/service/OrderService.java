package com.axxohub.dummyspringboot.service;

import com.axxohub.dummyspringboot.dto.OrderRequest;
import com.axxohub.dummyspringboot.exception.BadRequestException;
import com.axxohub.dummyspringboot.exception.ResourceNotFoundException;
import com.axxohub.dummyspringboot.model.OrderStatus;
import com.axxohub.dummyspringboot.model.Product;
import com.axxohub.dummyspringboot.model.PurchaseOrder;
import com.axxohub.dummyspringboot.repository.PurchaseOrderRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    private final PurchaseOrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductService productService;

    public OrderService(PurchaseOrderRepository orderRepository, CustomerService customerService, ProductService productService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productService = productService;
    }

    public List<PurchaseOrder> findAll() {
        return orderRepository.findAll();
    }

    public PurchaseOrder findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + id));
    }

    public List<PurchaseOrder> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    @Transactional
    public PurchaseOrder create(OrderRequest request) {
        customerService.findById(request.customerId());
        Product product = productService.findById(request.productId());

        if (product.getStockQuantity() < request.quantity()) {
            throw new BadRequestException("Not enough stock available");
        }

        PurchaseOrder order = new PurchaseOrder();
        order.setCustomerId(request.customerId());
        order.setProductId(request.productId());
        order.setQuantity(request.quantity());
        order.setTotalAmount(product.getPrice().multiply(BigDecimal.valueOf(request.quantity())));
        order.setStatus(OrderStatus.PENDING);
        order.setOrderedAt(LocalDateTime.now());

        product.setStockQuantity(product.getStockQuantity() - request.quantity());
        return orderRepository.save(order);
    }

    public PurchaseOrder confirm(Long id) {
        PurchaseOrder order = findById(id);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BadRequestException("Only pending orders can be confirmed");
        }
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }

    @Transactional
    public PurchaseOrder cancel(Long id) {
        PurchaseOrder order = findById(id);
        if (order.getStatus() == OrderStatus.CANCELLED) {
            return order;
        }
        Product product = productService.findById(order.getProductId());
        product.setStockQuantity(product.getStockQuantity() + order.getQuantity());
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    public void delete(Long id) {
        orderRepository.delete(findById(id));
    }
}
