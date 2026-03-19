package com.axxohub.dummyspringboot.service;

import com.axxohub.dummyspringboot.dto.DashboardSummary;
import com.axxohub.dummyspringboot.model.Customer;
import com.axxohub.dummyspringboot.model.OrderStatus;
import com.axxohub.dummyspringboot.model.PurchaseOrder;
import com.axxohub.dummyspringboot.model.TicketStatus;
import com.axxohub.dummyspringboot.repository.CustomerRepository;
import com.axxohub.dummyspringboot.repository.ProductRepository;
import com.axxohub.dummyspringboot.repository.PurchaseOrderRepository;
import com.axxohub.dummyspringboot.repository.SupportTicketRepository;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PurchaseOrderRepository orderRepository;
    private final SupportTicketRepository ticketRepository;

    public DashboardService(
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            PurchaseOrderRepository orderRepository,
            SupportTicketRepository ticketRepository
    ) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    public DashboardSummary summary() {
        long activeCustomers = customerRepository.findAll().stream().filter(Customer::isActive).count();
        long lowStockProducts = productRepository.findByStockQuantityLessThanEqual(20).size();
        long openTickets = ticketRepository.findByStatus(TicketStatus.OPEN).size()
                + ticketRepository.findByStatus(TicketStatus.IN_PROGRESS).size();
        BigDecimal pendingRevenue = orderRepository.findByStatus(OrderStatus.PENDING).stream()
                .map(PurchaseOrder::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DashboardSummary(
                customerRepository.count(),
                activeCustomers,
                productRepository.count(),
                lowStockProducts,
                orderRepository.count(),
                openTickets,
                pendingRevenue
        );
    }
}
