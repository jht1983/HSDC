package com.timing.impcl;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.util.EString;
import com.timing.impcl.MantraUtil;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
    /*
    *   安全性查评项目
    */
public class SafetyInquireTool {
	ProcessParameterVO proParVo = null;
	MantraUtil tool = new MantraUtil();
	//MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,primaryKey);
    // 汇总生成部门
	public void SafetyInquireSum(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null && primaryKey.trim().length() > 0){
			return;
	    }
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		String S_ZZ,S_FZBM,S_FZBM_BM,sonId,S_ID,SON_S_ID;
		String sql = "select T_AQXCPXMHZ.S_ZZ S_ZZ,T_AQXCPHZ_SON.S_FZBM S_FZBM,T_AQXCPHZ_SON.S_FZBM_BM S_FZBM_BM,group_concat(T_AQXCPHZ_SON.S_ID) sonId from T_AQXCPXMHZ left join T_AQXCPHZ_SON on T_AQXCPXMHZ.S_ID=T_AQXCPHZ_SON.S_FID where T_AQXCPXMHZ.S_ID= '"+primaryKey+"' group by T_AQXCPHZ_SON.S_FZBM_BM";
		MantraUtil mu = new MantraUtil();
		String shortUUID="";
        String flowVer = "";

		try {
			tableEx = dbf.query(sql);
			int recordLen = tableEx.getRecordCount();
			for(int i = 0 ; i < recordLen ; i++) {
				record = tableEx.getRecord(i);
				S_ID=EString.generId();
				S_ZZ=record.getFieldByName("S_ZZ").value.toString();
				S_FZBM=record.getFieldByName("S_FZBM").value.toString();
				S_FZBM_BM=record.getFieldByName("S_FZBM_BM").value.toString();
				sonId=record.getFieldByName("sonId").value.toString();
				String [] sonIdArr = sonId.split(",");
				String [] Leander = mu.getBranchLeader(S_FZBM_BM);
				shortUUID = mu.getShortUuid();    // 可复制
				flowVer = mu.getFlowVer("151772091835710956",S_ZZ);  // 可复制
				
				dbf.sqlExe("insert into T_BMAQXCP "
						+ "(T_BMAQXCP.S_WXZZ,T_BMAQXCP.S_BMCPDBM,T_BMAQXCP.S_CPYT,T_BMAQXCP.S_DJRQ,T_BMAQXCP.S_CPZFZR,T_BMAQXCP.S_CPZFZRBM,T_BMAQXCP.S_CPRQ,T_BMAQXCP.S_FZBM,T_BMAQXCP.S_FZBM_BM,T_BMAQXCP.S_FZR,T_BMAQXCP.S_FZR_BM,T_BMAQXCP.S_ID,T_BMAQXCP.SYS_FLOW_VER,T_BMAQXCP.S_RUN_ID)"
				+ " select T_AQXCPXMHZ.S_ZZ,T_AQXCPXMHZ.S_CPHZD,T_AQXCPXMHZ.S_CPYT,T_AQXCPXMHZ.S_DJRQ,T_AQXCPXMHZ.S_CPZFZR,T_AQXCPXMHZ.S_CPZFZR_BM,T_AQXCPXMHZ.S_CPRQ,'"+S_FZBM+"','"+S_FZBM_BM+"','"+Leander[0]+"','"+Leander[1]+"','"+S_ID+"','"+flowVer+"','"+shortUUID+"' "
								+ "from T_AQXCPXMHZ where T_AQXCPXMHZ.S_ID = '"+primaryKey+"';", false);
				
				mu.recordRel(S_ZZ, request.getParameter("SPAGECODE"), primaryKey, "T_AQXCPXMHZ", "151772091835710956", S_ID, "T_BMAQXCP");
       			//mu.recordRel(primaryKey,"T_AQXCPXMHZ",S_ID,"T_BMAQXCP");
       			for(int j = 0 , m = sonIdArr.length ; j < m ; j++) {
				    SON_S_ID=S_ID+""+j;
				    dbf.sqlExe("insert into T_BMAQXCP_SON "
				    		+ "(T_BMAQXCP_SON.S_CPXMFZR,T_BMAQXCP_SON.S_CPXMFZRBM,T_BMAQXCP_SON.S_XMXH,T_BMAQXCP_SON.S_XMMC,T_BMAQXCP_SON.S_NR,T_BMAQXCP_SON.S_BZF,T_BMAQXCP_SON.S_PFBZ,T_BMAQXCP_SON.S_BCP,T_BMAQXCP_SON.S_SPF,T_BMAQXCP_SON.S_SDF,T_BMAQXCP_SON.S_KF,T_BMAQXCP_SON.S_DFL,T_BMAQXCP_SON.S_ZTPJ,T_BMAQXCP_SON.S_SFXYZG,T_BMAQXCP_SON.S_ID,T_BMAQXCP_SON.S_FID) "
				    		+ "select T_AQXCPHZ_SON.S_CPXMFZR,T_AQXCPHZ_SON.S_CPXMFZR_BM,T_AQXCPHZ_SON.S_XMXH,T_AQXCPHZ_SON.S_XMMC,T_AQXCPHZ_SON.T_NR,T_AQXCPHZ_SON.S_BZF,T_AQXCPHZ_SON.S_PFBZ,T_AQXCPHZ_SON.S_BCP,T_AQXCPHZ_SON.S_SPF,T_AQXCPHZ_SON.S_SDF,T_AQXCPHZ_SON.S_KF,T_AQXCPHZ_SON.S_DFL,T_AQXCPHZ_SON.T_ZTPJ,T_AQXCPHZ_SON.S_SFXYZG,'"+SON_S_ID+"','"+S_ID+"' "
				    				+ "from T_AQXCPHZ_SON where T_AQXCPHZ_SON.S_ID = '"+sonIdArr[j]+"';", false);
				    
				    mu.recordRel(S_ZZ, "", sonIdArr[j], "T_AQXCPHZ_SON","", SON_S_ID, "T_BMAQXCP_SON");
	       			
					//mu.recordRel(sonIdArr[j],"T_AQXCPHZ_SON",SON_S_ID,"T_BMAQXCP_SON");
				}
			} 
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(dbf!=null)
				dbf.close();
			if(tableEx!=null)
				tableEx.close();
		}
	}
	// 部门生成负责人   安全性查评
	public void SafetyInquireBranch(HttpServletRequest request) {  
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null && primaryKey.trim().length() > 0){
			return;
	    }
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		String S_ZZ,sonId,S_ID,FZR,FZR_BM,SON_S_ID;
		String sql = "select T_BMAQXCP.S_WXZZ S_ZZ,"
				+ "T_BMAQXCP_SON.S_CPXMFZR FZR ,"
				+ "T_BMAQXCP_SON.S_CPXMFZRBM FZR_BM, "
				+ "group_concat(T_BMAQXCP_SON.S_ID) sonId "
				+ "from T_BMAQXCP left join T_BMAQXCP_SON "
				+ "on T_BMAQXCP.S_ID=T_BMAQXCP_SON.S_FID "
				+ "where T_BMAQXCP.S_ID= '"+primaryKey+"' group by T_BMAQXCP_SON.S_CPXMFZRBM";

		MantraUtil mu = new MantraUtil();
		String shortUUID="";
        String flowVer = "";
		try {
			tableEx = dbf.query(sql);
			int recordLen = tableEx.getRecordCount();
			for(int i = 0 ; i < recordLen ; i++) {
				record = tableEx.getRecord(i);
				S_ID=EString.generId();
				FZR=record.getFieldByName("FZR").value.toString();
				FZR_BM=record.getFieldByName("FZR_BM").value.toString();
				S_ZZ=record.getFieldByName("S_ZZ").value.toString();
				sonId=record.getFieldByName("sonId").value.toString();
				String [] sonIdArr = sonId.split(",");
				
				shortUUID = mu.getShortUuid();    // 可复制
				flowVer = mu.getFlowVer("151772214830210983",S_ZZ);  // 可复制
				dbf.sqlExe("insert into T_FZRAQXCP "
						+ "(T_FZRAQXCP.S_WXZZ,T_FZRAQXCP.S_DJRQ,T_FZRAQXCP.S_FZRPFBM,T_FZRAQXCP.S_CPYT,T_FZRAQXCP.S_CPZFZR,T_FZRAQXCP.S_CPZFZR_BM,T_FZRAQXCP.S_CPRQ,T_FZRAQXCP.S_FZBM,T_FZRAQXCP.S_FZBM_BM,T_FZRAQXCP.S_FZR,T_FZRAQXCP.S_FZR_BM,T_FZRAQXCP.S_CPXMFZR,T_FZRAQXCP.S_CPXMFZR_BM,T_FZRAQXCP.S_ID,T_FZRAQXCP.SYS_FLOW_VER,T_FZRAQXCP.S_RUN_ID) "
						+ "select T_BMAQXCP.S_WXZZ,T_BMAQXCP.S_DJRQ,T_BMAQXCP.S_DJBM,T_BMAQXCP.S_CPYT,T_BMAQXCP.S_CPZFZR,T_BMAQXCP.S_CPZFZRBM,T_BMAQXCP.S_CPRQ,T_BMAQXCP.S_FZBM,T_BMAQXCP.S_FZBM_BM,T_BMAQXCP.S_FZR,T_BMAQXCP.S_FZR_BM,'"+FZR+"','"+FZR_BM+"','"+S_ID+"','"+flowVer+"','"+shortUUID+"' "
								+ "from T_BMAQXCP where T_BMAQXCP.S_ID = "+primaryKey, false);
				
				mu.recordRel(S_ZZ, request.getParameter("SPAGECODE"), primaryKey, "T_BMAQXCP", "151772214830210983", S_ID, "T_FZRAQXCP");
				//mu.recordRel(primaryKey,"T_BMAQXCP",S_ID,"T_FZRAQXCP");

				for(int j = 0 , m = sonIdArr.length ; j < m ; j++) {
					
					RelationVO leftData = (RelationVO)mu.getDataByRel("RIGHT",sonIdArr[j],"T_BMAQXCP_SON",1).get(0);

					SON_S_ID=S_ID+""+j;
					
					String runSqlExe = "insert into T_FZRAQXCP_SON"
							+ "(T_FZRAQXCP_SON.S_ID,T_FZRAQXCP_SON.S_FID,T_FZRAQXCP_SON.S_BZF,T_FZRAQXCP_SON.S_CPXMFZR,T_FZRAQXCP_SON.S_CPXMFZR_BM,T_FZRAQXCP_SON.S_DFL,T_FZRAQXCP_SON.S_KF,T_FZRAQXCP_SON.S_PFBZ,T_FZRAQXCP_SON.S_SDF,T_FZRAQXCP_SON.S_SFXYZG,T_FZRAQXCP_SON.S_SPF,T_FZRAQXCP_SON.T_NR,T_FZRAQXCP_SON.T_ZTPJ,T_FZRAQXCP_SON.S_XMXH,T_FZRAQXCP_SON.S_XMMC,T_FZRAQXCP_SON.S_BCP) " + 
						"select '"+SON_S_ID+"','"+S_ID+"',T_BMAQXCP_SON.S_BZF,T_BMAQXCP_SON.S_CPXMFZR,T_BMAQXCP_SON.S_CPXMFZRBM,T_BMAQXCP_SON.S_DFL,T_BMAQXCP_SON.S_KF,T_BMAQXCP_SON.S_PFBZ,T_BMAQXCP_SON.S_SDF,T_BMAQXCP_SON.S_SFXYZG,T_BMAQXCP_SON.S_SPF,T_BMAQXCP_SON.S_NR,T_BMAQXCP_SON.S_ZTPJ,T_BMAQXCP_SON.S_XMXH,T_BMAQXCP_SON.S_XMMC,T_BMAQXCP_SON.S_BCP from T_BMAQXCP_SON where T_BMAQXCP_SON.S_ID = '"+sonIdArr[j]+"'" ; 
					
					dbf.sqlExe(runSqlExe, false);
					
					mu.recordRel(S_ZZ, "", sonIdArr[j], "T_BMAQXCP_SON", "", SON_S_ID, "T_FZRAQXCP_SON");
					
					//mu.recordRel(sonIdArr[j],"T_BMAQXCP_SON",SON_S_ID,"T_FZRAQXCP_SON");
				
					runSqlExe = "update T_BMAQXCP_SON,T_AQXCPHZ_SON set  T_AQXCPHZ_SON.S_CPXMFZR = T_BMAQXCP_SON.S_CPXMFZR,T_AQXCPHZ_SON.S_CPXMFZR_BM=T_BMAQXCP_SON.S_CPXMFZRBM,T_AQXCPHZ_SON.S_KF=T_BMAQXCP_SON.S_KF where T_BMAQXCP_SON.S_ID = '"+sonIdArr[j]+"' and T_AQXCPHZ_SON.S_ID='" + leftData.getS_LEFT_ID() + "'";

					dbf.sqlExe(runSqlExe, false);
				}
				
			}
			
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(dbf!=null)
				dbf.close();
			if(tableEx!=null)
				tableEx.close();
		}
	}
	//负责人
	public void SafetyInquireLeader(HttpServletRequest request) {  
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null && primaryKey.trim().length() > 0){
			return;
	    }
		MantraUtil mu = new MantraUtil();
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		String sql="",S_ID="";
		try {
			sql = "select S_ID S_ID from T_FZRAQXCP_SON where S_FID='"+primaryKey+"'";
			tableEx = dbf.query(sql);
			for(int i = 0 , j = tableEx.getRecordCount() ; i < j ; i++) {
				record = tableEx.getRecord(i);
				S_ID = record.getFieldByName("S_ID").value.toString();
				RelationVO leftData = (RelationVO)mu.getDataByRel("RIGHT",S_ID,"T_FZRAQXCP_SON",2).get(0);
				sql = "update T_BMAQXCP_SON,T_FZRAQXCP_SON set T_BMAQXCP_SON.S_BZF=T_FZRAQXCP_SON.S_BZF,T_BMAQXCP_SON.S_CPXMFZR=T_FZRAQXCP_SON.S_CPXMFZR,T_BMAQXCP_SON.S_CPXMFZRBM=T_FZRAQXCP_SON.S_CPXMFZR_BM,T_BMAQXCP_SON.S_DFL=T_FZRAQXCP_SON.S_DFL,T_BMAQXCP_SON.S_KF=T_FZRAQXCP_SON.S_KF,T_BMAQXCP_SON.S_SDF=T_FZRAQXCP_SON.S_SDF,T_BMAQXCP_SON.S_SFXYZG=T_FZRAQXCP_SON.S_SFXYZG,T_BMAQXCP_SON.S_SPF=T_FZRAQXCP_SON.S_SPF,T_BMAQXCP_SON.S_ZTPJ=T_FZRAQXCP_SON.T_ZTPJ "
						+ "where T_BMAQXCP_SON.S_ID = '"+leftData.getS_LEFT_ID()+"' and T_FZRAQXCP_SON.S_ID='" + S_ID + "'";
				dbf.sqlExe(sql, false);
				List<RelationVO> relList = leftData.getRelationVOs();
				for(int m = 0 , n = relList.size() ; m < n ; m++) {
					RelationVO leftDataSon = relList.get(m);
					
					sql = "update T_AQXCPHZ_SON,T_FZRAQXCP_SON set T_AQXCPHZ_SON.S_BZF=T_FZRAQXCP_SON.S_BZF,T_AQXCPHZ_SON.S_CPXMFZR=T_FZRAQXCP_SON.S_CPXMFZR,T_AQXCPHZ_SON.S_CPXMFZR_BM=T_FZRAQXCP_SON.S_CPXMFZR_BM,T_AQXCPHZ_SON.S_DFL=T_FZRAQXCP_SON.S_DFL,T_AQXCPHZ_SON.S_FMJJD=T_FZRAQXCP_SON.S_FMJJD,T_AQXCPHZ_SON.S_MJBDBS=T_FZRAQXCP_SON.S_MJBDBS,T_AQXCPHZ_SON.S_SDF=T_FZRAQXCP_SON.S_SDF,T_AQXCPHZ_SON.S_SFXYZG=T_FZRAQXCP_SON.S_SFXYZG,T_AQXCPHZ_SON.S_SPF=T_FZRAQXCP_SON.S_SPF,T_AQXCPHZ_SON.S_KF=T_FZRAQXCP_SON.S_KF,T_AQXCPHZ_SON.T_ZTPJ=T_FZRAQXCP_SON.T_ZTPJ "
							+ "where T_AQXCPHZ_SON.S_ID = '"+leftDataSon.getS_LEFT_ID()+"' and T_FZRAQXCP_SON.S_ID='" + S_ID + "'";
					dbf.sqlExe(sql, false);
				}
			}	
		}catch(Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null) 
				dbf.close();
		}
	}
	// 汇总生成整改单
	public void SafetyInquireToRect(HttpServletRequest request) {  
		String [] basicData= {
				"T_AQXCPXMHZ.S_DJRQ,T_AQXCPXMHZ.S_CPHZD,T_AQXCPXMHZ.S_CPRQ,T_AQXCPXMHZ.S_CPYT,T_AQXCPXMHZ.S_CPZFZR,T_AQXCPXMHZ.S_ZZ,<<SYS_GENER_ID>>,<<FLOW_ID>>,<<UUID>>",
				"T_AQXCPZGD.S_DJRQ,T_AQXCPZGD.S_CPHZD,T_AQXCPZGD.S_CPRQ,T_AQXCPZGD.S_CPYT,T_AQXCPZGD.S_CPZFZR,T_AQXCPZGD.S_WXZZ,T_AQXCPZGD.S_ID,T_AQXCPZGD.SYS_FLOW_VER,T_AQXCPZGD.S_RUN_ID",
				"T_AQXCPHZ_SON.S_BCP,T_AQXCPHZ_SON.S_BZF,T_AQXCPHZ_SON.S_CPXMFZR,T_AQXCPHZ_SON.S_CPXMFZR_BM,T_AQXCPHZ_SON.S_DFL,T_AQXCPHZ_SON.S_FMJJD,T_AQXCPHZ_SON.S_JC,T_AQXCPHZ_SON.S_KF,T_AQXCPHZ_SON.S_SDF,T_AQXCPHZ_SON.S_SJXMXH,T_AQXCPHZ_SON.S_SPF,T_AQXCPHZ_SON.S_XH,T_AQXCPHZ_SON.S_XMXH,T_AQXCPHZ_SON.S_ZGFZR,T_AQXCPHZ_SON.S_ZGFZR_BM,T_AQXCPHZ_SON.S_ZGHPF,T_AQXCPHZ_SON.S_ZGPFR,T_AQXCPHZ_SON.S_ZGPFR_BM,T_AQXCPHZ_SON.S_ZGRQ,T_AQXCPHZ_SON.S_ZGYSR,T_AQXCPHZ_SON.S_ZGYSR_BM,T_AQXCPHZ_SON.T_ZTPJ,T_AQXCPHZ_SON.T_NR,T_AQXCPHZ_SON.S_PFBZ,T_AQXCPHZ_SON.S_XMMC,T_AQXCPHZ_SON.S_YSRYJ,T_AQXCPHZ_SON.S_ZGFAJJG,<<SYS_GENER_ID>>,<<FID>>",
				"T_AQXCPZGD_SON.S_BCP,T_AQXCPZGD_SON.S_BZF,T_AQXCPZGD_SON.S_CPXMFZR,T_AQXCPZGD_SON.S_CPXMFZR_BM,T_AQXCPZGD_SON.S_DFL,T_AQXCPZGD_SON.S_FMJJD,T_AQXCPZGD_SON.S_JC,T_AQXCPZGD_SON.S_KF,T_AQXCPZGD_SON.S_SDF,T_AQXCPZGD_SON.S_SJXMXH,T_AQXCPZGD_SON.S_SPF,T_AQXCPZGD_SON.S_XH,T_AQXCPZGD_SON.S_XMXH,T_AQXCPZGD_SON.S_ZGFZR,T_AQXCPZGD_SON.S_ZGFZR_BM,T_AQXCPZGD_SON.S_ZGHPF,T_AQXCPZGD_SON.S_ZGPFR,T_AQXCPZGD_SON.S_ZGPFR_BM,T_AQXCPZGD_SON.S_ZGRQ,T_AQXCPZGD_SON.S_ZGYSR,T_AQXCPZGD_SON.S_ZGYSR_BM,T_AQXCPZGD_SON.S_ZTPJ,T_AQXCPZGD_SON.T_NR,T_AQXCPZGD_SON.T_PFBZ,T_AQXCPZGD_SON.T_XMMC,T_AQXCPZGD_SON.T_YSRYJ,T_AQXCPZGD_SON.T_ZGFAJJG,T_AQXCPZGD_SON.S_ID,T_AQXCPZGD_SON.S_FID",	
				"T_AQXCPXMHZ.S_ID = T_AQXCPHZ_SON.S_FID",
				"T_AQXCPZGD.S_ID = T_AQXCPXMHZ.S_FID"
		};
		
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		HashMap<String, String> breakUp = new HashMap<String, String>();
		String splitCondition = "T_AQXCPHZ_SON.S_XMXH" + " Mantra_splitCondition";
		try {
			String sql = "select " + splitCondition + ",T_AQXCPHZ_SON.S_ID Mantra_SON_S_ID from T_AQXCPXMHZ"
					+ " LEFT JOIN T_AQXCPHZ_SON ON "+basicData[4]
					+ " where T_AQXCPXMHZ.S_ID = '" + proParVo.getInpPkey() + "' and T_AQXCPHZ_SON.S_SFXYZG = 'true'";
			tableEx = dbf.query(sql);
			String insertSql = "";
			for (int i = 0, j = tableEx.getRecordCount(); i < j; i++) {
				record = tableEx.getRecord(i);
				String splitCond = record.getFieldByName("Mantra_splitCondition").value.toString();

				if (breakUp.get(splitCond) == null) {

					insertSql = "insert into" + " T_AQXCPZGD "
							+ "("+basicData[1]+") "
							+ "select "
							+ basicData[0]
							+ " from " 
							+ " T_AQXCPXMHZ " 
							+ " where T_AQXCPXMHZ.S_ID='" + proParVo.getInpPkey() + "'";

					insertSql = tool.sqlDisCom(insertSql,"pageCode=151944042188610027&bmid=" + proParVo.getBranck());
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"********"+insertSql);

					tool.recordRel(proParVo.getBranck(), proParVo.getSpageCode(), proParVo.getInpPkey(), "T_AQXCPXMHZ","151944042188610027", tool.getOrdGreId(), "T_AQXCPZGD");
					//tool.recordRel(proParVo.getInpPkey(),"T_AQXCPXMHZ",tool.getOrdGreId(),"T_AQXCPZGD");
					
					dbf.sqlExe(insertSql, false);
					breakUp.put(splitCond, tool.getOrdGreId());
				}

				String SPLITCONDID = breakUp.get(splitCond);

				String sonId = record.getFieldByName("Mantra_SON_S_ID").value.toString();

				insertSql = "insert into" 
						+ " T_AQXCPZGD_SON "
						+ "("+basicData[3]+")"
						+ " select "
						+ 	basicData[2]
						+ " from " 
						+ " T_AQXCPHZ_SON " 
						+ " where T_AQXCPHZ_SON.S_ID='" + sonId + "'";

				insertSql = tool.sqlDisCom(insertSql, "FID=" + SPLITCONDID + "&pageCode=&bmid=");
				tool.recordRel(proParVo.getBranck(),"",sonId,"T_AQXCPHZ_SON","",tool.getOrdGreId(),"T_AQXCPZGD_SON");
				dbf.sqlExe(insertSql, false);

			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
	}
	//自查整改
	public void SafetyInquireToSelf(HttpServletRequest request) {  
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		try {
			String sql = "select T_AQXCPZGD_SON.S_ID S_ID from T_AQXCPZGD_SON where T_AQXCPZGD_SON.S_FID='"+proParVo.getInpPkey()+"'";
			tableEx = dbf.query(sql);
			for(int i = 0 , j = tableEx.getRecordCount() ; i < j ; i++) {
				record = tableEx.getRecord(i);
				String S_ID = record.getFieldByName("S_ID").value.toString();
				RelationVO leftData = (RelationVO)tool.getDataByRel("RIGHT",S_ID,"T_AQXCPZGD_SON",1).get(0);
				
				sql = "update T_AQXCPHZ_SON,T_AQXCPZGD_SON set T_AQXCPHZ_SON.S_ZGFZR=T_AQXCPZGD_SON.S_ZGFZR,T_AQXCPHZ_SON.S_ZGFZR_BM=T_AQXCPZGD_SON.S_ZGFZR_BM,T_AQXCPHZ_SON.S_ZGHPF=T_AQXCPZGD_SON.S_ZGHPF,T_AQXCPHZ_SON.S_ZGPFR=T_AQXCPZGD_SON.S_ZGPFR,T_AQXCPHZ_SON.S_ZGPFR_BM=T_AQXCPZGD_SON.S_ZGPFR_BM,T_AQXCPHZ_SON.S_ZGRQ=T_AQXCPZGD_SON.S_ZGRQ,T_AQXCPHZ_SON.S_ZGYSR=T_AQXCPZGD_SON.S_ZGYSR,T_AQXCPHZ_SON.S_ZGYSR_BM=T_AQXCPZGD_SON.S_ZGYSR_BM,T_AQXCPHZ_SON.S_ZGFAJJG=T_AQXCPZGD_SON.T_ZGFAJJG,T_AQXCPHZ_SON.S_YSRYJ=T_AQXCPZGD_SON.T_YSRYJ "
						+ "where T_AQXCPHZ_SON.S_ID = '"+leftData.getS_LEFT_ID()+"' and T_AQXCPZGD_SON.S_ID='" + S_ID + "'";
				dbf.sqlExe(sql,false);
				
			}
		}catch(Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
		
		//RelationVO leftData = (RelationVO)tool.getDataByRel("RIGHT",sonIdArr[j],"T_BMAQXCP_SON",1).get(0);
	}
	// 汇总生成专家整改
	public void SafetyInquireToExperts(HttpServletRequest request) {  
		String [] basicData= {
				"T_AQXCPXMHZ.S_ZZ,T_AQXCPXMHZ.S_DJRQ,T_AQXCPXMHZ.S_CPYT,T_AQXCPXMHZ.S_CPZFZR,T_AQXCPXMHZ.S_CPZFZR_BM,T_AQXCPXMHZ.S_CPRQ,T_AQXCPXMHZ.S_CPHZD,<<SYS_GENER_ID>>,<<FLOW_ID>>,<<UUID>>",
				"T_ZGJH.S_ZZ,T_ZGJH.S_DJRQ,T_ZGJH.S_CPYT,T_ZGJH.S_CPZFZR,T_ZGJH.S_CPZFZR_BM,T_ZGJH.S_CPRQ,T_ZGJH.S_CPHZD,T_ZGJH.S_ID,T_ZGJH.SYS_FLOW_VER,T_ZGJH.S_RUN_ID",
				"T_AQXCPHZ_SON.S_XH,T_AQXCPHZ_SON.S_BCP,T_AQXCPHZ_SON.S_BZF,T_AQXCPHZ_SON.S_CPXMFZR,T_AQXCPHZ_SON.S_CPXMFZR_BM,T_AQXCPHZ_SON.S_DFL,T_AQXCPHZ_SON.S_FZBM,T_AQXCPHZ_SON.S_FZBM_BM,T_AQXCPHZ_SON.S_KF,T_AQXCPHZ_SON.S_SDF,T_AQXCPHZ_SON.S_SPF,T_AQXCPHZ_SON.S_XMMC,T_AQXCPHZ_SON.S_XMXH,T_AQXCPHZ_SON.T_NR,T_AQXCPHZ_SON.S_PFBZ,T_AQXCPHZ_SON.T_ZTPJ,<<SYS_GENER_ID>>,<<FID>>",
				"T_ZGJH_SON.S_XH,T_ZGJH_SON.S_BCP,T_ZGJH_SON.S_BZF,T_ZGJH_SON.S_CPXMFZR,T_ZGJH_SON.S_CPXMFZR_BM,T_ZGJH_SON.S_DFL,T_ZGJH_SON.S_FZBM,T_ZGJH_SON.S_FZBM_BM,T_ZGJH_SON.S_KF,T_ZGJH_SON.S_SDF,T_ZGJH_SON.S_SPF,T_ZGJH_SON.S_XMMC,T_ZGJH_SON.S_XMXH,T_ZGJH_SON.T_NR,T_ZGJH_SON.T_PFBZ,T_ZGJH_SON.S_ZTPJ,T_ZGJH_SON.S_ID,T_ZGJH_SON.S_FID",	
				"T_AQXCPXMHZ.S_ID = T_AQXCPHZ_SON.S_FID",
				"T_ZGJH.S_ID = T_ZGJH_SON.S_FID"
		};
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		HashMap<String, String> breakUp = new HashMap<String, String>();
		String splitCondition = "T_AQXCPXMHZ.S_ID" + " Mantra_splitCondition";
		
		try {
			String sql = "select " + splitCondition + ",T_AQXCPHZ_SON.S_ID Mantra_SON_S_ID from T_AQXCPXMHZ"
					+ " LEFT JOIN T_AQXCPHZ_SON ON "+basicData[4]
					+ " where T_AQXCPXMHZ.S_ID = '" + proParVo.getInpPkey() + "' and T_AQXCPHZ_SON.S_ZJPSZG = 'true'";
			tableEx = dbf.query(sql);
			String insertSql = "";
			for (int i = 0, j = tableEx.getRecordCount(); i < j; i++) {
				record = tableEx.getRecord(i);
				String splitCond = record.getFieldByName("Mantra_splitCondition").value.toString();

				if (breakUp.get(splitCond) == null) {

					insertSql = "insert into" + " T_ZGJH "
							+ "("+basicData[1]+") "
							+ "select "
							+ basicData[0]
							+ " from " 
							+ " T_AQXCPXMHZ " 
							+ " where T_AQXCPXMHZ.S_ID='" + proParVo.getInpPkey() + "'";

					insertSql = tool.sqlDisCom(insertSql,"pageCode=152237975399214970&bmid=" + proParVo.getBranck());
					MantraLog.WriteProgress(MantraLog.LOG_PROGRESS ,"********"+insertSql);
					
					tool.recordRel(proParVo.getBranck(), proParVo.getSpageCode(), proParVo.getInpPkey(), "T_AQXCPXMHZ","152237975399214970", tool.getOrdGreId(), "T_ZGJH");
					//tool.recordRel(proParVo.getInpPkey(),"T_AQXCPXMHZ",tool.getOrdGreId(),"T_ZGJH");
					
					dbf.sqlExe(insertSql, false);
					breakUp.put(splitCond, tool.getOrdGreId());
				}

				String SPLITCONDID = breakUp.get(splitCond);

				String sonId = record.getFieldByName("Mantra_SON_S_ID").value.toString();

				insertSql = "insert into" 
						+ " T_ZGJH_SON "
						+ "("+basicData[3]+")"
						+ " select "
						+ 	basicData[2]
						+ " from " 
						+ " T_AQXCPHZ_SON " 
						+ " where T_AQXCPHZ_SON.S_ID='" + sonId + "'";

				insertSql = tool.sqlDisCom(insertSql, "FID=" + SPLITCONDID + "&pageCode=&bmid=");
				tool.recordRel(proParVo.getBranck(),"", sonId, "T_AQXCPHZ_SON","",tool.getOrdGreId(), "T_ZGJH_SON");
				//tool.recordRel(sonId,"T_AQXCPHZ_SON",tool.getOrdGreId(),"T_ZGJH_SON");
				dbf.sqlExe(insertSql, false);

			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if (tableEx != null)
				tableEx.close();
			if (dbf != null)
				dbf.close();
		}
		
	}
	
}
