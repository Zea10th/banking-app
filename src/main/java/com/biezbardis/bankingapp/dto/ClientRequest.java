package com.biezbardis.bankingapp.dto;

import jakarta.validation.constraints.NotNull;

public record ClientRequest(@NotNull String clientName) {
}
