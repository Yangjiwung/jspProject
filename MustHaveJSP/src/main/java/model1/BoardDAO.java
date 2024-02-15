package model1;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.ServletContext;

import common.JDBConnect;

public class BoardDAO extends JDBConnect{ // 3번째 생성자 사용
	
	public BoardDAO() {
		super();
	}
	
	public BoardDAO(ServletContext app) {
		//ServletContext app -> WEB-INF / web.xml에 있는 값 활용
		super(app);
	}// con, stmt, pstmt, rs, close를 제공 받는다.
	
	
	
	// 게시물 개수 세는 메서드(목록에 출력할 게시물을 얻어오기 위한 메서드)
	public int selectCount(Map<String, Object> map) {
		int totalCount = 0; // 결과(게시물 수)를 담을 변수
		
		// 게시물 수 를 얻어오는 쿼리문 작성
        String query = "SELECT COUNT(*) FROM board";
        if (map.get("searchWord") != null) {
            query += " WHERE " + map.get("searchField") + " "
                   + " LIKE '%" + map.get("searchWord") + "%'";
        }
		try {
			stmt = con.createStatement(); // 쿼리문 생성
			rs = stmt.executeQuery(query); //쿼리 실행
			rs.next(); // 커서를 첫 번 째 행으로 이동
			totalCount = rs.getInt(1); // 첫 번째 칼럼 값을 가져옴
			
		} catch (Exception e) {
			System.out.println("게시물 수를 구하는 중 예외 발생");
			e.printStackTrace();
		}
		return totalCount;
	}
	
	// 검색 조건에 맞는 게시물 목록을 반환합니다.
	public List<BoardDTO> selectList(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>(); // 결과(게시물 목록)으 담을 리스트 변수 
		
		String query = "select * from BOARD "; // 조건이 없을 때
		if(map.get("searchWord") != null) {
			query += "where " + map.get("searchField") + " "
					+ " like '%" + map.get("searchWord") + "%'"; // 조건이 있을 때 where문 추가 if문
		}
		query += " order by num desc"; //내림차순 정렬
		System.out.println(query);
		try {
			stmt = con.createStatement(); // 쿼리문 생성
			rs = stmt.executeQuery(query); // 쿼리문 실행
			
			while(rs.next()) { // 결과를 순환하며
				//한 행(게시물 하나)의 내용을 DTO에 저장
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				bbs.add(dto); //결과 목록에 저장
			}
			
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생 : selectList()메서드를 확인하세요.");
			e.printStackTrace();
		}
		return bbs;
	}// selectList() 메서드 종료
	
	
	public List<BoardDTO> selectListPage(Map<String, Object> map){
		List<BoardDTO> bbs = new Vector<BoardDTO>(); // 결과(게시물 목록)으 담을 리스트 변수 
		
		String query = "select * from( select Tb.*, rownum rNum from( select * from board ";
				
		if(map.get("searchWord") != null) {
			query += "where " + map.get("searchField") + " "
					+ " like '%" + map.get("searchWord") + "%'"; // 조건이 있을 때 where문 추가 if문
		}
		query += " order by num desc ) Tb ) where rNum between ? and ?"; //내림차순 정렬
		
		System.out.println(query);
		try {
			pstmt = con.prepareStatement(query); // 쿼리문 생성
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			
			rs = pstmt.executeQuery(); // 쿼리문 실행
			
			while(rs.next()) { // 결과를 순환하며
				//한 행(게시물 하나)의 내용을 DTO에 저장
				BoardDTO dto = new BoardDTO();
				
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getString("visitcount"));
				
				bbs.add(dto); //결과 목록에 저장
			}
			
		} catch (Exception e) {
			System.out.println("게시물 조회 중 예외 발생 : selectListPage()메서드를 확인하세요.");
			e.printStackTrace();
		}
		return bbs;
	}// selectListPage() 메서드 종료
	
	public int insertWrite(BoardDTO dto) { // 파라미터를(BoardDTO) 가져와서 넣음 
		int result = 0; // 결과가 int형으로 반환되기 때문에 초기값 0 설정
		
		try {
			// insert 쿼리문 작성
			String query = "insert into board("
					+" num, title, content, id, visitcount) values"
					+"(seq_board_num.nextval, ?, ?, ?, 0)";
			
			pstmt = con.prepareStatement(query); //쿼리문을 DB로 보내는 과정
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getId());
			result = pstmt.executeUpdate(); //pstmt.executeUpdate(); 결과를 int형으로 반환
			
			
		} catch (Exception e) {
			System.out.println("게시물 입력 중 예외발생 : insertWrite()메서드를 확인하세요.");
			e.printStackTrace();
		}
		return result; // 결과값 리턴
	}// insertWrite() 종료
	
	
	
	//게시물을 클릭했을 때 조회수 증가하는 메서드
	public void updateVisitCount(String num) {
		// 쿼리문 준비
		String query = "update board set "
				+" visitcount=visitcount+1 "
				+" where num = ?";
		
		try {
			pstmt = con.prepareStatement(query); //DB에 쿼리문 전송
			pstmt.setString(1, num); // 인파라미터를 일련변호로 설정
			pstmt.executeQuery(); // 쿼리 실행
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생");
			e.printStackTrace();
		}
	}
	
	// 자세히 보기 (번호를 받아 DTO로 리턴)
	public BoardDTO selectView(String num) {
		BoardDTO dto = new BoardDTO(); // dto 객체 생성
		// 쿼리문 준비
		String query = "select B.*, M.name from member M inner join board B on M.id=B.id where num = ?";
		// member 테이블의 pk를 board테이블의 fk와 함께 출력 하기 INNER JOIN (SQL문) 
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, num);			// 인파라미터를 일련번호로 설정
			rs = pstmt.executeQuery();			// 쿼리 실행 후 결과가 표로 나옴
			
			// 결과처리
			if(rs.next()) { // Read의 상세보기는 if문로 처리한다 전체보기의 경우 while문으로 처리
				dto.setNum(rs.getString("num"));
				dto.setTitle(rs.getString("title"));
				dto.setContent(rs.getString("content"));
				dto.setId(rs.getString("id"));
				dto.setPostdate(rs.getDate("postdate"));
				dto.setVisitcount(rs.getString("visitcount"));
				dto.setName(rs.getString("name"));				// dto 객체에 값 저장
			}
		} catch (Exception e) {
			System.out.println("게시물 상세보기 중 예외 발생 : dao의 selectView()메서드를 확인하세요");
			e.printStackTrace();
		}
		return dto; // dto를 리턴하여 View.jsp로 넘겨줌
		
		
	}
	
	
	// 업데이트용 메서드 (dto를 받아 int로 리턴)
	public int updateEdit(BoardDTO dto) { // 업데이트이기 때문에 인파라미터 DTO를 받아옴
		int result = 0;
		try {
			String query = "update board set title=?, content=? where num=?"; // 쿼리문 완성
			pstmt =con.prepareStatement(query);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getContent());
			pstmt.setString(3, dto.getNum());
			
			result = pstmt.executeUpdate(); // 쿼리문을 실행하여 결과를 int로 받음
			
		} catch (Exception e) {
			System.out.println("게시물 수정 중 예외 발생 : updateEdit 메서드를 확인하세요");
			e.printStackTrace();
		}
		return result; // 결과 반환
		
	}
	
	// 지정한 게시물을 삭제합니다.
	public int deletePost(BoardDTO dto) {
		int result = 0;
		try {
			// 쿼리문 작성
			String query = "delete from board where num = ?";
			
			// 쿼리문 완성
			pstmt= con.prepareStatement(query);
			pstmt.setString(1, dto.getNum());
			
			// 쿼리문 실행
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생 : deletePost 메서드를 확인하세요");
			e.printStackTrace();
		}
		return result; // 결과값 반환
		
	}
	
	
	
	

}
