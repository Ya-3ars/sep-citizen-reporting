package de.hwg.sep.cityreports.controller;

import de.hwg.sep.cityreports.dto.LoginRequest;
import de.hwg.sep.cityreports.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Staff authentication. Returns a JWT the client sends on every later staff call.
 * Stubbed (HTTP 501) until AuthService + JWT issuing land.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    /** Public: staff logs in. 200 OK + { token, role } on success, 401 on bad credentials. */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest body) {
        return ResponseEntity.status(501).build();
    }
}
