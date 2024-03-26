package com.myspring.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller("fileDownloadController")
public class FileDownloadController {

	private static String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo";

	private static final Logger logger = LoggerFactory.getLogger(FileDownloadController.class);

	@RequestMapping("/download")
	public void download(@RequestParam("imageFileName") String imageFileName, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		logger.info("imageFileName : " + imageFileName);

		String downFile = CURR_IMAGE_REPO_PATH + "\\" + imageFileName;
		String encodedFileName = URLEncoder.encode(downFile, "UTF-8");
		// HTTP 응답 헤더에 "Cache-Control" 필드를 설정하여 캐시 제어를 지정하는 것을 의미합니다.
		// "no-cache" 값은 캐시된 응답을 사용하지 않고 항상 서버로부터 새로운 응답을 요청해야 함을 나타냅니다.
		// 이렇게 설정하면 브라우저는 캐시된 버전을 사용하지 않고 항상 서버로부터 새로운 데이터를 가져오게 됩니다.

		response.setHeader("Cache-Control", "no-cache");
		// HTTP 응답 헤더에 "Content-disposition" 필드를 추가하여 첨부 파일 다운로드를 지정하는 것을 의미합니다.
		// "attachment" 값은 브라우저에게 응답을 첨부 파일로 처리하도록 지시하는 역할을 합니다.
		// "fileName=" 부분은 첨부 파일의 이름을 지정하는 부분으로,
		// 여기서는 변수 fileName에 저장된 값을 사용하여 다운로드되는 파일의 이름을 동적으로 설정하고 있습니다.
		// 이렇게 설정된 헤더를 받은 브라우저는 응답을 파일로 다운로드하게 됩니다.

		response.addHeader("Content-disposition", "attachment; fileName=" + encodedFileName);

		System.out.println("서버에 있는 다운 파일" + downFile);

// 파일 내려 받기 위한 OutputStream 객체,

		OutputStream out = response.getOutputStream();

		File f = new File(downFile);

// 파일에 들어갈 파일 인풋스트림객체 생성
		FileInputStream in = new FileInputStream(f);

		try {
			
		}catch(Exception e) {
			logger.info("다운 관련 예외이나 크게 상관 없음");
		}
		
		
		byte[] buffer = new byte[1024 * 8];

		while (true) {
			int count = in.read(buffer);   // 버퍼에 읽어들인 문자개수
			if (count == -1) {  // 버퍼의 마지막에 도달했는지 체크
				break;
			}

			out.write(buffer, 0, count);

		}

		in.close();
		out.close();

	}
}
