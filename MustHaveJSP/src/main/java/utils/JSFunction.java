package utils;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

public class JSFunction {
	// jsp 파일에서 alert 창을 띄우려면 <% 스크립틀릿 %> 이 후에 작성을 해야 된다.
	// 자바 코드가 이어지는 부분에서는 코드가 복잡 해진다.
	
	
	// 메세지 알림창을 띄운 후 명시한 URL로 이동합니다.
	public static void alertLocation(String msg, String url, JspWriter out) {
	try {
		String script = "" //삽입할 자바 코드
				+"<script>"
				+"	alert('"+ msg +"');"
				+	"location.href='" + url + "';"
				+"</script>";
		out.println(script); // 자바스크립트 코드를 out 내장 객체로 출력(삽입)
		
	}catch(Exception e){
		
	}
		
	}//alertLocation() 종료
	
	
	//메세지 알림 창을 띄운 후 이전 페이지로 돌아갑니다.
	public static void alertBack(String msg, JspWriter out) {
		try {
			String script = ""
					+"<script>"
					+"	alert('" + msg + "');"
					+"	history.back();" // 실패할경우 뒤로 돌아감
					+"</script>";
			out.println(script);
			
		}catch (Exception e) {
		}
	}//alertBack() 메서드 종료
	
	
	// 메세지 알림창을 띄운 후 명시한 URL로 이동합니다.
	public static void alertLocation(HttpServletResponse resp, String msg, String url) {
		try {
			resp.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			String script = "<script>"
					+ "	alert('" + msg + "');"
					+ "	location.href='" + url + "';"
					+ " </script>";
			writer.print(script);
			
		} catch (Exception e) {
		}
		
	}
	
	public static void alertBack(HttpServletResponse resp, String msg) {
		try {
			resp.setContentType("text/html;charset=UTF-8");
			PrintWriter writer = resp.getWriter();
			String script = 
					"<script>"
					+ "	alert('" + msg + "');"
					+ " history.back();"
					+ " </script>";
			writer.print(script);
		} catch (Exception e) {
		}
		
	}
	
	
}
