<%@page import="utils.JSFunction"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
if(session.getAttribute("UserId") == null){ // 세션에서 UserId의 유무를 확인 없을 경우
	JSFunction.alertLocation("로그인 후 이용해주십시오", "../06Session/LoginForm.jsp", out); // 전에 만들어둔 모든 곳에서 사용할 alert창
	return;
}
%>
<!-- 로그인 여부를 확인하는 프로세스 ㄴ처리 -->