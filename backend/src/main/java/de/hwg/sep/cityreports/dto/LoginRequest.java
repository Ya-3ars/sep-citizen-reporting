package de.hwg.sep.cityreports.dto;

/** Body for POST /api/auth/login. */
public record LoginRequest(
        String username,
        String password
) {}
