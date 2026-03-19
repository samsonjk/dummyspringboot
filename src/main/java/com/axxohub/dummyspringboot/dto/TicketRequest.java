package com.axxohub.dummyspringboot.dto;

import com.axxohub.dummyspringboot.model.TicketPriority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketRequest(
        @NotBlank @Email String customerEmail,
        @NotBlank String subject,
        @NotBlank String description,
        @NotNull TicketPriority priority
) {
}
