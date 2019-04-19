/**
 * 
 */
package com.page.method;

import com.timing.impcl.MantraLog;
import com.yulongtao.db.DBFactory;
import com.yulongtao.util.EString;
import com.yulongtao.util.MisSerialUtil;

/**
 * @author tianshisheng
 *
 */
public final class FunDao {
	/**
	 * s电气一种典型工作票生成.
	 * 
	 * @return
	 */
	public static boolean exportWorkTicketEFirst(String sid, String djh) {
		boolean success = true;
		DBFactory dbf = new DBFactory();
		
		try {
			String uuid = EString.generId();
			dbf.sqlExe("INSERT INTO t_dqyzgzp_dx(S_ID,S_GZDDGZSB,S_GZNR,D_CREATETIME,S_DJH)(SELECT '" + uuid + "',S_GZDDGZSB,S_GZNR,NOW(),'" + djh + "' FROM t_dqyzgzp where s_id='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_dy_f1_dx(S_ID,S_FID,S_A)(SELECT UUID_SHORT(),'" + uuid + "',S_A FROM s_dy_f1 where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_dy_f2_dx(S_ID,S_FID,S_A)(SELECT UUID_SHORT(),'" + uuid + "',S_A FROM t_dy_f2 where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_dy_f3_dx(S_ID,S_FID,S_A)(SELECT UUID_SHORT(),'" + uuid + "',S_A FROM t_dy_f3 where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_dy_f6_dx(S_ID,S_FID,S_A,S_B)(SELECT UUID_SHORT(),'" + uuid + "',S_A,S_B FROM t_dy_f6 where s_fid='" + sid + "')", false);
		} catch (Exception e) {
			success = false;
		    MantraLog.fileCreateAndWrite(e);
		}
		finally {
			dbf.close();
		}
		
		return success;
	}
	
	/**
	 * s电气二种典型工作票生成.
	 * 
	 * @return
	 */
	public static boolean exportWorkTicketESecond(String sid, String djh) {
		boolean success = true;
		DBFactory dbf = new DBFactory();
		
		try {
			String uuid = EString.generId();
			dbf.sqlExe("INSERT INTO t_dqezgzp_dx(S_ID,S_GZDD,S_GZNR,D_CREATETIME,S_DJH)(SELECT '" + uuid + "',S_GZDD,S_GZNR,NOW(),'" + djh + "' FROM t_dqezgzp where s_id='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_d2_aqcs_dx(S_ID,S_FID,S_BXCQDAQCS)(SELECT UUID_SHORT(),'" + uuid + "',S_BXCQDAQCS FROM s_d2_aqcs where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_d2_wxdlb_dx(S_ID,S_FID,S_WXD,S_KZCS)(SELECT UUID_SHORT(),'" + uuid + "',S_WXD,S_KZCS FROM s_d2_wxdlb where s_fid='" + sid + "')", false);
		} catch (Exception e) {
			success = false;
		    MantraLog.fileCreateAndWrite(e);
		}
		finally {
			dbf.close();
		}
		
		return success;
	}
	
	/**
	 * s热力机械典型工作票生成.
	 * 
	 * @return
	 */
	public static boolean exportWorkTicketMachine(String sid, String djh) {
		boolean success = true;
		DBFactory dbf = new DBFactory();
		
		try {
			String uuid = EString.generId();
			dbf.sqlExe("INSERT INTO T_RLYZGZP_dx(S_ID,S_GZDD,S_GZNR,D_CREATETIME,S_DJH)(SELECT '" + uuid + "',S_GZDD,S_GZNR,NOW(),'" + djh + "' FROM T_RLYZGZP where s_id='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_rly_aqcs_dx(S_ID,S_FID,S_BXCQDAC)(SELECT UUID_SHORT(),'" + uuid + "',S_BXCQDAC FROM t_rly_aqcs where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_rly_yjcaqcs_dx(S_ID,S_FID,S_YJCDBH)(SELECT UUID_SHORT(),'" + uuid + "',S_YJCDBH FROM t_rly_yjcaqcs where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO t_rly_wxdlb_dx(S_ID,S_FID,S_WXD,S_YKCS)(SELECT UUID_SHORT(),'" + uuid + "',S_WXD,S_YKCS FROM t_rly_wxdlb where s_fid='" + sid + "')", false);
		} catch (Exception e) {
			success = false;
		    MantraLog.fileCreateAndWrite(e);
		}
		finally {
			dbf.close();
		}
		
		return success;
	}
	
	/**
	 * s热控典型工作票生成.
	 * 
	 * @return
	 */
	public static boolean exportWorkTicketThermalControl(String sid, String djh) {
		boolean success = true;
		DBFactory dbf = new DBFactory();
		
		try {
			String uuid = EString.generId();
			dbf.sqlExe("INSERT INTO T_RKGZP_dx(S_ID,S_GZDD,S_GZNR,D_CREATETIME,S_DJH)(SELECT '" + uuid + "',S_GZDD,S_GZNR,NOW(),'" + djh + "' FROM T_RKGZP where s_id='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO T_RK_AQCS_dx(S_ID,S_FID,S_YXRYZXD)(SELECT UUID_SHORT(),'" + uuid + "',S_YXRYZXD FROM T_RK_AQCS where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO T_RK_AQCS3_dx(S_ID,S_FID,S_GZFZRZXD)(SELECT UUID_SHORT(),'" + uuid + "',S_GZFZRZXD FROM T_RK_AQCS3 where s_fid='" + sid + "')", false);
			dbf.sqlExe("INSERT INTO T_RK_WXDLB_dx(S_ID,S_FID,S_WXD,S_YKCS)(SELECT UUID_SHORT(),'" + uuid + "',S_WXD,S_YKCS FROM T_RK_WXDLB where s_fid='" + sid + "')", false);
		} catch (Exception e) {
			success = false;
		    MantraLog.fileCreateAndWrite(e);
		}
		finally {
			dbf.close();
		}
		
		return success;
	}
}
