package model2.mvcboard;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.DBConnPool;

public class MVCBoardDAO extends DBConnPool{
	
	public MVCBoardDAO() {}

	
	// 검색 조건에 맞는 게시물의 개수를 반환합니다
	public int selectCount(Map<String, Object>map) {
		int totalCount = 0;
		
		String sql = "select count(*) from mvcboard";
		if(map.get("searchWord") != null) {
			sql += " where " + map.get("searchField") + " like '%" + map.get("searchWord")+ "%'";
		}
		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			totalCount = rs.getInt(1);
			
		} catch (Exception e) {
			System.out.println("게시물 카운트중 예외 발생 : selectCount()");
			e.printStackTrace();
		}
		return totalCount;
	}
	
	// 검색 조건에 맞는 게시물 목록을 반환합니다.(페이징 기능 지원)
	public List<MVCBoardDTO> selectListPage(Map<String, Object>map) {
		List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
		
		String sql = "select * from (select Tb.*, ROWNUM rNum from(select* from mvcboard";
		if(map.get("searchWord") != null) {
			sql += " where "+ map.get("searchField") + " like '%" + map.get("searchWord") + "%'";
		}
		sql += " order by idx desc) Tb ) where rNum between ? and ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, map.get("start").toString());
			pstmt.setString(2, map.get("end").toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				MVCBoardDTO dto = new MVCBoardDTO();
				
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
				
				board.add(dto);
			}
			
		} catch (Exception e) {
			System.out.println("게시물 조회중 예외 발생 : selectListPage()");
			e.printStackTrace();
		}
		return board;
	}
	
	
	
	public int insertWrite(MVCBoardDTO dto) {
		int result = 0;
		
		try {
			String sql = "insert into mvcboard(idx, name, title, content, ofile, sfile, pass)"
					+ " values(seq_board_num.nextval, ?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			pstmt.setString(6, dto.getPass());
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 입력중 예외 발생 : insertWrite()");
			e.printStackTrace();
		}
		return result;
	}
	
	// 주어진 일련번호에 해당하는 게시물의 조회수를 1 증가시킵니다.
	public void updateVisitCount(String idx) {
		String sql = "update mvcboard set visitcount = visitcount+1 where idx =?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			pstmt.executeQuery();
			
		} catch (Exception e) {
			System.out.println("게시물 조회수 증가 중 예외 발생 : updateVisitCount()");
		}
		
	}
	
	
	// 주어진 일련번호에 해당하는 게시물을 DTO에 담아 반환합니다.
	public MVCBoardDTO selectView(String idx) {
		MVCBoardDTO dto = new MVCBoardDTO(); // DTO객체 생성
		String sql = "select * from mvcboard where idx=?";
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto.setIdx(rs.getString(1));
				dto.setName(rs.getString(2));
				dto.setTitle(rs.getString(3));
				dto.setContent(rs.getString(4));
				dto.setPostdate(rs.getDate(5));
				dto.setOfile(rs.getString(6));
				dto.setSfile(rs.getString(7));
				dto.setDowncount(rs.getInt(8));
				dto.setPass(rs.getString(9));
				dto.setVisitcount(rs.getInt(10));
			}
			
		} catch (Exception e) {
			System.out.println("게시물 상세보기중 예외 발생 : selectView()");
			e.printStackTrace();
		}
		return dto;
		
	}
	
	
	// 다운로드 횟수 증가 메서드
	public void downCountPlus(String idx) {
		String sql = "update mvcboard set downcount = downcount +1 where idx = ?";
		
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("다운로드수 증가 중 예외 발생 : downCountPlus()");
			e.printStackTrace();
		}
	}
	
	// 게시물 삭제 : 입력한 비밀번호가 지정한 일련번호의 게시물의 비밀번호와 일치하는지 확인
	public boolean confirmPassword(String pass, String idx) {
		boolean isCorr = true;
		
		try {
			String sql = "select count(*) from mvcboard where pass = ? and idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, pass);
			pstmt.setString(2, idx);
			rs = pstmt.executeQuery();
			rs.next();
			if(rs.getInt(1) == 0) {
				isCorr = false;
			}
		} catch (Exception e) {
			isCorr = false;
			e.printStackTrace();
			System.out.println("게시물 삭제 메서드 예외 발생 : 비밀번호와 게시물 번호가 일치하는지 확인하는 메서드");
		}
		return isCorr;
	}
	
	// 게시물 삭제 메서드
	public int deletePost(String idx) {
		int result = 0;
		
		try {
			String sql = "delete from mvcboard where idx = ?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, idx);
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("게시물 삭제 중 예외 발생 : deletePost()");
			e.printStackTrace();
		}
		return result;
	}
	
	// 게시물 업데이트
	public int updatePost(MVCBoardDTO dto) {
		int result = 0;
		
		try {
			// 쿼리문 생성
			String sql = "update mvcboard set title=?, name=?, content=?, ofile=?, sfile=? where= idx=? and pass=?";
			
			// 쿼리문 세팅
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, dto.getTitle());
			pstmt.setString(2, dto.getName());
			pstmt.setString(3, dto.getContent());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			pstmt.setString(6, dto.getIdx());
			pstmt.setString(7, dto.getPass());
			result = pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("게시물 수정 중 예외 발생 : updatePost()");
		}
		return result;
	}
	
	
	
	
	
}
