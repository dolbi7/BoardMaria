<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<style>
.bottom_menu {
	magin-top: 10px;
	
}
.bottom_menu > a:link, .bottom_menu > a:visited  {
	background-color: #FFA500;
	color:maroon;
	padding:10px 15px;
	text-align: center;
	display: inline-bolck;
	text-decoration: none;
}
.bottom_menu > a:hover, .bottom_munu > a:active{
	background-color: #1E90FF
	text-decoration: none;
}
#floatMenu {
	position: absolute;
	width: 200px;
	height: 200px;
	left: 50px;
	top: 250px;
	background-color: #606060;
	color: #fff;
}
</style>
<script>
/* function logout() {
	let result = confirm("로그아웃을 하시겠습니까?");
	if(result == true){
		document.location.href='/logout';			
	} 
} */
</script>
<meta charset="UTF-8">
<link rel="stylesheet" href="/resources/css/board.css">
<title>게시물 목록 보기</title>
<script>
	const search = () => {
		
		const keyword = document.querySelector('#keyword');
		document.location.href='/board/list?page=1&keyword=' + encodeURIComponent(keyword.value);
		
		//keyword.value = encodeURIComponent(keyword.value);
		
		
	}
	
	const logout =() => {
		if(confirm('로그아웃하시겠습니까?')) {
			
			let authkey = getCookie('authkey');
			if(authkey !== undefined) 
				document.cookie = 'authkey= '+ authkey +'; path=/; max-age=0';
				document.location.href='/logout';
			
		}
	}
	const getCookie = (name) => {
			
			const cookies = document.cookie.split(`; `).map((el) => el.split('='));
			  let getItem = [];
		
			  for (let i = 0; i < cookies.length; i++) {		    
			    if (cookies[i][0] === name) {
			      getItem.push(cookies[i][1]);
			      break;
			    }
			  }
			  if (getItem.length > 0) {
					console.log(getItem[0]);
			    	return decodeURIComponent(getItem[0]);
			  }		
		}
		
	const press = () => {
		
		if(event.keyCode==13) search(); 
	}

</script>
</head>
<body>
<%

	String userid = (String)session.getAttribute("userid");
	String role = (String)session.getAttribute("role");
	if(userid == null) response.sendRedirect("/user/login");

%>
<div>
	<img id="topBanner" src="/resources/images/logo.jpg" title="서울기술교육센터" onclick="document.location.href='/board/list?page=1'">
	<img id="floatMenu" src ="/resources/images/Judge.jpg" title="floatMenu" onclick="document.location.href='/board/list?page=1'" >
</div>

<div class="tableDiv">
	<h1>게시물 목록 보기</h1>
	<input style="width:40%; height: 30px; border: 2px solid #168; font-size:16px;"
	  type="text" name="keyword" id="keyword" placeholder="검색할 제목, 작성자 이름 및 내용을 입력해 주세요" onkeydown="press()">
	  <input style="width: 5%; height: 30px; background: #158; color:white; font-weight:bold; cursor:pointer;" 
	  type="button" value="검색" onclick="search()">
	  
	  <br><br>
	<table class="InfoTable">
		<tr>
			<th>번호</th>
			<th>제목</th>
			<th>작성자</th>
			<th>작성일</th>
			<th>조회수</th>
		</tr>
		
		<tbody>
		<c:if test="${list != null}">
			<c:forEach items="${list}" var="list">
				<tr onmouseover="this.style.background='#46D2D2'" onmouseout="this.style.background='white'">
					<td>${list.seq}</td>
					<td style="text-align:left"><a id="hypertext" href="/board/view?seqno=${list.seqno}&page=${page}&keyword=${keyword}" 
						onmouseover="this.style.textDecoration='underline'"  
						onmouseout="this.style.textDecoration='none'">${list.title}</a></td>
					<td>${list.writer}</td>
					<td>${list.regdate}</td>				
					<td>${list.hitno}</td>
				</tr>
			</c:forEach>			
		</c:if>
		<c:if test="${list == null}">
			<tr>
				<td colspan="5"> 등록된 게시물이 없습니다.</td>
			</tr>
			</c:if>
		</tbody>
	</table>
	<br>
	<div>${pageList}</div>
	<br>
	<div class="bottom_menu">
		<a href="/board/list?page=1">처음으로</a>&nbsp;&nbsp;
		<a href="/board/write">글쓰기</a>&nbsp;&nbsp;
		<a href="/user/userinfo">사용자관리</a>&nbsp;&nbsp;
		<c:if test="${role == 'MASTER'}">
			<a href="/master/sysmanage">시스템관리</a>&nbsp;&nbsp;
		</c:if>
		<a href="javascript:logout()">로그아웃</a>		
	</div>
	<br><br>
	
</div>

</body>
</html>