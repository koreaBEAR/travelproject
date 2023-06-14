package com.human.springboot;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TDAO {
	ArrayList <SignDTO> member_list();
	void member_insert(String s1, String s2, String s3, String s4, String s5, String s6, String s7, String s8);
	int id_check(String id);
	int nickName_check(String nickName);
	int member_check(String id, String pw);
	String member_class(String id);
	String member_id(String id, String mail);
	String findPw(String name,String id,String email);
	int findPwVal(String name,String id,String email);
	int nowPw(String id,String pw);
	void newPwUpdate(String id, String pw);
	void setRandomPw(String id, String pw);
	
	//리뷰
	ArrayList <RevDTO> placeList();
}
