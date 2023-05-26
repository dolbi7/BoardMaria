<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<script>

	window.onload = () => {
		
		document.cookie = "userid=kim; path=/; expires=Sun, 31 Dec 2023 23:59:59 GMT";
		document.cookie = "username=" + encodeURIComponent("김상경")  + "; kim; path=/; expires=Sun, 31 Dec 2023 23:59:59 GMT";
				
	}  
	
	
	

</script>

<meta charset="UTF-8">
<title>로그인</title>
</head>
<body>

<div>
	<img id="topBanner" src="/resources/images/logo.jpg" title="서울기술교육센터">
</div>



<h1> 로그인 정보가 올바르지 않습니다. </h1>
<button id="btn_modify" class="btn_modify" onclick = "document.location.href='/'">뒤로가기</button>	


</body>
</html>