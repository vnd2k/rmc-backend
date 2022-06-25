package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.*;
import rmc.backend.rmc.entities.dto.GetMemberInfoResponse;
import rmc.backend.rmc.entities.dto.GetSavedResponse;
import rmc.backend.rmc.entities.dto.GetSavedStatus;
import rmc.backend.rmc.entities.dto.PutMemberInfoResponse;
import rmc.backend.rmc.repositories.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    private final CompanyRepository companyRepository;

    private final RatingRepository ratingRepository;

    private final ReportRepository reportRepository;

    private final SavedCompanyRepository savedCompanyRepository;

    private final RUserRepository userRepository;

    public MemberService(MemberRepository memberRepository, CompanyRepository companyRepository, RatingRepository ratingRepository, ReportRepository reportRepository, SavedCompanyRepository savedCompanyRepository, RUserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
        this.ratingRepository = ratingRepository;
        this.reportRepository = reportRepository;
        this.savedCompanyRepository = savedCompanyRepository;
        this.userRepository = userRepository;
    }

    public PutMemberInfoResponse updateMemberInfo(String userId, String nickname) {
        RMember member = memberRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));

        member.setNickname(nickname);
        member.setUpdatedAt(LocalDateTime.now());
        memberRepository.save(member);

        return new PutMemberInfoResponse(member.getNickname(), member.getAvatar());
    }

    @Transactional
    public PutMemberInfoResponse updateAvatar(String userId, String avatar) {
        RMember member = memberRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));

        member.setAvatar(avatar);
        member.setUpdatedAt(LocalDateTime.now());
        memberRepository.save(member);

        return new PutMemberInfoResponse(member.getNickname(), member.getAvatar());
    }

    public GetMemberInfoResponse findById(String userId) {
        RMember member = memberRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        return new GetMemberInfoResponse(member.getId(), member.getNickname(), member.getAvatar());
    }

    @Transactional
    public void reportRating(String email, String ratingId) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        RMember member = memberRepository.findById(rUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));

        Report report = new Report();
        report.setId(NanoIdUtils.randomNanoId());
        report.setMember(member);
        report.setRating(rating);
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    @Transactional
    public GetSavedStatus saveCompany(String email, String companyId) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        RMember member = memberRepository.findById(rUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));

        SavedCompany savedCompany = savedCompanyRepository.findByMemberAndCompany(member, company);
        if (savedCompany == null) {
            savedCompanyRepository.save(new SavedCompany(NanoIdUtils.randomNanoId(), member, company));
            return new GetSavedStatus(true);
        } else {
            savedCompanyRepository.delete(savedCompany);
            return new GetSavedStatus(false);
        }
    }

    public List<GetSavedResponse> getSavedList(String email) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        RMember member = memberRepository.findById(rUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        List<SavedCompany> savedCompanyList = savedCompanyRepository.findAllByMember(member);
        List<GetSavedResponse> responses = new ArrayList<>();
        for (SavedCompany saved : savedCompanyList) {
            GetSavedResponse item = new GetSavedResponse();
            item.setId(saved.getId());
            item.setName(saved.getCompany().getName());
            item.setAddress(saved.getCompany().getAddress());
            item.setNation(saved.getCompany().getNation());
            item.setDescription(saved.getCompany().getDescription());
            item.setLogoImage(saved.getCompany().getLogoImage());
            item.setWebsite(saved.getCompany().getWebsite());
            item.setCompanySize(saved.getCompany().getCompanySize());
            item.setType(saved.getCompany().getType());
            item.setCompanyId(saved.getCompany().getId());
            responses.add(item);
        }
        return responses;
    }

    public GetSavedStatus checkSavedStatus(String email, String companyId) {
        RUser rUser = userRepository.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        RMember member = memberRepository.findById(rUser.getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        SavedCompany saved = savedCompanyRepository.findByMemberAndCompany(member, company);

        return new GetSavedStatus(saved != null);
    }
}
