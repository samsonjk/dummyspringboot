package com.axxohub.dummyspringboot.dto;

import com.axxohub.dummyspringboot.model.CustomerTier;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequest(
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotNull CustomerTier tier,
        boolean active
) {
}
