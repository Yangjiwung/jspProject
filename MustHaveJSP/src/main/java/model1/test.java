package model1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class test {
	public static void main(String[] args) {
		Map<String, Object> map = new HashMap<String, Object>();

	    map.put("searchField", "title");
	    map.put("searchWord", "3");
		BoardDAO dao = new BoardDAO();
		List<BoardDTO> lists = dao.selectList(map);
		
		for (BoardDTO a : lists) {
			System.out.println(a);
		}
	}
}
