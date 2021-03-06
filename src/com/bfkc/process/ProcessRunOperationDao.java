/**
 * 
 */
package com.bfkc.process;

import java.util.Arrays;

import com.timing.impcl.MantraLog;
import com.yonyou.mis.util.ApplicationUtils;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.FieldEx;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

/**
 * @author tianshisheng
 *
 */
public final class ProcessRunOperationDao {
	/**
	 * 操作流程
	 * @param _strFlowId 流程号
	 * @param _strFlowRunId 节点号
	 * @param _strVersion 版本号
	 * @param _strType 1:挂起 0:启用 3:作废
	 * @return
	 */
	public static boolean processFlowHand(String _strFlowId,String _strFlowRunId,String _strType){
		DBFactory dbf = new DBFactory();
		TableEx exRun = null;
		try {
			exRun = queryFlowRun(_strFlowId,_strFlowRunId);
			String strAuditUsers=exRun.getRecord(0).getFieldByName("S_AUDIT_ARRAY").value.toString();
			int index = Integer.parseInt(exRun.getRecord(0).getFieldByName("S_AUDIT_INDEX").value.toString());//索引
			if("1".equals(_strType)){//挂起 更新人为空,状态为2
				dbf.sqlExe("update t_sys_flow_run set I_ISOVER='2',S_AUD_USER='' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
			}else if("0".equals(_strType)){//回复人,状态为0 
				dbf.sqlExe("update t_sys_flow_run set I_ISOVER='0',S_AUD_USER='"+strAuditUsers.split("\\|",-1)[index]+"' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
			}else if("3".equals(_strType)){
				dbf.sqlExe("update t_sys_flow_run set I_ISOVER='3',S_AUD_USER='' where s_run_id='"+_strFlowRunId+"' and s_flow_id='"+_strFlowId+"'", true);
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			dbf.close();
			if(exRun!=null){exRun.close();}
			e.printStackTrace();
		}finally{
			dbf.close();
			if(exRun!=null){exRun.close();}
			return true;
		}
	}
	
	/**
	 * 更新运行日志T_SYS_FLOW_RUN
	 * @param _strArrayFlowRun
	 * @param _strType 1:插入 2:更新 3:更新 4:更新父
	 */
	public static void updateFlowRun(String[] _strArrayFlowRunVal,String _strType){
		DBFactory dbf = new DBFactory();
		try {
			if("1".equals(_strType)){
				String strTabCol ="(S_FLOW_ID,S_RUN_ID,S_NODE_CODE,S_AUDIT_VERSION,S_LAUNCH_DATE,S_LAUNCH_USER,S_AUD_USER,S_AUDIT_INDEX,S_AUDIT_MSG,S_LAUNCH_BRANCH,S_AUDIT_ARRAYYQ,S_AUDIT_ARRAY,S_AUDIT_NODES,I_ISOVER,S_AUDIT_OTHER,S_AUDIT_SEL,S_AUD_OVER,S_FLOW_SON,S_FLOW_TYPE,S_FLOW_PARENT_ID,S_AUDIT_FSPJ,S_TAB)";
				_strArrayFlowRunVal = ApplicationUtils.arrayAddSingleQuotes(_strArrayFlowRunVal);
				String strTabVal = Arrays.toString(_strArrayFlowRunVal);
				strTabVal = strTabVal.substring(1,strTabVal.length()-1);
				//重新发起流程:删除----插入
				dbf.sqlExe("delete from T_SYS_FLOW_RUN where S_FLOW_ID="+_strArrayFlowRunVal[0] +" and S_RUN_ID="+_strArrayFlowRunVal[1]+" and S_AUDIT_VERSION="+_strArrayFlowRunVal[3]+"",false);
				dbf.sqlExe("insert into T_SYS_FLOW_RUN "+strTabCol+" values("+strTabVal+")", true);
			}else if("2".equals(_strType)){

				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_NODE_CODE","S_AUD_USER","S_AUDIT_INDEX","I_ISOVER","S_AUDIT_OTHER","S_AUDIT_ARRAY"};
				String strTabVal = "";
				for(int i=3,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", false);
			
			 	}else if("3".equals(_strType)){
				String strTabVal = "";
				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_VERSION","S_AUDIT_MSG","S_AUDIT_ARRAYYQ","S_AUDIT_ARRAY","S_AUDIT_NODES","S_AUDIT_OTHER","I_ISOVER","S_AUDIT_SEL","S_FLOW_SON","S_AUDIT_FSPJ"};
				for(int i=3,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"' and S_AUDIT_VERSION='"+_strArrayFlowRunVal[2]+"'", false);
		        
		       
			}else if("4".equals(_strType)){
//				{strFlowParentId,strFlowRunId,strNextAuditUser,strNodesParent[indexParent],indexParent+"",strIsOverParent}
				String strTabVal = "";
				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUD_USER","S_NODE_CODE","S_AUDIT_INDEX","I_ISOVER","S_AUDIT_ARRAY"};
				for(int i=2,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
			}else if("5".equals(_strType)){
				String strTabVal = "";
				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUD_USER","S_NODE_CODE","S_AUDIT_INDEX"};
				for(int i=2,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
			}else if("6".equals(_strType)){
				String strTabVal = "";
				String[] strArrayTabCols ={"S_FLOW_ID","S_RUN_ID","S_AUDIT_SEL"};
				for(int i=2,j=strArrayTabCols.length;i<j;i++){
					strTabVal = ("".equals(strTabVal)?"":(strTabVal+","))+strArrayTabCols[i]+"='"+_strArrayFlowRunVal[i]+"' ";
				}
				
				dbf.sqlExe("update T_SYS_FLOW_RUN set "+strTabVal+"  where S_FLOW_ID='"+_strArrayFlowRunVal[0]+"' and S_RUN_ID='"+_strArrayFlowRunVal[1]+"'", false);
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if(dbf!=null){dbf.close();}
		}
	}
	
	/**
	 * 查询并列子流程
	 * @param strFlowRunId
	 * @param strFlowParentId
	 * @return
	 */
	public static boolean queryFlowRunIsOverSameLevel(String strFlowRunId, String strFlowParentId) {
		TableEx exParent = null;
		boolean bIsOver = true;
		try {
			exParent = new TableEx("I_ISOVER","T_SYS_FLOW_RUN"," S_RUN_ID='"+strFlowRunId+"' and S_FLOW_PARENT_ID='"+strFlowParentId+"'");
			int iCount = exParent.getRecordCount();
			for(int i=0;i<iCount;i++){
				if("0".equals(getColString("I_ISOVER", exParent.getRecord(i)))){
					bIsOver = false;
				}
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			if(exParent!=null)
				exParent.close();
		}
		return bIsOver;
	}
	
	/**
	 * 某表中的一列的值.
	 * 
	 * @param strFlowRunId
	 * @param strFlowParentId
	 * @return
	 */
	public static String queryTableValue(String tableName, String columnName, String pkName, String id) {
		TableEx exParent = null;
		String value = null;
		try {
			exParent = new TableEx(columnName, tableName, " " + pkName + "='" + id + "'");
			value = getColString(columnName, exParent.getRecord(0));
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
		    value = "";
		}finally{
			if(exParent!=null)
				exParent.close();
		}
		return value;
	}
	
	/**
	 * 查询运行表T_SYS_FLOW_RUN
	 * @param _strFlowId
	 * @param _strVersion
	 * @param _strFlowRunId
	 * @return
	 */
	public static TableEx queryFlowRun(String _strFlowId,String _strFlowRunId){
		TableEx ex = null;
		try {
			StringBuffer sr = new StringBuffer();
			sr.append(" 1=1");
			sr.append((_strFlowId==null||"".equals(_strFlowId))?"":(" and S_FLOW_ID='"+_strFlowId+"'"));
			sr.append((_strFlowRunId==null||"".equals(_strFlowRunId))?"":(" and S_RUN_ID ='"+_strFlowRunId+"'"));
			ex = new TableEx("*", "T_SYS_FLOW_RUN", sr.toString());
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
		    
			e.printStackTrace();
		}
		return ex;
	}
	
	/**
	 * 
	 * @param _strCol
	 * @param rd
	 * @return
	 */
	public static String getColString(String _strCol,Record rd){
		String strReturn = "";
		try {
			FieldEx ex = rd.getFieldByName(_strCol);
			Object obj= ((ex==null||"".equals(ex))?"":(ex.value));
			strReturn = (obj==null||"".equals(obj))?"":obj.toString();
		} catch (Exception e) {
			strReturn = "";
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		}finally{
			return strReturn ;
		}
	}
	
	/**
	 * 
	 * @param S_RUN_ID
	 */
	public static void DelMsg( String S_RUN_ID) {
		DBFactory dbf = new DBFactory();
		try {
			dbf.sqlExe("delete from T_MSG_RECORDS where S_YXID='"+S_RUN_ID+"' ;", false);
		}catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			if(dbf!=null) {
				dbf.close();
			}
		}
	}
	
	/**
	 * 
	 * @param S_RUN_ID
	 */
	public static void updateValueColumns(String updateValueColumnStr, String runId, String flowId) {
		DBFactory dbf = null;
		try {
			dbf = new DBFactory();
			dbf.sqlExe("update T_SYS_FLOW_RUN set S_UPVALUE_COLS='" + updateValueColumnStr + "' where S_RUN_ID='" + runId + "' and S_FLOW_ID='" +flowId + "'", true);
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if(dbf!=null){dbf.close();}
		}
	}
	
	/**
	 * 
	 * @param strFlowId
	 * @param strVersion
	 * @param strNodeIdNow
	 * @param strCustomNodeId
	 * @return
	 */
	public static String getNextBranchTableControl(String strFlowId, String strVersion, String strNodeIdNow, String strCustomNodeId) {
		TableEx tableEx1 = null;
		TableEx tableEx2 = null;
		String result = "";
		try {
			tableEx1 = new TableEx("S_CHILD_ID", "t_sys_flow_node", "t_sys_flow_node.S_FLOW_ID='" + strFlowId + "' and t_sys_flow_node.S_AUDIT_VERSION='" + strVersion + "' and t_sys_flow_node.I_NODE_ID=" + strNodeIdNow);
        	String strChildId = tableEx1.getRecord(0).getFieldByName("S_CHILD_ID").value.toString();
        	tableEx2 = new TableEx("S_AUDIT_TABLECONTROL,I_TYPE", "t_sys_flow_node", "t_sys_flow_node.S_FLOW_ID='" + strFlowId + "' and t_sys_flow_node.S_AUDIT_VERSION='" + strVersion + "' and t_sys_flow_node.I_NODE_ID=" + strChildId);
        	Record recordTableEx2 = tableEx2.getRecord(0);
        	String nodeType = recordTableEx2.getFieldByName("I_TYPE").value.toString();
        	if ("2".equals(nodeType)) { //2. 网管分支
				String tableControl = recordTableEx2.getFieldByName("S_AUDIT_TABLECONTROL").value.toString();
				final String[] branchTableControls = tableControl.split("###");
				
				for (int i = 0; i < branchTableControls.length; i++) {
					final String[] branchTableControlContent = branchTableControls[i].split("##");
					String branchId = strChildId + "-" + strCustomNodeId;
					if (branchId.equals(branchTableControlContent[0])) {
						result = branchTableControlContent[1];
						break;
					}
				}
			}
		} catch (Exception e) {
		    MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if(tableEx1!=null){tableEx1.close();}
			if(tableEx2!=null){tableEx2.close();}
		}
		
		return result;
	}
}
