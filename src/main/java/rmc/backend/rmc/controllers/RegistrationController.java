package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.dto.LoginRequest;
import rmc.backend.rmc.entities.dto.LoginResponse;
import rmc.backend.rmc.entities.dto.RegisterRequest;
import rmc.backend.rmc.entities.dto.UserDetailsResponse;
import rmc.backend.rmc.services.RegistrationService;

@RestController
@RequestMapping(path = "/auth")
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(path = "/register")
    public String register(@RequestBody RegisterRequest request) {
        return registrationService.register(request);
    }

    @GetMapping(path = "/register/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.verifyToken(token);
    }

    @PostMapping(path ="/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws IllegalAccessException {
        return registrationService.login(request);
    }

    @GetMapping(path = "{email}")
    public ResponseEntity<LoginResponse> findUserEmail(@PathVariable("email") String email) {
        return new ResponseEntity<>(registrationService.getUserEmail(email), HttpStatus.OK);
    }


}