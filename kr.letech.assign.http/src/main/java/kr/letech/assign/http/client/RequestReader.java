package kr.letech.assign.http.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.util.StringUtil;

public class RequestReader {
	
	private static final Logger log = LoggerFactory.getLogger(RequestReader.class);
	
	private static RequestReader instance = null;
	public RequestReader() {}
	
	/** Stream 데이터를 읽어오는 방법 */
	@SuppressWarnings("finally")
	public Map<String, Object> readReq(BufferedReader in) {
		
		String line = null;
		int idx = 0;
		boolean isRun = true;
		boolean isBody = false;
		List<String> headerNames = new ArrayList<>();
		log.debug("[] RequestReader.readReq(<BufferedReader>) 실행 중...");
		
		String method   = null;
		String url      = null;
		String protocol = null;
		Map<String, String> headers = new HashMap<>();
		String body     = null;
		
		Map<String, Object> reqInfo = new HashMap<>();
		
		try {
			// 읽단 읽습니다.
			line = in.readLine();
			
			// start-line
			if(idx == 0 && line != null) {
				String[] arrStartLine = line.split(" ");
				if(arrStartLine.length == 3) {
					method   = arrStartLine[0];
					url      = arrStartLine[1];
					protocol = arrStartLine[2];
					log.debug("▦ start-line : {}\t{}\t{}.", method, url, protocol);
					idx++;
					log.debug("▦ idx 증가 : {}", idx);
				} else {
					log.warn("▦ 예상치 못한 start-line 수신. 뭘 어떻게 처리할까?");
				}
			}
			
			
			// headers
			while(idx!=0 && !isBody) {
				line = in.readLine();
				
				// 반복문 탈출 분기
				// empty-line
				if(idx !=0 && !isBody && (line == null || line.trim().length() == 0)) {
					break;
				}
				
				String[] arrHeader = line.split(":", 2);
				String key = arrHeader[0].trim();
				String value = arrHeader[1].trim();
				headers.put(key, value);
				headerNames.add(key);
				log.debug("▦ {} : {}", key, value);
				continue;
			}

			
			// GET || POST 분기
			// body
			if ("POST".equalsIgnoreCase(method)) {
				
				String contentLength = headers.get("Content-Length");
				if(contentLength != null) {
					int cl = Integer.parseInt(contentLength.trim());
					//isBody = true;					
					/* 긴 문자열 읽는 스트림 메서드 */
					char[] bodyChar = new char[cl];
					in.read(bodyChar, 0, cl);
					body = new String(bodyChar);
				}
			}
			log.debug("▦ 반복문 탈출 완료");
			
			
			
			log.debug("******************* E N D ******************");
			log.info("▦ 요청 확인.");
			log.info("▦ START-LINE : {} {} {}", method, url, protocol);
			log.info("▦    HEADERS : {}", String.join(", ", headerNames));
			log.info("▦ EMPTY-LINE :");
			if(body != null) {
				log.info("▦ BODY ----------------------------------------");
				log.info(body);
			}
			
			reqInfo.put("Method", method);
			reqInfo.put("URL", url);
			reqInfo.put("Protocol", protocol);
			reqInfo.put("Headers", headers);
			reqInfo.put("Body", body);
			
		}
		catch(IOException e) {
			log.error("▦ 클라이언트 요청 확인 중 오류 발생", e);
		}
		catch(NumberFormatException e) {
			log.error("▦ Content-Length의 값이 유효하지 않습니다.", e);
		}
		finally {
			log.debug("▦ RequestReader RETURN문 실행...");
			return reqInfo;			
		}
		
		/* GET/POST 파라미터 방식
		 * GET /restapi/v1.0 HTTP/1.1
		 * Accept: application/json
		 * Authorization: Bearer UExBMDFUMDRQV1MwMnzpdvtYYNWMSJ7CL8h0zM6q6a9ntw
		 * 
		 * HTTP/1.1 200 OK
		 * Date: Mon, 23 May 2005 22:38:34 GMT
		 * Content-Type: text/html; charset=UTF-8
		 * Content-Encoding: UTF-8
		 * Content-Length: 138
		 * Last-Modified: Wed, 08 Jan 2003 23:11:55 GMT
		 * Server: Apache/1.3.3.7 (Unix) (Red-Hat/Linux)
		 * ETag: "3f80f-1b6-3e1cb03b"
		 * Accept-Ranges: bytes
		 * Connection: close
		 * 
		 * <html>
		 * <head>
		 * <title>An Example Page</title>
		 * </head>
		 * <body>
		 *     Hello World, this is a very simple HTML document.
		 * </body>
		 * </html>
		 */
	}
	
	/** 파라미터 조회 후 반환하는 메서드 */
	private void readParams(String line) {
		
	}
	
	
	
}
