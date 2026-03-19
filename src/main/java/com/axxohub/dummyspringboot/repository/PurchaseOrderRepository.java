package com.axxohub.dummyspringboot.repository;

import com.axxohub.dummyspringboot.model.OrderStatus;
import com.axxohub.dummyspringboot.model.PurchaseOrder;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(OrderStatus status);
}
