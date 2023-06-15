package com.human.springboot;

import lombok.Data;

@Data
public class LDTO {
	
	//member
	int member_seq;
	String member_id;
	String member_name;
	String member_nickName;
	String member_mobile;
	String member_email;
	String member_birth;
	String member_gender;
	String member_class;
	
	
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
	int place_lat;
	int place_lng;
	
	
	//city
	int city_num;
	String city_name;
	String city_img;
	String city_content;
	int city_count;
	
	//city_category
	int city_category_num;
	String city_category_name;
	
	//place
	int place_category_num;
	String place_category_name;
	
	
	//help
	int help_seq;
	String help_category;
	String help_title;
	String help_content;
	String help_created;
	String help_img;
	String help_comment;
	String help_complete;
	
	
	//paging
//	int pageNo;
//	int amount;
	
	
	//search
//	String type;
//	String keyword;

}
