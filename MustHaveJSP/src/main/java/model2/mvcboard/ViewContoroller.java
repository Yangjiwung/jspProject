package model2.mvcboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/mvcboard/view.ozo")
public class ViewContoroller extends HttpServlet{
	
	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//게시물 가져오기
		MVCBoardDAO dao = new MVCBoardDAO();
		String idx = req.getParameter("idx"); // db에 idx값을 가져옴
		dao.updateVisitCount(idx); // 조회수 1씩 증가
		MVCBoardDTO dto = dao.selectView(idx); 
		// 인덱스 검색 (dao에서 인덱스 번호를 받아와 불러온 1개의 게시물 모든 내용)
		dao.close();
		
		// 줄바꿈 처리
		dto.setContent(dto.getContent().replaceAll("\r\n", "<br/>"));
		// 게시물 내용의 엔터값을 html형식으로 변환
		
		// 게시물(dto) 저장 후 view로 포워드
		req.setAttribute("dto", dto); // 리퀘스트 영역에 저장
		req.getRequestDispatcher("/14MVCBoard/View.jsp").forward(req, resp);
		// /14MVCBoard/View.jsp 경로로 보냄
		
	}
	
	


	
	
}
