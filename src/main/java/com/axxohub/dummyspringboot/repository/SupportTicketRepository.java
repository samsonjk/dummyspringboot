package com.axxohub.dummyspringboot.repository;

import com.axxohub.dummyspringboot.model.SupportTicket;
import com.axxohub.dummyspringboot.model.TicketStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportTicketRepository extends JpaRepository<SupportTicket, Long> {
    List<SupportTicket> findByStatus(TicketStatus status);
}
