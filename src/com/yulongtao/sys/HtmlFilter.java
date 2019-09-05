package com.yulongtao.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.phone.AppPub;
import com.yulongtao.web.component.PageModel;
import com.yulongtao.web.entrance.Face;

public class HtmlFilter implements Filter
{
    public static String CONTENT_TYPE;
    
    static {
        HtmlFilter.CONTENT_TYPE = "text/html; charset=" + Dic.strAppCharset;
    }
    
    public void destroy() {
    }
    
    public void doFilter(final ServletRequest arg0, final ServletResponse arg1, final FilterChain arg2) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest)arg0;
        final HttpServletResponse response = (HttpServletResponse)arg1;
        response.addHeader("X-Frame-Options", "SAMEORIGIN");
        response.setContentType(HtmlFilter.CONTENT_TYPE);
        String strPageName = request.getServletPath();
        final int iEndIndex = strPageName.lastIndexOf("/");
        if (iEndIndex != -1) {
            strPageName = strPageName.substring(iEndIndex + 1);
        }
        final String strName = strPageName.split("\\.")[0];
        if (QRSC.HASHISLOGINPAGE == null) {
            return;
        }
        final HttpSession session = request.getSession();
        if (QRSC.HASHISLOGINPAGE.get(strPageName) != null && session.getAttribute(QRSC.STRLOGIN_SESSION) == null) {
            String strQueryString = request.getQueryString();
            if (strQueryString == null) {
                strQueryString = "";
            }
            if (strQueryString.equals("")) {
                session.setAttribute("SYS_LOGIN_REDIRECT_QUERYSTR", (Object)strPageName);
            }
            else {
                session.setAttribute("SYS_LOGIN_REDIRECT_QUERYSTR", (Object)(String.valueOf(strPageName) + "?" + strQueryString));
            }
            response.sendRedirect(QRSC.STRLOGIN_LOGINPAGE);
        }
        final PrintWriter out = AppPub.getWriter(request, response);
        session.setAttribute("SYS_CUR_PAGE_NAME", (Object)strName);
        final PageModel tm = new PageModel(request, response);
        tm.hashSysParam = new Hashtable();
        tm.strPageName = strPageName;
        tm.hashSysParam.put("page", String.valueOf(strName) + ".view");
        if (strName.equals("zcfgjs")) {
            tm.bIsCon = true;
        }
        else {
            tm.bIsCon = false;
        }
        tm.initModleFile(strName);
        final String strVerJs = request.getParameter("sys_ver");
        final String strPageType = request.getParameter("sys_pagetype");
        boolean bIsJs = false;
        if (strVerJs != null || strPageType != null) {
            bIsJs = true;
            tm.bIsJs = true;
        }
        if (PageModel.hashPageStyle.get(strName) != null) {
            if (!bIsJs) {
                this.importHead(out, Face.strFrontTitle, request, tm);
            }
            if (strPageType != null) {
                if (strPageType.equals("param")) {
                    out.print(tm.getParamJsData(strName));
                }
                else if (strPageType.equals("graph")) {
                    out.print(tm.getGraphJsData(strName, request.getParameter("sys_graphid")));
                }
                else if (strPageType.equals("page")) {
                    out.print(tm.getPageJsData(strName));
                }
            }
            else {
                out.print(tm.getStyle(strName));
            }
        }
        else {
            arg2.doFilter((ServletRequest)request, (ServletResponse)response);
        }
        out.close();
    }
    
    public void getUpdate(final PrintWriter _out, final String _strName, final HttpServletRequest _request, final PageModel _pm) {
        final String strUid = _request.getParameter("sys_u_id");
        if (strUid != null) {
            final String strPageCode = _request.getParameter("sys_pcode");
            final Hashtable hashHQRC = (Hashtable) QRSC.HASHQRSC.get(strPageCode);
            final String strEditCon = hashHQRC.get("SEDITPAGE").toString();
            _out.print("<script>");
            final String[] arrTableField = strEditCon.split("\\$");
            final Record record = PageModel.getRecord("select * from " + arrTableField[0] + " where " + arrTableField[1] + "='" + strUid + "'");
            for (int iFieldCount = record.index, i = 1; i <= iFieldCount; ++i) {
                try {
                    final Object objValue = record.getFieldById(i);
                    if (objValue != null) {
                        final FieldEx field = (FieldEx)objValue;
                        if (!field.value.toString().equals("")) {
                            _out.println("add." + arrTableField[0] + "$" + field.fieldName + ".value='" + field.value + "';");
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            _out.print("</script>");
        }
    }
    
    public void importHead(final PrintWriter _out, final String _strName, final HttpServletRequest _request, final PageModel _pm) {
        _out.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" style=\"height:100%\">");
        _out.print("<meta charset=\"" + Dic.strAppCharset + "\"><link rel=\"shortcut icon\" href=\"favicon.ico\"><title>" + _strName + "</title>");
    }
    
    public void doFilter_bak(final ServletRequest arg0, final ServletResponse arg1, final FilterChain arg2) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest)arg0;
        final HttpServletResponse response = (HttpServletResponse)arg1;
        response.setContentType(HtmlFilter.CONTENT_TYPE);
        String strName = request.getServletPath();
        final int iEndIndex = strName.lastIndexOf("/");
        if (iEndIndex != -1) {
            strName = strName.substring(iEndIndex + 1).split("\\.")[0];
        }
        final PageModel tm = new PageModel(request, response);
        tm.hashSysParam = new Hashtable();
        final String[] arrStrName = strName.split("_");
        if (arrStrName.length > 1) {
            strName = arrStrName[0];
            tm.hashSysParam.put("id", arrStrName[1]);
        }
        if (PageModel.hashPageStyle.get(strName) != null) {
            response.getWriter().println(tm.getStyle(strName));
        }
        else {
            arg2.doFilter((ServletRequest)request, (ServletResponse)response);
        }
    }
    
    public void init(final FilterConfig arg0) throws ServletException {
    }
}
