package com.myspring.pro27.member.vo;

import java.sql.Date;

import org.springframework.stereotype.Component;

import lombok.Data;


@Component("memberVO")
@Data
public class MemberVO {
	private String id;
	private String pwd;
	private String name;
	private String email;
	private Date joinDate;
}
