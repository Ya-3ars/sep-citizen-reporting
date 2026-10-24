package de.hwg.sep.cityreports.dto;

import de.hwg.sep.cityreports.entity.Role;

/** Response for POST /api/auth/login: a JWT plus the staff member's role. */
public record LoginResponse(
        String token,
        Role role
) {}
