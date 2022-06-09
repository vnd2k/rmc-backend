package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.dto.GetCompanyInfoResponse;
import rmc.backend.rmc.entities.dto.PutCompanyInfoRequest;
import rmc.backend.rmc.services.CompanyService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(path = "/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;

    @PutMapping(path = "/{userId}")
    public void update(@PathVariable("userId") String userId, @RequestBody PutCompanyInfoRequest request) {
        companyService.updateCompanyInfo(userId, request);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<GetCompanyInfoResponse> findById(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(companyService.findById(userId), HttpStatus.OK);
    }
}
