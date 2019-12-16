package com.timing.impcl;

import javax.servlet.http.HttpServletRequest;

import com.timing.util.MisLogger;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

public class CheckTool {
	private static MisLogger logger = new MisLogger(CheckTool.class);
	MantraUtil mu = new MantraUtil();

	public static void main(String[] args) {

		// try {
		// long start = System.currentTimeMillis();
		// Process process = Runtime.getRuntime().exec(
		// new String[] { "wmic", "cpu", "get", "ProcessorId" });
		// process.getOutputStream().close();
		// Scanner sc = new Scanner(process.getInputStream());
		// String property = sc.next();
		// String serial = sc.next();
		// System.out.println(property + ": " + serial);
		// System.out.println("time:" + (System.currentTimeMillis() - start));
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

//		String a = "1505813861487";
//		for(int i = 0 , j = a.length() ; i < j ; i++) {
//			System.out.print(a.charAt(i)^7);
//		}
	
	}

	/*
	 * 字段对应
	 * 
	 * 
	 */
	public boolean checkToolBusinessDeal(String _planPk) {
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record;
		String T_DJJH__S_ZZ, T_DJJH_BS__S_DJLB, autoS_id;
		StringBuffer returnSbf = new StringBuffer();
		String runId="";
		String runVersion="";
		// select
		// T_DJJH.S_ZZ T_DJJH__S_ZZ,
		// T_DJJH_BS.S_DJLB T_DJJH_BS__S_DJLB,
		// group_concat(T_DJJH_BS.S_ID) s
		// from T_DJJH left join T_DJJH_BS
		// on T_DJJH.S_ID = T_DJJH_BS.S_DJJHID
		// where T_DJJH.S_ID = "1515658857216"
		// group by T_DJJH_BS.S_DJLB

		// mu.getShortUuid();
		if ("".equals(_planPk)) {
			return false;
		}
        logger.debug("checkToolId:"+_planPk);
		StringBuffer sbf = new StringBuffer();
		sbf.append("select ");
		sbf.append(" T_DJJH.S_ZZ T_DJJH__S_ZZ,");
		sbf.append(" T_DJJH_BS.S_DJLB T_DJJH_BS__S_DJLB ,");
		sbf.append(" group_concat(T_DJJH_BS.S_ID) autoS_id ");
		sbf.append(" from T_DJJH left join T_DJJH_BS ");
		sbf.append(" on T_DJJH.S_ID = T_DJJH_BS.S_DJJHID ");
		sbf.append(" where T_DJJH.S_ID = '" + _planPk + "' ");
		sbf.append(" group by T_DJJH_BS.S_DJLB ");
		try {
			tableEx = dbf.query(sbf.toString());
			String UuId = System.currentTimeMillis() + "";
			int iRecordC = tableEx.getRecordCount();
			for (int i = 0; i < iRecordC; i++) {
				record = tableEx.getRecord(i);
			
				// insert into T_LCJHBZ (S_ID,S_ZZ) values ("<<S_ID>>","<<bmid>>");
				// update T_LC_GYZB set T_LC_GYZB.S_JHID="<<S_ID>>" where T_LC_GYZB.S_ID in
				// (<<autoId>>);
				// djtype = 1 日常
				// djtype = 2专业and精密
				//1505813981968   日常点检
				//1505813861487   精密点检
				//String runId="";
		        //String runVersion="";
		        //1506155869228   日常点检 表单
                //1507393584583   精密点检 表单
				T_DJJH_BS__S_DJLB = getBillDataToString(record, "T_DJJH_BS__S_DJLB");
				T_DJJH__S_ZZ = getBillDataToString(record, "T_DJJH__S_ZZ");
				autoS_id = getBillDataToString(record, "autoS_id");
				runId = mu.getShortUuid(); //得到  短版UUID  22
				//S_RUN_ID 运行ID  	SYS_FLOW_VER  版本
				//运行版本 getFlowVer(String _fromNum,String _bmid)
				if("1505813981968".equals(T_DJJH_BS__S_DJLB)) {  //日常
				    runVersion = mu.getFlowVer("1506155869228",T_DJJH__S_ZZ);
					dbf.sqlExe("insert into T_ZYDJ (S_ID,S_ZZ,S_TYPE,S_RUN_ID,SYS_FLOW_VER) values (\"" + UuId + i+ "\",\"" + T_DJJH__S_ZZ + "\",1,'"+runId+"','"+runVersion+"');", false);
				
				mu.recordRel(T_DJJH__S_ZZ, "1505896107531",_planPk, "T_DJJH", "1506155869228",UuId +""+ i,
						"T_ZYDJ");//创建关系
				    
				}else {//专业和精
				    runVersion = mu.getFlowVer("1507393584583",T_DJJH__S_ZZ);
					dbf.sqlExe("insert into T_ZYDJ (S_ID,S_ZZ,S_TYPE,S_RUN_ID,SYS_FLOW_VER) values (\"" + UuId + i +"\",\"" + T_DJJH__S_ZZ + "\",2,'"+runId+"','"+runVersion+"');", false);
			
				mu.recordRel(T_DJJH__S_ZZ, "1505896107531",_planPk, "T_DJJH", "1507393584583", UuId +""+ i,
						"T_ZYDJ");//创建关系
				    
				}
				//T_DJJH_BS
				  
						
				dbf.sqlExe("update T_DJJH_BS set T_DJJH_BS.S_RCOZY_ID ='"+UuId + i +"' where T_DJJH_BS.S_ID in (" + autoS_id + ");", false);
			}
			return true;
		
		
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		return false;
	}
    
    public boolean inspectionPlanGeneratesRecords(HttpServletRequest _request) {
    //     select 
    // T_XJJHMX.S_XJSX S_XJSX,
    // T_XJLX.S_XJDBM S_XJDBM,
    // T_XJLX.S_XJDMC S_XJDMC,
    // T_XJXM.S_XBBM S_XBBM,  
    // T_XJXM.S_XMMC S_XMMC,
    // T_XJXM.S_BZZ S_BZZ,
    // T_XJXM.S_JLDW S_JLDW
    // from T_XJJH
    // LEFT JOIN T_XJJHMX ON T_XJJH.S_ID = T_XJJHMX.S_FID
    // LEFT JOIN T_XJLX ON T_XJJHMX.S_XJDID = T_XJLX.S_ID
    // LEFT JOIN T_XJLXZXM ON T_XJLX.S_ID = T_XJLXZXM.S_FID
    // LEFT JOIN T_XJXM ON T_XJLXZXM.S_XJXMID = T_XJXM.S_ID
    // WHERE T_XJJH.S_ID = '1516589294844' ORDER BY T_XJJHMX.S_XJSX,T_XJLX.S_XJDBM .
    

                //T_XJJLMX$S_XJDSX
                // T_XJJLMX$S_XJDBM
                // T_XJJLMX$S_XJDMC
                // T_XJJLMX$S_XJXMBM
                // T_XJJLMX$S_XJXMMC
                // T_XJJLMX$S_BZZ
                // T_XJJLMX$S_JLDW
        TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record;
		StringBuffer sqlJoint = new StringBuffer();
		sqlJoint.append("select ");
        sqlJoint.append("T_XJJHMX.S_XJSX S_XJSX,");
        sqlJoint.append("T_XJLX.S_XJDBM S_XJDBM,");
        sqlJoint.append("T_XJLX.S_XJDMC S_XJDMC,");
        sqlJoint.append("T_XJXM.S_XBBM S_XBBM, ");
        sqlJoint.append("T_XJXM.S_XMMC S_XMMC,");
        sqlJoint.append("T_XJXM.S_BZZ S_BZZ,");
        sqlJoint.append("T_XJXM.S_JLDW S_JLDW");
        sqlJoint.append(" from T_XJJH");
        sqlJoint.append(" LEFT JOIN T_XJJHMX ON T_XJJH.S_ID = T_XJJHMX.S_FID ");
        sqlJoint.append(" LEFT JOIN T_XJLX ON T_XJJHMX.S_XJDID = T_XJLX.S_ID ");
        sqlJoint.append(" LEFT JOIN T_XJLXZXM ON T_XJLX.S_ID = T_XJLXZXM.S_FID ");
        sqlJoint.append(" LEFT JOIN T_XJXM ON T_XJLXZXM.S_XJXMID = T_XJXM.S_ID ");
        sqlJoint.append(" WHERE T_XJJH.S_ID = '"+_request.getParameter("S_ID")+"'  ORDER BY T_XJJHMX.S_XJSX,T_XJLX.S_XJDBM ");
        try {
			tableEx = dbf.query(sqlJoint.toString());
			int iRecordC = tableEx.getRecordCount();
			String createDataID = System.currentTimeMillis() + "";
			dbf.sqlExe("insert into T_XJJL (S_ID,S_ZZ) values ('"+createDataID+"','"+_request.getParameter("bmid")+"');", false);
			for(int i = 0 ; i < iRecordC ; i++){
			    record = tableEx.getRecord(i);
			    dbf.sqlExe("insert into T_XJJLMX (S_ID,S_FID,S_XJDSX,S_XJDBM,S_XJDMC,S_XJXMBM,S_XJXMMC,S_BZZ,S_JLDW) values ('"+i+"','"+createDataID+"','"+getBillDataToString(record, "S_XJSX")+"','"+getBillDataToString(record, "S_XJDBM")+"','"+getBillDataToString(record, "S_XJDMC")+"','"+getBillDataToString(record, "S_XBBM")+"','"+getBillDataToString(record, "S_XMMC")+"','"+getBillDataToString(record, "S_BZZ")+"','"+getBillDataToString(record, "S_JLDW")+"');", false);
			}
        } catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		return false;
    }
	public String getBillDataToString(Record record, String str) {
		return record.getFieldByName(str).value.toString();
	}	
	
}
