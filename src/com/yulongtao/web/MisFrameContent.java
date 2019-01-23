/**
 * 
 */
package com.yulongtao.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.Debug;
import com.yulongtao.sys.QRSC;
import com.yulongtao.web.entrance.Face;
import com.yulongtao.web.style.MisThreeItem;

/**
 * @author tianshisheng
 *
 */
public class MisFrameContent extends FrameContent {
    private static final String CONTENT_TYPE = "text/html; charset=GBK";
    
    public void init() throws ServletException {
    	super.init();
    }
    
    public void doGet(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=GBK");
        final PrintWriter out = response.getWriter();
        final String strInit = request.getParameter("hometype");
        if (strInit != null) {
            if (strInit.equals("sys_1")) {
                Face.initTitle();
                out.print("\u5237\u65b0\u6210\u529f\uff01");
            }
            else if (strInit.equals("sys_log")) {
                final Object objType = request.getParameter("type");
                if (objType != null) {
                    if (objType.toString().equals("open")) {
                        Debug.bLog = true;
                    }
                    else {
                        Debug.bLog = false;
                    }
                }
                if (Debug.bLog) {
                    out.println("<div onclick=\"window.location='dohome?hometype=sys_log&type=close';\" style='background:#efefef;padding:5px 10px;height:25x;line-height:25px;float:right;cursor:pointer;'>\u5173\u95ed\u8c03\u8bd5</div>");
                    out.print(Debug.getLog());
                }
                else {
                    out.println("<div onclick=\"window.location='dohome?hometype=sys_log&type=open';\" style='background:#efefef;padding:5px 10px;height:25x;line-height:25px;float:right;cursor:pointer;'>\u5f00\u542f\u8c03\u8bd5</div>");
                }
            }
            else {
                QRSC.init();
                out.print("\u5237\u65b0\u6210\u529f\uff01");
            }
            return;
        }
        WebHome wHome = new MisThreeItem(request, response);
        out.println(wHome.doHome());
//        System.out.println("Face.iType" + Face.iType);
//        switch (Face.iCsType) {
//            case 1: {
//                wHome = (WebHome)new DeskTopFace(request, response);
//                break;
//            }
//            case 2: {
//                wHome = new AppleHome(request, response);
//                break;
//            }
//            case 3: {
//                wHome = new ThreeItem(request, response);
//                break;
//            }
//            case 4: {
//                wHome = new ProHome(request, response);
//                break;
//            }
//            case 5: {
//                wHome = new TmsFace(request, response);
//                break;
//            }
//            case 6: {
//                wHome = new MutMenuFace(request, response, 1);
//                break;
//            }
//            case 8: {
//                wHome = new MutMenuFace(request, response, 2);
//                break;
//            }
//            default: {
//                wHome = (WebHome)new DeskTopFace(request, response);
//                break;
//            }
//        }
//        if (wHome != null) {
//            out.println(wHome.doHome());
//        }
    }
    
    public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }
    
    public void destroy() {
    	super.destroy();
    }
}
