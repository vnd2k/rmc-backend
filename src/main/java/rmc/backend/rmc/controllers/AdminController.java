package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmc.backend.rmc.entities.dto.*;
import rmc.backend.rmc.services.AdminService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@AllArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping(path = "/list-report")
    public ResponseEntity<List<GetReportResponse>> reportList() {
        return new ResponseEntity<>(adminService.getReportList(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/report/{reportId}")
    public void deleteReport(@PathVariable("reportId") String reportId) {
        adminService.deleteReport(reportId);
    }

    @GetMapping(path = "/list-company")
    public ResponseEntity<List<GetListCompaniesResponse>> companyList() {
        return new ResponseEntity<>(adminService.getCompanyList(), HttpStatus.OK);
    }

    @PutMapping(path = "/company/{companyId}")
    public void updateCompanyInfo(@PathVariable("companyId")String companyId,@RequestBody PutCompanyByAdminRequest request){
        adminService.updateCompany(companyId,request);
    }

    @DeleteMapping(path = "/company/{companyId}")
    public void deleteCompany(@PathVariable("companyId") String companyId) {
        adminService.deleteCompany(companyId);
    }

    @GetMapping(path = "/list-member")
    public ResponseEntity<List<GetListMemberResponse>> memberList() {
        return new ResponseEntity<>(adminService.getMemberList(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/member/{memberId}")
    public void deleteMember(@PathVariable("memberId") String memberId) {
        adminService.deleteMember(memberId);
    }

    @PutMapping(path = "/verify/company/{companyId}")
    public void verifyCompany(@PathVariable("companyId")String companyId){
        adminService.verifyCompany(companyId);
    }

    @GetMapping(path = "/list-rating")
    public ResponseEntity<List<GetRatingsResponse>> ratingList() {
        return new ResponseEntity<>(adminService.getRatingList(), HttpStatus.OK);
    }

    @GetMapping(path = "/list-job")
    public ResponseEntity<List<GetListJobResponse>> jobList() {
        return new ResponseEntity<>(adminService.getJobsList(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/rating/{ratingId}")
    public void deleteRating(@PathVariable("ratingId") String ratingId) {
        adminService.deleteRating(ratingId);
    }

    @DeleteMapping(path = "/job/{jobId}")
    public void deleteJob(@PathVariable("jobId") String jobId) {
        adminService.deleteJob(jobId);
    }

    @GetMapping(path = "report/{ratingId}")
    public ResponseEntity<List<GetReportResponse>> findReportByRatingId(@PathVariable("ratingId") String ratingId) {
        return new ResponseEntity<>(adminService.findReportByRatingId(ratingId), HttpStatus.OK);
    }
}
