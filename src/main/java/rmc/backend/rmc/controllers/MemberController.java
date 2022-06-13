package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmc.backend.rmc.entities.dto.GetMemberInfoResponse;
import rmc.backend.rmc.entities.dto.PostRatingRequest;
import rmc.backend.rmc.entities.dto.PutMemberInfoRequest;
import rmc.backend.rmc.entities.dto.PutMemberInfoResponse;
import rmc.backend.rmc.repositories.MemberRepository;
import rmc.backend.rmc.services.AmazonClient;
import rmc.backend.rmc.services.MemberService;

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

    @PostMapping(path = "/{userId}/rating")
    public void rating(@PathVariable("userId") String userId, @RequestBody PostRatingRequest request) {
        memberService.ratingCompany(userId, request);
    }
}
