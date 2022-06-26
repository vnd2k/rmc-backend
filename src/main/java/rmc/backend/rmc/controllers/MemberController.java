package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmc.backend.rmc.entities.dto.*;
import rmc.backend.rmc.services.AmazonClient;
import rmc.backend.rmc.services.MemberService;

import java.util.List;

import static rmc.backend.rmc.security.FeignClientInterceptor.getEmailByToken;

@RestController
@RequestMapping(path = "/member")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    private final AmazonClient amazonClient;

    @PutMapping(path = "/{userId}")
    public ResponseEntity<PutMemberInfoResponse> update(@PathVariable("userId") String userId,
                                                        @RequestBody PutMemberInfoRequest request) {

        return new ResponseEntity<>(memberService.updateMemberInfo(userId, request.getNickname()), HttpStatus.OK);
    }

    @PutMapping(path = "/{userId}/avatar")
    public ResponseEntity<PutMemberInfoResponse> updateAvatar(@PathVariable("userId") String userId,
                                                              @RequestBody PutMemberInfoRequest request) {

        String avatarUrl = amazonClient.uploadFile(request.getAvatar());
        return new ResponseEntity<>(memberService.updateAvatar(userId, avatarUrl), HttpStatus.OK);
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<GetMemberInfoResponse> findById(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(memberService.findById(userId), HttpStatus.OK);
    }

    @PostMapping(path = "/report/{ratingId}")
    public void report(@PathVariable("ratingId") String ratingId, @RequestBody PostReportRequest request) throws JSONException {
        memberService.reportRating(getEmailByToken(), ratingId, request);
    }

    @PostMapping(path = "/save/{companyId}")
    public ResponseEntity<GetSavedStatus> save(@PathVariable("companyId") String companyId) throws JSONException {
        return new ResponseEntity<>(memberService.saveCompany(getEmailByToken(), companyId),HttpStatus.OK);
    }

    @GetMapping(path = "/list-saved")
    public ResponseEntity<List<GetSavedResponse>> savedList() throws JSONException {
        return new ResponseEntity<>(memberService.getSavedList(getEmailByToken()), HttpStatus.OK);
    }

    @GetMapping(path="/saved/{companyId}/check")
    public ResponseEntity<GetSavedStatus> checkSavedStatus(@PathVariable("companyId") String companyId) throws JSONException {
        return new ResponseEntity<>(memberService.checkSavedStatus(getEmailByToken(),companyId), HttpStatus.OK);
    }
}
