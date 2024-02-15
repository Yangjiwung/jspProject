package model2.mvcboard;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.JSFunction;

@WebServlet("/mvcboard/pass.ozo")
public class PassController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		req.setAttribute("mode", req.getParameter("mode"));
		req.getRequestDispatcher("/14MVCBoard/Pass.jsp").forward(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// 매개변수 저장
		String idx = req.getParameter("idx");
		String mode = req.getParameter("mode");
		String pass = req.getParameter("pass");

		// 비밀번호 확인
		MVCBoardDAO dao = new MVCBoardDAO();
		boolean confirmed = dao.confirmPassword(pass, idx); // pass.jsp에서 mode값과 idx값을 받아옴
		dao.close();

		if (confirmed) { // 비밀번호 일치 확인
			if (mode.equals("edit")) { // 들어온 mode값이 edit이면 수정으로 이동
				HttpSession session = req.getSession(); // 보안때문에 세션 영역에 비밀번호 저장
				session.setAttribute("pass", pass);
				resp.sendRedirect("../mvcboard/edit.ozo?idx=" + idx);

			} else if (mode.equals("delete")) { // 들어온 mode값이 delete이면 삭제 진행
				dao = new MVCBoardDAO();
				MVCBoardDTO dto = dao.selectView(idx);
				int result = dao.deletePost(idx);
				dao.close();
				if (result == 1) { // 삭제 성공시 리턴값 -> 첨부파일까지 삭제
					String saveFileName = dto.getSfile();
					FileUtil.deleteFile(req, "/Uploads", saveFileName);
				}
				JSFunction.alertLocation(resp, "삭제되었습니다.", "../mvcboard/list.ozo");
			}

		} else {
			JSFunction.alertBack(resp, "비밀번호가 잘못되었습니다.");
		}
	}

}
