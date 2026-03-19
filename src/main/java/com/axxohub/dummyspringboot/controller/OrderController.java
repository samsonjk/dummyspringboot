package com.axxohub.dummyspringboot.controller;

import com.axxohub.dummyspringboot.dto.OrderRequest;
import com.axxohub.dummyspringboot.model.OrderStatus;
import com.axxohub.dummyspringboot.model.PurchaseOrder;
import com.axxohub.dummyspringboot.service.OrderService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<PurchaseOrder> getAll() {
        return orderService.findAll();
    }

    @GetMapping("/{id}")
    public PurchaseOrder getById(@PathVariable Long id) {
        return orderService.findById(id);
    }

    @GetMapping("/status/{status}")
    public List<PurchaseOrder> getByStatus(@PathVariable OrderStatus status) {
        return orderService.findByStatus(status);
    }

    @PostMapping
    public PurchaseOrder create(@Valid @RequestBody OrderRequest request) {
        return orderService.create(request);
    }

    @PostMapping("/{id}/confirm")
    public PurchaseOrder confirm(@PathVariable Long id) {
        return orderService.confirm(id);
    }

    @PostMapping("/{id}/cancel")
    public PurchaseOrder cancel(@PathVariable Long id) {
        return orderService.cancel(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        orderService.delete(id);
    }
}
