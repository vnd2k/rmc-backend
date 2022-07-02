package rmc.backend.rmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.*;
import rmc.backend.rmc.entities.dto.*;
import rmc.backend.rmc.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    private final ReportRepository reportRepository;

    private final CompanyRepository companyRepository;

    private final MemberRepository memberRepository;

    private final RUserRepository userRepository;

    private final RatingRepository ratingRepository;

    private final JobRepository jobRepository;

    public AdminService(AdminRepository adminRepository, ReportRepository reportRepository, CompanyRepository companyRepository, MemberRepository memberRepository, RUserRepository userRepository, RatingRepository ratingRepository, JobRepository jobRepository) {
        this.adminRepository = adminRepository;
        this.reportRepository = reportRepository;
        this.companyRepository = companyRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.ratingRepository = ratingRepository;
        this.jobRepository = jobRepository;
    }

    public List<GetReportResponse> getReportList() {
        List<GetReportResponse> result = new ArrayList<>();
        List<Report> reports = reportRepository.findAll();

        for (Report report : reports) {
            GetReportResponse response = new GetReportResponse();
            response.setReportId(report.getId());
            response.setReporterId(report.getMember().getId());
            response.setRatingId(report.getRating().getId());
            response.setReporter(report.getMember().getRUser().getEmail());
            response.setReason(report.getReason());
            response.setDateReport(report.getCreatedAt());
            response.setReporterAvatar(report.getMember().getAvatar());
            result.add(response);
        }

        return result;
    }

    public List<GetListCompaniesResponse> getCompanyList() {
        List<RCompany> rCompanies = companyRepository.findAll();
        List<GetListCompaniesResponse> responses = new ArrayList<>();
        for (RCompany company : rCompanies) {
            GetListCompaniesResponse item = new GetListCompaniesResponse();
            item.setId(company.getId());
            item.setName(company.getName());
            item.setAddress(company.getAddress());
            item.setCompanySize(company.getCompanySize());
            item.setNation(company.getNation());
            item.setDescription(company.getDescription());
            item.setLogoImage(company.getLogoImage());
            item.setType(company.getType());
            item.setRatingCount(company.getRatingCount());
            item.setRatingScore(company.getRatingScore());
            item.setWebsite(company.getWebsite());
            item.setVerified(company.isVerified());
            item.setEmail(company.getRUser().getEmail());
            responses.add(item);
        }

        return responses;
    }

    @Transactional
    public void deleteCompany(String companyId) {
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        companyRepository.delete(company);
    }

    @Transactional
    public void deleteReport(String reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report not found"));
        reportRepository.delete(report);
    }

    public List<GetListMemberResponse> getMemberList() {
        List<RMember> memberList = memberRepository.findAll();
        List<GetListMemberResponse> responses = new ArrayList<>();
        for (RMember member : memberList) {
            GetListMemberResponse item = new GetListMemberResponse();
            RUser user = userRepository.findById(member.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
            item.setId(member.getId());
            item.setName(member.getNickname());
            item.setEmail(user.getEmail());
            item.setAvatar(member.getAvatar());
            responses.add(item);
        }

        return responses;
    }

    @Transactional
    public void deleteMember(String memberId) {
        RMember member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        memberRepository.delete(member);
    }

    @Transactional
    public void verifyCompany(String companyId) {
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        if (!company.isVerified()) {
            company.setVerified(true);
            companyRepository.save(company);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company already verified");
        }
    }

    @Transactional
    public void updateCompany(String companyId, PutCompanyByAdminRequest request) {
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setType(request.getType());
        company.setWebsite(request.getWebsite());
        company.setNation(request.getNation());
        company.setCompanySize(request.getCompanySize());
        company.setDescription(request.getDescription());
        company.setVerified(request.isVerified());
        company.setUpdatedAt(LocalDateTime.now());

        companyRepository.save(company);
    }

    public List<GetRatingsResponse> getRatingList() {
        List<Rating> ratingList = ratingRepository.findAll();
        List<GetRatingsResponse> responses = new ArrayList<>();

        for (Rating rating : ratingList) {
            GetRatingsResponse item = new GetRatingsResponse();
            List<Report> reportList = reportRepository.findAllByRating(rating);
            item.setId(rating.getId());
            item.setCompanyId(rating.getCompany().getId());
            item.setRatingPoint(rating.getRatingPoint());
            item.setRaterName(rating.getMember().getRUser().getEmail());
            item.setPositivePoint(rating.getPositivePoint());
            item.setPointToImprove(rating.getPointsToImprove());
            item.setLikeCount(rating.getLikes().size());
            item.setDislikeCount(rating.getUnlike().size());
            item.setCompanyName(rating.getCompany().getName());
            item.setCreatedAt(rating.getCreatedAt());
            item.setReportedCount(reportList.size());
            responses.add(item);
        }

        return responses;
    }

    public List<GetListJobResponse> getJobsList() {
        List<Job> jobList = jobRepository.findAll();
        List<GetListJobResponse> responses = new ArrayList<>();

        for (Job job : jobList) {
            GetListJobResponse item = new GetListJobResponse();
            item.setId(job.getId());
            item.setCompanyId(job.getCompany().getId());
            item.setCompanyName(job.getCompany().getName());
            item.setTitle(job.getTitle());
            item.setDescription(job.getDescription());
            item.setLogo(job.getCompany().getLogoImage());
            responses.add(item);
        }

        return responses;
    }

    @Transactional
    public void deleteRating(String ratingId) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));
        ratingRepository.delete(rating);
    }

    @Transactional
    public void deleteJob(String jobId) {
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Job not found"));
        jobRepository.delete(job);
    }

    public List<GetReportResponse> findReportByRatingId(String ratingId) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));
        List<Report> reportList = reportRepository.findAllByRating(rating);
        List<GetReportResponse> responses = new ArrayList<>();

        for (Report report : reportList) {
            GetReportResponse item = new GetReportResponse();

            item.setReportId(report.getId());
            item.setReporterId(report.getMember().getId());
            item.setReporterAvatar(report.getMember().getAvatar());
            item.setRatingId(report.getRating().getId());
            item.setReporter(report.getMember().getRUser().getEmail());
            item.setReason(report.getReason());
            item.setDateReport(report.getCreatedAt());
            responses.add(item);
        }

        return responses;
    }

    public GetReportResponse findReportById(String reportId) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report not found"));
        GetReportResponse response = new GetReportResponse();
        response.setReportId(report.getId());
        response.setReporterId(report.getMember().getId());
        response.setReporterAvatar(report.getMember().getAvatar());
        response.setRatingId(report.getRating().getId());
        response.setReporter(report.getMember().getRUser().getEmail());
        response.setReason(report.getReason());
        response.setDateReport(report.getCreatedAt());

        return response;
    }

    @Transactional
    public void updateReportById(String reportId, PutReportRequest request) {
        Report report = reportRepository.findById(reportId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Report not found"));
        report.setReason(request.getReason());
        reportRepository.save(report);
    }
}
