package rmc.backend.rmc.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.RUser;
import rmc.backend.rmc.entities.Report;
import rmc.backend.rmc.entities.dto.GetListCompaniesResponse;
import rmc.backend.rmc.entities.dto.GetListMemberResponse;
import rmc.backend.rmc.entities.dto.GetReportResponse;
import rmc.backend.rmc.entities.dto.PutCompanyByAdminRequest;
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

    public AdminService(AdminRepository adminRepository, ReportRepository reportRepository, CompanyRepository companyRepository, MemberRepository memberRepository, RUserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.reportRepository = reportRepository;
        this.companyRepository = companyRepository;
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
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
        if(!company.isVerified()){
            company.setVerified(true);
            companyRepository.save(company);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Company already verified");
        }
    }

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
}
