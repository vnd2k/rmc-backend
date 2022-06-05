package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmc.backend.rmc.entities.dto.LoginRequest;
import rmc.backend.rmc.entities.dto.LoginResponse;
import rmc.backend.rmc.entities.dto.RegisterRequest;
import rmc.backend.rmc.services.RegistrationService;

@RestController
@RequestMapping(path = "/auth/")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(path = "register")
    public String register(@RequestBody RegisterRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "register/confirm")
    public String confirm(@RequestParam("token") String token) {
        System.out.println(token);
        return registrationService.verifyToken(token);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws IllegalAccessException {
        return registrationService.login(request);
    }


}