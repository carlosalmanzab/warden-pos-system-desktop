package me.carlosalmanzab.wardenpos.backend.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public record AuthResponse(
    @JsonUnwrapped EmployeeBaseResponse base, @JsonUnwrapped JwtResponse jwt) {}
