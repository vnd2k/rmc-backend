package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import rmc.backend.rmc.entities.RCompany;
import rmc.backend.rmc.entities.RMember;
import rmc.backend.rmc.entities.Rating;
import rmc.backend.rmc.entities.dto.GetMemberInfoResponse;
import rmc.backend.rmc.entities.dto.PostRatingRequest;
import rmc.backend.rmc.entities.dto.PutMemberInfoRequest;
import rmc.backend.rmc.entities.dto.PutMemberInfoResponse;
import rmc.backend.rmc.repositories.CompanyRepository;
import rmc.backend.rmc.repositories.MemberRepository;
import rmc.backend.rmc.repositories.RatingRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    private final CompanyRepository companyRepository;

    private final RatingRepository ratingRepository;

    public MemberService(MemberRepository memberRepository, CompanyRepository companyRepository, RatingRepository ratingRepository) {
        this.memberRepository = memberRepository;
        this.companyRepository = companyRepository;
        this.ratingRepository = ratingRepository;
    }

    public PutMemberInfoResponse updateMemberInfo(String userId, String nickname) {
        RMember member = memberRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));

        member.setNickname(nickname);
        member.setUpdatedAt(LocalDateTime.now());
        memberRepository.save(member);

        return new PutMemberInfoResponse(member.getNickname(), member.getAvatar());
    }

    public PutMemberInfoResponse updateAvatar(String userId, String avatar) {
        RMember member = memberRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));

        member.setAvatar(avatar);
        member.setUpdatedAt(LocalDateTime.now());
        memberRepository.save(member);

        return new PutMemberInfoResponse(member.getNickname(), member.getAvatar());
    }

    public GetMemberInfoResponse findById(String userId) {
        RMember member = memberRepository.findById(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        return new GetMemberInfoResponse(member.getNickname(),member.getAvatar());
    }

    public void ratingCompany(String memberId, PostRatingRequest request) {
        RCompany company = companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        RMember member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));

        Rating rating = new Rating();
        rating.setId(NanoIdUtils.randomNanoId());
        rating.setMember(member);
        rating.setCompany(company);
        rating.setRatingTitle(request.getRatingTitle());
        rating.setPositivePoint(request.getPositivePoint());
        rating.setPointsToImprove(request.getPointsToImprove());
        rating.setRatingPoint(request.getRatingPoint());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);

        List<Rating> companyRatings = company.getRatings();
        double ratingScore = companyRatings.stream().mapToDouble(Rating::getRatingPoint).average().orElse(Double.NaN);
        company.setRatingScore((float) ratingScore);
        companyRepository.save(company);
    }
}
