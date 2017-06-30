package com.xxl.util.core.skill.flowcontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.xxl.util.core.util.JacksonUtil;

/**
 * 异常解析器
 * @author xuxueli 2016-7-7 22:33:00
 */
public class WebExceptionResolver implements HandlerExceptionResolver {
	private static transient Logger logger = LoggerFactory.getLogger(WebExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("common.result");
		
		if (ex instanceof WebException) {
			// 是否JSON返回
			HandlerMethod method = (HandlerMethod)handler;
			ResponseBody responseBody = method.getMethodAnnotation(ResponseBody.class);
			if (responseBody != null) {
				mv.addObject("json", true);
				mv.addObject("data", JacksonUtil.writeValueAsString(new ReturnT<String>(((WebException) ex).getCode(), ((WebException) ex).getMsg())));
			} else {
				mv.addObject("data", ((WebException) ex).getMsg());	
			}
			
		} else {
			mv.addObject("data", ex.toString().replaceAll("\n", "<br/>"));	
			mv.setViewName("common.result");
			
			logger.info("==============异常开始=============");
			logger.info("system exceptionMsg:{}", ex);
			logger.info("==============异常结束=============");
		}
		return mv;
	}

	
}
