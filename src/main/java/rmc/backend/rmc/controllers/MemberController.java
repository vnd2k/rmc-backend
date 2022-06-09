package rmc.backend.rmc.controllers;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rmc.backend.rmc.entities.dto.GetMemberInfoResponse;
import rmc.backend.rmc.entities.dto.PostRatingRequest;
import rmc.backend.rmc.entities.dto.PutMemberInfoRequest;
import rmc.backend.rmc.repositories.MemberRepository;
import rmc.backend.rmc.services.MemberService;

@RestController
@RequestMapping(path = "/member")
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PutMapping(path = "/{userId}")
    public void update(@PathVariable("userId") String userId, @RequestBody PutMemberInfoRequest request) {
        memberService.updateMemberInfo(userId, request);
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
