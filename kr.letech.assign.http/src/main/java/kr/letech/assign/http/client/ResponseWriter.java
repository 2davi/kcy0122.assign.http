package kr.letech.assign.http.client;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.letech.assign.http.HttpStatus;
import kr.letech.assign.http.MimeTypes;

public class ResponseWriter {

	private static final Logger log = LoggerFactory.getLogger(ResponseWriter.class);

	public ResponseWriter() {
	}

	String doc_root = "D:/z/flexy-bootstrap-lite-1.0.0";

	public String writeResp(Map<String, Object> process) {
		
		log.debug("▩ ResponseWriter 진입.");
		
		Map<String, String> headers = new HashMap<>();
		BufferedWriter out = (BufferedWriter) process.get("BufferedWriter");
		OutputStream respStream = (OutputStream) process.get("OutputStream");
		String protocol = (String) process.get("Protocol");
		String respUrl = (String) process.get("RespURL");
		String location = doc_root + "/index.html";
		StringBuilder respEnvelope = null;
		StringBuilder bodyLine = null;
		File file = null;
		Integer statusCode = (Integer) process.get("StatusCode");
		String httpStatus = null;
		List<String> startLine = new ArrayList<>();
		List<String> headerLine = new ArrayList<>();
		String mimeType = null;

		try {
			switch (statusCode) {
			case 200:

				break;
			case 300:
				/*
				 * 바디 없음
				 */
				/// 301
				statusCode = 301;

				/// 302
				statusCode = 302;
				break;
			case 400:
				/// 400
				statusCode = 400;

				/// 401
				statusCode = 401;

				/// 403
				statusCode = 403;

				/// 404
				statusCode = 404;

				/// 405
				statusCode = 405;
				break;
			default:
				/// 500
				statusCode = 500;
				break;
			}
			log.debug("▩ 상태코드 : {}", statusCode);

			// 응답파일 준비
			log.debug("▩ respUrl : {}", respUrl);
			file = new File(doc_root + respUrl);
			if (file.isDirectory()) {
				log.debug("file이 디렉토리래^0^");
//				if (respUrl.charAt(respUrl.length() - 1) == '/') {
//					file = new File(doc_root + respUrl);
//				} else {
//					file = new File(doc_root + respUrl);
//				}
			}
			log.debug("▩ 결정된 정적 파일: {}", file.getAbsolutePath());

			FileInputStream fis = null;
			ByteArrayOutputStream baos = null;
			if (file.exists()) {
				log.debug("file이 존재한대^0^");
				mimeType = MimeTypes.of(file.getName());
				
				log.debug("▩ MIME-TYPE: {}", mimeType);
				
				fis = new FileInputStream(file);
				baos = new ByteArrayOutputStream();
				fis.transferTo(baos);
				fis.close();

				headerLine.add(String.format("Content-Type: %s \r\n", mimeType));
				headerLine.add(String.format("Content-Length: %s \r\n", baos.toByteArray().length));
			}

			httpStatus = HttpStatus.fromCode(statusCode).reason();
			log.debug("▩ HTTP-STATUS: {}", httpStatus);
			startLine = List.of(protocol, String.valueOf(statusCode), httpStatus);

			
			for (String key : headers.keySet()) {
				headerLine.add(key + ": " + headers.get(key));
			}

			// BODY??
			if (   !MimeTypes.Value.getMimeType("octet-stream").equalsIgnoreCase(mimeType)
				&& !mimeType.startsWith("image")) {
				bodyLine = new StringBuilder(new String(baos.toByteArray()));
			} else {
				///
			}

			// 응답 작성
			respEnvelope = new StringBuilder(String.join(" ", startLine) + "\r\n")
					.append(String.join(" ", headerLine) + "\r\n")
					.append("\r\n")
					.append(bodyLine);

			out.append(respEnvelope);
			


			

			log.debug("******************* E N D ******************");
			log.info("▦ 응답 작성 완료.");
			log.info("▦ RESP_ENVELOPE : ");
			log.info(respEnvelope.toString());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "fin";
	}

}
