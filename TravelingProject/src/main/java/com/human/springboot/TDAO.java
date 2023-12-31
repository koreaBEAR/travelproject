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
	ArrayList <RevDTO> placeListCity(int cityNum);
	ArrayList <RevDTO> placeListCategory(int n1, int n2);
	ArrayList <RevDTO> placeListPopular();
	ArrayList <RevDTO> placeListASC();
	ArrayList <RevDTO> placeListDESC();
	ArrayList <RevDTO> placeReviews(int placeSeq);
	ArrayList <RevDTO> reviewPlace(int placeSeq);
	ArrayList <RevDTO> reviewContent(int placeSeq);
	int reviewsCheck(int placeSeq);
	void revInsert(int placeNum, String memberNickname, String content);
	
	
	//마이페이지
	int findMemberSeq(String member_id);
	String selectMemberNickName(String member_id);
	ArrayList <MyPageDTO> myPageSchedule(int memberSeq);
	String selectMemberMobile(String member_id);
	String selectMemberMail(String member_id);
	void memberInfoModify(String s1, String s2, String s3, String s4);
	void scheduledelete(int scheduleSeq);
	int loadPlaceLike (int placeNum);
	void updateUpLike(int placeNum);
	void updateDownLike(int placeNum);
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
		void place_update(int city, int category, String name, String address, String tel, String open, String content, String img ,double lat, double lng, int place_seq);
		void place_insert(int city, int category, String name, String address, String tel, String open, String content, String img ,double lat, double lng, int like);
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
		
		
////////////////////재민////////////////	
		// 메인
		ArrayList<cityDTO> cityasc(); 
		ArrayList<cityDTO> citydesc();
		ArrayList<cityDTO> citybest();
		ArrayList<cityDTO> serachcity(String keyword);
		ArrayList<cityDTO> getCityDetails(int city_num);
		
//		고객센터
		ArrayList<contactDTO> contactlist(); //게시판 리스트
		ArrayList<contactDTO> search(String type, String keyword);  //키워드 게시판 리스트
		int contacttotal(); //게시판 글 count
		void contactinsert(String member_id, String help_category, String help_title, String help_content, String help_img, String help_password); //게시판 글작성
		contactDTO contactdetail(int help_seq); //게시판 상세보기
		void contactdelete(int help_seq); // 게시글 삭제
		String pwdselect(int help_seq); // 게시글 비밀번호 입력
		contactDTO updatelist(int help_seq); //게시판 업데이트 리스트
		void contactupdate(int help_seq, String help_category, String help_title, String help_content, String help_img, String help_password);
		
		
	//  게시물 비공개/공개 판별
		String sortPost(int post_seq);
		
		
////////////////////////현준//////////////////
		
		//업체 개수 체크
		int placeListAllCount(int city, String pSeq);
		int placeListCount(int city, String pSeq, String pCategory);
		
		//맵
		ArrayList<ScheduleCreateDTO> mapCreate(int city);
		
		//리스트
		ArrayList<ScheduleCreateDTO> allPlaceList(int city,int currentP,String pCategory);
		ArrayList<ScheduleCreateDTO> scheduleAddPlaceList(int city,int currentP,String pSeq, String pCategory);
		ArrayList<ScheduleCreateDTO> placeSearchNull(int city, String bigCategory, String searchText, int currentP);
		ArrayList<ScheduleCreateDTO> placeSearch(int city, String bigCategory, String pSeq, String searchText, int currentP);
		
		//업체정보
		ArrayList<ScheduleCreateDTO> placeInfo(int placeInfoId);
		
		//마커생성
		ArrayList<ScheduleCreateDTO> markerScheduleCreate(String pSeq);
		
		//유저seq 및 일정저장
		int  UserSeq(String UserId);
		void scheduleInsert(int city,int UserSeq,String sData,String sDays);
		void cityCountInc(int city); // 일정생성 시 시티카운트 증가(city_count+1)
		
		//일정수정
		ScheduleDTO scheduleData(int UserSeq, int scheduleSeq);
		ArrayList<ScheduleCreateDTO> scheduleUpdateDataList(String scheduleUpdatePlaceSeq);
		void scheduleUpdata(int scheduleSeq, int city, int UserSeq,String sData, String sDays);
	}



