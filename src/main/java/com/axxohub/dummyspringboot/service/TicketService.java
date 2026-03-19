package com.axxohub.dummyspringboot.service;

import com.axxohub.dummyspringboot.dto.TicketRequest;
import com.axxohub.dummyspringboot.exception.ResourceNotFoundException;
import com.axxohub.dummyspringboot.model.SupportTicket;
import com.axxohub.dummyspringboot.model.TicketStatus;
import com.axxohub.dummyspringboot.repository.SupportTicketRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    private final SupportTicketRepository ticketRepository;

    public TicketService(SupportTicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public List<SupportTicket> findAll() {
        return ticketRepository.findAll();
    }

    public SupportTicket findById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found: " + id));
    }

    public List<SupportTicket> findByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }

    public SupportTicket create(TicketRequest request) {
        SupportTicket ticket = new SupportTicket();
        ticket.setCustomerEmail(request.customerEmail());
        ticket.setSubject(request.subject());
        ticket.setDescription(request.description());
        ticket.setPriority(request.priority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedAt(LocalDateTime.now());
        return ticketRepository.save(ticket);
    }

    public SupportTicket assign(Long id, String agent) {
        SupportTicket ticket = findById(id);
        ticket.setAssignedAgent(agent);
        ticket.setStatus(TicketStatus.IN_PROGRESS);
        return ticketRepository.save(ticket);
    }

    public SupportTicket close(Long id) {
        SupportTicket ticket = findById(id);
        ticket.setStatus(TicketStatus.CLOSED);
        return ticketRepository.save(ticket);
    }

    public void delete(Long id) {
        ticketRepository.delete(findById(id));
    }
}
