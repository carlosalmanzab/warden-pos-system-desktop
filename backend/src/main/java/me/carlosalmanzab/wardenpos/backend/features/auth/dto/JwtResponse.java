package me.carlosalmanzab.wardenpos.backend.features.auth.dto;

public record JwtResponse(String accessToken, String refreshToken, String tokenType) {}
