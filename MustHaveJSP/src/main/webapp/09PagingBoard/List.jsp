<%@page import="utils.BoardPage"%>
<%@page import="model1.BoardDAO"%>
<%@page import="model1.BoardDTO"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// DAO를 생성해 DB에 연결
	BoardDAO dao = new BoardDAO(application);
	
	// 사용자가 입력한 검색 조건을 Map에 저장
	Map<String, Object> param = new HashMap<String, Object>(); 
	String searchField = request.getParameter("searchField");
	String searchWord = request.getParameter("searchWord");
	if (searchWord != null) {
	    param.put("searchField", searchField);
	    param.put("searchWord", searchWord);
	}
	
	int totalCount = dao.selectCount(param);  // 게시물 수 확인
	
	// 전체 페이지 수 계산
	int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE")); //10
	int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK")); //5
	int totalPage = (int)Math.ceil((double)totalCount / pageSize); // 전체 페이지 수 
				       //Math.ceil(105/10) = Math.ceil(10.5) -> (전체 게시물은 105개일 때 11페이지)
	
	// 현제 페이지 확인
	int pageNum = 1; // 기본값
	String pageTemp = request.getParameter("pageNum");
	if(pageTemp != null && !pageTemp.equals("")){
		pageNum = Integer.parseInt(pageTemp); // 요청 받은 페이지로 수정
	}
	
	// 목록에 출력할 게시물 범위 계산
	int start = (pageNum - 1) * pageSize + 1; 	//첫 번째 게시물 번호 (1-1) * 10 + 1 = 1
	int end = pageNum * pageSize ; 				// 마지막 게시물 번호  1 * 10 = 10
	
	// 위에서 선언한  Map<String, Object> param = new HashMap<String, Object>()에 k,v를 삽입
	param.put("start", start);	//1
	param.put("end", end);		//10
	/* 페이지 처리 end */	
	
	
	List<BoardDTO> boardLists = dao.selectListPage(param);  // 게시물 목록 받기
	dao.close();  // DB 연결 닫기



%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원제 게시판</title>
<script>
function deletePost() {
    var confirmed = confirm("정말로 삭제하겠습니까?"); 
    if (confirmed) {
        var form = document.writeFrm;       // 이름(name)이 "writeFrm"인 폼 선택
        form.method = "post";               // 전송 방식 
        form.action = "DeleteProcess.jsp";  // 전송 경로
        form.submit();                      // 폼값 전송
    }
}
</script>
</head>
<body>
    <jsp:include page="../Common/Link.jsp" />  <!-- 공통 링크 -->

    <h2>목록 보기(List)</h2>
    <!-- 검색폼 --> 
    <form method="get">  
    <table border="1" width="90%">
    <tr>
        <td align="center">
            <select name="searchField"> 
                <option value="title">제목</option> 
                <option value="content">내용</option>
            </select>
            <input type="text" name="searchWord" />
            <input type="submit" value="검색하기" />
        </td>
    </tr>   
    </table>
    </form>
    <!-- 게시물 목록 테이블(표) --> 
    <table border="1" width="90%">
        <!-- 각 칼럼의 이름 --> 
        <tr>
            <th width="10%">번호</th>
            <th width="50%">제목</th>
            <th width="15%">작성자</th>
            <th width="10%">조회수</th>
            <th width="15%">작성일</th>
        </tr>
        <!-- 목록의 내용 --> 
<%
if (boardLists.isEmpty()) {
    // 게시물이 하나도 없을 때 
%>
        <tr>
            <td colspan="5" align="center">
                등록된 게시물이 없습니다^^*
            </td>
        </tr>
<%
}
else {
    // 게시물이 있을 때 
    int virtualNum = 0;  // 화면상에서의 게시물 번호
    int countNum = 0;
    for (BoardDTO dto : boardLists)
    {
        //virtualNum = totalCount--;  // 전체 게시물 수에서 시작해 1씩 감소
        virtualNum = totalCount - (((pageNum - 1) * pageSize) + countNum++);
        
%>
        <tr align="center">
            <td><%= virtualNum %></td>  <!--게시물 번호-->
            <td align="left">  <!--제목(+ 하이퍼링크)-->
                <a href="View.jsp?num=<%= dto.getNum() %>"><%= dto.getTitle() %></a> <!-- get방식으로 번호가 넘어감 -->
            </td>
            <td align="center"><%= dto.getId() %></td>          <!--작성자 아이디-->
            <td align="center"><%= dto.getVisitcount() %></td>  <!--조회수-->
            <td align="center"><%= dto.getPostdate() %></td>    <!--작성일-->
        </tr>
<%
    }
}
%>
    </table>
    <!--목록 하단의 [글쓰기] 버튼-->
    <table border="1" width="90%">
    	<tr align="center">
    	<!-- 페이징 처리 -->
    		<td>
    			<%= BoardPage.pagingStr(totalCount, pageSize, blockPage, pageNum, request.getRequestURI()) %>
    		</td>
    		<!-- 글쓰기 버튼 -->
    		<td><button type="button" onclick="location.href='write.jsp'">글쓰기</button></td>
    </table>
</body>
</html>
