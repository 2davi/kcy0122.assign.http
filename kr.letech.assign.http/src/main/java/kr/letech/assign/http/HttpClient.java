package kr.letech.assign.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.letech.assign.http.client.RequestReader;
import kr.letech.assign.http.client.ResponseWriter;

public class HttpClient implements Runnable {
	
	private static final Logger log = LoggerFactory.getLogger(HttpClient.class);
	
	/// 03. 소켓 생성은 서버에서 관리,
	//      요청응답 처리는 각각의 HttpClient에서 소켓 하나씩 넘겨받아 담당.
	private final Socket client;
	private RequestReader requestReader;
	private ResponseWriter responseWriter;
	public HttpClient(Socket socket) {
		this.client = socket;
		this.requestReader = new RequestReader();
		this.responseWriter = new ResponseWriter();
	} 
	
	@Override
	public void run() {
		try (
			/// 04. socket에 스트림 연결
			// 닫아줘야 하는 객체는 메서드 밖으로 빼봤자 불편함.
			InputStream reqStream = client.getInputStream();
			OutputStream respStream = client.getOutputStream();
			BufferedReader in = new BufferedReader(new InputStreamReader(reqStream));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(respStream));
		) {
			/// 05. 요청 수신
			//-> 무슨 요청을??
			Map<String, Object> reqInfo = requestReader.readReq(in);
			
			/// 06. 뭔가 처리
			//-> 뭘 처리??
			@SuppressWarnings("unchecked")
			Map<String, String> reqHeaders = (Map<String, String>)reqInfo.get("Headers");
			Map<String, String> respHeaders = new HashMap<>();
			
			/*
			 * 1.세션인증(인증 못함)
			 * - 헤더에 세션아이디 없으면 새로 만들어 줘.
			 * 
			 * 2.첫 접속
			 * - root페이지로 보내줘.
			 * 
			 * 3.CSRF토큰 있나?
			 * 
			 */
			String tempSessionId = reqHeaders.get("Cookie").trim();
			if(tempSessionId != null && !tempSessionId.startsWith("LESESSIONID")) {
//				ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
//				ZonedDateTime expires = now.plus(30, ChronoUnit.MINUTES);
				respHeaders.put("Set-Cookie", "LESESSIONID=" + String.valueOf(UUID.randomUUID())
											  +"; domain=\"/\"");
//											  +"; expires=" +expires);
			}

			respHeaders.put("Location", "D:/z/flexy-bootstrap-lite-1.0.0/index.html");

			Map<String, Object> respParams = new HashMap<>();
		    respParams.put("BufferedWriter", out);
		    respParams.put("OutputStream", respStream);
		    respParams.put("Protocol", "HTTP/1.1");
		    respParams.put("RespURL", "/index.html");
		    respParams.put("StatusCode", 200);
			log.debug("respParams: {}", respParams.toString());
			/// 07. 응답 송신
			//-> 뭐라고 응답??
			String result = responseWriter.writeResp(respParams);
			
			
			log.debug("▩ " + result);
			
			out.flush();
			
		}
		catch(IOException e) {}
		finally {
			/// 08. 연결 종료
			//->는 서버에서 ^^
		}
		
	}

}
