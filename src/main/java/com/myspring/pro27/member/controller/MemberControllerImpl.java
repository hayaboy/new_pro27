package com.myspring.pro27.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.pro27.member.service.MemberService;
import com.myspring.pro27.member.vo.MemberVO;

@Controller("memberController")
public class MemberControllerImpl extends MultiActionController implements MemberController {

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberVO memberVO;

	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);

	// 로그인 폼
	@RequestMapping(value = "/member/loginForm.do")
	public ModelAndView loginForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/member/loginForm");
	}

	// 로그인 동작
	@Override
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST) // POST일경우 PRG, RedirectAttributes 리디렉션 URL에 사용됨, 속성 값은 문자열로 형식화되고 해당 방식으로 저장되어 쿼리 문자열에 추가되거나 
	public ModelAndView login(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 로그인 동작하는 과정
		logger.info("로그인 동작 메서드");

		// 1. 회원 가입 여부 확인
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);

		logger.info("로그인한 멤버 : " + memberVO);

		if (memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			mav.setViewName("redirect:/member/listMembers.do");

			// 2. 회원일 경우/아닐경우
		} else {
			
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");

		}

		return mav;
	}

		
	//로그아웃 동작
	@Override
	@RequestMapping(value = "/member/logout.do", method =  RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		session.invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/loginForm.do");
		return mav;
	}
	
	
	// 회원 목록
	@Override
	@RequestMapping(value = "/member/listMembers.do", method = RequestMethod.GET)
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = getViewName(request);

		logger.info("뷰네임# : " + viewName);
		// System.out.println("뷰네임 : " + viewName );

		List membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		return mav;
	}

	// 회원 가입 폼으로 가기
	@RequestMapping(value = "/member/memberForm.do")
	public ModelAndView memberForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/member/memberForm");
	}

	// 회원 수정 폼으로 가기
	@RequestMapping(value = "/member/updateMemberForm.do")
	public ModelAndView memberForm2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/member/updateMemberForm");
	}

	// 회원 추가(내거)
	@RequestMapping(value = "/member/addMember.do", method = RequestMethod.POST)
	public ModelAndView addMember(@ModelAttribute("member") MemberVO memberVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		logger.info("memberVO 회원 추가  : " + memberVO);
		memberService.addMember(memberVO);

		// PRG 패턴( Post - Redirect - GET )
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");

		return mav;
	}

	// 회원 삭제
	@Override
	@RequestMapping(value = "/member/removeMember.do", method = RequestMethod.GET)
	public ModelAndView removeMember(String id, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		memberService.removeMember(id);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}

	// 요청명과 같은 파일명으로 나오게 하도록
	private String getViewName(HttpServletRequest request) throws Exception {
		String contextPath = request.getContextPath();
		// System.out.println("contextPath : " + contextPath);

		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
		// System.out.println("uri : " + uri);

		if (uri == null || uri.trim().equals("")) {
			uri = request.getRequestURI();
			// System.out.println("null인 경우 uri: " + uri );
		}
		int begin = 0;

		if (!((contextPath == null) || ("".equals(contextPath)))) {
			begin = contextPath.length();
			// System.out.println("begin : " + begin);
		}
		int end;

		// System.out.println(uri.indexOf(";"));
		if (uri.indexOf(";") != -1) {
			end = uri.indexOf(";");
		} else if (uri.indexOf("?") != -1) {
			end = uri.indexOf("?");
		} else {
			end = uri.length();
		}
		// System.out.println("end : " + end );
		String fileName = uri.substring(begin, end);
		// System.out.println("fileName : " +fileName);

		if (fileName.indexOf(".") != -1) {
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
			// System.out.println("fileName : " +fileName);
		}
		if (fileName.lastIndexOf("/") != -1) {
			fileName = fileName.substring(fileName.lastIndexOf("/", 1), fileName.length());
			// System.out.println("fileName : " +fileName);

		}

		return fileName;
	}

	// 회원 수정
	@Override
	@RequestMapping(value = "/member/updateMember.do", method = RequestMethod.POST)
	public ModelAndView upDateMember(@ModelAttribute MemberVO memberVO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		/*
		 * String id=request.getParameter("id"); logger.info("수정할 id : " +id); String
		 * pwd=request.getParameter("pwd"); String name=request.getParameter("name");
		 * String email=request.getParameter("email"); logger.info("수정할 name : " +name);
		 * 
		 * 
		 * memberVO.setId(id); memberVO.setPwd(pwd); memberVO.setName(name);
		 * memberVO.setEmail(email);
		 */

		memberService.updateMember(memberVO);
		// PRG 패턴( Post - Redirect - GET )
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");

		return mav;
	}

	

}
