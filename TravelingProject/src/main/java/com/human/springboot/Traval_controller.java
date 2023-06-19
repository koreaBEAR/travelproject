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
	
	//아이디 비밀번호 찾기
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
	
	//비밀번호 변경
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

	@GetMapping("/changePw")
	public String changePw(HttpServletRequest req, Model model) {
		HttpSession session = req.getSession();
		model.addAttribute("id",session.getAttribute("id"));
		return "changePw";
	}
	
	//리뷰 피드
	@GetMapping("/place")
	public String reviewPage() {
		return "reviews";
	}
	//리뷰에 place보이기
	
	@PostMapping("/loadReview")
	@ResponseBody
	public String loadReviews(HttpServletRequest req) {
		int pageNum = Integer.parseInt(req.getParameter("pageNum"));
		ArrayList <RevDTO> alPlace = tdao.placeList();
		int howmanyPlace = alPlace.size();
		int howmanyPages = (howmanyPlace/13)+1;
		if (howmanyPlace%13==0) {howmanyPages = howmanyPages-1;}
		JSONArray ja = new JSONArray();
		int start = (pageNum - 1)*13;
		int end = (pageNum * 13)-1;
		try {
			JSONObject jo = new JSONObject();
			jo.put("howmany",howmanyPages);
			ja.put(jo);
			for(int i=start; i<end; i++) {
				if(i>=start && i<=end) {
					jo = new JSONObject();
					jo.put("placeId",alPlace.get(i).getPlace_seq());
					jo.put("placeImg",alPlace.get(i).getPlace_img());
					jo.put("placeName", alPlace.get(i).getPlace_name());					
					ja.put(jo);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return ja.toString();
	}
	
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
	public String loadPlaceInfo(HttpServletRequest req) {
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
	    int like = Integer.parseInt(req.getParameter("like"));
//	    double lat = Double.parseDouble(req.getParameter("lat"));
//	    double lng = Double.parseDouble(req.getParameter("lng"));
	    String latParam = req.getParameter("lat");
	    String lngParam = req.getParameter("lng");
	    Double lat = latParam != null && !latParam.isEmpty() ? Double.parseDouble(latParam) : 0.0;
	    Double lng = lngParam != null && !lngParam.isEmpty() ? Double.parseDouble(lngParam) : 0.0;


	    
	    String place_address = postcode + "," + address + "," + detailAddress;

	    	    
	    String[] fileNames = req.getParameterValues("imageNames[]");
	    System.out.println(Arrays.toString(fileNames));
	    
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

	
	//이미지 업로드
	@PostMapping("/upload_image")
	public String uploadImage(@RequestParam("images") MultipartFile[] images) {
	    try {
	        for (MultipartFile image : images) {
	            String originalFilename = image.getOriginalFilename();
	            String filePath = "C:/Users/admin/git/travelproject/TravelingProject/src/main/resources/static/img/place/" + originalFilename; 
	            File dest = new File(filePath);
	            image.transferTo(dest);
	            System.out.println(dest);
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

	    System.out.println(lat);
	    String place_address = postcode + "," + address + "," + detailAddress;

	    String[] fileNames = req.getParameterValues("imageNames[]");
	    System.out.println(Arrays.toString(fileNames));
	    
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
// 이미지 파일이 업로드되었는지 확인
			if (imgs != null && imgs.length > 0) {
				for (MultipartFile file : imgs) {
					if (!file.isEmpty()) {
						String fileName = file.getOriginalFilename();
						fileNames1.add(fileName);
						String filePath = "C:/Users/admin/git/travelproject/TravelingProject/src/main/resources/static/img/contact";
						File newFile = new File(filePath + fileName);
						System.out.println(newFile);
						file.transferTo(newFile);
					}
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

			List<String> fileNames1 = new ArrayList<>();
// 이미지 파일이 업로드되었는지 확인
			if (imgs != null && imgs.length > 0) {
				for (MultipartFile file : imgs) {
					if (!file.isEmpty()) {
						String fileName = file.getOriginalFilename();
						fileNames1.add(fileName);
						String filePath = "C:/Users/admin/git/travelproject/TravelingProject/src/main/resources/static/img/contact";
						File newFile = new File(filePath + fileName);
						file.transferTo(newFile);
					}
				}
			}

			String help_img = String.join(",", fileNames1);
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

		System.out.println("getpwd=" + getpwd);
		System.out.println("putpwd=" + putpwd);

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
	
	
	
///////////////현준///////////////////////
	@GetMapping("/schedulecreate")
	public String Admin() {
		return "scheduleCreate";
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

		a_count = tdao.placeListCount(city,pSeq,pCategory);

		return String.valueOf(a_count);
	}

	//검색을 위한 컨트롤러
	@PostMapping("/placeSearch")
	@ResponseBody
	public String placeSearch(HttpServletRequest req) {
		String searchText = req.getParameter("search");
		String bigCategory = req.getParameter("bigCategory");
		String pSeq = req.getParameter("pSeq");
		
		ArrayList<ScheduleCreateDTO> search = new ArrayList<ScheduleCreateDTO>();
		
		if ( pSeq == "" ) {
			search = tdao.placeSearchNull(searchText,bigCategory);
		}
		else {
			search = tdao.placeSearch(searchText,bigCategory,pSeq);
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
	
}
