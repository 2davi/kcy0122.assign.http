package kr.letech.assign.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerApp {
	
	private static final Logger log = LoggerFactory.getLogger(HttpServerApp.class);
	private static final int PORT = 8081;
	private static final boolean IS_RUN = true;
	public static final List<String> SESSION_ID = new ArrayList<>();
	
	public static void main(String[] args) {
		log.warn("HTTP_SERVER_APPLICATION launched...");
		log.warn("PORT : {}", PORT);
		log.trace(null);
		
		/// 01. 서버환경 세팅
		ExecutorService threadPool = Executors.newFixedThreadPool(35);
		/** 각 연결마다 생성될 객체들은 쓰레드 작업 안에서 처리. */
		
		try (ServerSocket serverSocket = new ServerSocket(PORT)){
			while(IS_RUN) {
				
				try {
					/// 02. 멀티 유저 처리 - 연결된 소켓 별로 쓰레드풀에서 관리
					Socket socket = serverSocket.accept();
					HttpClient client = new HttpClient(socket);
					threadPool.submit(client);
				}
				catch(IOException e) {
					System.out.println("클라이언트 요청 수락 실패");
					e.printStackTrace();
				}
				
			}
		}
		catch(IOException e) {
			System.out.println("치명적인 오류 발생 --- 서버 종료");
			e.printStackTrace();
		}
		finally {
			if(threadPool != null) threadPool.shutdown();
		}
	}
}
