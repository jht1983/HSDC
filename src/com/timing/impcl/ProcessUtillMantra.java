package com.timing.impcl;

import com.yulongtao.db.TableEx;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;

public class ProcessUtillMantra {
	TableEx table = null;
	DBFactory dbf = null;

	
	/**
	 * @param _flowId
	 * @param _runId
	 * @param _flowVer
	 * @param _nowUserCode
	 * @return boolean
	 */
	public final boolean delRunLog(String _flowId, String _runId, String _flowVer, String _nowUserCode) {
		Record record = null;
		String lastAuditUser = ""; // 最后处理人
		int rowCount = 0;
		int colCount = 0;
		StringBuffer doSql = new StringBuffer();
		dbf = new DBFactory();
		try {
			table = new TableEx("*", "T_SYS_FLOW_LOG", "S_RUN_ID='" + _runId + "' AND S_FLOW_ID='" + _flowId
					+ "' AND S_AUDIT_VERSION='" + _flowVer + "' order by S_AUD_DATE desc");
			
			rowCount = table.getRecordCount();
			
			colCount = table.getColCount();

			if (rowCount == 0) return false;
	
			record = table.getRecord(0);
			
			lastAuditUser = record.getFieldByName("S_AUD_USER").value.toString();
			
			if (_nowUserCode.equals(lastAuditUser)) {// 删除 数据 , 由于表中无独立主键, 故全条匹配
				
				doSql.append("DELETE FROM T_SYS_FLOW_LOG WHERE 1=1 ");
				
				for(int i = 1 ; i <= colCount ; i ++){ 
					if (record.getFieldByName(record.getFieldName(i)) == null || record.getFieldByName(record.getFieldName(i)).value == null) {
						continue;
					}
					
					doSql.append("and ");
					doSql.append(record.getFieldName(i)+"='"+record.getFieldByName(record.getFieldName(i)).value.toString()+"' ");
				}

				dbf.sqlExe(doSql.toString(), false);
				
				return true;
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		}finally {
			table.close();
			dbf.close();
		}
		return false;
	}
}
