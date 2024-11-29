package com.kh.miniProjectJdbc.controller;

import com.kh.miniProjectJdbc.dao.MemberDao;
import com.kh.miniProjectJdbc.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {
    private final MemberDao memberDao;
    // 전체 회원 조회
    @GetMapping
    public ResponseEntity<List<MemberVo>> memberList() {
        List<MemberVo> list = memberDao.memberList();
        return ResponseEntity.ok(list);
    }

    // 개별 회원 조회
    @GetMapping("/{email}")
    public ResponseEntity<MemberVo> getMemberInfo(@PathVariable String email) {
        List<MemberVo> list = memberDao.findMemberByEmail(email);
        return ResponseEntity.ok(list.get(0));
    }

    // 이메일 또는 이름으로 멤버 조회
    @GetMapping("/search")
    public ResponseEntity<List<MemberVo>> getMemberByEmailOrName(@RequestParam(required = false) String email,
                                                                 @RequestParam(required = false) String name) {
        log.info("이메일 : {}, 이름 {}", email, name);
        List<MemberVo> list = memberDao.findMemberByEmailOrName(email, name);
        return ResponseEntity.ok(list);
    }

    // 회원 정보 수정
    @PutMapping("/{email}")
    public ResponseEntity<Boolean> updateMember(@PathVariable String email, @RequestBody MemberVo vo) {
        log.error("이메일 : {}", email);
        log.error("멤버 : {}", vo);
        boolean isSuccess = memberDao.updateMember(email, vo);
        return ResponseEntity.ok(isSuccess);
    }

    // 회원 삭제
    @DeleteMapping("/{email}")
    public ResponseEntity<Boolean> deleteMember(@PathVariable String email) {
        boolean isSuccess = memberDao.deleteMember(email);
        return ResponseEntity.ok(isSuccess);
    }

}
