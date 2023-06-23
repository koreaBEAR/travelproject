package com.human.springboot;

import lombok.Data;

@Data
public class MyPageDTO {
	int schedule_seq;
	int city_seq;
	int member_seq;
	String city_name;
	String city_img;
	String schedule_days;
	String schedule_updated;
	String member_nickname;
}
