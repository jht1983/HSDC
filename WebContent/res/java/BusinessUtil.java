package com.sip.business;

import java.util.Hashtable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yulongtao.sys.QRSC;
//可删除   
// 幻想业务处理层
public class BusinessUtil {
	
	// SFIELDNAME=1519625328671,   子表编号
	// SFIELDCODE=1519625299855,   主表号
	// SPAGENAME=标准化项目-标准化评分项目汇总,   //名称
	// SFIELDSIZE=T_BMBZHXMPF_SON$S_FID=T_BMBZHXMPF$S_ID, 分割条件
	
	// SFIELDNAME=ID,用户,功能名称,功能代码,功能图片,功能地址,排序,NO_DOSCRIPT,   //字段名称
	// SFIELDCODE=T_CYGN$S_ID,   T_CYGN$S_YH,T_CYGN$S_GNMC,T_CYGN$S_GNDM, T_CYGN$S_PIC, T_CYGN$S_GNDZ,  T_CYGN$S_PX,   NO_DOSCRIPT,  //字段
	
	
	public JSONObject getPageField(String _strPageId) throws JSONException {
		Object objPage = QRSC.HASHQRSC.get(_strPageId);
		JSONObject json = new JSONObject();
		Hashtable ht = new Hashtable();
		ht = (Hashtable)objPage;
		String SPAGETYPE = ht.get("SPAGETYPE").toString();
		if("9".equalsIgnoreCase(SPAGETYPE)) {
			json.put("SFIELDSIZE", ht.get("SFIELDSIZE").toString().replace("$", "."));
			json.put("SPAGENAME", ht.get("SPAGENAME").toString());
			JSONArray json_son = new JSONArray();
			json_son.put(getPageField(ht.get("SFIELDCODE").toString()));//主表编号
			json_son.put(getPageField(ht.get("SFIELDNAME").toString()));//子表编号
			json.put("children", json_son);
			//SFIELDSIZE  分割条件   主子表关系
		}else {	
			json.put("SPAGENAME", ht.get("SPAGENAME").toString());
			json.put("SEDITPAGE", ht.get("SEDITPAGE").toString().replace("$", "."));
			
			json.put("SFIELDNAME", ht.get("SFIELDNAME").toString().replace("$", "."));
			json.put("SFIELDCODE", ht.get("SFIELDCODE").toString().replace("$", "."));
		}

		return json;
	}
	public String getJsonToString(String _left,String _right) throws JSONException{
		JSONObject json = new JSONObject();
		json.put("left", getPageField(_left));
		json.put("right", getPageField(_right));
		return json.toString();
	}
}
