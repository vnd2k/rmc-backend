package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmc.backend.rmc.entities.dto.*;
import rmc.backend.rmc.services.RatingService;

import java.util.List;

import static rmc.backend.rmc.security.FeignClientInterceptor.getEmailByToken;

@RestController
@RequestMapping(path = "/rating")
@AllArgsConstructor
public class RatingController {
    private final RatingService ratingService;

    @PostMapping(path = "/{companyId}")
    public void createRating(@PathVariable("companyId") String companyId, @RequestBody PostRatingRequest request) throws JSONException {
        ratingService.ratingCompany(getEmailByToken(),companyId, request);
    }

    @PutMapping(path = "/{ratingId}")
    public void editRating(@PathVariable("ratingId") String ratingId, @RequestBody PutRatingRequest request) throws JSONException {
        ratingService.editRating(ratingId, request);
    }

    @DeleteMapping(path = "/{ratingId}")
    public void deleteRating(@PathVariable("ratingId") String ratingId) throws JSONException {
        ratingService.deleteRating(ratingId);
    }

    @GetMapping(path = "/{companyId}/company")
    public ResponseEntity<List<GetRatingsResponse>> findByCompanyId(@PathVariable("companyId") String companyId, @RequestParam("page") int page, @RequestParam("sortType") String sortType) throws JSONException {
        return new ResponseEntity<>(ratingService.findByCompanyId(companyId,page,sortType, getEmailByToken()), HttpStatus.OK);
    }

    @GetMapping(path = "/{memberId}/member")
    public ResponseEntity<List<GetRatingsResponse>> findByMemberId(@PathVariable("memberId") String memberId) {
        return new ResponseEntity<>(ratingService.findByMemberId(memberId), HttpStatus.OK);
    }

    @PostMapping(path = "{memberId}/like/{ratingId}")
    public void likeRating(@PathVariable("memberId") String memberId, @PathVariable("ratingId") String ratingId) {
        ratingService.likeRating(memberId, ratingId);
    }

    @PostMapping(path = "{memberId}/unlike/{ratingId}")
    public void unlikeRating(@PathVariable("memberId") String memberId, @PathVariable("ratingId") String ratingId) {
        ratingService.unlikeRating(memberId, ratingId);
    }

    @GetMapping(path = "{memberId}/reaction-status/{ratingId}")
    public ResponseEntity<CheckLikedAndUnlikedResponse> checkReaction(@PathVariable("memberId") String memberId, @PathVariable("ratingId") String ratingId) {
        return new ResponseEntity<>(ratingService.checkLiked(memberId, ratingId), HttpStatus.OK);
    }

    @GetMapping(path = "/{ratingId}")
    public ResponseEntity<GetRatingsResponse> getRatingById(@PathVariable("ratingId") String ratingId) {
        return new ResponseEntity<>(ratingService.getRatingById(ratingId), HttpStatus.OK);
    }
}
