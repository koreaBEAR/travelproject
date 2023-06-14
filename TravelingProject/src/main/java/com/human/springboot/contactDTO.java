package com.human.springboot;

import lombok.Data;

@Data
public class contactDTO {
	int help_seq;             
	String member_id; 
	String help_category;   
	String help_title; 
	String help_content; 
	String help_created;          
	String help_img;
	String help_comment;
	String help_complete;    
	String help_password;
	String member_nickname;
	int count;
}
