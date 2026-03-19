package com.axxohub.dummyspringboot.controller;

import com.axxohub.dummyspringboot.dto.TicketRequest;
import com.axxohub.dummyspringboot.model.SupportTicket;
import com.axxohub.dummyspringboot.model.TicketStatus;
import com.axxohub.dummyspringboot.service.TicketService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping
    public List<SupportTicket> getAll(@RequestParam(required = false) TicketStatus status) {
        return status == null ? ticketService.findAll() : ticketService.findByStatus(status);
    }

    @GetMapping("/{id}")
    public SupportTicket getById(@PathVariable Long id) {
        return ticketService.findById(id);
    }

    @PostMapping
    public SupportTicket create(@Valid @RequestBody TicketRequest request) {
        return ticketService.create(request);
    }

    @PostMapping("/{id}/assign")
    public SupportTicket assign(@PathVariable Long id, @RequestParam String agent) {
        return ticketService.assign(id, agent);
    }

    @PostMapping("/{id}/close")
    public SupportTicket close(@PathVariable Long id) {
        return ticketService.close(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        ticketService.delete(id);
    }
}
