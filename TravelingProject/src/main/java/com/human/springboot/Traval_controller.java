package com.human.springboot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
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
	    System.out.println(place_seq);
	    place_seq = place_seq.replaceFirst(",", "");
	    place_seq = place_seq.replace("on,", "");
	    String[] placeSeqArray = place_seq.split(",");
	    System.out.println(Arrays.toString(placeSeqArray));
	    
	    for (String seq : placeSeqArray) {
	        int placeSeq = Integer.parseInt(seq);
	        tdao.place_delete(placeSeq);
	        System.out.println(placeSeq);
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
	    
	    String place_address = postcode + "," + address + "," + detailAddress;

	    String[] fileNames = req.getParameterValues("imageNames[]");
	    System.out.println(Arrays.toString(fileNames));
	    
	    String[] imageUrls;
	    if (fileNames != null) {
	        imageUrls = new String[fileNames.length];
	        for (int i = 0; i < fileNames.length; i++) {
	            imageUrls[i] = "/img/place/" + fileNames[i];
	        }
	    } else {
	        imageUrls = new String[]{""};
	    }
	    String img = String.join(",", imageUrls);

	    tdao.place_insert(city, place, name, place_address, tel, open, content, img);
	    return "manage_place";
	}

	
	//이미지 업로드
	@PostMapping("/upload_image")
	public String uploadImage(@RequestParam("images") MultipartFile[] images) {
	    try {
	        for (MultipartFile image : images) {
	            String originalFilename = image.getOriginalFilename();
	  
	            String filePath = "C:/Users/admin/eclipse-workspace/Final_project/src/main/resources/static/place/" + originalFilename; 
	            File dest = new File(filePath);
	            image.transferTo(dest);
	            System.out.println(dest);
	        }
	        return "manage_place"; 
	    } catch (IOException e) {
	    	
	        e.printStackTrace();
	        for (MultipartFile image : images) {
	            String originalFilename = image.getOriginalFilename();
	            String filePath = "C:/Users/admin/eclipse-workspace/Final_project/src/main/resources/static/place/" + originalFilename; 
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
	    
	    String place_address = postcode + "," + address + "," + detailAddress;

	    String[] fileNames = req.getParameterValues("imageNames[]");
	    System.out.println(Arrays.toString(fileNames));
	    
	    String[] imageUrls;
	    if (fileNames != null) {
	        imageUrls = new String[fileNames.length];
	        for (int i = 0; i < fileNames.length; i++) {
	            imageUrls[i] = "/img/place/" + fileNames[i];
	        }
	    } else {
	        imageUrls = new String[]{""};
	    }
	    String img = String.join(",", imageUrls);

	    tdao.place_update(city, place, name, place_address, tel, open, content, img, seq);
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
		//System.out.println(comment);
		//System.out.println(help_seq);
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
}
