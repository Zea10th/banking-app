package com.biezbardis.bankingapp.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ClientResponse(
        @Schema(description = "Client Id", example = "123")
        Long id,
        @Schema(description = "Client Name", example = "John Doe")
        String clientName
) {
}
