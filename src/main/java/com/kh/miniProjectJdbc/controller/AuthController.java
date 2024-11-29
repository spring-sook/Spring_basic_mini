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
@CrossOrigin(origins = "http://localhost:3000") // 다른 포트도 사용하려면 이렇게 열어줘야됨. 이렇게 안하려면 JWT에서 가능
// React가 3000포트이고 그 접속 가능하게 해줘라.
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final MemberDao memberDao;
    // 로그인 - /auth/login으로 들어오면 받아줌
    @PostMapping("/login")
    // 성공/실패 Boolean으로 넘겨줄거고, login 함수이름은 아무거나 써도 됨
    public ResponseEntity<Boolean> login(@RequestBody MemberVo vo) {
//        log.info("memberVo : {}", vo);
        log.info("이메일 : {}", vo.getEmail());
        log.info("패스워드 : {}", vo.getPassword());
        boolean isSuccess = memberDao.login(vo.getEmail(), vo.getPassword());

        return ResponseEntity.ok(isSuccess);
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Boolean> signup(@RequestBody MemberVo vo) {
//        log.info("이메일 : {}", vo.getEmail());
//        log.info("패스워드 : {}", vo.getPassword());
//        log.info("이름 : " + vo.getName());
        log.info("member : {}", vo);
        boolean isSuccess = memberDao.signup(vo);

        return ResponseEntity.ok(isSuccess);
    }

    // 가입여부 확인
    @GetMapping("/exists/{email}")
    public ResponseEntity<Boolean> exists(@PathVariable String email) {
        log.error("email : {}", email);
        boolean isExist = memberDao.isEmailExist(email); // 이거 존재해? 응(true) 존재해.
        return ResponseEntity.ok(!isExist); // 존재한대(true). 그럼 만들면 안되네(false).
    }

//    // 모든 회원 조회
//    @GetMapping("/memberAll")
//    public ResponseEntity<List<MemberVo>> getAllMembers() {
//        List<MemberVo> members = memberDao.memberList();
//        return ResponseEntity.ok(members); // 성공 시 200 상태 코드와 함께 데이터를 반환
//    }
//
//    // 회원 검색
//    @GetMapping("/searchMem")
//    public ResponseEntity<List<MemberVo>> getRequestParamMem(@RequestParam String searchMemKeyword) {
//        List<MemberVo> searchMember = memberDao.searchMember(searchMemKeyword);
//        return ResponseEntity.ok(searchMember);
//    }
//
//    // 회원 정보 업데이트
//    @PostMapping("/updateMem")
//    public ResponseEntity<Boolean> updateMem(@RequestBody MemberVo vo) {
//        boolean isSuccess = memberDao.updateMember(vo);
//        return ResponseEntity.ok(isSuccess);
//    }
//
//    // 회원 정보 삭제
//    @PostMapping("/deleteMem")
//    public ResponseEntity<Boolean> deleteMem(@RequestBody MemberVo vo) {
//        boolean isSuccess = memberDao.deleteMember(vo);
//        return ResponseEntity.ok(isSuccess);
//    }
}
