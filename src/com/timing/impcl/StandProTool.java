package com.timing.impcl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import com.timing.impcl.MantraUtil;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.util.EString;
import com.timing.impcl.RelationVO;
/**
 * 标准化项目
 */
public class StandProTool {
	ProcessParameterVO proParVo = null;
	MantraUtil tool = new MantraUtil();

	/***
	 * 标准化项目汇总->部门标准化项目评分
	 */
	public void standProSumTobranck(HttpServletRequest request) {
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		HashMap<String, String> breakUp = new HashMap<String, String>();
		String splitCondition = "T_BMBZHXMPF_SON.S_FZBM_BM" + " Mantra_splitCondition";
		
		try {
			String sql = "select " + splitCondition + ",T_BMBZHXMPF_SON.S_FZBM Mantra_splitCondition_Name,T_BMBZHXMPF_SON.S_ID Mantra_SON_S_ID from T_BMBZHXMPF "
					+ "LEFT JOIN T_BMBZHXMPF_SON ON " + "T_BMBZHXMPF.S_ID = T_BMBZHXMPF_SON.S_FID "
					+ "where T_BMBZHXMPF.S_ID = '" + proParVo.getInpPkey() + "'";
			tableEx = dbf.query(sql);
			String insertSql = "";
			for (int i = 0, j = tableEx.getRecordCount(); i < j; i++) {
				record = tableEx.getRecord(i);
				String splitCond = record.getFieldByName("Mantra_splitCondition").value.toString();
				if (breakUp.get(splitCond) == null) {
					String sys_id = EString.generId();
					String [] Leander = tool.getBranchLeader(splitCond);
					String splitCond_Name = record.getFieldByName("Mantra_splitCondition_Name").value.toString();
					insertSql = "insert into" + " T_BZHPFXMHZ "
							+ "(T_BZHPFXMHZ.S_WXZZ,T_BZHPFXMHZ.S_DJRQ,T_BZHPFXMHZ.S_BMCPDBM,T_BZHPFXMHZ.S_CPYT,T_BZHPFXMHZ.S_CPZFZR,T_BZHPFXMHZ.S_CPZFZRBM,T_BZHPFXMHZ.S_CPRQ,T_BZHPFXMHZ.S_FZBM,T_BZHPFXMHZ.S_FZBM_BM,T_BZHPFXMHZ.S_FZR,T_BZHPFXMHZ.S_FZRBM,T_BZHPFXMHZ.S_ID,T_BZHPFXMHZ.S_RUN_ID,T_BZHPFXMHZ.SYS_FLOW_VER) "
							+ "select "
							+ "T_BMBZHXMPF.S_WXZZ,T_BMBZHXMPF.S_DJRQ,T_BMBZHXMPF.S_DJBH,T_BMBZHXMPF.S_CPYT,T_BMBZHXMPF.S_CPZFZR,T_BMBZHXMPF.S_CPZFZR_BM,T_BMBZHXMPF.S_CPRQ,<<S_FZBM>>,<<S_FZBM_BM>>,<<S_FZR>>,<<S_FZRBM>>,<<SPLITCONDID>>,<<UUID>>,<<FLOW_ID>> "
							+ "from " 
							+ "T_BMBZHXMPF " 
							+ "where T_BMBZHXMPF.S_ID='" + proParVo.getInpPkey() + "'";
					
					insertSql = tool.sqlDisCom(insertSql,
							"SPLITCONDID=" + sys_id + "&pageCode=151969739273210002&bmid=" + proParVo.getBranck()+"&S_FZBM="+splitCond_Name+"&S_FZBM_BM="+splitCond+"&S_FZR="+Leander[0]+"&S_FZRBM="+Leander[1]);
					
					tool.recordRel(proParVo.getBranck(),proParVo.getSpageCode(),proParVo.getInpPkey(),"T_BMBZHXMPF","151969739273210002",sys_id,"T_BZHPFXMHZ");
					
					//mu.recordRel(S_ZZ, request.getParameter("SPAGECODE"), primaryKey, "T_AQXCPXMHZ", "151772091835710956", S_ID, "T_BMAQXCP");
					dbf.sqlExe(insertSql, false);
					breakUp.put(splitCond, sys_id);
				}
				String SPLITCONDID = breakUp.get(splitCond);
				String sonId = record.getFieldByName("Mantra_SON_S_ID").value.toString();
				insertSql = "insert into" 
						+ " T_BZHXMPFHZ_SON "
						+ " (T_BZHXMPFHZ_SON.S_XH,T_BZHXMPFHZ_SON.S_FZBM,T_BZHXMPFHZ_SON.S_FZBM_BM,T_BZHXMPFHZ_SON.S_CPXMFZR,T_BZHXMPFHZ_SON.S_CPXMFZR_BM,T_BZHXMPFHZ_SON.S_XMXH,T_BZHXMPFHZ_SON.S_XMMC,T_BZHXMPFHZ_SON.T_NR,T_BZHXMPFHZ_SON.S_BZF,T_BZHXMPFHZ_SON.T_PFBZ,T_BZHXMPFHZ_SON.S_BCP,T_BZHXMPFHZ_SON.S_SPF,T_BZHXMPFHZ_SON.S_FMJJD,T_BZHXMPFHZ_SON.S_SJXMXH,T_BZHXMPFHZ_SON.S_FID,T_BZHXMPFHZ_SON.S_ID)  "
						+ " select "
						+ " T_BMBZHXMPF_SON.S_XH,T_BMBZHXMPF_SON.S_FZBM,T_BMBZHXMPF_SON.S_FZBM_BM,T_BMBZHXMPF_SON.S_CPXMFZR,T_BMBZHXMPF_SON.S_CPXMFZR_BM,T_BMBZHXMPF_SON.S_XMXH,T_BMBZHXMPF_SON.S_XMMC,T_BMBZHXMPF_SON.T_NR,T_BMBZHXMPF_SON.S_BZF,T_BMBZHXMPF_SON.S_PFBZ,T_BMBZHXMPF_SON.S_BCP,T_BMBZHXMPF_SON.S_SPF,T_BMBZHXMPF_SON.S_FMJJD,T_BMBZHXMPF_SON.S_SJXMXH,<<FID>>,<<SYS_GENER_ID>> "
						+ " from " 
						+ " T_BMBZHXMPF_SON " 
						+ " where T_BMBZHXMPF_SON.S_ID='" + sonId + "'";

				insertSql = tool.sqlDisCom(insertSql, "FID=" + SPLITCONDID + "&pageCode=&bmid=");
				//tool.recordRel(sonId,"T_BMBZHXMPF_SON",tool.getOrdGreId(),"T_BZHXMPFHZ_SON");
				
				tool.recordRel(proParVo.getBranck(),"",sonId,"T_BMBZHXMPF_SON","",tool.getOrdGreId(),"T_BZHXMPFHZ_SON");
				
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

	/***
	 * 部门标准化项目评分->负责人标准化项目
	 */
	public void branckStandProSumToleader(HttpServletRequest request) {
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		
		HashMap<String, String> breakUp = new HashMap<String, String>();
		//修改
		String splitCondition = "T_BZHXMPFHZ_SON.S_CPXMFZR_BM" + " Mantra_splitCondition";
		try {
			//修改
			String sql = "select " + splitCondition + ",T_BZHXMPFHZ_SON.S_CPXMFZR Mantra_splitCondition_Name ,T_BZHXMPFHZ_SON.S_ID Mantra_SON_S_ID from T_BZHPFXMHZ "
					+ "LEFT JOIN T_BZHXMPFHZ_SON ON " + "T_BZHPFXMHZ.S_ID = T_BZHXMPFHZ_SON.S_FID "
					+ "where T_BZHPFXMHZ.S_ID = '" + proParVo.getInpPkey() + "'";
			tableEx = dbf.query(sql);
			String insertSql = "";
			for (int i = 0, j = tableEx.getRecordCount(); i < j; i++) {
				record = tableEx.getRecord(i);
				String splitCond = record.getFieldByName("Mantra_splitCondition").value.toString();
				String splitCondition_Name = record.getFieldByName("Mantra_splitCondition_Name").value.toString();
				if (breakUp.get(splitCond) == null) {
					//修改
					//T_FZRBZHXM$S_CPXMFZR  T_FZRBZHXM$S_CPXMFZR_BM
					insertSql = "insert into" + " T_FZRBZHXM "
							+ "(T_FZRBZHXM.S_DJRQ,T_FZRBZHXM.S_WXZZ,T_FZRBZHXM.S_BMCPDBM,T_FZRBZHXM.S_CPYT,T_FZRBZHXM.S_CPZFZR,T_FZRBZHXM.S_CPZFZR_BM,T_FZRBZHXM.S_CPRQ,T_FZRBZHXM.S_FZBM,T_FZRBZHXM.S_FZBM_BM,T_FZRBZHXM.S_FZR,T_FZRBZHXM.S_FZR_BM,T_FZRBZHXM.S_ID,T_FZRBZHXM.SYS_FLOW_VER,T_FZRBZHXM.S_RUN_ID,T_FZRBZHXM.S_CPXMFZR,T_FZRBZHXM.S_CPXMFZR_BM) "
							+ "select "
							+ "T_BZHPFXMHZ.S_DJRQ,T_BZHPFXMHZ.S_WXZZ,T_BZHPFXMHZ.S_DJBH,T_BZHPFXMHZ.S_CPYT,T_BZHPFXMHZ.S_CPZFZR,T_BZHPFXMHZ.S_CPZFZRBM,T_BZHPFXMHZ.S_CPRQ,T_BZHPFXMHZ.S_FZBM,T_BZHPFXMHZ.S_FZBM_BM,T_BZHPFXMHZ.S_FZR,T_BZHPFXMHZ.S_FZRBM,<<SYS_GENER_ID>>,<<FLOW_ID>>,<<UUID>>,<<S_FZR>>,<<S_FZR_BM>> "
							+ "from " 
							+ "T_BZHPFXMHZ " 
							+ "where T_BZHPFXMHZ.S_ID='" + proParVo.getInpPkey() + "'";

					insertSql = tool.sqlDisCom(insertSql,"pageCode=151969803022610006&bmid=" + proParVo.getBranck()+"&S_FZR="+splitCondition_Name+"&S_FZR_BM="+splitCond);
					//修改
					//tool.recordRel(proParVo.getInpPkey(),"T_BZHPFXMHZ",tool.getOrdGreId(),"T_FZRBZHXM");
					
					tool.recordRel(proParVo.getBranck(),proParVo.getSpageCode(),proParVo.getInpPkey(),"T_BZHPFXMHZ","151969803022610006",tool.getOrdGreId(),"T_FZRBZHXM");

					
					dbf.sqlExe(insertSql, false);
					breakUp.put(splitCond, tool.getOrdGreId());
				}

				String SPLITCONDID = breakUp.get(splitCond);

				String sonId = record.getFieldByName("Mantra_SON_S_ID").value.toString();
				
				
					//修改
				insertSql = "insert into" 
						+ " T_FZRBZHXM_SON "
						+ " (T_FZRBZHXM_SON.S_CPXMFZR,T_FZRBZHXM_SON.S_CPXMFZRBM,T_FZRBZHXM_SON.S_XMXH,T_FZRBZHXM_SON.S_XMMC,T_FZRBZHXM_SON.T_NR,T_FZRBZHXM_SON.S_BZF,T_FZRBZHXM_SON.T_PFBZ,T_FZRBZHXM_SON.S_BCP,T_FZRBZHXM_SON.S_SPF,T_FZRBZHXM_SON.S_SDF,T_FZRBZHXM_SON.S_FMJJD,T_FZRBZHXM_SON.S_SJXMXH,T_FZRBZHXM_SON.S_ID,T_FZRBZHXM_SON.S_FID)  "
						+ " select "
						+ " T_BZHXMPFHZ_SON.S_CPXMFZR,T_BZHXMPFHZ_SON.S_CPXMFZR_BM,T_BZHXMPFHZ_SON.S_XMXH,T_BZHXMPFHZ_SON.S_XMMC,T_BZHXMPFHZ_SON.T_NR,T_BZHXMPFHZ_SON.S_BZF,T_BZHXMPFHZ_SON.T_PFBZ,T_BZHXMPFHZ_SON.S_BCP,T_BZHXMPFHZ_SON.S_SPF,T_BZHXMPFHZ_SON.S_SDF,T_BZHXMPFHZ_SON.S_FMJJD,T_BZHXMPFHZ_SON.S_SJXMXH,<<SYS_GENER_ID>>,<<FID>> "
						+ " from " 
						+ " T_BZHXMPFHZ_SON " 
						+ " where T_BZHXMPFHZ_SON.S_ID='" + sonId + "'";
				
				//修改
				insertSql = tool.sqlDisCom(insertSql, "FID=" + SPLITCONDID + "&pageCode=&bmid=");

				//tool.recordRel(sonId,"T_BZHXMPFHZ_SON",tool.getOrdGreId(),"T_FZRBZHXM_SON");
				
				tool.recordRel(proParVo.getBranck(),"",sonId,"T_BZHXMPFHZ_SON","",tool.getOrdGreId(),"T_FZRBZHXM_SON");

				
				
				dbf.sqlExe(insertSql, false);
				
				//修改汇总
				RelationVO leftData = (RelationVO)tool.getDataByRel("RIGHT",sonId,"T_BZHXMPFHZ_SON",1).get(0);
				sql = "update T_BMBZHXMPF_SON,T_BZHXMPFHZ_SON set T_BMBZHXMPF_SON.S_CPXMFZR=T_BZHXMPFHZ_SON.S_CPXMFZR,T_BMBZHXMPF_SON.S_CPXMFZR_BM=T_BZHXMPFHZ_SON.S_CPXMFZR_BM "
						+ "where T_BMBZHXMPF_SON.S_ID = '"+leftData.getS_LEFT_ID()+"' and T_BZHXMPFHZ_SON.S_ID='" + sonId + "'";

				dbf.sqlExe(sql, false);
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

	/***
	 * 负责人标准化项目
	 */
	public void leaderStandProSum(HttpServletRequest request) {
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		String sql = "",S_ID="";
		try {
			sql = "select T_FZRBZHXM_SON.S_ID S_ID from T_FZRBZHXM_SON where T_FZRBZHXM_SON.S_FID='" + proParVo.getInpPkey() + "'";
			tableEx = dbf.query(sql);
			for(int i = 0 , j = tableEx.getRecordCount() ; i < j ; i++) {
				record = tableEx.getRecord(i);
				S_ID = record.getFieldByName("S_ID").value.toString();
				RelationVO leftData = (RelationVO)tool.getDataByRel("RIGHT",S_ID,"T_FZRBZHXM_SON",2).get(0);
				sql = "update T_BZHXMPFHZ_SON,T_FZRBZHXM_SON set T_BZHXMPFHZ_SON.S_DFL=T_FZRBZHXM_SON.S_DFL,T_BZHXMPFHZ_SON.S_KF=T_FZRBZHXM_SON.S_KF,T_BZHXMPFHZ_SON.S_SDF=T_FZRBZHXM_SON.S_SDF,T_BZHXMPFHZ_SON.S_SFXYZG=T_FZRBZHXM_SON.S_SFXYZG,T_BZHXMPFHZ_SON.S_SPF=T_FZRBZHXM_SON.S_SPF,T_BZHXMPFHZ_SON.T_ZTPJ=T_FZRBZHXM_SON.T_ZTPJ "
						+ "where T_BZHXMPFHZ_SON.S_ID = '"+leftData.getS_LEFT_ID()+"' and T_FZRBZHXM_SON.S_ID='" + S_ID + "'";
				dbf.sqlExe(sql, false);
				List<RelationVO> relList = leftData.getRelationVOs();
				for(int m = 0 , n = relList.size() ; m < n ; m++) {
					RelationVO leftDataSon = relList.get(m);
					sql = "update T_BMBZHXMPF_SON,T_FZRBZHXM_SON set T_BMBZHXMPF_SON.S_DFL=T_FZRBZHXM_SON.S_DFL,T_BMBZHXMPF_SON.S_KF=T_FZRBZHXM_SON.S_KF,T_BMBZHXMPF_SON.S_MJBDBS=T_FZRBZHXM_SON.S_MJBDBS,T_BMBZHXMPF_SON.S_SDF=T_FZRBZHXM_SON.S_SDF,T_BMBZHXMPF_SON.S_SFXYZG=T_FZRBZHXM_SON.S_SFXYZG,T_BMBZHXMPF_SON.S_SPF=T_FZRBZHXM_SON.S_SPF,T_BMBZHXMPF_SON.T_ZTPJ=T_FZRBZHXM_SON.T_ZTPJ "
							+ "where T_BMBZHXMPF_SON.S_ID = '"+leftDataSon.getS_LEFT_ID()+"' and T_FZRBZHXM_SON.S_ID='" + S_ID + "'";
					dbf.sqlExe(sql, false);
				}
			}	
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(tableEx != null)
				tableEx.close();
			if(dbf != null)
				dbf.close();
		}
	}

	/***
	 * 标准化项目汇总->标准化项目整改单
	 */
	public void standProSumToAlter(HttpServletRequest request) {
		String [] basicData= {
				"<<FLOW_ID>>,<<UUID>>,<<SYS_GENER_ID>>,T_BMBZHXMPF.S_DJBH,T_BMBZHXMPF.S_CPRQ,T_BMBZHXMPF.S_CPYT,T_BMBZHXMPF.S_CPZFZR,T_BMBZHXMPF.S_CPZFZR_BM,T_BMBZHXMPF.S_DJRQ,T_BMBZHXMPF.S_WXZZ",
				"T_BZHXMZGD.SYS_FLOW_VER,T_BZHXMZGD.S_RUN_ID,T_BZHXMZGD.S_ID,T_BZHXMZGD.S_CPHZD,T_BZHXMZGD.S_CPRQ,T_BZHXMZGD.S_CPYT,T_BZHXMZGD.S_CPZFZR,T_BZHXMZGD.S_CPZFZR_BM,T_BZHXMZGD.S_DJRQ,T_BZHXMZGD.S_WXZZ",
				"T_BMBZHXMPF_SON.S_BCP,T_BMBZHXMPF_SON.S_BDBXMS,T_BMBZHXMPF_SON.S_BZF,T_BMBZHXMPF_SON.S_CPXMFZR,T_BMBZHXMPF_SON.S_CPXMFZR_BM,T_BMBZHXMPF_SON.S_DFL,T_BMBZHXMPF_SON.S_FMJJD,T_BMBZHXMPF_SON.S_JC,T_BMBZHXMPF_SON.S_KF,T_BMBZHXMPF_SON.S_SDF,T_BMBZHXMPF_SON.S_SJXMXH,T_BMBZHXMPF_SON.S_SPF,T_BMBZHXMPF_SON.S_XMMC,T_BMBZHXMPF_SON.S_XMXH,T_BMBZHXMPF_SON.T_YSRYJ,T_BMBZHXMPF_SON.T_NR,T_BMBZHXMPF_SON.S_PFBZ,T_BMBZHXMPF_SON.S_ZXMS,T_BMBZHXMPF_SON.T_ZTPJ,<<SYS_GENER_ID>>,<<FID>>",
				"T_BZHXMZGD_SON.S_BCP,T_BZHXMZGD_SON.S_BDBXMS,T_BZHXMZGD_SON.S_BZF,T_BZHXMZGD_SON.S_CPXMFZR,T_BZHXMZGD_SON.S_CPXMFZR_BM,T_BZHXMZGD_SON.S_DFL,T_BZHXMZGD_SON.S_FMJJD,T_BZHXMZGD_SON.S_JC,T_BZHXMZGD_SON.S_KF,T_BZHXMZGD_SON.S_SDF,T_BZHXMZGD_SON.S_SJXMXH,T_BZHXMZGD_SON.S_SPF,T_BZHXMZGD_SON.S_XMMC,T_BZHXMZGD_SON.S_XMXH,T_BZHXMZGD_SON.S_YSRYJ,T_BZHXMZGD_SON.T_NR,T_BZHXMZGD_SON.T_PFBZ,T_BZHXMZGD_SON.S_ZXMS,T_BZHXMZGD_SON.T_ZTPJ,T_BZHXMZGD_SON.S_ID,T_BZHXMZGD_SON.S_FID",	
				"T_BMBZHXMPF.S_ID = T_BMBZHXMPF_SON.S_FID",
				"T_AQXCPZGD.S_ID = T_AQXCPXMHZ.S_FID"
		};
		//<<FLOW_ID>>,<<UUID>>,<<SYS_GENER_ID>>,T_BMBZHXMPF.S_DJBH,T_BMBZHXMPF.S_CPRQ,T_BMBZHXMPF.S_CPYT,T_BMBZHXMPF.S_CPZFZR,T_BMBZHXMPF.S_CPZFZR_BM,T_BMBZHXMPF.S_DJRQ,T_BMBZHXMPF.S_WXZZ
		//T_BZHXMZGD.SYS_FLOW_VER,T_BZHXMZGD.S_RUN_ID,T_BZHXMZGD.S_ID,T_BZHXMZGD.S_CPHZD,T_BZHXMZGD.S_CPRQ,T_BZHXMZGD.S_CPYT,T_BZHXMZGD.S_CPZFZR,T_BZHXMZGD.S_CPZFZR_BM,T_BZHXMZGD.S_DJRQ,T_BZHXMZGD.S_WXZZ
		//T_BMBZHXMPF_SON.S_BCP,T_BMBZHXMPF_SON.S_BDBXMS,T_BMBZHXMPF_SON.S_BZF,T_BMBZHXMPF_SON.S_CPXMFZR,T_BMBZHXMPF_SON.S_CPXMFZR_BM,T_BMBZHXMPF_SON.S_DFL,T_BMBZHXMPF_SON.S_FMJJD,T_BMBZHXMPF_SON.S_JC,T_BMBZHXMPF_SON.S_KF,T_BMBZHXMPF_SON.S_SDF,T_BMBZHXMPF_SON.S_SJXMXH,T_BMBZHXMPF_SON.S_SPF,T_BMBZHXMPF_SON.S_XMMC,T_BMBZHXMPF_SON.S_XMXH,T_BMBZHXMPF_SON.T_YSRYJ,T_BMBZHXMPF_SON.T_NR,T_BMBZHXMPF_SON.S_PFBZ,T_BMBZHXMPF_SON.S_ZXMS,T_BMBZHXMPF_SON.T_ZTPJ,<<SYS_GENER_ID>>,<<FLOW_ID>>,
		//T_BZHXMZGD_SON.S_BCP,T_BZHXMZGD_SON.S_BDBXMS,T_BZHXMZGD_SON.S_BZF,T_BZHXMZGD_SON.S_CPXMFZR,T_BZHXMZGD_SON.S_CPXMFZR_BM,T_BZHXMZGD_SON.S_DFL,T_BZHXMZGD_SON.S_FMJJD,T_BZHXMZGD_SON.S_JC,T_BZHXMZGD_SON.S_KF,T_BZHXMZGD_SON.S_SDF,T_BZHXMZGD_SON.S_SJXMXH,T_BZHXMZGD_SON.S_SPF,T_BZHXMZGD_SON.S_XMMC,T_BZHXMZGD_SON.S_XMXH,T_BZHXMZGD_SON.S_YSRYJ,T_BZHXMZGD_SON.T_NR,T_BZHXMZGD_SON.T_PFBZ,T_BZHXMZGD_SON.S_ZXMS,T_BZHXMZGD_SON.T_ZTPJ,T_BZHXMZGD_SON.S_ID,T_BZHXMZGD_SON.S_FID,
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		HashMap<String, String> breakUp = new HashMap<String, String>();
		String splitCondition = "T_BMBZHXMPF_SON.S_ID" + " Mantra_splitCondition";
		try {
			String sql = "select " + splitCondition + ",T_BMBZHXMPF_SON.S_ID Mantra_SON_S_ID from T_BMBZHXMPF"
					+ " LEFT JOIN T_BMBZHXMPF_SON ON "+basicData[4]
					+ " where T_BMBZHXMPF.S_ID = '" + proParVo.getInpPkey() + "' and T_BMBZHXMPF_SON.S_SFXYZG = 'true'";
			tableEx = dbf.query(sql);
			String insertSql = "";
			for (int i = 0, j = tableEx.getRecordCount(); i < j; i++) {
				record = tableEx.getRecord(i);
				String splitCond = record.getFieldByName("Mantra_splitCondition").value.toString();

				if (breakUp.get(splitCond) == null) {

					insertSql = "insert into" + " T_BZHXMZGD "
							+ "("+basicData[1]+") "
							+ "select "
							+ basicData[0]
							+ " from " 
							+ " T_BMBZHXMPF " 
							+ " where T_BMBZHXMPF.S_ID='" + proParVo.getInpPkey() + "'";

					insertSql = tool.sqlDisCom(insertSql,"pageCode=151969840233410008&bmid=" + proParVo.getBranck());
					//tool.recordRel(proParVo.getInpPkey(),"T_BMBZHXMPF",tool.getOrdGreId(),"T_BZHXMZGD");
					
					tool.recordRel(proParVo.getBranck(),proParVo.getSpageCode(),proParVo.getInpPkey(),"T_BMBZHXMPF","151969840233410008",tool.getOrdGreId(),"T_BZHXMZGD");

					
					dbf.sqlExe(insertSql, false);
					breakUp.put(splitCond, tool.getOrdGreId());
				}

				String SPLITCONDID = breakUp.get(splitCond);

				String sonId = record.getFieldByName("Mantra_SON_S_ID").value.toString();

				insertSql = "insert into" 
						+ " T_BZHXMZGD_SON "
						+ "("+basicData[3]+")"
						+ " select "
						+ 	basicData[2]
						+ " from " 
						+ " T_BMBZHXMPF_SON " 
						+ " where T_BMBZHXMPF_SON.S_ID='" + sonId + "'";

				insertSql = tool.sqlDisCom(insertSql, "FID=" + SPLITCONDID + "&pageCode=&bmid=");
	
				//tool.recordRel(sonId,"T_BMBZHXMPF_SON",tool.getOrdGreId(),"T_BZHXMZGD_SON");
				
				tool.recordRel(proParVo.getBranck(),"",proParVo.getInpPkey(),"T_BMBZHXMPF_SON","",tool.getOrdGreId(),"T_BZHXMZGD_SON");
				
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
	public void standProRectif(HttpServletRequest request) {
		proParVo = new ProcessParameterVO(request);
		TableEx tableEx = null;
		Record record = null;
		DBFactory dbf = new DBFactory();
		try {
			String sql = "select T_BZHXMZGD_SON.S_ID S_ID from T_BZHXMZGD_SON where T_BZHXMZGD_SON.S_FID='"+proParVo.getInpPkey()+"'";
			tableEx = dbf.query(sql);
			for(int i = 0 , j = tableEx.getRecordCount() ; i < j ; i++) {
				record = tableEx.getRecord(i);
				String S_ID = record.getFieldByName("S_ID").value.toString();
				RelationVO leftData = (RelationVO)tool.getDataByRel("RIGHT",S_ID,"T_BZHXMZGD_SON",1).get(0);
				
				sql = "update T_BMBZHXMPF_SON,T_BZHXMZGD_SON set T_BMBZHXMPF_SON.S_ZGRQ=T_BZHXMZGD_SON.S_ZGRQ,T_BMBZHXMPF_SON.S_ZGFAJJG=T_BZHXMZGD_SON.T_ZGFZJJG,T_BMBZHXMPF_SON.S_ZGYSR=T_BZHXMZGD_SON.S_ZGYSR,T_BMBZHXMPF_SON.S_ZGYSR_BM=T_BZHXMZGD_SON.S_ZGYSRBM,T_BMBZHXMPF_SON.T_YSRYJ=T_BZHXMZGD_SON.S_YSRYJ,T_BMBZHXMPF_SON.S_ZGHPF=T_BZHXMZGD_SON.S_ZGHDF,T_BMBZHXMPF_SON.S_ZGPFR=T_BZHXMZGD_SON.S_ZGPFR,T_BMBZHXMPF_SON.S_ZGPFR_BM=T_BZHXMZGD_SON.S_ZGPFR_BM,T_BMBZHXMPF_SON.S_ZGZRR=T_BZHXMZGD_SON.S_ZGFZR,T_BMBZHXMPF_SON.S_ZGZRR_BM=T_BZHXMZGD_SON.S_ZGFZR_BM "
						+ "where T_BMBZHXMPF_SON.S_ID = '"+leftData.getS_LEFT_ID()+"' and T_BZHXMZGD_SON.S_ID='" + S_ID + "'";
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

	}
}
