package com.myspring.pro27a;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/*@Controller*/
public class MyController {
	

	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String  hi() throws Exception{
		
		return "hi1";
	}
	
	
	@RequestMapping(value = "/nana", method = RequestMethod.GET)
	public ModelAndView  hi2() throws Exception{
		
		ModelAndView mav=new ModelAndView();
		
		
		Student stu=new Student();
		stu.setName("김삿갓");
		
		mav.addObject("stu", stu);
		
		mav.setViewName("nana");
		return mav;
	}

}
