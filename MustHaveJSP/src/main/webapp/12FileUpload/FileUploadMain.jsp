<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!DOCTYPE html><html><head>
	
	<!-- 에러메세지 출력   -->
	<script> 
		function validateForm(form) {
			if(form.name.value == ""){
				alert("작성자를 입력하세요.");
				return false;
			}
			if(form.title.value == ""){
				alert("제목을 입력하세요.");
				return false;
			}
			if(form.attachedFile.value == ""){
				alert("첨부파일은 필수 입력입니다.");
				return false;
			}
		}
	</script>


<meta charset="UTF-8"><title>FileUploadMain.jsp</title></head><body>
	<h3>파일 업로드</h3>
	<span style="color: red;">${errorMessage }</span>
	<form name="fileFrm" method="post" enctype="multipart/form-data" 
	action="UploadProcess.jsp" onsubmit="return validateForm(this);">
	작성자 : <input type="text" name="name" value="김기원"><br/>
	제목	 : <input type="text" name="title" /> <br/>
	카테고리 : 
		<input type="checkbox" name="cate" vlaue="사진" checked />사진
		<input type="checkbox" name="cate" vlaue="과제"  />과제
		<input type="checkbox" name="cate" vlaue="워드" />워드
		<input type="checkbox" name="cate" vlaue="음원" />음원<br/>
	첨부파일 : <input type="file" name="attacheFile" /><br/>
			<input type="submit" value="전송하기">	
</body>
</html>