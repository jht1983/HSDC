/**
 * 
 */
package com.yulongtao.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yulongtao.phone.AppPub;

/**
 * @author tianshisheng
 *
 */
public class RepeatSubmissionFilter implements Filter {
	public static final String SYS_SUBMISSION_TOKEN_LIST = "SYS_SUBMISSION_TOKEN_LIST";
	public static final String SYS_SUBMISSION_TOKEN_PARAM = "NO_CURRENT_PAGE_SUBMISSION_TOKEN";
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpSession session = httpServletRequest.getSession();
		List submissionTokenList = (List) session.getAttribute(SYS_SUBMISSION_TOKEN_LIST);
		if (submissionTokenList == null) {
			submissionTokenList = new ArrayList();
			session.setAttribute(SYS_SUBMISSION_TOKEN_LIST, submissionTokenList);
		}
		
		String pageToken = httpServletRequest.getParameter(SYS_SUBMISSION_TOKEN_PARAM);
		//只检查有值的情况，避免对其他页面造成影响
		if (pageToken == null || "".equals(pageToken)) {
			chain.doFilter(request, response);
			return;
		}
		
		if (submissionTokenList.contains(pageToken)) {
			submissionTokenList.remove(pageToken);
			chain.doFilter(request, response);
			return;
		}
		else {
			final PrintWriter out = AppPub.getWriter(httpServletRequest, httpServletResponse);
			out.print("<script>alert('\u8868\u5355\u5df2\u63d0\u4ea4\uff0c\u8bf7\u52ff\u91cd\u590d\u64cd\u4f5c\uff01');parent.closeById('" + httpServletRequest.getParameter("NO_UPL_KC") + "');</script>");
			out.close();
			return;
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
	}
}
