package com.kh.miniProjectJdbc.dao;

import com.kh.miniProjectJdbc.vo.MemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor // 생성자를 만들어주는 것
@Slf4j
public class MemberDao {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL_MEMBERS = "SELECT email, name, reg_date FROM mini_member";
    private static final String SELECT_MEMBER = "SELECT email, name, reg_date FROM mini_member WHERE email LIKE ? OR name LIKE ?";
    private static final String SELECT_EMAIL = "SELECT email, name, reg_date, password FROM mini_member WHERE email = ?";
    private static final String LOGIN_QUERY = "SELECT COUNT(*) FROM mini_member WHERE email = ? AND password = ?";
    private static final String SIGNUP_QUERY = "INSERT INTO mini_member (email, password, name) VALUES (?, ?, ?)";
    private static final String CHECK_EMAIL = "SELECT COUNT(*) FROM mini_member WHERE email = ?";
//    private static final String UPDATE_MEMBER = "UPDATE mini_member SET name = ?, password = ? WHERE email = ?";
//    private static final String DELETE_MEMBER = "DELETE FROM mini_member WHERE email = ?";
private static final String DELETE_MEMBER = "DELETE FROM mini_member WHERE email = ?";
    private static final String UPDATE_MEMBER = "UPDATE mini_member SET email = ?, password = ?, name = ? WHERE email = ?";

    // 전체 회원 조회
    public List<MemberVo> memberList() {
        try{
            return jdbcTemplate.query(SELECT_ALL_MEMBERS, new MemberRowMapper());
        }catch (DataAccessException e) {
            log.error("회원 목록 조회 중 예외 발생 ", e);
            throw e;
        }
    }

    // 특정 회원 조회
//    public List<MemberVo> searchMember(String searchMemKeyword) {
//        try{
//            return jdbcTemplate.query(SELECT_MEMBER, new Object[]{"%" + searchMemKeyword + "%", "%" + searchMemKeyword + "%"}, new MemberRowMapper());
//        } catch (DataAccessException e) {
//            log.error("특정 회원 조회 중 예외 발생 ", e);
//            throw e;
//        }
//    }
    public List<MemberVo> findMemberByEmailOrName(String email, String name) {
        StringBuilder sql = new StringBuilder(SELECT_ALL_MEMBERS);
        boolean hasEmail = (email != null && !email.isEmpty());
        boolean hasName = (name != null && !name.isEmpty());

        if (hasEmail || hasName) {
            sql.append(" WHERE ");
            if (hasEmail) {
                sql.append("email = ?");
            }
            if (hasName) {
                if (hasEmail) {
                    sql.append(" OR ");
                }
                sql.append("name = ?");
            }
        }

        try {
            Object[] params = hasEmail && hasName
                    ? new Object[]{email, name}
                    : hasEmail
                    ? new Object[]{email}
                    : new Object[]{name};

            return jdbcTemplate.query(sql.toString(), params, new MemberRowMapper());
        } catch (DataAccessException e) {
            log.error("회원 목록 조회 중 예외 발생 ", e);
            throw e;
        }
    }

    // 이메일로 회원 조회
    public List<MemberVo> findMemberByEmail(String email) {
        try {
            return jdbcTemplate.query(SELECT_EMAIL, new Object[]{email}, new MemberRowMapper());
        } catch (DataAccessException e) {
            log.error("이메일로 회원 조회 중 예외 발생", e);
            throw e;
        }
    }

    // 로그인
    public boolean login(String email, String password) {
        try{
            int count = jdbcTemplate.queryForObject(LOGIN_QUERY, new Object[]{email, password}, Integer.class);
            return count > 0;
        } catch (DataAccessException e) {
            log.error("로그인 조회 실패 ", e);
            return false;
        }
    }

    // 회원 가입
    public boolean signup(MemberVo vo) {
        try {
            int result = jdbcTemplate.update(SIGNUP_QUERY, vo.getEmail(), vo.getPassword(), vo.getName());
            return result > 0;
        } catch (DataAccessException e) {
            log.error("회원가입 중 예외 발생 ", e);
            return false;
        }
    }

    // 회원 가입 여부 확인
    public boolean isEmailExist(String email) {
        try{
            int count = jdbcTemplate.queryForObject(CHECK_EMAIL, new Object[]{email}, Integer.class);
            return count > 0;
        } catch (DataAccessException e) {
            log.error("이메일 존재 여부 확인 중 에러 ", e);
            return false;
        }
    }

    // 회원 정보 업데이트
//    public boolean updateMember(MemberVo vo) {
//        try {
//            int result = jdbcTemplate.update(UPDATE_MEMBER, vo.getName(), vo.getPassword(), vo.getEmail());
//            return result > 0;
//        } catch (DataAccessException e) {
//            log.error("회원정보 업데이트 중 예외 발생 ", e);
//            return false;
//        }
//    }
    public boolean updateMember(String email, MemberVo vo) {
        int rowsAffected = jdbcTemplate.update(UPDATE_MEMBER, vo.getEmail(), vo.getPassword(), vo.getName(), email);
        return rowsAffected > 0;
    }

    // 회원 삭제
//    public boolean deleteMember(MemberVo vo) {
//        try {
//            int result = jdbcTemplate.update(DELETE_MEMBER, vo.getEmail());
//            return result > 0;
//        } catch (DataAccessException e) {
//            log.error("회원 삭제 중 예외 발생 ", e);
//            return false;
//        }
//    }
    public boolean deleteMember(String email) {
        int rowAffected = jdbcTemplate.update(DELETE_MEMBER, email);
        return rowAffected > 0;
    }

    private static class MemberRowMapper implements RowMapper<MemberVo> {
        @Override
        public MemberVo mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberVo(
                    rs.getString("email"),
                    null,
                    rs.getString("name"),
                    rs.getDate("reg_Date")
            );
        }
    }
}
