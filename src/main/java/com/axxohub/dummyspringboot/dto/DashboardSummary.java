package com.axxohub.dummyspringboot.dto;

import java.math.BigDecimal;

public record DashboardSummary(
        long customers,
        long activeCustomers,
        long products,
        long lowStockProducts,
        long orders,
        long openTickets,
        BigDecimal pendingRevenue
) {
}
