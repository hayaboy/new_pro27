package com.myspring.common;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller(value = "fileUploadController")
public class FileUploadController {
	private static final String CURR_IMAGE_REPO_PATH = "c:\\spring\\image_repo2";

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping(value = "/form")
	public String form() {
		return "uploadForm";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ModelAndView upload(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {

		multipartRequest.setCharacterEncoding("utf-8");
		Map map = new HashMap();

		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			logger.info(name + " : " + value);
			map.put(name, value);
		}
		List fileList = fileProcess(multipartRequest);

		logger.info("fileList" + fileList);

		ModelAndView mav = new ModelAndView();
		mav.addObject("map", map);
		mav.addObject("fileList", fileList);
		mav.setViewName("result");
		return mav;
	}

	public List fileProcess(MultipartHttpServletRequest multipartRequest) {
		List<String> fileList = new ArrayList<String>();
		Iterator<String> fileNames = multipartRequest.getFileNames();

		while (fileNames.hasNext()) {

			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			String originalFileName = mFile.getOriginalFilename();
			fileList.add(originalFileName);
			File file = new File(CURR_IMAGE_REPO_PATH + "\\" + fileName);

			try {
				if (mFile.getSize() != 0) {// File Null Check
					if (!file.exists()) { // 경로상에 파일이 존재하지 않을 경우
						if (file.getParentFile().mkdirs()) {// 경로에 해당하는 디렉토리들을 생성
							file.createNewFile();
						}
						mFile.transferTo(new File(CURR_IMAGE_REPO_PATH + "\\" + originalFileName));
					}
				}
			} catch (Exception e) {

			}

		}
		return fileList;
	}

}