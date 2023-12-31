package com.human.springboot;

import lombok.Data;

@Data
public class ScheduleCreateDTO {
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
	Double place_lat;
	Double place_lng;
	int r_;

	Double city_lat;
	Double city_lng;
}
