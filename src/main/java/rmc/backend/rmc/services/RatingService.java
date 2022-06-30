package rmc.backend.rmc.services;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import java.util.Objects;
import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    private final CompanyRepository companyRepository;

    private final MemberRepository memberRepository;

    private final LikeRatingRepository likeRatingRepository;

    private final UnlikeRepository unlikeRatingRepository;

    private final RUserRepository userRepository;

    private final ReportRepository reportRepository;

    public RatingService(RatingRepository ratingRepository, CompanyRepository companyRepository, MemberRepository memberRepository, LikeRatingRepository likeRatingRepository, UnlikeRepository unlikeRatingRepository, RUserRepository userRepository, ReportRepository reportRepository) {
        this.ratingRepository = ratingRepository;
        this.companyRepository = companyRepository;
        this.memberRepository = memberRepository;
        this.likeRatingRepository = likeRatingRepository;
        this.unlikeRatingRepository = unlikeRatingRepository;
        this.userRepository = userRepository;
        this.reportRepository = reportRepository;
    }

    public void ratingCompany(String companyId, PostRatingRequest request) {
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        RMember member = memberRepository.findById(request.getMemberId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));

        Optional<Rating> ratingCheck = ratingRepository.findByCompanyAndMember(company, member);
        if (ratingCheck.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member already rating this company");
        }

        Rating rating = new Rating();
        rating.setId(NanoIdUtils.randomNanoId());
        rating.setMember(member);
        rating.setCompany(company);
        rating.setPositivePoint(request.getPositivePoint());
        rating.setPointsToImprove(request.getPointsToImprove());
        rating.setRatingPoint(request.getRatingPoint());
        rating.setCreatedAt(LocalDateTime.now());
        ratingRepository.save(rating);

        List<Rating> companyRatings = company.getRatings();
        double ratingScore = companyRatings.stream().mapToDouble(Rating::getRatingPoint).average().orElse(Double.NaN);
        double v = Math.round(ratingScore * 100.0) / 100.0;
        company.setRatingScore((float) v);
        companyRepository.save(company);
    }

    @Transactional
    public void editRating(String ratingId, PutRatingRequest request) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));
        rating.setPositivePoint(request.getPositivePoint());
        rating.setPointsToImprove(request.getPointsToImprove());
        rating.setRatingPoint(request.getRatingPoint());
        ratingRepository.save(rating);

        RCompany company = companyRepository.findById(rating.getCompany().getId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        List<Rating> companyRatings = company.getRatings();
        double ratingScore = companyRatings.stream().mapToDouble(Rating::getRatingPoint).average().orElse(Double.NaN);
        double v = Math.round(ratingScore * 100.0) / 100.0;
        company.setRatingScore((float) v);
        companyRepository.save(company);
    }

    @Transactional
    public void deleteRating(String ratingId) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));
        ratingRepository.delete(rating);
    }

    public List<GetRatingsResponse> findByCompanyId(String companyId, int page, String sortType, String email) {
        RCompany company = companyRepository.findById(companyId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Company not found"));
        List<Rating> totalRating = ratingRepository.findAllByCompanyOrderByCreatedAtDesc(company);
        List<Rating> ratingList;
        if (Objects.equals(sortType, "popularity")) {
            ratingList = ratingRepository.findAllByCompany(company, PageRequest.of(page, 4, Sort.by("ReactionCount").descending()));
        } else {
            ratingList = ratingRepository.findAllByCompany(company, PageRequest.of(page, 4, Sort.by("CreatedAt").descending()));
        }
        List<GetRatingsResponse> ratingsResponses = new ArrayList<>();
        Optional<RUser> rUser = userRepository.findByEmail(email);
        String memberId = "";
        if (rUser.isPresent()) {
            memberId = rUser.get().getId();
        }
        RMember member = memberRepository.findById(memberId).orElse(null);

        List<Rating> companyRatings = company.getRatings();
        double ratingScore = companyRatings.stream().mapToDouble(Rating::getRatingPoint).average().orElse(Double.NaN);
        double v = Math.round(ratingScore * 100.0) / 100.0;
        company.setRatingScore((float) v);
        companyRepository.save(company);

        for (Rating rating : ratingList) {
            GetRatingsResponse response = new GetRatingsResponse();
            response.setId(rating.getId());
            response.setRatingPoint(rating.getRatingPoint());
            response.setRaterName(rating.getMember().getNickname());
            response.setPositivePoint(rating.getPositivePoint());
            response.setPointToImprove(rating.getPointsToImprove());
            response.setLikeCount(rating.getLikes().size());
            response.setDislikeCount(rating.getUnlike().size());
            response.setLiked(likeRatingRepository.existsByRatingAndMemberId(rating, memberId));
            response.setUnliked(unlikeRatingRepository.existsByRatingAndMemberId(rating, memberId));
            response.setMyRating(!(ratingRepository.findByIdAndMember(rating.getId(), member) == null));
            response.setReported(reportRepository.existsByMember(member));
            response.setCreatedAt(rating.getCreatedAt());
            response.setTotalPage((int) Math.ceil(totalRating.size() / 5 + 1));

            ratingsResponses.add(response);
        }

        return ratingsResponses;
    }

    public List<GetRatingsResponse> findByMemberId(String memberId) {
        RMember member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        List<Rating> ratingList = ratingRepository.findAllByMemberOrderByCreatedAtDesc(member);
        List<GetRatingsResponse> ratingsResponses = new ArrayList<>();

        for (Rating rating : ratingList) {
            GetRatingsResponse response = new GetRatingsResponse();
            response.setId(rating.getId());
            response.setRatingPoint(rating.getRatingPoint());
            response.setRaterName(rating.getMember().getNickname());
            response.setPositivePoint(rating.getPositivePoint());
            response.setPointToImprove(rating.getPointsToImprove());
            response.setLikeCount(rating.getLikes().size());
            response.setDislikeCount(rating.getUnlike().size());
            response.setLiked(likeRatingRepository.existsByRatingAndMemberId(rating, memberId));
            response.setUnliked(unlikeRatingRepository.existsByRatingAndMemberId(rating, memberId));
            response.setMyRating(true);
            response.setCreatedAt(rating.getCreatedAt());

            ratingsResponses.add(response);
        }

        return ratingsResponses;
    }

    @Transactional
    public void likeRating(String memberId, String ratingId) {
        RMember member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));
        List<LikeRating> membersLiked = likeRatingRepository.findAllByRating(rating);
        List<UnlikeRating> membersUnliked = unlikeRatingRepository.findAllByRating(rating);
        boolean plusLike = true;
        boolean canLike = true;
        for (UnlikeRating memberUnliked : membersUnliked) {
            if (memberUnliked.getMemberId().equals(memberId)) {
                canLike = false;
                break;
            }
        }
        if (canLike) {
            for (LikeRating memberLiked : membersLiked) {
                if (memberLiked.getMemberId().equals(memberId)) {
                    plusLike = false;
                    break;
                }
            }
            LikeRating likeRating = new LikeRating(NanoIdUtils.randomNanoId(), rating, memberId);
            if (plusLike) {
                likeRatingRepository.save(likeRating);
                rating.setReactionCount(rating.getReactionCount() + 1);
                ratingRepository.save(rating);
            } else {
                LikeRating likedRating = likeRatingRepository.findByRatingAndMemberId(rating, memberId);
                likeRatingRepository.delete(likedRating);
                rating.setReactionCount(rating.getReactionCount() - 1);
                ratingRepository.save(rating);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member cant like this rating");
        }
    }

    @Transactional
    public void unlikeRating(String memberId, String ratingId) {
        RMember member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));

        List<UnlikeRating> membersUnliked = unlikeRatingRepository.findAllByRating(rating);
        List<LikeRating> membersLiked = likeRatingRepository.findAllByRating(rating);
        boolean plusLike = true;
        boolean canUnlike = true;

        for (LikeRating memberLiked : membersLiked) {
            if (memberLiked.getMemberId().equals(memberId)) {
                canUnlike = false;
                break;
            }
        }

        if (canUnlike) {
            for (UnlikeRating memberUnliked : membersUnliked) {
                if (memberUnliked.getMemberId().equals(memberId)) {
                    plusLike = false;
                    break;
                }
            }
            UnlikeRating unlikeRating = new UnlikeRating(NanoIdUtils.randomNanoId(), rating, memberId);
            if (plusLike) {
                unlikeRatingRepository.save(unlikeRating);
                rating.setReactionCount(rating.getReactionCount() + 1);
                ratingRepository.save(rating);
            } else {
                UnlikeRating unlikedRating = unlikeRatingRepository.findByRatingAndMemberId(rating, memberId);
                unlikeRatingRepository.delete(unlikedRating);
                rating.setReactionCount(rating.getReactionCount() - 1);
                ratingRepository.save(rating);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member cant unlike this rating");
        }
    }

    public CheckLikedAndUnlikedResponse checkLiked(String memberId, String ratingId) {
        RMember member = memberRepository.findById(memberId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found"));
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));

        boolean checkLiked = likeRatingRepository.existsByRatingAndMemberId(rating, memberId);
        boolean checkUnliked = unlikeRatingRepository.existsByRatingAndMemberId(rating, memberId);
        return new CheckLikedAndUnlikedResponse(checkLiked, checkUnliked);
    }


    public GetRatingsResponse getRatingById(String ratingId) {
        Rating rating = ratingRepository.findById(ratingId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rating not found"));
        GetRatingsResponse response = new GetRatingsResponse();
        response.setId(ratingId);
        response.setCompanyId(rating.getCompany().getId());
        response.setPositivePoint(rating.getPositivePoint());
        response.setPointToImprove(rating.getPointsToImprove());
        response.setRatingPoint(rating.getRatingPoint());
        response.setCompanyName(rating.getCompany().getName());
        response.setRaterName(rating.getMember().getRUser().getEmail());
        return response;
    }
}
