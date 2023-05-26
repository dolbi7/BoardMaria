package com.BoardMaria.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.BoardMaria.dto.AddressVO;
import com.BoardMaria.dto.UserVO;


@Repository
public class UserDAOImpl implements UserDAO {
	
	@Autowired //의존성 주입을 통해 스프링 빈을 가져와서 사용함.
	private SqlSession sql;
	
	private static String namespace = "com.BoardMaria.mappers.User";
	

	//아이디 중복체크
	@Override
	public int idCheck(String userid) {
		
	
		
		return sql.selectOne(namespace + ".idCheck", userid);
	}
	//로그인 정보 가져오기
	@Override
	public UserVO login(String userid) {
		
		return sql.selectOne(namespace + ".login", userid);
	}
	//회원가입
	@Override
	public void signup(UserVO user) {
		sql.insert(namespace + ".signup", user);
		
	}
	
	// 사용자 아이디 찾기
	@Override
	public String searchID(UserVO user) {
		return sql.selectOne(namespace + ".searchID", user);
	}

	// 사용자 패스워드 변경
	@Override
	public void passwordUpdate(UserVO user) {
		sql.update(namespace + ".passwordUpdate", user);
	}

	// 사용자 패스워드 신규 발급을 위한 확인
	@Override
	public int searchPassword(UserVO user) {
		return sql.selectOne(namespace + ".searchPassword", user);
	}
	
	// 패스워드 변경 후 30일 경과 확인
	@Override
	public UserVO pwcheck(String userid) {
		return sql.selectOne(namespace + ".pwcheck", userid);
	}

	// 30일 이후에 패스워드 변경하도록 pwcheck 값 변경
	@Override
	public void userPasswordModifyAfter30(String userid) {
		sql.update(namespace + ".userPasswordModifyAfter30", userid);
	}
	
	// 로그인 시 마지막 로그인 날짜 등록
	@Override
	public void logindateUpdate(String userid) {
		sql.update(namespace + ".logindateUpdate", userid);
	}

	// 로그 아웃 날짜 등록
	@Override
	public void logoutUpdate(String userid) {
		sql.insert(namespace + ".logoutUpdate", userid);

	}
	
	// 사용자 정보 보기
	@Override
	public UserVO userInfoView(String userid) {
		return sql.selectOne(namespace + ".userInfoView", userid);
	}

	// 사용자 정보 수정
	@Override
	public void userInfoUpdate(UserVO user) {
		sql.update(namespace + ".userInfoUpdate", user);
	}

	// 우편번호 최대 행수 계산
	@Override
	public int addrTotalCount(String addrSearch) {
		return sql.selectOne(namespace + ".addrTotalCount", addrSearch);
	}

	// 우편번호 검색
	@Override
	public List<AddressVO> addrSearch(int startPoint, int postNum, String addrSearch) {

		Map<String, Object> data = new HashMap<>();
		data.put("startPoint", startPoint);
		data.put("postNum", postNum);
		data.put("addrSearch", addrSearch);

		return sql.selectList(namespace + ".addrSearch", data);
	}

	// 자동 로그인
	@Override
	public void authkeyUpdate(UserVO user) {
		sql.update(namespace + ".authkeyUpdate", user);
	}

	@Override
	// 자동 로그인
	public UserVO userinfoByAuthkey(String authkey) {
		return sql.selectOne(namespace + ".userinfoByAuthkey", authkey);
	}
	

	//회원 탈퇴
	@Override
	public void userInfoDelete(String userid) {
		sql.delete(namespace + ".userInfoDelete", userid);
		
	}

}
