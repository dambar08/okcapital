package com.okcapital.application.dto;

import com.okcapital.application.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record TransitionApplicationRequest(@NotNull ApplicationStatus status) {
}
