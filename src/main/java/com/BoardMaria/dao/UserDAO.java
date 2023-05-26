package com.BoardMaria.dao;

import java.util.List;

import com.BoardMaria.dto.AddressVO;
import com.BoardMaria.dto.UserVO;

public interface UserDAO {
	//아이디 중복체크 
		public int idCheck(String userid);
		
		//로그인 정보 가져오기
		public UserVO login(String userid);
		
		//사용자 등록
		public void signup(UserVO user);
		
		//사용자 아이디 찾기
		public String searchID(UserVO user);
		
		//마지막 로그인 시간 등록
		public void logindateUpdate(String userid);
		
		//로그아웃 날짜 업데이트
		public void logoutUpdate(String userid);
		
		//패스워드 변경 후 30일 경과 확인
		public UserVO pwcheck(String userid);
		
		//30일 이후에 패스워드 변경하도록 pwcheck 값 변경
		public void userPasswordModifyAfter30(String userid);
		
		//사용자 패스워드 신규 발급을 위한 확인
		public int searchPassword(UserVO user);
		
		//패스워드 수정
		public void passwordUpdate(UserVO user);
		
		//사용자 정보 보기
		public UserVO userInfoView(String userid);
		
		//사용자 정보 수정
		public void userInfoUpdate(UserVO user);
		
		//자동 로그인
		public void authkeyUpdate(UserVO user);
		
		//자동 로그인
		public UserVO userinfoByAuthkey(String authkey);
		
		//주소 전체 갯수 계산
		public int addrTotalCount(String addrSearch);

		//주소 검색
		public List<AddressVO> addrSearch(int startPoint, int postNum, String addrSearch);
		
		//회원 탈퇴
		public void userInfoDelete(String userid);

}
