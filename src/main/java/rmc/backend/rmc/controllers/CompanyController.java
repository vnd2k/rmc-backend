package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.dto.GetCompanyInfoResponse;
import rmc.backend.rmc.entities.dto.PutCompanyInfoRequest;
import rmc.backend.rmc.entities.dto.PutCompanyInfoResponse;
import rmc.backend.rmc.entities.dto.SearchCompanyResponse;
import rmc.backend.rmc.services.AmazonClient;
import rmc.backend.rmc.services.CompanyService;

import java.util.List;

@RestController
@RequestMapping(path = "/company")
@AllArgsConstructor
public class CompanyController {
    private final CompanyService companyService;
    private final AmazonClient amazonClient;

    @PutMapping(path = "/{userId}")
    public ResponseEntity<PutCompanyInfoResponse> update(@PathVariable("userId") String userId, @RequestBody PutCompanyInfoRequest request) {
        return new ResponseEntity<>(companyService.updateCompanyInfo(userId, request), HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}/logo")
    public ResponseEntity<PutCompanyInfoResponse> updateLogo(@PathVariable("userId") String userId, @RequestBody PutCompanyInfoRequest request) {
        String logoImage = amazonClient.uploadFile(request.getLogoImage());
        return new ResponseEntity<>(companyService.updateCompanyLogo(userId, logoImage), HttpStatus.OK);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<GetCompanyInfoResponse> findById(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(companyService.findById(userId), HttpStatus.OK);
    }

    @GetMapping(path = "/search/{nameCharacter}")
    public ResponseEntity<List<SearchCompanyResponse>> searchByCharacter(@PathVariable("nameCharacter") String nameCharacter) {
        return new ResponseEntity<>(companyService.searchByCharacter(nameCharacter),HttpStatus.OK);
    }
}
