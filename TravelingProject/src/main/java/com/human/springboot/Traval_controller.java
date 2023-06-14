package com.human.springboot;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class Traval_controller {
	
	@Autowired
	private TDAO tdao;
	@Autowired
	private EmailService emailService; // EmailService 주입
	
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
			
			System.out.println(id+"/"+pw+"/"+name+"/"+nickName+"/"+mobile+"/"+email+"/"+birth+"/"+gender);
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
			System.out.println(memberClass);
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
	@PostMapping("/findId")
	@ResponseBody
	public String findId(HttpServletRequest req) {
		String findId=null;
		try {
			String id = req.getParameter("id");
			String Email = req.getParameter("mail");
			String memberId = tdao.member_id(id, Email);
			System.out.println(memberId);
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
	@PostMapping("/changeMemberPw")
	@ResponseBody
	public String changeMemberPw(HttpServletRequest req) {
		String val="";
		try {
			String newPw = req.getParameter("newPw");
			String chkNewPw = req.getParameter("chkNewPw");
			String id = req.getParameter("id");
			System.out.println(newPw);
			System.out.println(chkNewPw);
			System.out.println(id);
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

	@GetMapping("/main")
	public String loadMain(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
			model.addAttribute("id",session.getAttribute("id"));
			model.addAttribute("memberClass",session.getAttribute("memberClass"));
		return "main";
	}
	@GetMapping("/changePw")
	public String changePw(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		model.addAttribute("id",session.getAttribute("id"));
		return "changePw";
	}
	@GetMapping("/tourlist")
	public String reviewPage() {
		return "reviews";
	}
	@PostMapping("/loadReview")
	@ResponseBody
	public String loadReviews(HttpServletRequest req) {
		int pageNum = Integer.parseInt(req.getParameter("pageNum"));
		ArrayList <RevDTO> alPlace = tdao.placeList();
		int howmanyPlace = alPlace.size();
		int howmanyPages = (howmanyPlace/12)+1;
		if (howmanyPlace%12==0) {howmanyPages = howmanyPages-1;}
		JSONArray ja = new JSONArray();
		int start = (pageNum - 1)*12;
		int end = (pageNum * 12)-1;
		try {
			JSONObject jo = new JSONObject();
			jo.put("howmany",howmanyPages);
			ja.put(jo);
			for(int i=start; i<=end; i++) {
				if(i>=start && i<=end) {
					jo = new JSONObject();
					jo.put("placeId",alPlace.get(i).getPlace_seq());
					jo.put("placeImg",alPlace.get(i).getPlace_img());
					jo.put("placeName", alPlace.get(i).getPlace_name());
					jo.put("placeAddress", alPlace.get(i).getPlace_address());
					
					ja.put(jo);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ja.toString();
	}
	
}
