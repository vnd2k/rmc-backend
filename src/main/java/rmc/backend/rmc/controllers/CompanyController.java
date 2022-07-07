package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.dto.*;
import rmc.backend.rmc.services.AmazonClient;
import rmc.backend.rmc.services.CompanyService;

import java.util.List;

import static rmc.backend.rmc.security.FeignClientInterceptor.getEmailByToken;

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

    @GetMapping(path = "/search/{character}")
    public ResponseEntity<List<SearchCompanyResponse>> searchByCharacter(@PathVariable("character") String character) {
        return new ResponseEntity<>(companyService.searchByCharacter(character), HttpStatus.OK);
    }

    @PostMapping(path = "/job")
    public void addJob(@RequestBody PostJobRequest request) throws JSONException {
        companyService.addJob(getEmailByToken(), request);
    }

    @GetMapping(path = "/job-list")
    public ResponseEntity<List<GetListJobResponse>> getListJob() throws JSONException {
        return new ResponseEntity<>(companyService.getListJob(getEmailByToken()), HttpStatus.OK);
    }

    @GetMapping(path = "/job-list/{companyId}")
    public ResponseEntity<List<GetListJobResponse>> getListJobByCompanyId(@PathVariable("companyId")String companyId) throws JSONException {
        return new ResponseEntity<>(companyService.getListJobByCompanyId(companyId), HttpStatus.OK);
    }

    @GetMapping(path = "/job/{jobId}")
    public ResponseEntity<GetListJobResponse> getJobById(@PathVariable("jobId")String jobId) throws JSONException {
        return new ResponseEntity<>(companyService.getJobById(jobId), HttpStatus.OK);
    }

    @PutMapping(path = "/job/{jobId}")
    public void editJob(@PathVariable("jobId") String jobId, @RequestBody PutJobRequest request) {
        companyService.editJob(jobId, request);
    }

    @DeleteMapping(path="/job/{jobId}")
    public void deleteJob(@PathVariable("jobId") String jobId) {
        companyService.deleteJob(jobId);
    }


    @GetMapping(path = "/job/{jobId}/cv")
    public ResponseEntity<List<GetListCvResponse>> getCvsByJobId(@PathVariable("jobId")String jobId) throws JSONException {
        return new ResponseEntity<>(companyService.getCvsByJobId(jobId), HttpStatus.OK);
    }
}
