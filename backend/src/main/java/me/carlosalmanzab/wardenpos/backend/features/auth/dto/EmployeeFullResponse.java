package me.carlosalmanzab.wardenpos.backend.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record EmployeeFullResponse(
    @JsonUnwrapped EmployeeBaseResponse base,
    String firstName,
    String lastName,
    Boolean isActive) {}
