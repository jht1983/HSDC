package com.timing.impcl;

import java.util.List;
import java.util.ArrayList;

public class RelationVO {
	private String S_CREATE_DATE;
	private String S_ID;
	private String S_LEFT_ID;
	private String S_LEFT_TABLE_COL;
	private String S_LEFT_TABLE_NAME;
	private String S_RIGHT_ID;
	private String S_RIGHT_TABLE_COL;
	private String S_RIGHT_TABLE_NAME;
	private List<RelationVO> RelationVOs = new ArrayList<RelationVO>();
	
	public List<RelationVO> getRelationVOs() {
		return RelationVOs;
	}
	public void setRelationVOs(List<RelationVO> relationVOs) {
		RelationVOs = relationVOs;
	}
	public String getS_CREATE_DATE() {
		return S_CREATE_DATE;
	}
	public void setS_CREATE_DATE(String s_CREATE_DATE) {
		S_CREATE_DATE = s_CREATE_DATE;
	}
	public String getS_ID() {
		return S_ID;
	}
	public void setS_ID(String s_ID) {
		S_ID = s_ID;
	}
	public String getS_LEFT_ID() {
		return S_LEFT_ID;
	}
	public void setS_LEFT_ID(String s_LEFT_ID) {
		S_LEFT_ID = s_LEFT_ID;
	}
	public String getS_LEFT_TABLE_COL() {
		return S_LEFT_TABLE_COL;
	}
	public void setS_LEFT_TABLE_COL(String s_LEFT_TABLE_COL) {
		S_LEFT_TABLE_COL = s_LEFT_TABLE_COL;
	}
	public String getS_LEFT_TABLE_NAME() {
		return S_LEFT_TABLE_NAME;
	}
	public void setS_LEFT_TABLE_NAME(String s_LEFT_TABLE_NAME) {
		S_LEFT_TABLE_NAME = s_LEFT_TABLE_NAME;
	}
	public String getS_RIGHT_ID() {
		return S_RIGHT_ID;
	}
	public void setS_RIGHT_ID(String s_RIGHT_ID) {
		S_RIGHT_ID = s_RIGHT_ID;
	}
	public String getS_RIGHT_TABLE_COL() {
		return S_RIGHT_TABLE_COL;
	}
	public void setS_RIGHT_TABLE_COL(String s_RIGHT_TABLE_COL) {
		S_RIGHT_TABLE_COL = s_RIGHT_TABLE_COL;
	}
	public String getS_RIGHT_TABLE_NAME() {
		return S_RIGHT_TABLE_NAME;
	}
	public void setS_RIGHT_TABLE_NAME(String s_RIGHT_TABLE_NAME) {
		S_RIGHT_TABLE_NAME = s_RIGHT_TABLE_NAME;
	}
	
}
