package com.axxohub.dummyspringboot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderRequest(
        @NotNull Long customerId,
        @NotNull Long productId,
        @NotNull @Min(1) Integer quantity
) {
}
