package com.human.springboot;

import lombok.Data;

@Data
public class SignDTO {
	int member_seq;
	String member_id;
	String member_pw;
	String member_name;
	String member_nickName;
	String member_mobile;
	String member_email;
	String member_birth;
	String member_gender;
	String member_class;
}
