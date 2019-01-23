package com.timing.impcl;

import javax.servlet.http.HttpServletRequest;

import com.timing.impcl.MantraUtil;

/**
 * @author Mantra 2018-03-14 17:08:18
 */
public class ProcessParameterVO {
	private String runCode; //运行流水�?
	private String runFlow; //运行版本�?
	private String inpPkey; //输入的主�?
	private String pKey;	//输出的主�?
	private String branck;	//输入的组�?
	private String spageCode;//输入的表�?
	private static MantraUtil tool = new MantraUtil(); // 工具�?

	public ProcessParameterVO() {
		
	}
	
	
	/**
	 * @param request 浏览器请�?
	 */
	public ProcessParameterVO(HttpServletRequest request) {
		setHttpServletRequest(request);
	}
	
	/**
	 * @param request 浏览器请�?
	 */
	public void setHttpServletRequest(HttpServletRequest request) {
		try {
			inpPkey = request.getParameter("S_ID");
			branck = request.getParameter("_Bmid");
			spageCode = request.getParameter("SPAGECODE");
			if (inpPkey == null && inpPkey.trim().length() > 0 && branck == null && branck.trim().length() > 0
					&& spageCode == null && spageCode.trim().length() > 0) {
			}
			runFlow = tool.getFlowVer(spageCode, branck);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	public void setRunCode(String runCode) {
		this.runCode = runCode;
	}


	public void setRunFlow(String runFlow) {
		this.runFlow = runFlow;
	}


	public void setInpPkey(String inpPkey) {
		this.inpPkey = inpPkey;
	}


	public void setpKey(String pKey) {
		this.pKey = pKey;
	}


	public void setBranck(String branck) {
		this.branck = branck;
	}


	public void setSpageCode(String spageCode) {
		this.spageCode = spageCode;
	}

	public String getRunCode() {
		return runCode;
	}

	public String getRunFlow() {
		return runFlow;
	}

	public String getInpPkey() {
		return inpPkey;
	}

	public String getpKey() {
		return pKey;
	}
	
	public String getBranck() {
		return branck;
	}

	public String getSpageCode() {
		return spageCode;
	}

}
