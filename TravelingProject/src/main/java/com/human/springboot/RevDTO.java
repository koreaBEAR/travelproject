package com.human.springboot;

import lombok.Data;

@Data
public class RevDTO {
	
	//place
	int place_seq;
	int place_city;
	int place_category;
	String place_name;
	String place_address;
	String place_tel;
	String place_content;
	String place_img;
	String place_date;
	String place_open;
	int place_like;
	
	//travel_rev
	int review_seq;
	String member_nickname;
	String review_content;
	String review_date;
}
