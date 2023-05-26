package com.BoardMaria.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.BoardMaria.dto.AddressVO;
import com.BoardMaria.dto.FileVO;
import com.BoardMaria.dto.UserVO;
import com.BoardMaria.service.BoardService;
import com.BoardMaria.service.UserService;
import com.BoardMaria.util.Page;

@Controller
public class UserController {

	Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired // 비밀번호 암호화 이존성 주입
	private BCryptPasswordEncoder pwdEncoder;

	@Autowired // mapper 인터페이스 의존성 주입
	UserService service;
	
	@Autowired
	BoardService boardService;

	@GetMapping("/user/signup") // 회원가입 등록 (화면 보기)
	public void getsignup() throws Exception {
	}

	@ResponseBody
	@PostMapping("/user/signup") // 회원가입 등록
	public String postSignup(UserVO user, @RequestParam("fileUpload") MultipartFile mpr, HttpSession session)
			throws Exception {
			
			String path = "c:\\Repository\\profile\\";
			String org_filename = "";
			long filesize = 0L;
			
			if (!mpr.isEmpty()) {
			File targetFile = null;

			org_filename = mpr.getOriginalFilename();
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));
			String stored_filename = UUID.randomUUID().toString().replaceFirst("-", "") + org_fileExtension;
			filesize = mpr.getSize();
			targetFile = new File(path + stored_filename);
			mpr.transferTo(targetFile); // row data를 targetFile에서 가진 정보대로 변환

			user.setOrg_filename(org_filename);
			user.setStored_filename(stored_filename);
			user.setFilesize(filesize);

			}

			user.setPassword(pwdEncoder.encode(user.getPassword()));
			service.signup(user);
	
			session.setAttribute("userid", user.getUserid());
			session.setAttribute("usename", user.getUsername());
	
			return "{\"username\":\"" + URLEncoder.encode(user.getUsername(), "UTF-8") + "\",\"status\":\"good\"}";
			// {"username": "김철수", "status":"good"}
		}

		// 가입정보 가져오기
	@GetMapping("/user/userinfo")
	public void gerMemberInfoView(Model model, HttpSession session) {

		String userid = (String) session.getAttribute("userid");
		UserVO user = service.userInfoView(userid);
		UserVO user_date = service.login(userid);

		model.addAttribute("user", user);
		model.addAttribute("user_date", user_date);

		}

		// 사용자 정보 수정 보기
	@GetMapping("/user/userInfoModify")
	public void getMemberInfoModify(Model model, HttpSession session) {

		String userid = (String)session.getAttribute("userid");
		UserVO user = service.userInfoView(userid);
		UserVO user_date = service.login(userid);
		model.addAttribute("user", user);
		model.addAttribute("user_date", user_date);
			
		}
		
	//사용자 정보 수정
	@PostMapping("/user/userInfoModify")
	public String postMemberInfoModify(UserVO user,
			@RequestParam("fileUpload") MultipartFile multipartFile ) {
		
		String path = "c:\\Repository\\profile\\";
		File targetFile;
		
		if(!multipartFile.isEmpty()) {

			//기존 프로파일 이미지 삭제
			UserVO vo = new UserVO();
			vo = service.userInfoView(user.getUserid());
			File file = new File(path + vo.getStored_filename());
			file.delete();
			
			String org_filename = multipartFile.getOriginalFilename();	
			String org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));	
			String stored_filename =  UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;	
							
			try {
				targetFile = new File(path + stored_filename);
				
				multipartFile.transferTo(targetFile);
				
				user.setOrg_filename(org_filename);
				user.setStored_filename(stored_filename);
				user.setFilesize(multipartFile.getSize());
																			
			} catch (Exception e ) { e.printStackTrace(); }
				
		}	

		service.userInfoUpdate(user);

		return "redirect:/user/userinfo";
			
		}

		//기존에 존재하는 아이디와 일치하는지 확인
	@ResponseBody
	@PostMapping("/user/idCheck")
	public int getIdCheck(@RequestBody String userid) {
		return service.idCheck(userid);
	}

	// 로그인
	@GetMapping("/user/login")
	public void getLogIn() {
	}

	@ResponseBody
	@PostMapping("/user/login") // 로그인 검사
	public String loginCheck(UserVO loginData, HttpSession session, @RequestParam("autologin") String autologin)
			throws Exception {
		String authkey = "";
		
		//로그인 시 자동 로그인 체크할 경우 신규 authkey로 이동
		if(autologin.equals("NEW")) {
			UserVO loginInfo = service.login(loginData.getUserid());
			authkey = UUID.randomUUID().toString().replaceAll("-", "");
			loginData.setAuthkey(authkey);
			service.authkeyUpdate(loginData);
		}
		//authkey가 클라이언트에 쿠키로 존재할 경우 로그인 과정없이 세션 생성 후 게시판 목록 페이지로 이동
		if(autologin.equals("PASS")) {
			UserVO userinfo = service.userinfoByAuthkey(loginData.getAuthkey());
			if(userinfo != null) {
				
				//세션 생성
				session.setMaxInactiveInterval(3600 * 24 * 7);
				  session.setAttribute("userid", userinfo.getUserid());
					session.setAttribute("username", userinfo.getUsername());
					session.setAttribute("role", userinfo.getRole());
					
					return "{\"message\":\"good\"}";
					
			}else {
				return "{\"message\":\"bad\"}";
			}
		}
		
		// 아이디 존재 여부 확인
		if(service.idCheck(loginData.getUserid()) == 0) {
			return "{\"message\":\"ID_NOT_FOUND\"}";
		}
		  //아이디가 존재하면 읽어온 userid로 로그인 정보 가져 오기
		  UserVO user = service.login(loginData.getUserid());
		  //패스워드 확인 
		  if (!pwdEncoder.matches(loginData.getPassword(), user.getPassword())) {
			  return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		  }else {//패스워드가 존재하면
				service.logindateUpdate(loginData.getUserid());
				
				session.setMaxInactiveInterval(3600 * 24 * 7);
				session.setAttribute("userid", user.getUserid());
				session.setAttribute("username", user.getUsername());
				session.setAttribute("role", user.getRole());
				//로그인 날짜 등록
				
				//패스워드 변경 후 30일이 경과했는지 확인
				UserVO pwcheck = new UserVO();
				pwcheck = service.pwcheck(loginData.getUserid());
				
				if(pwcheck.getPwdiff() > (30*pwcheck.getPwcheck())) {
					return "{\"message\":\"bad\"}";
				}else {
					return "{\"message\":\"good\",\"authkey\":\"" + user.getAuthkey() +"\"}";
				}
			
		  }
		  	
		 
			/*
			 * try { if (pwdEncoder.matches(userVo.getPassword(), userInfo.getPassword())) {
			 * session.setAttribute("userid", userInfo.getUserid());
			 * session.setAttribute("username", userInfo.getUsername());
			 * 
			 * session.setMaxInactiveInterval(3600 * 24 * 7);
			 * System.out.println("userInfo.getUserid() = " + userInfo.getUserid());
			 * System.out.println("userInfo.getUsername() =" + userInfo.getUsername());
			 * 
			 * return "redirect:/board/list?page=1"; } else {
			 * 
			 * System.out.println("아이디가 존재하지 않습니다."); return "/user/message"; }
			 * 
			 * } catch (Exception e) { System.out.println("접근이 올바르지 않습니다."); return
			 * "/user/message"; }
			 */
		    
	}

	//패스워드 변경 안내 공지
	@GetMapping("/user/pwCheckNotice")
	public void getPwCheckNotice() { }
	
	// 패스워드 30일 이후에 변경 공지 나오도록 pwchek 값 변경
	@GetMapping("/user/userPasswordModifyAfter30")
	public String postMemberPasswordAfter30(HttpSession session) {

		service.userPasswordModifyAfter30((String) session.getAttribute("userid"));
		return "redirect:/board/list?page=1";
	}
	
	//사용자 패스워드 변경 보기
	@GetMapping("/user/userPasswordModify")
	public void getMemberPasswordModify() { }
	
	// 사용자 패스워드 변경
	@PostMapping("/user/userPasswordModify")
	public String postMemberPasswordModify(@RequestParam("old_userpassword") String old_password,
			@RequestParam("new_userpassword") String new_password, HttpSession session) {

		String userid = (String) session.getAttribute("userid");

		UserVO user = service.userInfoView(userid);
		if (pwdEncoder.matches(old_password, user.getPassword())) {
			user.setPassword(pwdEncoder.encode(new_password));
			service.passwordUpdate(user);
		}
		return "redirect:/logout";
	}

	// 사용자 아이디 찾기 보기
	@GetMapping("/user/searchID")
	public void getSearchID() {
	}

	// 사용자 아이디 찾기
	@PostMapping("/user/searchID")
	public String postSearchID(UserVO user, RedirectAttributes rttr) {

		String userid = service.searchID(user);

		// 조건에 해당하는 사용자가 아닐 경우
		if (userid == null) {
			rttr.addFlashAttribute("msg", "ID_NOT_FOUND");
			return "redirect:/user/searchID";
		}

		return "redirect:/user/IDView?userid=" + userid;
	}

	//찾은 아이디 보기
	@GetMapping("/user/IDView")
	public void postSearchID(@RequestParam("userid") String userid, Model model) {
			
		model.addAttribute("userid", userid);
			
		}
		
		//사용자 패스워드 임시 발급 보기
	@GetMapping("/user/searchPassword")
	public void getSearchPassword() { } 
		
		
		//사용자 패스워드 임시 발급
	@PostMapping("/user/searchPassword")
	public String postSearchPassword(UserVO user, RedirectAttributes rttr) { 
			
		if(service.searchPassword(user)==0) {
				
			rttr.addFlashAttribute("msg", "PASSWORD_NOT_FOUND");
			return "redirect:/user/searchPassword"; 
			}
			
			//숫자 + 영문대소문자 7자리 임시패스워드 생성
			StringBuffer tempPW = new StringBuffer();
			Random rnd = new Random();
			for (int i = 0; i < 7; i++) {
			    int rIndex = rnd.nextInt(3);
			    switch (rIndex) {
			    case 0:
			        // a-z : 아스키코드 97~122
			    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 97));
			        break;
			    case 1:
			        // A-Z : 아스키코드 65~122
			    	tempPW.append((char) ((int) (rnd.nextInt(26)) + 65));
			        break;
			    case 2:
			        // 0-9
			    	tempPW.append((rnd.nextInt(10)));
			        break;
			    }
			}
			
			user.setPassword(pwdEncoder.encode(tempPW));
			service.passwordUpdate(user);
				
			return "redirect:/user/tempPWView?password=" + tempPW;
		} 
		
	//발급한 임시패스워드 보기
	@GetMapping("/user/tempPWView")
	public void getTempPWView(Model model, @RequestParam("password") String password) {
			model.addAttribute("password", password);
			
		}

	// 로그아웃
	@GetMapping("/logout")
	public String getLogout(HttpSession session, Model model) {
		
		String userid = (String)session.getAttribute("userid");
		String username = (String)session.getAttribute("username");
		
		//로그 아웃 날짜 등록
		service.logoutUpdate(userid);
		
		model.addAttribute("userid", userid);
		model.addAttribute("username", username);
		
		session.invalidate(); // 모든 세션 종료
		return "redirect:/";

		/*
		 * @PostMapping("/user/loginPage") //로그인 검사 public String loginCheck(UserVO
		 * user,@RequestParam("userid")String userid,@RequestParam("password")String
		 * password) throws Exception {
		 * 
		 * user = mapper.login(userid);
		 * 
		 * if(pwdEncoder.matches(password, user.getPassword())) {
		 * System.out.println(user.getUserid()); System.out.println(user.getPassword());
		 * 
		 * return "redirect:/board/list"; } else {
		 * System.out.println("아이디가 존재하지 않습니다."); return "redirect:/"; } }
		 */

	}
	
	//회원탈퇴
	@GetMapping("/user/userInfoDelete")
	public String getDeleteMember(HttpSession session) throws Exception {
		
		String userid = (String)session.getAttribute("userid"); 
		
		String path_profile = "c:\\Repository\\profile\\";
		String path_file = "c:\\Repository\\file\\";
		
		//회원 프로필 사진 삭제
		UserVO user = new UserVO();
		user = service.userInfoView(userid);		
		File file = new File(path_profile + user.getStored_filename());
		file.delete();
		
		//회원이 업로드한 파일 삭제
		List<FileVO> fileList = boardService.fileInfoByUserid(userid);
		for(FileVO vo:fileList) {
			File f = new File(path_file + vo.getStored_filename());
			f.delete();
		}
		
		//게시물,댓글,파일업로드 정보, 회원정보 전체 삭제
		service.userInfoDelete((String)session.getAttribute("userid"));
		
		session.invalidate();
		
		return "redirect:/user/login";
	}

	// 우편번호 검색
	@GetMapping("/user/addrSearch")
	public void getSearchAddr(@RequestParam("addrSearch") String addrSearch, @RequestParam("page") int pageNum,
			Model model) throws Exception {

		int postNum = 10;
		int startPoint = (pageNum - 1) * postNum + 1; // 테이블에서 읽어 올 행의 위치
		int listCount = 10;

		Page page = new Page();

		int totalCount = service.addrTotalCount(addrSearch);
		List<AddressVO> list = new ArrayList<>();
		list = service.addrSearch(startPoint, postNum, addrSearch);

		model.addAttribute("list", list);
		model.addAttribute("pageListView", page.getPageAddress(pageNum, postNum, listCount, totalCount, addrSearch));

	}
}
