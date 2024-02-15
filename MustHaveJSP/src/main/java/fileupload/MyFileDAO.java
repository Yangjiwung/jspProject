package fileupload;

import java.util.List;
import java.util.Vector;

import common.DBConnPool;

public class MyFileDAO extends DBConnPool{ // jdbc 연결해서 sql문을 처리한다.
	//새로운 게시물을 입력
	public int insertFile(MyFileDTO dto) {
		int applyResult = 0;
		try {
			String query = "insert into myfile(idx, name, title, cate, ofile, sfile) values(seq_board_num.nextval, ?, ?, ?, ?, ?)";
			
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, dto.getName());
			pstmt.setString(2, dto.getTitle());
			pstmt.setString(3, dto.getCate());
			pstmt.setString(4, dto.getOfile());
			pstmt.setString(5, dto.getSfile());
			
			applyResult = pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("INSERT 중 예외 발생 : insertFile()메서드를 확인하세요.");
			e.printStackTrace();
		}
		return applyResult;
	}
	
	public List<MyFileDTO> myfileList() { // DB에 있는 모든 내용을 List에 저장하는 메서드
		List<MyFileDTO> fileList = new Vector<MyFileDTO>();
		// 쿼리문 작성
		String query = "select * from myfile order by idx desc"; // where절 없음 모든 파일 불러옴
		try {
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery(); // 쿼리 실행
			
			while(rs.next()) {
				MyFileDTO dto = new MyFileDTO(); // dto객체 생성
				dto.setIdx(rs.getString("idx")); //ResultSet으로 가져온 값을 셋팅
				dto.setName(rs.getString("name"));
				dto.setTitle(rs.getString("title"));
				dto.setCate(rs.getString("cate"));
				dto.setOfile(rs.getString("ofile"));
				dto.setSfile(rs.getString("sfile"));
				dto.setPostdate(rs.getString("postdate"));
				
				fileList.add(dto); // dto에 추가
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("myfileList() 메서드 오류발생");
		}
		
		
		
		return fileList;
	}
	
	
	

}
