package com.kh.miniProjectJdbc.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString // lombok이 자동으로 오버라이딩 해줌
public class MemberVo {
    private String email;
    private String password;
    private String name;
    private Date date;

}

