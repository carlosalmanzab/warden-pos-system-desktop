package me.carlosalmanzab.wardenpos.backend.features.auth.dto;

import java.util.Set;

public record EmployeeBaseResponse(String id, String username, String email, Set<String> roles) {}
