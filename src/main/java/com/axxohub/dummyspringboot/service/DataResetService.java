package com.axxohub.dummyspringboot.service;

import com.axxohub.dummyspringboot.dto.CustomerRequest;
import com.axxohub.dummyspringboot.dto.OrderRequest;
import com.axxohub.dummyspringboot.dto.ProductRequest;
import com.axxohub.dummyspringboot.dto.TicketRequest;
import com.axxohub.dummyspringboot.model.Customer;
import com.axxohub.dummyspringboot.model.CustomerTier;
import com.axxohub.dummyspringboot.model.Product;
import com.axxohub.dummyspringboot.model.PurchaseOrder;
import com.axxohub.dummyspringboot.model.SupportTicket;
import com.axxohub.dummyspringboot.model.TicketPriority;
import com.axxohub.dummyspringboot.repository.CustomerRepository;
import com.axxohub.dummyspringboot.repository.ProductRepository;
import com.axxohub.dummyspringboot.repository.PurchaseOrderRepository;
import com.axxohub.dummyspringboot.repository.SupportTicketRepository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DataResetService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository orderRepository;
    private final SupportTicketRepository ticketRepository;
    private final CustomerService customerService;
    private final ProductService productService;
    private final OrderService orderService;
    private final TicketService ticketService;

    public DataResetService(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            PurchaseOrderRepository orderRepository,
            SupportTicketRepository ticketRepository,
            CustomerService customerService,
            ProductService productService,
            OrderService orderService,
            TicketService ticketService
    ) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.customerService = customerService;
        this.productService = productService;
        this.orderService = orderService;
        this.ticketService = ticketService;
    }

    @PostConstruct
    @Transactional
    public void initialize() {
        reset();
    }

    @Transactional
    public void reset() {
        ticketRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        customerRepository.deleteAll();

        Customer anita = customerService.create(new CustomerRequest("Anita Rao", "anita@example.com", CustomerTier.PREMIUM, true));
        Customer michael = customerService.create(new CustomerRequest("Michael Stone", "michael@example.com", CustomerTier.STANDARD, true));
        customerService.create(new CustomerRequest("Nexa Systems", "ops@nexa.example", CustomerTier.ENTERPRISE, false));

        Product license = productService.create(new ProductRequest("SKU-1001", "AI Gateway License", "SOFTWARE", new BigDecimal("499.99"), 12, true));
        Product sensor = productService.create(new ProductRequest("SKU-1002", "Automation Sensor", "HARDWARE", new BigDecimal("129.50"), 45, true));
        productService.create(new ProductRequest("SKU-1003", "Support Hours Pack", "SERVICE", new BigDecimal("299.00"), 8, true));

        orderService.create(new OrderRequest(anita.getId(), license.getId(), 1));
        PurchaseOrder confirmedOrder = orderService.create(new OrderRequest(michael.getId(), sensor.getId(), 2));
        orderService.confirm(confirmedOrder.getId());

        ticketService.create(new TicketRequest("anita@example.com", "Login failure", "Unable to sign in after password reset.", TicketPriority.HIGH));
        SupportTicket assignedTicket = ticketService.create(new TicketRequest("michael@example.com", "Invoice mismatch", "Invoice shows incorrect product count.", TicketPriority.MEDIUM));
        ticketService.assign(assignedTicket.getId(), "Ava");
    }
}
