/**
 * 
 */
package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

/**
 * @author tianshisheng
 *
 */
public final class WorkTicketProcessTool {
	/**
	 * 电气一种工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setDqyzSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_GZZJ_GZZZSJ,S_YQ_YQZ,S_PZJSGZSJ from T_DQYZGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_GZZJ_GZZZSJ").value));
				Date yqz = null;
				
				try {
					yqz = sdf.parse(String.valueOf(record.getFieldByName("S_YQ_YQZ").value));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZJSGZSJ").value));
				
				boolean valid = true;
				if (yqz != null) {
					if (gzzzsj.after(yqz)) {
						valid = false;
					}
				} else if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				//BHG>不合格; HG>合格; ZF>作废;
				if (valid) {
					//dbf.sqlExe("update T_DQYZGZP set S_SPJG='HG' where S_ID='" + primaryKey + "'", false);
				} else {
					dbf.sqlExe("update T_DQYZGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 电气二种工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setDqezSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_GZZJ_GZZJSJ,S_YQ_YQZ,S_PZ_PZJSGZSJ from T_DQEZGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_GZZJ_GZZJSJ").value));
				Date yqz = null;
				
				try {
					yqz = sdf.parse(String.valueOf(record.getFieldByName("S_YQ_YQZ").value));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_PZJSGZSJ").value));
				
				boolean valid = true;
				if (yqz != null) {
					if (gzzzsj.after(yqz)) {
						valid = false;
					}
				} else if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (valid) {
					//dbf.sqlExe("update T_DQEZGZP set S_SPJG='HG' where S_ID='" + primaryKey + "'", false);
				} else {
					dbf.sqlExe("update T_DQEZGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 热力机械工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setRljxSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_GZZJ_GZZJSJ,S_YQ_YQZ,S_PZ_PZJSGZSJ from T_RLYZGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_GZZJ_GZZJSJ").value));
				Date yqz = null;
				
				try {
					yqz = sdf.parse(String.valueOf(record.getFieldByName("S_YQ_YQZ").value));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_PZJSGZSJ").value));
				
				boolean valid = true;
				if (yqz != null) {
					if (gzzzsj.after(yqz)) {
						valid = false;
					}
				} else if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (!valid) {
					dbf.sqlExe("update T_RLYZGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 一级动火工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setYjdhSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_GZZJ_DHGZZJSJ,S_PZ_PZDHJSGZSJ from T_YJDHGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_GZZJ_DHGZZJSJ").value));
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_PZDHJSGZSJ").value));
				
				boolean valid = true;
				if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (!valid) {
					dbf.sqlExe("update T_YJDHGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 二级动火工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setEjdhSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_ZJ_DHGZZJSJ,S_PZ_DHJS from T_EJDHGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_ZJ_DHGZZJSJ").value));
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_DHJS").value));
				
				boolean valid = true;
				if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (!valid) {
					dbf.sqlExe("update T_EJDHGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 热控工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setRKSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_GZZJ_GZZJSJ,S_YQ_YQZ,S_PZ_PZJSGZSJ from T_RKGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_GZZJ_GZZJSJ").value));
				Date yqz = null;
				
				try {
					yqz = sdf.parse(String.valueOf(record.getFieldByName("S_YQ_YQZ").value));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_PZJSGZSJ").value));
				
				boolean valid = true;
				if (yqz != null) {
					if (gzzzsj.after(yqz)) {
						valid = false;
					}
				} else if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (!valid) {
					dbf.sqlExe("update T_RKGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 应急抢修工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setYjqxSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_GZZJ_GZZJSJ,S_PZ_PJJSGZSJ from T_YJQXGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_GZZJ_GZZJSJ").value));
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_PJJSGZSJ").value));
				
				boolean valid = true;
				if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (!valid) {
					dbf.sqlExe("update T_YJQXGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
	
	/**
	 * 动土工作票审票结果判断.
	 * 
	 * @param request
	 */
	public void setDTSpjg(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		if(primaryKey == null || primaryKey.trim().length() == 0){
			return;
	    }
		
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record = null;
		
		try {
			String sql = "select S_SPJG,S_DTJSSJ,S_YQ_YQZ,S_PZ_JSGZSJ from T_DTGZP where S_ID='" + primaryKey + "'";
			tableEx = dbf.query(sql);
			
			if (tableEx.getRecordCount() > 0) {
				record = tableEx.getRecord(0);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				
				Date gzzzsj = sdf.parse(String.valueOf(record.getFieldByName("S_DTJSSJ").value));
				Date yqz = null;
				
				try {
					yqz = sdf.parse(String.valueOf(record.getFieldByName("S_YQ_YQZ").value));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Date pzjssj = sdf.parse(String.valueOf(record.getFieldByName("S_PZ_JSGZSJ").value));
				
				boolean valid = true;
				if (yqz != null) {
					if (gzzzsj.after(yqz)) {
						valid = false;
					}
				} else if (gzzzsj.after(pzjssj)) {
					valid = false;
				}
				
				if (!valid) {
					dbf.sqlExe("update T_DTGZP set S_SPJG='BHG' where S_ID='" + primaryKey + "'", false);
				}
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(dbf!=null)
				dbf.close();
		}
	}
}
