package com.timing.impcl;

import java.util.UUID;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.timing.impcl.MantraLog;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.db.DBFactory;
import com.yulongtao.pub.Pub;
import com.yulongtao.util.EString;
import com.timing.impcl.RelationVO;

/*
*chars 字符数组
*getShortUuid  得到22位UUID  <待优化>
*getFlowVer  得到班前表单的 版本号    _formNum   表单号    _bmid  所属组织编码
*getBranchLeader  得到部门负责人   _bmid   所属组织编码
*
*
*
*recordRel   建立左右表关系   LEFT_ID 左表ID    LEFT_NAME左表表明     RIGHT_ID右表id     RIGHT_NAME右表表明
*
**/
public class MantraUtil {
	public static final String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
			"n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7",
			"8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
			"T", "U", "V", "W", "X", "Y", "Z", "-", "_" };
	private String ordGreId = "";
	private String ordUUID = "";

	/*
	 * 36位UUID中有4个- 去掉后 得32个
	 * 
	 * 32-22
	 * 
	 * 双向去掉两位 30-20 每3个变成2个
	 * 
	 */
	public String getShortUuid() {
		StringBuffer shortBuffer = new StringBuffer();
		String uuid = UUID.randomUUID().toString().replace("-", ""); // 36位-4
		for (int i = 0; i < 10; i++) { // 3 *10 =30
			String str = uuid.substring(i * 3, i * 3 + 3);
			int x = Integer.parseInt(str, 16); // 十六进制
			shortBuffer.append(chars[x / 0x40]); // /64 取整
			shortBuffer.append(chars[x % 0x40]); // 64取余
		}
		// 留下最后两位不变
		shortBuffer.append(uuid.charAt(30));
		shortBuffer.append(uuid.charAt(31));
		return shortBuffer.toString();
	}

	// 2018-03-15 15:29:29 修改
	public String getFlowVer(String _fromNum, String _bmid) {
		StringBuffer sr = new StringBuffer();
		TableEx tableEx = null;
		_fromNum = _fromNum == null ? "" : _fromNum;
		_bmid = _bmid == null ? "" : _bmid;
		try {
			tableEx = new TableEx("S_AUDIT_VERSION", "T_SYS_FLOW_MAIN",
					" S_FORMS =\"" + _fromNum + "\" and I_FLOWSTATUS = '0' and S_ORG_ID=\"" + _bmid
							+ "\" order by S_AUDIT_VERSION desc limit 1 ");
			int iRecordC = tableEx.getRecordCount();
			Record record;
			for (int i = 0; i < iRecordC; i++) {
				record = tableEx.getRecord(i);
				sr.append(record.getFieldByName("S_AUDIT_VERSION").value);
			}
		} catch (Exception e) {
			// MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null) {
				tableEx.close();
			}
		}
		return sr.toString();
	}

	public String[] getBranchLeader(String _bmid) { // 半成品 中文 未翻译
		if ("".equals(_bmid)) {
			return null;
		}
		Pub pub = new Pub();
		DBFactory dbf = new DBFactory();
		Record record = null;
		TableEx tableEx = null;
		String returnStr[] = { "", "" };
		try {
			tableEx = dbf.query(
					"select T_SYS_BRANCH.S_LEADER S_LEADER from T_SYS_BRANCH where T_SYS_BRANCH.S_CODE = " + _bmid);
			int iRecordC = tableEx.getRecordCount();
			for (int i = 0; i < iRecordC; i++) {
				record = tableEx.getRecord(i);
				String leader = record.getFieldByName("S_LEADER").value.toString();
				returnStr[0] = pub.getDic("T_RGXX", leader)[0];
				returnStr[1] = leader;
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null)
				dbf.close();
			if (tableEx != null)
				tableEx.close();
		}
		return returnStr;
	}

	public StringBuffer getTableCol(String _tableName) {
		StringBuffer sbf = new StringBuffer();
		String inputOptionElement;
		Record record = null;
		TableEx tableEx = null;
		sbf.append("<select name='" + _tableName + "'>");
		sbf.append(
				"<optgroup label='\u53d8\u91cf' style='color:red'><option value='<<UUID>>'>\u8fd0\u884c\u0049\u0044\u53f7</option><option value='<<FLOW_ID>>'>\u6d41\u7a0b\u7248\u672c\u53f7</option><option value='<<SYS_GENER_ID>>'>\u6d41\u6c34\u53f7</option></optgroup>");

		try {
			tableEx = new TableEx("SITEMCODE,SITEMNAME", "t_sys_item", "STYPECODE='" + _tableName + "'");
			int iRecordC = tableEx.getRecordCount();
			for (int i = 0; i < iRecordC; i++) {
				record = tableEx.getRecord(i);
				sbf.append("<option value='" + record.getFieldByName("SITEMCODE").value.toString() + "'>"
						+ record.getFieldByName("SITEMNAME").value.toString() + "</option>");
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
		}
		sbf.append("</select>");
		return sbf;
	}

	public String getPageFieldStr(String _formId) {
		if (_formId == "") {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		String[][] PageField = new Pub().getPageField(_formId);
		String[] onePageField = PageField[0];
		String[] twoPageField = PageField[1];
		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "&onePageField.length" + onePageField.length);
		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "&twoPageField.length" + twoPageField.length);
		// if(onePageField.length == twoPageField.length && onePageField.length!=1){
		// sb.append(_formId+"|");
		// for(int i = 0 , j = onePageField.length ; i < j ; i ++){
		// sb.append(onePageField[0]);
		// sb.append(":");
		// sb.append(onePageField[1]);
		// }
		// }else{
		// sb.append(getPageFieldStr(PageField[0][0]));
		// sb.append("&");
		// for(int i = 0 , j = twoPageField.length ; i < j ; i ++){
		// sb.append(getPageFieldStr(twoPageField[j]));
		// sb.append("&");
		// }
		// }
		return sb.toString();
	}

	/**
	 * 
	 * @param S_ORGANISATION
	 * @param S_LEFT_PAGECODE
	 * @param LEFT_ID
	 * @param LEFT_NAME
	 * @param S_RIGHT_PAGECODE
	 * @param RIGHT_ID
	 * @param RIGHT_NAME
	 */
	public void recordRel(String S_ORGANISATION, String S_LEFT_PAGECODE, String LEFT_ID, String LEFT_NAME,
			String S_RIGHT_PAGECODE, String RIGHT_ID, String RIGHT_NAME) {
		// 独立主键 S_ID
		// 左关系ID S_LEFT_ID
		// 左表字段 S_LEFT_TABLE_COL
		// 左表 S_LEFT_TABLE_NAME
		// 右关系ID S_RIGHT_ID
		// 右表表字段 S_RIGHT_TABLE_COL
		// 右表 S_RIGHT_TABLE_NAME
		// 所属组织 S_ORGANISATION
		// 左表表单号 S_LEFT_PAGECODE
		// 右表表单号 S_RIGHT_PAGECODE

		StringBuffer runSqlExe = new StringBuffer();
		DBFactory dbf = new DBFactory();

		runSqlExe.append("insert into ");
		runSqlExe.append("T_YWSXYGX ");
		runSqlExe.append(
				"(S_ID,S_LEFT_ID,S_LEFT_TABLE_NAME,S_RIGHT_ID,S_RIGHT_TABLE_NAME,S_ORGANISATION,S_LEFT_PAGECODE,S_RIGHT_PAGECODE,S_CREATE_DATE ) ");
		runSqlExe.append("values ");
		runSqlExe.append("(");
		runSqlExe.append("'" + EString.generId() + "',");
		runSqlExe.append("'" + LEFT_ID + "',");
		runSqlExe.append("'" + LEFT_NAME + "',");
		runSqlExe.append("'" + RIGHT_ID + "',");
		runSqlExe.append("'" + RIGHT_NAME + "',");
		runSqlExe.append("'" + S_ORGANISATION + "',");
		runSqlExe.append("'" + S_LEFT_PAGECODE + "',");
		runSqlExe.append("'" + S_RIGHT_PAGECODE + "',");
		runSqlExe.append("date_format(now(),\"%Y-%m-%d %h:%i:%s\")");
		runSqlExe.append(")");

		try {
			dbf.sqlExe(runSqlExe.toString(), false);
		} catch (Exception e) {
			// MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null)
				dbf.close();
		}
	}

	// 2018-03-16 17:29:08 数据整理
	public List<RelationVO> getDataByRel(String _direction, String _id, String _table, int layerNumber) { // 左找右 或者 右找左
																											// 然后字段对应 修改
		DBFactory dbf = new DBFactory();
		TableEx tableEx = null;
		Record record = null;
		List<RelationVO> relVoList = new ArrayList<RelationVO>();
		int layerNu = 0;
		String sql = "";
		switch (_direction) {
		case "LEFT":
			sql = "select T_YWSXYGX.S_CREATE_DATE T_YWSXYGX__S_CREATE_DATE,T_YWSXYGX.S_ID T_YWSXYGX__S_ID,T_YWSXYGX.S_LEFT_ID T_YWSXYGX__S_LEFT_ID,T_YWSXYGX.S_LEFT_TABLE_COL T_YWSXYGX__S_LEFT_TABLE_COL,T_YWSXYGX.S_LEFT_TABLE_NAME T_YWSXYGX__S_LEFT_TABLE_NAME,T_YWSXYGX.S_RIGHT_ID T_YWSXYGX__S_RIGHT_ID,T_YWSXYGX.S_RIGHT_TABLE_COL T_YWSXYGX__S_RIGHT_TABLE_COL,T_YWSXYGX.S_RIGHT_TABLE_NAME T_YWSXYGX__S_RIGHT_TABLE_NAME from T_YWSXYGX where S_LEFT_ID='"
					+ _id + "' and S_LEFT_TABLE_NAME='" + _table + "'";
			break;
		case "RIGHT":
			sql = "select T_YWSXYGX.S_CREATE_DATE T_YWSXYGX__S_CREATE_DATE,T_YWSXYGX.S_ID T_YWSXYGX__S_ID,T_YWSXYGX.S_LEFT_ID T_YWSXYGX__S_LEFT_ID,T_YWSXYGX.S_LEFT_TABLE_COL T_YWSXYGX__S_LEFT_TABLE_COL,T_YWSXYGX.S_LEFT_TABLE_NAME T_YWSXYGX__S_LEFT_TABLE_NAME,T_YWSXYGX.S_RIGHT_ID T_YWSXYGX__S_RIGHT_ID,T_YWSXYGX.S_RIGHT_TABLE_COL T_YWSXYGX__S_RIGHT_TABLE_COL,T_YWSXYGX.S_RIGHT_TABLE_NAME T_YWSXYGX__S_RIGHT_TABLE_NAME from T_YWSXYGX where S_RIGHT_ID='"
					+ _id + "' and S_RIGHT_TABLE_NAME='" + _table + "'";
			break;
		}
		try {
			tableEx = dbf.query(sql);
			for (int i = 0, j = tableEx.getRecordCount(); i < j; i++) {
				RelationVO relVo = new RelationVO();
				record = tableEx.getRecord(i);
				relVo.setS_CREATE_DATE(getBillDataToString(record, "T_YWSXYGX__S_CREATE_DATE"));
				relVo.setS_ID(getBillDataToString(record, "T_YWSXYGX__S_ID"));
				relVo.setS_LEFT_ID(getBillDataToString(record, "T_YWSXYGX__S_LEFT_ID"));
				relVo.setS_LEFT_TABLE_COL(getBillDataToString(record, "T_YWSXYGX__S_LEFT_TABLE_COL"));
				relVo.setS_LEFT_TABLE_NAME(getBillDataToString(record, "T_YWSXYGX__S_LEFT_TABLE_NAME"));
				relVo.setS_RIGHT_ID(getBillDataToString(record, "T_YWSXYGX__S_RIGHT_ID"));
				relVo.setS_RIGHT_TABLE_COL(getBillDataToString(record, "T_YWSXYGX__S_RIGHT_TABLE_COL"));
				relVo.setS_RIGHT_TABLE_NAME(getBillDataToString(record, "T_YWSXYGX__S_RIGHT_TABLE_NAME"));

				layerNu = layerNumber - 1;
				if (layerNu == -1) {
					if ("LEFT".equalsIgnoreCase(_direction)) {
						relVo.setRelationVOs(
								getDataByRel(_direction, relVo.getS_RIGHT_ID(), relVo.getS_RIGHT_TABLE_NAME(), 0));
					} else {
						relVo.setRelationVOs(
								getDataByRel(_direction, relVo.getS_LEFT_ID(), relVo.getS_LEFT_TABLE_NAME(), 0));
					}
				} else if (layerNu != 0) {
					if ("LEFT".equalsIgnoreCase(_direction)) {
						relVo.setRelationVOs(getDataByRel(_direction, relVo.getS_RIGHT_ID(),
								relVo.getS_RIGHT_TABLE_NAME(), layerNu));
					} else {
						relVo.setRelationVOs(
								getDataByRel(_direction, relVo.getS_LEFT_ID(), relVo.getS_LEFT_TABLE_NAME(), layerNu));
					}
				}

				relVoList.add(relVo);
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			return null;
		} finally {
			if (dbf != null)
				dbf.close();
			if (tableEx != null)
				tableEx.close();
		}
		return relVoList;
	}

	public String getBillDataToString(Record record, String str) {
		try {
			return record.getFieldByName(str).value.toString();
		} catch (Exception e) {
			return "";
		}
	}

	// 2018-03-15 15:29:29 新增
	public String sqlDisCom(String _str, String _term) {
		int i = _str.indexOf("<<");
		String prtn = "";
		int b = -1;
		if (i > -1) {
			prtn = _str.substring(i + 2);
			b = prtn.indexOf(">>");
			if (b > -1) {
				prtn = prtn.substring(0, b);
				_str = _str.replace("<<" + prtn + ">>", repT(prtn, sqlCondSepa(_term)));
				_str = sqlDisCom(_str, _term);
			}
		}
		return _str;
	}

	private String repT(String _str, HashMap<String, String> _termExtract) {
		String returnValue = "'";
		switch (_str) {
		case "UUID":
			this.ordUUID = this.getShortUuid();
			returnValue += this.ordUUID;
			break;
		case "SYS_GENER_ID":
			this.ordGreId = EString.generId();
			returnValue += this.ordGreId;
			break;
		case "FLOW_ID":
			returnValue += getFlowVer(_termExtract.get("pageCode"), _termExtract.get("bmid"));
			break;
		case "Branch_Leader":
			// returnValue += getFlowVer(_pageCode,_bmid);
			break;
		case "Branch_Leader_Name":
			// returnValue += getFlowVer(_pageCode,_bmid);
			break;
		default:
			returnValue += _termExtract.get(_str) == null ? "" : _termExtract.get(_str);
			break;
		}
		return returnValue + "'";
	}

	public int getDateCont(String _strTableName, String _sqlCond) {
		int retInt = 0;
		TableEx tableEx = null;
		try {
			tableEx = new TableEx("count(1) cont", _strTableName, _sqlCond);
			retInt = Integer.parseInt(tableEx.getRecord(0).getFieldByName("cont").value.toString());
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null) {
				tableEx.close();
			}
		}
		return retInt;
	}

	public String getOrdGreId() {
		return this.ordGreId;
	}

	public String getUUID() {
		return this.ordUUID;
	}

	private HashMap<String, String> sqlCondSepa(String _cond) {
		HashMap<String, String> retMap = new HashMap<String, String>();
		try {
			String[] _condAttr = _cond.split("&");
			for (int i = 0, j = _condAttr.length; i < j; i++) {
				String[] _sple = _condAttr[i].split("=");
				retMap.put(_sple[0], _sple[1]);
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		return retMap;
	}

	public void runSqlEx(String _str) {
		DBFactory dbf = new DBFactory();
		MantraLog.WriteProgress(MantraLog.LOG_PROGRESS, "&_str&" + _str + "&");
		try {
			dbf.sqlExe(_str, false);
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);

		} finally {

			dbf.close();
		}

	}

	public void delAss(String S_ID, String S_RUN_ID, String S_FLOW_ID, String S_AUTO_VER) {
		DBFactory dbf = new DBFactory();
		try {
			dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID='" + S_FLOW_ID + "' AND S_RUN_ID='" + S_RUN_ID
					+ "' AND S_AUDIT_VERSION='" + S_AUTO_VER + "' ;", true);
			dbf.sqlExe("delete from T_SYS_FLOW_LOG where S_FLOW_ID='" + S_FLOW_ID + "' AND S_RUN_ID='" + S_RUN_ID
					+ "' AND S_AUDIT_VERSION='" + S_AUTO_VER + "' ;", true);
			dbf.sqlExe("delete from T_MSG_RECORDS where S_LCID='" + S_FLOW_ID + "' AND S_YXID='" + S_RUN_ID + "' ;",
					true);
			dbf.sqlExe("delete from T_YWSXYGX where S_RIGHT_ID='" + S_ID + "';", true);
			dbf.sqlExe("delete from T_YWSXYGX where S_LEFT_ID='" + S_ID + "';", false);
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null) {
				dbf.close();
			}
		}
	}

	public void flowOverDelMsg(String S_RUN_ID, String S_FLOW_ID, String S_AUTO_VER) {
		DBFactory dbf = new DBFactory();
		try {
			dbf.sqlExe("delete from T_MSG_RECORDS where S_LCID='" + S_FLOW_ID + "' AND S_YXID='" + S_RUN_ID + "' ;",
					false);
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (dbf != null) {
				dbf.close();
			}
		}
	}

	public String getStrByRecord(Record _rec, String _value) {
		String _retvalue = "";
		if (_rec.getFieldByName(_value).value != null) {
			_retvalue = _rec.getFieldByName(_value).value.toString();
		}
		return _retvalue;
	}

	public void setMsgRecord(String _id, String _bmid, String _spagecode, String _msg, String _runId) {

		String sql = "";

	}
	
	public String codingToGbk(String _strValue) {
		try {
			return new String(_strValue.getBytes("UTF-8"),"GBK");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public void etMsgRecord() {
		MantraUtil tool =new MantraUtil();
		String sql = "select T_BZGZRZ.S_BM T_BZGZRZ__S_BM,T_BZGZRZ.S_BZ T_BZGZRZ__S_BZ,T_BZGZRZ.SYS_FLOW_VER T_BZGZRZ__SYS_FLOW_VER,T_BZGZRZ.S_ZZ T_BZGZRZ__S_ZZ,T_BZGZRZ.S_TXRQ T_BZGZRZ__S_TXRQ,T_BZGZRZ.S_XQ T_BZGZRZ__S_XQ,T_BZGZRZ.S_TQ T_BZGZRZ__S_TQ,T_BZGZRZ.S_BZRS T_BZGZRZ__S_BZRS,T_BZGZRZ.S_CQRS T_BZGZRZ__S_CQRS,T_BZGZRZ.S_CQL T_BZGZRZ__S_CQL,T_BZGZRZ.S_BJ T_BZGZRZ__S_BJ,T_BZGZRZ.S_SJ T_BZGZRZ__S_SJ,T_BZGZRZ.S_CD T_BZGZRZ__S_CD,T_BZGZRZ.S_ZT T_BZGZRZ__S_ZT,T_BZGZRZ.S_KG T_BZGZRZ__S_KG,T_BZGZRZ.S_HX T_BZGZRZ__S_HX,T_BZGZRZ.S_GCRY T_BZGZRZ__S_GCRY,T_BZGZRZ.S_YJZBRY T_BZGZRZ__S_YJZBRY	 from T_BZGZRZ where S_ID='152894022615110036'";
		String value = "T_BZGZRZ__S_BM,T_BZGZRZ__S_BZ,T_BZGZRZ__S_TXRQ,T_BZGZRZ__S_TXRQ,T_BZGZRZ__S_TXRQ,T_BZGZRZ__S_XQ,T_BZGZRZ__S_TQ,T_BZGZRZ__S_BZRS,T_BZGZRZ__S_CQRS,T_BZGZRZ__S_CQL,T_BZGZRZ__S_BJ,T_BZGZRZ__S_SJ,T_BZGZRZ__S_CD,T_BZGZRZ__S_ZT,T_BZGZRZ__S_KG,T_BZGZRZ__S_HX,T_BZGZRZ__S_GCRY,T_BZGZRZ__S_YJZBRY";
		String[] varArr = value.split(",");
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf =new DBFactory();
		HashMap hm = new HashMap();
		try {
			tableEx = dbf.query(sql);
			for(int i = 0 ; i < tableEx.getRecordCount() ; i++) {
				record = tableEx.getRecord(i);
				for(int j = 0 ; j < varArr.length ; j++) {
					hm.put(j, getStrByRecord(record, varArr[j]));
				}
				
			}
			
			
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
	}

}
