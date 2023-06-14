package com.human.springboot;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
	
	
	//정아////////////////
	//회원관리
		void member_delete(int member_seq);
		ArrayList<LDTO> member_paging(@Param("pageNo") int pageNo, @Param("amount") int amount);
		int member_TotalCount();
		ArrayList<LDTO> msearch_paging(@Param("pageNo") int pageNo, @Param("amount") int amount, String type, String keyword);
		int msearch_TotalCount(String type, String keyword);

	//업체관리
		LDTO place_view(int place_seq);
		void place_delete(int place_seq);
		void place_update(int city, int category, String name, String address, String tel, String open, String content, String img, int place_seq);
		void place_insert(int city, int category, String name, String address, String tel, String open, String content, String img);
		ArrayList<LDTO> place_paging(@Param("pageNo") int pageNo, @Param("amount") int amount);
		int place_TotalCount();
		ArrayList<LDTO> psearch_paging(@Param("pageNo") int pageNo, @Param("amount") int amount, String type, String keyword);
		int psearch_TotalCount(String type, String keyword);

		
	//문의관리	
		void comment_insert(String help_comment,String help_complete, int help_seq);
		ArrayList<LDTO> help_paging(@Param("pageNo") int pageNo, @Param("amount") int amount);
		int help_TotalCount();
		ArrayList<LDTO> hsearch_paging(@Param("pageNo") int pageNo, @Param("amount") int amount, String type, String keyword);
		int hsearch_TotalCount(String type, String keyword);
		
	}


