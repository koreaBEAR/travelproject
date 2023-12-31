package com.human.springboot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class Traval_controller {
	
	@Autowired
	private TDAO tdao;
	@Autowired
	private EmailService emailService; // EmailService 주입
	
	
	//현빈/////////////////
	//로그인 & 회원가입
	@GetMapping("/")
	public String doLogin() {
		return "login";
	}
	
	@GetMapping("/join")
	public String Join_page() {
		return "join";
	}
	
	@PostMapping("/member_insert")
	@ResponseBody
	public  String do_member_insert(HttpServletRequest req) {
		String retval = "ok";
		try {
			String id = req.getParameter("id");
			String pw = req.getParameter("pw");
			String name = req.getParameter("name");
			String nickName = req.getParameter("nickName");
			String birth = req.getParameter("birth");
			String email = req.getParameter("email");
			String mobile = req.getParameter("mobile");
			String gender = req.getParameter("gender");
			tdao.member_insert(id,pw,name,nickName,mobile,email,birth,gender);
			
		} catch(Exception e) {
			retval = "failed";
			e.printStackTrace();
		}
		return retval;
	}
	@PostMapping("/id_check")
	@ResponseBody
	public  String do_chk_id(HttpServletRequest req) {
		String findval=null;
		try {
			String id = req.getParameter("id");
			int n = tdao.id_check(id);
			if(n==1) {
				findval="ok";
			} else {
				findval="not ok";
			}
		} catch(Exception e) {
			findval="fail";
		}
		return findval;
	}
	@PostMapping("/nickName_check")
	@ResponseBody
	public  String nickName_check(HttpServletRequest req) {
		String findval=null;
		try {
			String aka = req.getParameter("aka");
			int n = tdao.nickName_check(aka);
			if(n==1) {
				findval="ok";
			} else {
				findval="not ok";
			}
		} catch(Exception e) {
			findval="fail";
		}
		return findval;
	}
	@PostMapping("/member_check")
	@ResponseBody
	public  String member_check(HttpServletRequest req) {
		String findval=null;
		try {
			String id = req.getParameter("id");
			String pw = req.getParameter("pw");
			int n = tdao.member_check(id,pw);
			String memberClass = tdao.member_class(id);
			HttpSession session = req.getSession();
			if(n==1) {
				session.setAttribute("id", id);
				session.setAttribute("memberClass", memberClass);
				findval="ok";
			} else {
				findval="not ok";
			}
		} catch(Exception e) {
			findval="fail";
			e.printStackTrace();
		}
		return findval;
	}
	@GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession();
		session.invalidate();
		return "redirect:/";
	}
	
	//아이디 비밀번호 찾기
	@PostMapping("/findId")
	@ResponseBody
	public String findId(HttpServletRequest req) {
		String findId=null;
		try {
			String id = req.getParameter("id");
			String Email = req.getParameter("mail");
			String memberId = tdao.member_id(id, Email);
			if(memberId!="") {
				findId=memberId;
			}else {
				findId="";
			}
		}catch(Exception e) {
			findId="fail";
			e.printStackTrace();
		}
		return findId;
	}
	@PostMapping("/findPw")
	@ResponseBody
	public String findPw(HttpServletRequest req) {
		String findPw=null;
		try {
			String id = req.getParameter("id");
			String Email = req.getParameter("email");
			String userName = req.getParameter("name");
			String rPw= req.getParameter("randomPw");
			tdao.setRandomPw(id, rPw);
			int n = tdao.findPwVal(userName, id, Email);
			if(n!=0) {
				findPw=rPw;
			}else {
				findPw="";
			}
		}catch(Exception e) {
			findPw="fail";
			e.printStackTrace();
		}
		return findPw;
	}
	// 이메일 전송 기능 추가
	@PostMapping("/sendEmail")
	@ResponseBody
	public String sendEmail(HttpServletRequest req) {
		String response = "";
		try {
			String recipient = req.getParameter("recipient");
			String subject = req.getParameter("subject");
			String content = req.getParameter("content");

			emailService.sendEmail(recipient, subject, content);
			response = "회원님의 메일로 임시비밀번호가 전송되었습니다.";
		} catch (Exception e) {
			response = "Failed to send email.";
			e.printStackTrace();
		}
		return response;
	}
	@PostMapping("/nowPwCheck")
	@ResponseBody
	public String nowPwCheck(HttpServletRequest req) {
		String val="";
		try {
			String nowPw = req.getParameter("nowPw");
			String id = req.getParameter("id");
			
			int n = tdao.nowPw(id,nowPw);
			if(n!=0) {
				val="ok";
			}else {
				val="not ok";
			}
		}catch(Exception e) {
			val="fail";
			e.printStackTrace();
		}
		return val;
	}
	
	//비밀번호 변경
	@PostMapping("/changeMemberPw")
	@ResponseBody
	public String changeMemberPw(HttpServletRequest req) {
		String val="";
		try {
			String newPw = req.getParameter("newPw");
			String chkNewPw = req.getParameter("chkNewPw");
			String id = req.getParameter("id");
			if(newPw.equals(chkNewPw)) {
				tdao.newPwUpdate(id,newPw);
				val="ok";
			}else {
				val="not ok";
			}
		}catch(Exception e) {
			val="fail";
			e.printStackTrace();
		}
		return val;
	}

	@GetMapping("/changePw")
	public String changePw(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		model.addAttribute("id",session.getAttribute("id"));
		return "changePw";
	}
	
	//리뷰 피드
	@GetMapping("/place")
	public String reviewPage(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		String memberID = (String)session.getAttribute("id");
		String memberNickName = tdao.selectMemberNickName(memberID);
		model.addAttribute("nickName", memberNickName);
		return "reviews";
	}
	//리뷰에 place보이기 (페이지네이션 10페이지씩 보이게 변경)
	@PostMapping("/loadReview")
	@ResponseBody
	public String loadReviews(HttpServletRequest req) {
		
	    int pageNum = Integer.parseInt(req.getParameter("pageNum"));
	    
	    ArrayList <RevDTO> alPlace = tdao.placeList();
	    int howmanyPlace = alPlace.size();
	    int howmanyPages = (howmanyPlace/12)+1;
	    if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
	    JSONArray ja = new JSONArray();
	    int start = (pageNum - 1)*12;
	    int end = (pageNum * 12)-1;
	    int startPage = (int)(((pageNum-1)/10)*10) + 1;
	    int endPage = (int)(startPage+9 < howmanyPages ? startPage+9 : howmanyPages);
	    try {
	        JSONObject jo = new JSONObject();
	        jo.put("howmany",howmanyPages);
	        jo.put("startPage", startPage);
	        jo.put("endPage", endPage);
	        ja.put(jo);
	        for(int i=start; i<=end && i<howmanyPlace; i++) {
	            jo = new JSONObject();
	            jo.put("placeId",alPlace.get(i).getPlace_seq());
	            jo.put("placeImg",alPlace.get(i).getPlace_img());
	            jo.put("placeName", alPlace.get(i).getPlace_name());					
	            ja.put(jo);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}
	@PostMapping("/loadReviewPopular")
	@ResponseBody
	public String loadReviewPopular(HttpServletRequest req) {
		
	    int pageNum = Integer.parseInt(req.getParameter("pageNum"));
	    
	    ArrayList <RevDTO> alPlace = tdao.placeListPopular();
	    int howmanyPlace = alPlace.size();
	    int howmanyPages = (howmanyPlace/12)+1;
	    if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
	    JSONArray ja = new JSONArray();
	    int start = (pageNum - 1)*12;
	    int end = (pageNum * 12)-1;
	    int startPage = (int)(((pageNum-1)/10)*10) + 1;
	    int endPage = (int)(startPage+9 < howmanyPages ? startPage+9 : howmanyPages);
	    try {
	        JSONObject jo = new JSONObject();
	        jo.put("howmany",howmanyPages);
	        jo.put("startPage", startPage);
	        jo.put("endPage", endPage);
	        ja.put(jo);
	        for(int i=start; i<=end && i<howmanyPlace; i++) {
	            jo = new JSONObject();
	            jo.put("placeId",alPlace.get(i).getPlace_seq());
	            jo.put("placeImg",alPlace.get(i).getPlace_img());
	            jo.put("placeName", alPlace.get(i).getPlace_name());					
	            ja.put(jo);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}
	@PostMapping("/loadReviewASC")
	@ResponseBody
	public String loadReviewASC(HttpServletRequest req) {
		
	    int pageNum = Integer.parseInt(req.getParameter("pageNum"));
	    
	    ArrayList <RevDTO> alPlace = tdao.placeListASC();
	    int howmanyPlace = alPlace.size();
	    int howmanyPages = (howmanyPlace/12)+1;
	    if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
	    JSONArray ja = new JSONArray();
	    int start = (pageNum - 1)*12;
	    int end = (pageNum * 12)-1;
	    int startPage = (int)(((pageNum-1)/10)*10) + 1;
	    int endPage = (int)(startPage+9 < howmanyPages ? startPage+9 : howmanyPages);
	    try {
	        JSONObject jo = new JSONObject();
	        jo.put("howmany",howmanyPages);
	        jo.put("startPage", startPage);
	        jo.put("endPage", endPage);
	        ja.put(jo);
	        for(int i=start; i<=end && i<howmanyPlace; i++) {
	            jo = new JSONObject();
	            jo.put("placeId",alPlace.get(i).getPlace_seq());
	            jo.put("placeImg",alPlace.get(i).getPlace_img());
	            jo.put("placeName", alPlace.get(i).getPlace_name());					
	            ja.put(jo);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}
	@PostMapping("/loadReviewDESC")
	@ResponseBody
	public String loadReviewDESC(HttpServletRequest req) {
		
	    int pageNum = Integer.parseInt(req.getParameter("pageNum"));
	    
	    ArrayList <RevDTO> alPlace = tdao.placeListDESC();
	    int howmanyPlace = alPlace.size();
	    int howmanyPages = (howmanyPlace/12)+1;
	    if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
	    JSONArray ja = new JSONArray();
	    int start = (pageNum - 1)*12;
	    int end = (pageNum * 12)-1;
	    int startPage = (int)(((pageNum-1)/10)*10) + 1;
	    int endPage = (int)(startPage+9 < howmanyPages ? startPage+9 : howmanyPages);
	    try {
	        JSONObject jo = new JSONObject();
	        jo.put("howmany",howmanyPages);
	        jo.put("startPage", startPage);
	        jo.put("endPage", endPage);
	        ja.put(jo);
	        for(int i=start; i<=end && i<howmanyPlace; i++) {
	            jo = new JSONObject();
	            jo.put("placeId",alPlace.get(i).getPlace_seq());
	            jo.put("placeImg",alPlace.get(i).getPlace_img());
	            jo.put("placeName", alPlace.get(i).getPlace_name());					
	            ja.put(jo);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}
	@PostMapping("/loadReviewCity")
	@ResponseBody
	public String loadReviewCity(HttpServletRequest req) {
		
	    int pageNum = Integer.parseInt(req.getParameter("pageNum"));
	    int cityNum = Integer.parseInt(req.getParameter("cityNum"));
	    
	    ArrayList <RevDTO> alPlace = tdao.placeListCity(cityNum);
	    int howmanyPlace = alPlace.size();
	    int howmanyPages = (howmanyPlace/12)+1;
	    if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
	    JSONArray ja = new JSONArray();
	    int start = (pageNum - 1)*12;
	    int end = (pageNum * 12)-1;
	    int startPage = (int)(((pageNum-1)/10)*10) + 1;
	    int endPage = (int)(startPage+9 < howmanyPages ? startPage+9 : howmanyPages);
	    try {
	        JSONObject jo = new JSONObject();
	        jo.put("howmany",howmanyPages);
	        jo.put("startPage", startPage);
	        jo.put("endPage", endPage);
	        ja.put(jo);
	        for(int i=start; i<=end && i<howmanyPlace; i++) {
	            jo = new JSONObject();
	            jo.put("placeId",alPlace.get(i).getPlace_seq());
	            jo.put("placeImg",alPlace.get(i).getPlace_img());
	            jo.put("placeName", alPlace.get(i).getPlace_name());					
	            ja.put(jo);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}
	@PostMapping("/loadReviewCategory")
	@ResponseBody
	public String loadReviewCategory(HttpServletRequest req) {
		
	    int pageNum = Integer.parseInt(req.getParameter("pageNum"));
	    int n1 = Integer.parseInt(req.getParameter("n1"));
	    int n2 = Integer.parseInt(req.getParameter("n2"));
	    
	    ArrayList <RevDTO> alPlace = tdao.placeListCategory(n1,n2);
	    int howmanyPlace = alPlace.size();
	    int howmanyPages = (howmanyPlace/12)+1;
	    if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
	    JSONArray ja = new JSONArray();
	    int start = (pageNum - 1)*12;
	    int end = (pageNum * 12)-1;
	    int startPage = (int)(((pageNum-1)/10)*10) + 1;
	    int endPage = (int)(startPage+9 < howmanyPages ? startPage+9 : howmanyPages);
	    try {
	        JSONObject jo = new JSONObject();
	        jo.put("howmany",howmanyPages);
	        jo.put("startPage", startPage);
	        jo.put("endPage", endPage);
	        ja.put(jo);
	        for(int i=start; i<=end && i<howmanyPlace; i++) {
	            jo = new JSONObject();
	            jo.put("placeId",alPlace.get(i).getPlace_seq());
	            jo.put("placeImg",alPlace.get(i).getPlace_img());
	            jo.put("placeName", alPlace.get(i).getPlace_name());					
	            ja.put(jo);
	        }
	    }catch(Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}

	/* 기존코드 */
//	@PostMapping("/loadReview")
//	@ResponseBody
//	public String loadReviews(HttpServletRequest req) {
//		int pageNum = Integer.parseInt(req.getParameter("pageNum"));
//		ArrayList <RevDTO> alPlace = tdao.placeList();
//		int howmanyPlace = alPlace.size();
//		int howmanyPages = (howmanyPlace/13)+1;
//		if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
//		JSONArray ja = new JSONArray();
//		int start = (pageNum - 1)*13;
//		int end = (pageNum * 13)-1;
//		try {
//			JSONObject jo = new JSONObject();
//			jo.put("howmany",howmanyPages);
//			ja.put(jo);
//			for(int i=start; i<end; i++) {
//				if(i>=start && i<=end) {
//					jo = new JSONObject();
//					jo.put("placeId",alPlace.get(i).getPlace_seq());
//					jo.put("placeImg",alPlace.get(i).getPlace_img());
//					jo.put("placeName", alPlace.get(i).getPlace_name());					
//					ja.put(jo);
//				}
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//		return ja.toString();
//	}
	
	@PostMapping("/loadReviewOne")
	@ResponseBody
	public String loadReviewOne(HttpServletRequest req) {
		String dataValue="";
		int placeNum = Integer.parseInt(req.getParameter("placeNum"));
		int n = tdao.reviewsCheck(placeNum);
		try {
			if(n==0) {
				dataValue = "zero";
			}else if(n>=1) {
				dataValue = "overOne";
			}else {
				dataValue = "error";
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return dataValue;
	}
	@PostMapping("/loadPlaceInfo")
	@ResponseBody
	public String loadPlaceInfo(HttpServletRequest req,Model model) {
		int placeNum = Integer.parseInt(req.getParameter("placeNum"));
		ArrayList <RevDTO> alPlace = tdao.reviewPlace(placeNum);
		JSONArray ja = new JSONArray();
		try {
			JSONObject jo = new JSONObject();
			for(int i=0; i<alPlace.size(); i++) {				
				jo.put("placeImg", alPlace.get(i).getPlace_img());
				jo.put("placeName", alPlace.get(i).getPlace_name());
				jo.put("placeContent", alPlace.get(i).getPlace_content());
				jo.put("placeTel", alPlace.get(i).getPlace_tel());
				jo.put("placeAddress", alPlace.get(i).getPlace_address());
				jo.put("placeLike", alPlace.get(i).getPlace_like());
				ja.put(jo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ja.toString();
	}
	@PostMapping("/loadReviewInfo")
	@ResponseBody
	public String loadReviewInfo(HttpServletRequest req,Model model) {
		int placeNum = Integer.parseInt(req.getParameter("placeNum"));
		ArrayList <RevDTO> alPlace = tdao.placeReviews(placeNum);
		JSONArray ja = new JSONArray();
		try {
			JSONObject jo = new JSONObject();
			for(int i=0; i<alPlace.size(); i++) {				
				jo.put("placeImg", alPlace.get(i).getPlace_img());
				jo.put("placeName", alPlace.get(i).getPlace_name());
				jo.put("placeContent", alPlace.get(i).getPlace_content());
				jo.put("placeTel", alPlace.get(i).getPlace_tel());
				jo.put("placeAddress", alPlace.get(i).getPlace_address());
				jo.put("placeLike", alPlace.get(i).getPlace_like());
				ja.put(jo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ja.toString();
	}
	@PostMapping("/loadReviewContent")
	@ResponseBody
	public String loadReviewContent(HttpServletRequest req) {
	    int placeNum = Integer.parseInt(req.getParameter("placeNum"));
	    int page = 1; // Get the page parameter
	    int startIndex = (page - 1);
	    
	    ArrayList<RevDTO> alPlace = tdao.reviewContent(placeNum); // Retrieve all reviews

	    JSONArray ja = new JSONArray();
	    try {
	    	 for (int i = startIndex; i < alPlace.size(); i++) {
	            JSONObject jo = new JSONObject();
	            jo.put("reviewNickName", alPlace.get(i).getMember_nickname());
	            jo.put("reviewContent", alPlace.get(i).getReview_content());
	            jo.put("reviewDate", alPlace.get(i).getReview_date());
	            ja.put(jo);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return ja.toString();
	}
	@PostMapping("/loadLike")
	@ResponseBody
	public String loadLike(HttpServletRequest req) {
		String val = null;
		int placeNum = Integer.parseInt(req.getParameter("placeNum"));
		int n = tdao.loadPlaceLike(placeNum);
		try {
			if(n<0) {
				return val;
			}
			else {
				val = ""+n;
			}
		}catch(Exception e) {
			
		}
		return val;
	}
	@PostMapping("/upLike")
	@ResponseBody
	public String upLike(HttpServletRequest req) {
		String val = "ok";
		int placeNum = Integer.parseInt(req.getParameter("placeNum"));
		try {
			tdao.updateUpLike(placeNum);
		}catch(Exception e) {
			val = "not OK";			
		}
		return val;
		
	}
	@PostMapping("/downLike")
	@ResponseBody
	public String downLike(HttpServletRequest req) {
		String val = "ok";
		int placeNum = Integer.parseInt(req.getParameter("placeNum"));
		try {
			tdao.updateDownLike(placeNum);
		}catch(Exception e) {
			val = "not OK";			
		}
		return val;
		
	}
	@PostMapping("/revContentInsert")
	@ResponseBody
	public String revContentInsert(HttpServletRequest req) {
		String retval=null;
		try {
			int placeNum = Integer.parseInt(req.getParameter("placeNum"));
			String member = req.getParameter("memberNickName");
			String revContent = req.getParameter("revContent");
			tdao.revInsert(placeNum, member, revContent);
			retval="ok";
		}catch(Exception e) {
			retval="not ok";
			e.printStackTrace();
		}
		return retval;
	}


	
	//마이페이지
	@GetMapping("/myPage")
	public String loadMyPage(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		String memberID = (String)session.getAttribute("id");
		String memberNickName = tdao.selectMemberNickName(memberID);
		String memberMail = tdao.selectMemberMail(memberID);
		String membermobile = tdao.selectMemberMobile(memberID);
		model.addAttribute("mail", memberMail);
		model.addAttribute("mobile", membermobile);
		model.addAttribute("nickName", memberNickName);
		return "myPage";
	}
	@PostMapping("/memberInfoModify")
	@ResponseBody
	public String memberInfoModify(HttpServletRequest req) {
		String retval = null;
		try {
			String memberMail = req.getParameter("mail");
			String memberNickname = req.getParameter("nickname");
			String memberMobile = req.getParameter("tel");
			String memberId = req.getParameter("id");
			tdao.memberInfoModify(memberMail, memberNickname, memberMobile, memberId);
			retval = "ok";
		}catch(Exception e) {
			retval = "not ok";
			e.printStackTrace();
		}
		return retval;
	}
	//마이페이지 schedule load
	@PostMapping("/loadMyPageSchedule")
	@ResponseBody
	public String loadMyPageSchedule(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String member_id = (String)session.getAttribute("id");
		int memberSeq = tdao.findMemberSeq(member_id);
		ArrayList <MyPageDTO> alSchedule = tdao.myPageSchedule(memberSeq);
		JSONArray ja = new JSONArray();
		try {
			for(int i=0; i<alSchedule.size(); i++) {
				JSONObject jo = new JSONObject();
				jo.put("schedule_seq", alSchedule.get(i).getSchedule_seq());
				jo.put("citySeq", alSchedule.get(i).getCity_seq());
				jo.put("cityName", alSchedule.get(i).getCity_name());
				jo.put("cityImg", alSchedule.get(i).getCity_img());
				jo.put("scheduleDays", alSchedule.get(i).getSchedule_days());
				jo.put("scheduleUpdated", alSchedule.get(i).getSchedule_updated());
				ja.put(jo);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ja.toString();
	}
	
	@PostMapping("/scheduledelete")
	@ResponseBody
	public String scheduledelete(HttpServletRequest req) {
		int scheduleSeq = Integer.parseInt(req.getParameter("scheduleSeq"));
		String check = "true";
		
		if ( scheduleSeq >= 0 ) {
			tdao.scheduledelete(scheduleSeq);
		}
		else {
			check = "false";
		}
		
		return check;
	}
	
	//정아
	@GetMapping("/manage_member")
	public String manage_member(@RequestParam(defaultValue = "1") int pageNo,
			 					@RequestParam(defaultValue = "10") int amount, Model model) {
		ArrayList<LDTO> member=tdao.member_paging(pageNo, amount);
		model.addAttribute("memberList",member);
		
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("amount", amount);
		int totalCount = tdao.member_TotalCount();
		model.addAttribute("totalCount", totalCount);
		int endPage = (int) (Math.ceil((double) totalCount / amount));
		model.addAttribute("endPage", endPage);
		return "manage_member";	
	}
		
	//회원삭제
	@PostMapping("/member_delete")
	@ResponseBody
	public String memberDelete(HttpServletRequest req) {
		int member_seq=Integer.parseInt(req.getParameter("member_seq"));
		tdao.member_delete(member_seq);
		return "manage_member";
	}

	//회원검색&페이징
	   @GetMapping("/member/search")
	   public String doSearch(@RequestParam(defaultValue = "1") int pageNo,
			   				  @RequestParam(defaultValue = "10") int amount, 
	                          Model model, HttpServletRequest req, HttpSession session) {

	      String keyword = req.getParameter("keyword"); 
	      String type = req.getParameter("type");


	      if (keyword != null && type != null) {
	         session.setAttribute("keyword", keyword);
	         session.setAttribute("type", type);
	      }
	      else {
	         keyword = (String) session.getAttribute("keyword");
	         type = (String) session.getAttribute("type");
	      }

	      ArrayList<LDTO> member = new ArrayList<LDTO>();
	      int totalCount = 0;	    
	      if(!type.equals("")){
	    	  member = tdao.msearch_paging(pageNo, amount, type, keyword);
	    	  totalCount = tdao.msearch_TotalCount(type, keyword);    
	      }
	      
	      if(totalCount ==0) {
	    	  model.addAttribute("errorMessage","일치하는 검색 결과가 없습니다.");
	    	  return "manage_member";
	      }

	      model.addAttribute("memberList",member);    
	      model.addAttribute("pageNo", pageNo);
	      model.addAttribute("amount", amount);
	      model.addAttribute("totalCount", totalCount);
	      int endPage = (int) (Math.ceil((double) totalCount / amount));
	      model.addAttribute("endPage", endPage);    

	      return "manage_member";
	   }


///////////////////////////////////////////////////////////////////////////////////////		   
	//업체관리&페이징
	@GetMapping("/manage_place")
	public String manage_place(@RequestParam(defaultValue = "1") int pageNo,
			 				   @RequestParam(defaultValue = "10") int amount, Model model) {
		ArrayList<LDTO> place=tdao.place_paging(pageNo, amount);
		model.addAttribute("placeList",place);
		
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("amount", amount);
		int totalCount = tdao.place_TotalCount();
		model.addAttribute("totalCount", totalCount);
		int endPage = (int) (Math.ceil((double) totalCount / amount));
		model.addAttribute("endPage", endPage);
		return "manage_place";	
	}

	
	//업체검색&페이징
   @GetMapping("/place/search")
   public String placeSearch(@RequestParam(defaultValue = "1") int pageNo,
                    	     @RequestParam(defaultValue = "10") int amount, 
                             Model model, HttpServletRequest req, HttpSession session) {
	  String keyword = req.getParameter("keyword"); 
      String type = req.getParameter("type");


      if (keyword != null && type != null) {
         session.setAttribute("keyword", keyword);
         session.setAttribute("type", type);
      }
      else {
         keyword = (String) session.getAttribute("keyword");
         type = (String) session.getAttribute("type");
      }

      ArrayList<LDTO> place = new ArrayList<LDTO>();
      int totalCount = 0;	    
      if(!type.equals("")){
    	  place = tdao.psearch_paging(pageNo, amount, type, keyword);
    	  totalCount = tdao.psearch_TotalCount(type, keyword);    
      }
      
      if(totalCount ==0) {
    	  model.addAttribute("errorMessage","일치하는 검색 결과가 없습니다.");
    	  return "manage_place";
      }

      model.addAttribute("placeList",place);    
      model.addAttribute("pageNo", pageNo);
      model.addAttribute("amount", amount);
      model.addAttribute("totalCount", totalCount);
      int endPage = (int) (Math.ceil((double) totalCount / amount));
      model.addAttribute("endPage", endPage);    

      return "manage_place";
   }
	   
	//업체삭제 1(일괄,개별)
	@PostMapping("/place_delete")
	@ResponseBody
	public String place_delete(HttpServletRequest req) {
	    String place_seq = req.getParameter("placeSeqs");
	    place_seq = place_seq.replaceFirst(",", "");
	    place_seq = place_seq.replace("on,", "");
	    String[] placeSeqArray = place_seq.split(",");
	    
	    for (String seq : placeSeqArray) {
	        int placeSeq = Integer.parseInt(seq);
	        tdao.place_delete(placeSeq);
	    }
	    
	    return "manage_place";
	}
	//업체삭제 2(상세페이지에서)
	@GetMapping("/delete_place/{place_seq}")
	public String doDelete(@PathVariable("place_seq") int place_seq) {
		tdao.place_delete(place_seq);
		return "redirect:/manage_place";
	}
	

	@GetMapping("/place_insert")
	public String place_insert() {
		return "place_insert";	
	}
	
	//업체등록
	@PostMapping("/place_upload")
	@ResponseBody
	public String place_insert(HttpServletRequest req) {
	    int city = Integer.parseInt(req.getParameter("city"));
	    int place = Integer.parseInt(req.getParameter("place"));
	    String name = req.getParameter("name");
	    String tel = req.getParameter("tel");
	    String open = req.getParameter("open");
	    String content = req.getParameter("content");
	    String postcode = req.getParameter("postcode");
	    String address = req.getParameter("address");
	    String detailAddress = req.getParameter("detailAddress");
	    int like = Integer.parseInt(req.getParameter("like"));
//	    double lat = Double.parseDouble(req.getParameter("lat"));
//	    double lng = Double.parseDouble(req.getParameter("lng"));
	    String latParam = req.getParameter("lat");
	    String lngParam = req.getParameter("lng");
	    Double lat = latParam != null && !latParam.isEmpty() ? Double.parseDouble(latParam) : 0.0;
	    Double lng = lngParam != null && !lngParam.isEmpty() ? Double.parseDouble(lngParam) : 0.0;


	    
	    String place_address = postcode + "," + address + "," + detailAddress;

	    	    
	    String[] fileNames = req.getParameterValues("imageNames[]");
	    
	    String[] imageUrls;
	    if (fileNames != null) {
	        imageUrls = new String[fileNames.length];
	        for (int i = 0; i < fileNames.length; i++) {
	            imageUrls[i] = "/img//place/" + fileNames[i];
	        }
	    } else {
	        imageUrls = new String[]{""};
	    }
	    String img = String.join(",", imageUrls);

	    tdao.place_insert(city, place, name, place_address, tel, open, content, img, lat, lng, like);
	    return "manage_place";
	}

	
	// 이미지 업로드
	@PostMapping("/upload_image")
	public String uploadImage(@RequestParam("images") MultipartFile[] images) {
	    try {
	        for (MultipartFile image : images) {
	            String originalFilename = image.getOriginalFilename();
	            String filePath = "C:/Users/admin/git/travelproject/TravelingProject/src/main/resources/static/img/place/" + originalFilename;
	            File dest = new File(filePath);
	            image.transferTo(dest);
	        }
	        return "manage_place";
	    } catch (IOException e) {
	        e.printStackTrace();
	        for (MultipartFile image : images) {
	            String originalFilename = image.getOriginalFilename();
	            String filePath = "C:/Users/admin/git/travelproject/TravelingProject/src/main/resources/static/img/place/" + originalFilename;
	            File file = new File(filePath);
	            if (file.exists() && file.isFile()) {
	                file.delete();
	            }
	        }

	        return "redirect:/manage_place";
	    }
	}


	
	//업체상세보기	
	@GetMapping("/place_view/{place_seq}")
	public String place_view(@PathVariable("place_seq") int place_seq,Model model) {
		model.addAttribute("p",tdao.place_view(place_seq));
		return"place_view";
	}
	
	@GetMapping("/place_update/{place_seq}")
	public String place_update(@PathVariable("place_seq") int place_seq,Model model) {
		model.addAttribute("p",tdao.place_view(place_seq));
		return "place_update";	
	}
	
	//업체수정
	@PostMapping("/place_update")
	@ResponseBody
	public String place_update(HttpServletRequest req) {
		int seq = Integer.parseInt(req.getParameter("place_seq"));
		int city = Integer.parseInt(req.getParameter("city"));
	    int place = Integer.parseInt(req.getParameter("place"));
	    String name = req.getParameter("name");
	    String tel = req.getParameter("tel");
	    String open = req.getParameter("open");
	    String content = req.getParameter("content");
	    String postcode = req.getParameter("postcode");
	    String address = req.getParameter("address");
	    String detailAddress = req.getParameter("detailAddress");
	    double lat = Double.parseDouble(req.getParameter("lat"));
	    double lng = Double.parseDouble(req.getParameter("lng"));

	    String place_address = postcode + "," + address + "," + detailAddress;

	    String[] fileNames = req.getParameterValues("imageNames[]");
	    
	    String[] imageUrls;
	    if (fileNames != null) {
	        imageUrls = new String[fileNames.length];
	        for (int i = 0; i < fileNames.length; i++) {
	            imageUrls[i] = "/img//place/" + fileNames[i];
	        }
	    } else {
	    	String originalImageUrls = tdao.place_view(seq).getPlace_img();
	        imageUrls = originalImageUrls.split(",");
	    }
	    String img = String.join(",", imageUrls);

	    tdao.place_update(city, place, name, place_address, tel, open, content, img, lat, lng, seq);
	    return "manage_place";
	}
	
	
///////////////////////////////////////////////////////////////////////////////////////		
	//문의관리&페이징
	@GetMapping("/manage_help")
	public String manage_help(@RequestParam(defaultValue = "1") int pageNo,
			 				  @RequestParam(defaultValue = "10") int amount, Model model,HttpServletRequest req) {
		ArrayList<LDTO> help=tdao.help_paging(pageNo, amount);
		model.addAttribute("helpList",help);
							
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("amount", amount);
		int totalCount = tdao.help_TotalCount();
		model.addAttribute("totalCount", totalCount);
		int endPage = (int) (Math.ceil((double) totalCount / amount));
		model.addAttribute("endPage", endPage);
		return "manage_help";	
	}
	
	 
	//관리자 답변달기
	@PostMapping("comment_insert")
	@ResponseBody
	public String doinsert(HttpServletRequest req) {
		String check = "ok";
		String comment=req.getParameter("comment");
		String help_complete=req.getParameter("help_complete");
		int help_seq = Integer.parseInt(req.getParameter("help_seq"));
		tdao.comment_insert(comment,help_complete,help_seq);
		return check;		
	}
	
	
	//문의검색&페이징
	@GetMapping("/help/search")
	   public String hSearch(@RequestParam(defaultValue = "1") int pageNo,
			   				 @RequestParam(defaultValue = "10") int amount, 
	                         Model model, HttpServletRequest req, HttpSession session) {
	      String keyword = req.getParameter("keyword"); 
	      String type = req.getParameter("type");


	      if (keyword != null && type != null) {
	         session.setAttribute("keyword", keyword);
	         session.setAttribute("type", type);
	      }
	      else {
	         keyword = (String) session.getAttribute("keyword");
	         type = (String) session.getAttribute("type");
	      }

	      ArrayList<LDTO> help = new ArrayList<LDTO>();
	      int totalCount = 0;	    
	      if(!type.equals("")){
	    	  help = tdao.hsearch_paging(pageNo, amount, type, keyword);
	    	  totalCount = tdao.hsearch_TotalCount(type, keyword);    
	      }
	      
	      if(totalCount ==0) {
	    	  model.addAttribute("errorMessage","일치하는 검색 결과가 없습니다.");
	    	  return "manage_member";
	      }

	      model.addAttribute("helpList",help);    
	      model.addAttribute("pageNo", pageNo);
	      model.addAttribute("amount", amount);
	      model.addAttribute("totalCount", totalCount);
	      int endPage = (int) (Math.ceil((double) totalCount / amount));
	      model.addAttribute("endPage", endPage);    

	      return "manage_help";
	   }

	
	@GetMapping("/manage_error")
	public String manage_error() {
		return "manage_error";	
	}	
	
	
/////////////////////////	재민 ////////////////////////////////
		
		//메인 홈페이지//
	@GetMapping("/main")
	public String loadMain(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		model.addAttribute("id", session.getAttribute("id"));
		model.addAttribute("memberClass", session.getAttribute("memberClass"));
		return "main";
	}

// 메인 리스트 오름차순	   
	@PostMapping("/cityasc")
	@ResponseBody
	public String cityAsc(HttpServletRequest req) {

		ArrayList<cityDTO> ascList = tdao.cityasc();

		JSONArray ja = new JSONArray();
		for (int i = 0; i < ascList.size(); i++) {
			JSONObject jo = new JSONObject();

			jo.put("city_num", ascList.get(i).getCity_num());
			jo.put("city_name", ascList.get(i).getCity_name());
			jo.put("city_img", ascList.get(i).getCity_img());
			jo.put("city_content", ascList.get(i).getCity_content());
			jo.put("city_count", ascList.get(i).getCity_count());
			jo.put("city_engname", ascList.get(i).getCity_engname());

			ja.put(jo);
		}
		return ja.toString();
	}

// 메인 리스트 내림차순	   
	@PostMapping("/citydesc")
	@ResponseBody
	public String cityDesc(HttpServletRequest req) {

		ArrayList<cityDTO> descList = tdao.citydesc();

		JSONArray ja = new JSONArray();
		for (int i = 0; i < descList.size(); i++) {
			JSONObject jo = new JSONObject();

			jo.put("city_num", descList.get(i).getCity_num());
			jo.put("city_name", descList.get(i).getCity_name());
			jo.put("city_img", descList.get(i).getCity_img());
			jo.put("city_engname", descList.get(i).getCity_engname());

			ja.put(jo);
		}
		return ja.toString();
	}

// 메인 리스트 인기순	   
	@PostMapping("/citybest")
	@ResponseBody
	public String cityBest(HttpServletRequest req) {

		ArrayList<cityDTO> descList = tdao.citybest();

		JSONArray ja = new JSONArray();
		for (int i = 0; i < descList.size(); i++) {
			JSONObject jo = new JSONObject();

			jo.put("city_num", descList.get(i).getCity_num());
			jo.put("city_name", descList.get(i).getCity_name());
			jo.put("city_img", descList.get(i).getCity_img());
			jo.put("city_engname", descList.get(i).getCity_engname());

			ja.put(jo);
		}
		return ja.toString();
	}

//  고객센터 검색
	@PostMapping("/searchcity")
	@ResponseBody
	public String searchCity(HttpServletRequest req) {
		String keyword = req.getParameter("keyword");

		ArrayList<cityDTO> searchCitys = tdao.serachcity(keyword);

		JSONArray ja = new JSONArray();
		for (int i = 0; i < searchCitys.size(); i++) {
			JSONObject jo = new JSONObject();

			jo.put("city_num", searchCitys.get(i).getCity_num());
			jo.put("city_name", searchCitys.get(i).getCity_name());
			jo.put("city_img", searchCitys.get(i).getCity_img());
			jo.put("city_engname", searchCitys.get(i).getCity_engname());

			ja.put(jo);
		}
		return ja.toString();
	}

// 메인에서 dialog
	@PostMapping("/getCityDetails/{city_num}")
	@ResponseBody
	public String getCityDetails(@PathVariable("city_num") int city_num, HttpServletRequest req) {

		ArrayList<cityDTO> cityDetaillist = tdao.getCityDetails(city_num);

		JSONArray ja = new JSONArray();
		for (int i = 0; i < cityDetaillist.size(); i++) {
			JSONObject jo = new JSONObject();

			jo.put("city_num", cityDetaillist.get(i).getCity_num());
			jo.put("city_name", cityDetaillist.get(i).getCity_name());
			jo.put("city_img", cityDetaillist.get(i).getCity_img());
			jo.put("city_count", cityDetaillist.get(i).getCity_count());
			jo.put("city_content", cityDetaillist.get(i).getCity_content());
			jo.put("city_engname", cityDetaillist.get(i).getCity_engname());

			ja.put(jo);
		}
		return ja.toString();
	}

// 고객센터 문의게시판
	@GetMapping("/contact")
	public String getListtotal(Model model) {

		int num = tdao.contacttotal();
		model.addAttribute("num", num);

		return "contact";
	}

// 고객센터 문의 리스트	   
	@PostMapping("/getList")
	@ResponseBody
	public String doGetList(HttpServletRequest req) {

		int page_num = Integer.parseInt(req.getParameter("pageNum"));

		ArrayList<contactDTO> alList = tdao.contactlist();

		int howmanyProd = alList.size();
		int howmanyPages = (howmanyProd / 10) + 1;
		if (howmanyProd % 10 == 0) {
			howmanyPages = howmanyPages - 1;
		}
		int start = (page_num - 1) * 10;
		int end = (page_num * 10) - 1;

		JSONArray ja = new JSONArray();
		try {
			JSONObject jo = new JSONObject();
			jo.put("howmany", howmanyPages);
			ja.put(jo);

			for (int i = 0; i < alList.size(); i++) {
				if (i >= start && i <= end) {
					jo = new JSONObject();
					jo.put("help_seq", alList.get(i).getHelp_seq());
					jo.put("help_category", alList.get(i).getHelp_category());
					jo.put("help_title", alList.get(i).getHelp_title());
					jo.put("member_nickname", alList.get(i).getMember_nickname());
					jo.put("help_created", alList.get(i).getHelp_created());
					jo.put("help_complete", alList.get(i).getHelp_complete());

					ja.put(jo);
				}
			}
		} catch (Exception e) {
		}
		return ja.toString();
	}

//  고객센터 검색
	@PostMapping("/search")
	@ResponseBody
	public String search(HttpServletRequest req) {

		String pageNumStr = req.getParameter("pageNum");
		int pageNum = pageNumStr != null ? Integer.parseInt(pageNumStr) : 1;

		String searchType = req.getParameter("type");
		String keyword = req.getParameter("keyword");

		ArrayList<contactDTO> searchResults = tdao.search(searchType, keyword);

		int howmanyProd = searchResults.size();
		int howmanyPages = (howmanyProd / 10) + 1;
		if (howmanyProd % 10 == 0) {
			howmanyPages = howmanyPages - 1;
		}
		int start = (pageNum - 1) * 10;
		int end = (pageNum * 10) - 1;

		JSONObject result = new JSONObject();
		JSONArray ja = new JSONArray();
		try {
			JSONObject jo = new JSONObject();
			jo.put("howmany", howmanyPages);
			ja.put(jo);

			for (int i = 0; i < searchResults.size(); i++) {
				if (i >= start && i <= end) {
					jo = new JSONObject();
					jo.put("count", searchResults.get(i).getCount());
					jo.put("help_seq", searchResults.get(i).getHelp_seq());
					jo.put("help_category", searchResults.get(i).getHelp_category());
					jo.put("help_title", searchResults.get(i).getHelp_title());
					jo.put("member_nickname", searchResults.get(i).getMember_nickname());
					jo.put("help_created", searchResults.get(i).getHelp_created());
					jo.put("help_complete", searchResults.get(i).getHelp_complete());

					ja.put(jo);
				}
			}
			result.put("results", ja);
			result.put("count", searchResults.size());
		} catch (Exception e) {
		}
		return result.toString();
	}

// 고객센터 문의작성
	@GetMapping("/contactwrite")
	public String contactwrite() {

		return "contactwrite";
	}	
	
	
	// 고객센터 문의 insert
	   @PostMapping("/contactinsert")
	   @ResponseBody
	   public String prodInsert(HttpServletRequest req,
	         @RequestParam(value = "img", required = false) MultipartFile[] imgs,
	         @RequestParam("help_category") String help_category, @RequestParam("help_title") String help_title,
	         @RequestParam("help_content") String help_content, @RequestParam("help_password") String help_password) {

	      String insertval = "ok";

	      try {
	         HttpSession session = req.getSession();
	         String member_id = (String) session.getAttribute("id");

	         List<String> fileNames1 = new ArrayList<>();

	         String filePath = req.getServletContext().getRealPath("/img/contact/");
	         
	         File folder = new File(filePath);
	         if (!folder.exists()) {
	             folder.mkdirs();
	         }
	         for (MultipartFile file : imgs) {
	        	    if (!file.isEmpty()) {
	        	        String fileName = file.getOriginalFilename();
	        	        String filePathAndName = filePath + fileName;
	        	        File newFile = new File(filePathAndName);
	        	        file.transferTo(newFile);
	        	        fileNames1.add(fileName);  // 파일 이름을 리스트에 추가
	        	    }
	        	}


	         String help_img = String.join(",", fileNames1);
	         tdao.contactinsert(member_id, help_category, help_title, help_content, help_img, help_password);

	      } catch (Exception e) {
	         insertval = "fail";
	         e.printStackTrace();
	      }

	      return insertval;
	   }

	@PostMapping("/contactupate")
	@ResponseBody
	public String doupdate(HttpServletRequest req,
			@RequestParam(value = "help_img", required = false) MultipartFile[] imgs,
			@RequestParam("help_seq") int help_seq, @RequestParam("help_category") String help_category,
			@RequestParam("help_title") String help_title, @RequestParam("help_content") String help_content,
			@RequestParam("help_password") String help_password) {

		String updateval = "ok";

		try {
		    HttpSession session = req.getSession();
		    String member_id = (String) session.getAttribute("id");

		    List<String> fileNames1 = new ArrayList<>();
		    String filePath = req.getServletContext().getRealPath("/img/contact/");
		    File folder = new File(filePath);
		    if (!folder.exists()) {
		        folder.mkdirs();
		    }
		    boolean fileExists = false;
		    for (MultipartFile file : imgs) {
		        if (!file.isEmpty()) {
		            fileExists = true;
		            String fileName = file.getOriginalFilename();
		            String filePathAndName = filePath + fileName;
		            File newFile = new File(filePathAndName);
		            file.transferTo(newFile);
		            fileNames1.add(fileName);  // 파일 이름을 리스트에 추가
		        }
		    }
		    
		    String help_img = fileExists ? String.join(",", fileNames1) : req.getParameter("old_img");
		    tdao.contactupdate(help_seq, help_category, help_title, help_content, help_img, help_password);

		} catch (Exception e) {
			updateval = "fail";
			e.printStackTrace();
		}
		return updateval;
	}

//	고객센터 상세페이지
	@GetMapping("/contactdetail/{help_seq}")
	public String mypagereviewwrite(@PathVariable("help_seq") int help_seq, HttpServletRequest req, Model model) {

		contactDTO detail = tdao.contactdetail(help_seq);
		model.addAttribute("details", detail);
		return "contactdetail";

	}

//게시판 공개글/비밀글 체크
	@PostMapping("/sortPost")
	@ResponseBody
	public String doSortPost(HttpServletRequest req) {

		int post_seq = Integer.parseInt(req.getParameter("post_seq"));

		Object password = tdao.sortPost(post_seq);

		if (password == null) {
			return "open";
		} else {
			return "secret";
		}

	}

// 고객센터 상세페이지 - 비밀번호 입력get   
	@PostMapping("/pwdselect/{help_seq}")
	@ResponseBody
	public ResponseEntity<String> pwdSelect(@PathVariable("help_seq") int help_seq, HttpServletRequest req) {

		String getpwd = tdao.pwdselect(help_seq);
		String putpwd = req.getParameter("help_password");

		if (getpwd.equals(putpwd)) {
			return new ResponseEntity<>("contactdetail", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("invalid password", HttpStatus.OK);
		}
	}

// 고객센터 게시글 수정  
	@GetMapping("/updatelist/{help_seq}")
	public String updatelist(@PathVariable("help_seq") int help_seq, HttpServletRequest req, Model model) {

		contactDTO updatelist = tdao.contactdetail(help_seq);
		model.addAttribute("updatelists", updatelist);

		return "contactupdate";

	}

// 고객센터 게시글 삭제   
	@PostMapping("/contactdelete")
	@ResponseBody
	public String reviewdelete(HttpServletRequest req) {
		int help_seq = Integer.parseInt(req.getParameter("help_seq"));

		tdao.contactdelete(help_seq);

		return "contact";
	}
	
	
	
/////////////////////////////////현준///////////////////////////////////////
	@GetMapping("/schedulecreate/{cityNum}/{cityName}")
	public String schedulecreate(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String UserId = (String)session.getAttribute("id");
		if(UserId =="" || UserId == null){			
			return "redirect:/";
		}else {
			return "schedulecreate";
		}
	}
	
	@GetMapping("/scheduleupdate/{cityNum}/{cityName}/{scheduleNum}")
	public String scheduleupdate(HttpServletRequest req) {
		return "scheduleupdate";
	}
	
	@GetMapping("/scheduleai")
	public String scheduleai() {
		return "scheduleAi";
	}

	// 맵을 그리는 컨트롤러
	@PostMapping("/mapCreate")
	@ResponseBody
	public String mapCreate(HttpServletRequest req) {
		int city = Integer.parseInt(req.getParameter("city"));

		ArrayList<ScheduleCreateDTO> mc = new ArrayList<ScheduleCreateDTO>();

		mc = tdao.mapCreate(city);

		JSONArray ja = new JSONArray();
		for ( int i = 0; i < mc.size(); i++ ) {
			JSONObject jo = new JSONObject();

			jo.put("lat",mc.get(i).city_lat);
			jo.put("lng",mc.get(i).city_lng);

			ja.put(jo);
		}
		return ja.toString();
	}
	
	//업체를 불러오는 컨트롤러
	@PostMapping("/placeList")
	@ResponseBody
	public String placeList(HttpServletRequest req) {
		int city = Integer.parseInt(req.getParameter("city"));
		int currentP = Integer.parseInt(req.getParameter("currentP"));
		String pSeq = req.getParameter("pSeq");
		String pCategory = req.getParameter("pCategory");

		ArrayList<ScheduleCreateDTO> pl = new ArrayList<ScheduleCreateDTO>();

		if ( pCategory.equals("1") ) {
			pCategory = "5,6";
		}
		else if ( pCategory.equals("2") ){
			pCategory = "1,2,3,4";
		}
		
		if (pSeq == "") {
			pl = tdao.allPlaceList(city,currentP,pCategory);
		}
		else {
			pl = tdao.scheduleAddPlaceList(city,currentP,pSeq,pCategory);
		}

		JSONArray ja = new JSONArray();

		for ( int i = 0; i < pl.size(); i++ ) {

			JSONObject jo = new JSONObject();
			
			jo.put("seq",pl.get(i).place_seq);
			jo.put("name",pl.get(i).place_name);
			jo.put("img",pl.get(i).place_img);

			ja.put(jo);
		}
		return ja.toString();
	}

	//마커를 그리기 위한 컨트롤러
	@PostMapping("/markerScheduleCreate")
	@ResponseBody
	public String markerScheduleCreate(HttpServletRequest req) {
		String pSeq = req.getParameter("pSeq");

		ArrayList<ScheduleCreateDTO> msc = new ArrayList<ScheduleCreateDTO>();
		
		msc = tdao.markerScheduleCreate(pSeq);
		
		JSONArray ja = new JSONArray();
		for ( int i = 0; i < msc.size(); i++ ) {
			JSONObject jo = new JSONObject();
			
			jo.put("seq", msc.get(i).place_seq);
			jo.put("lat",msc.get(i).place_lat);
			jo.put("lng",msc.get(i).place_lng);
			
			ja.put(jo);
		}
		return ja.toString();
	}

	//업체 개수를 체크하는 컨트롤러
	@PostMapping("/placeListCount")
	@ResponseBody
	public String placeListCount(HttpServletRequest req) {
		int city = Integer.parseInt(req.getParameter("city"));
		String pSeq = req.getParameter("pSeq");
		String pCategory = req.getParameter("pCategory");
		int a_count = 0;

		if ( pCategory.equals("1") ) {
			pCategory = "5,6";
		}
		else if ( pCategory.equals("2") ){
			pCategory = "1,2,3,4";
		}
		
		if ( pSeq == "" || pSeq == null ) {
			a_count = tdao.placeListAllCount(city,pCategory);
		}
		else {
			a_count = tdao.placeListCount(city,pSeq,pCategory);
		}
			
		return String.valueOf(a_count);
	}

	//검색을 위한 컨트롤러
	@PostMapping("/placeSearch")
	@ResponseBody
	public String placeSearch(HttpServletRequest req) {
		int city = Integer.parseInt(req.getParameter("city"));
		String bigCategory = req.getParameter("bigCategory");
		String pSeq = req.getParameter("pSeq");
		String searchText = req.getParameter("search");
		int currentP = Integer.parseInt(req.getParameter("currentP"));
		
		ArrayList<ScheduleCreateDTO> search = new ArrayList<ScheduleCreateDTO>();
		
		if ( pSeq == "" ) {
			search = tdao.placeSearchNull(city,bigCategory,searchText,currentP);
		}
		else {
			search = tdao.placeSearch(city,bigCategory,pSeq,searchText,currentP);
		}


		JSONArray ja = new JSONArray();
		for ( int i = 0; i < search.size(); i++ ){
			JSONObject jo = new JSONObject();
			jo.put("seq",search.get(i).place_seq);
			jo.put("name",search.get(i).place_name);
			jo.put("img",search.get(i).place_img);

			ja.put(jo);
			}
			return ja.toString();
	}

	//업체정보를 위한 컨트롤러
	@PostMapping("/placeInfo")
	@ResponseBody
	public String placeInfo(HttpServletRequest req) {
		int placeInfoId = Integer.parseInt(req.getParameter("placeInfoId"));

		ArrayList<ScheduleCreateDTO> placeInfo = new ArrayList<ScheduleCreateDTO>();

		placeInfo = tdao.placeInfo(placeInfoId);
		
		JSONArray ja = new JSONArray();
		for ( int i = 0; i < placeInfo.size(); i++ ){
			JSONObject jo = new JSONObject();

			jo.put("name",placeInfo.get(i).place_name);
			jo.put("address",placeInfo.get(i).place_address);
			jo.put("tel",placeInfo.get(i).place_tel);
			jo.put("content",placeInfo.get(i).place_content);
			jo.put("img",placeInfo.get(i).place_img);
			jo.put("open",placeInfo.get(i).place_open);

			ja.put(jo);
		}
		return ja.toString();
	}
	
	@PostMapping("/modalSaveButton")
	@ResponseBody
	public String modalSaveButton(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String UserId = (String)session.getAttribute("id");
		int city = Integer.parseInt(req.getParameter("city"));
		String sData = req.getParameter("sData");
		String sDays = req.getParameter("sDays");
		int siteNum = Integer.parseInt(req.getParameter("siteNum"));
		int scheduleSeq = Integer.parseInt(req.getParameter("scheduleSeq"));
		String check = "true";

		int UserSeq = tdao.UserSeq(UserId);
		tdao.cityCountInc(city);			
		
		if ( siteNum == 1 ) {
			tdao.scheduleInsert(city,UserSeq,sData,sDays);
		}
		else if ( siteNum == 2 ) {
			tdao.scheduleUpdata(scheduleSeq,city,UserSeq,sData,sDays);
		}
		else {
			check = "false";
		}
		
		
		return check;
	}
	
	@PostMapping("/scheduleupdateimport")
	@ResponseBody
	public String scheduleupdateimport(HttpServletRequest req) {
		HttpSession session = req.getSession();
		String UserId = (String)session.getAttribute("id");
		int scheduleSeq = Integer.parseInt(req.getParameter("scheduleSeq"));
		
		int UserSeq = tdao.UserSeq(UserId);
		
		ScheduleDTO scheduleData = new ScheduleDTO();
		scheduleData = tdao.scheduleData(UserSeq,scheduleSeq);
		
		String[] scheduleDataSP1 = scheduleData.getSchedule_data().split("-");
		
		ArrayList<ScheduleCreateDTO> placeArray = new ArrayList<ScheduleCreateDTO>();
		
		JSONArray ja = new JSONArray();
		for ( int i = 0; i < scheduleDataSP1.length; i++ ) {
			String[] scheduleDataSP2 = scheduleDataSP1[i].split("/");
			String[] scheduleUpdatePlaceSeq = scheduleDataSP2[1].split(",");
			for ( int j = 0; j < scheduleUpdatePlaceSeq.length; j++ ) {
				placeArray = tdao.scheduleUpdateDataList(scheduleUpdatePlaceSeq[j]);
				
				for ( int k = 0; k < placeArray.size(); k++ ){
					JSONObject jo1 = new JSONObject();
					
					jo1.put("seq",placeArray.get(k).place_seq);
					jo1.put("category",placeArray.get(k).place_category);
					jo1.put("name",placeArray.get(k).place_name);
					jo1.put("img",placeArray.get(k).place_img);
					
					ja.put(jo1);
				}
			}
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("days",scheduleData.getSchedule_days());
		ja.put(jo2);
	return ja.toString();
	}
	
}
