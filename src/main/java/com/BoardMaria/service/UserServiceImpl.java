package com.BoardMaria.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.BoardMaria.dao.UserDAO;
import com.BoardMaria.dto.AddressVO;
import com.BoardMaria.dto.UserVO;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserDAO dao;
	
	//아이디 중복확인
	@Override
	public int idCheck(String userid) {
		
		return dao.idCheck(userid);
	}
	
	//마지막 로그인 날짜 등록/수정
	@Override
	public void logindateUpdate(String userid) {
		dao.logindateUpdate(userid);
	}
	
	// 사용자 정보 보기
	@Override
	public UserVO userInfoView(String userid) {
		return dao.userInfoView(userid);
	}

	// 사용자 정보 수정
	@Override
	public void userInfoUpdate(UserVO user) {
		dao.userInfoUpdate(user);
	}

	
	// 로그 아웃 날짜 등록
	@Override
	public void logoutUpdate(String userid) {
		dao.logoutUpdate(userid);
	}

	//로그인
	@Override
	public UserVO login(String userid) {
		return dao.login(userid);
	}

	//회원가입
	@Override
	public void signup(UserVO user) {
		dao.signup(user);
	}
	
	// 사용자 아이디 찾기
	@Override
	public String searchID(UserVO user) {
		return dao.searchID(user);
	}
	
	//패스워드 변경 후 30일 경과 확인
	@Override
	public UserVO pwcheck(String userid) {
		return dao.pwcheck(userid);
	}
	
	//30일 이후에 패스워드 변경하도록 pwcheck 값 변경
	@Override
	public void userPasswordModifyAfter30(String userid) {
		dao.userPasswordModifyAfter30(userid);
	}
	

	// 사용자 패스워드 신규 발급을 위한 확인
	@Override
	public int searchPassword(UserVO user) {
		return dao.searchPassword(user);
	}

	// 사용자 패스워드 변경
	@Override
	public void passwordUpdate(UserVO user) {
		dao.passwordUpdate(user);
	}

	//주소 전체 갯수 계산
	public int addrTotalCount(String addrSearch) {
		return dao.addrTotalCount(addrSearch);
	}

	//주소 검색
	public List<AddressVO> addrSearch(int startPoint, int postNum, String addrSearch){
		return dao.addrSearch(startPoint, postNum, addrSearch);
	}
	
	//자동 로그인
	@Override
	public void authkeyUpdate(UserVO user) {
		dao.authkeyUpdate(user);
	}
				
	
	//자동 로그인
	@Override
	public UserVO userinfoByAuthkey(String authkey) {
		return dao.userinfoByAuthkey(authkey);
	}
	
	//회원 탈퇴
	@Override
	public void userInfoDelete(String userid) {
		dao.userInfoDelete(userid);		
	}

}
