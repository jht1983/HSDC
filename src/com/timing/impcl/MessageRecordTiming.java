/**
 * 
 */
package com.timing.impcl;

import com.yulongtao.db.DBFactory;

/**
 * @author tianshisheng
 *
 */
public class MessageRecordTiming {
	private static final String SQL_PRE = "DELETE FROM T_MSG_RECORDS ";
	
	/**
	 * 
	 */
	public void cleanDutyMessages() {
		DBFactory dbf = null;
		try {
			dbf = new DBFactory();
			//(
			//'1500428508300','1504686822663','1505700684880','1505703509013','1505706136535',
			//'1505729336281','1505787525562','1506309943847','1508322738124','1508409020345',
			//'1516587886146','1531366190826','1531376571473','1538216343313','155609021542713968',
			//'155609021542713969','1567328930800', '1520222065484', '1510924423686'
			//)
			
			try {
				//1. 维修定期工作计划
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1500428508300' and S_SID not in (select S_ZJ from T_WXDQGZJH)", false);
				
				//2. 电气一种工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1504686822663' and S_SID not in (select s_id from T_DQYZGZP WHERE T_DQYZGZP.S_DEL<>1)", false);
				
				//3. 电气二种工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1505700684880' and S_SID not in (select s_id from T_DQEZGZP WHERE T_DQEZGZP.S_DEL<>1)", false);
				
				//4. 热控工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1505703509013' and S_SID not in (select s_id from T_RKGZP WHERE T_RKGZP.S_DEL<>1)", false);
				
				//5. 工作票-应急抢修
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1505706136535' and S_SID not in (select s_id from T_YJQXGZP WHERE T_YJQXGZP.S_DEL<>1)", false);
				
				//6. 受限空间工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1505729336281' and S_SID not in (select s_id from T_RLJXEZGZP WHERE T_RLJXEZGZP.S_DEL<>1)", false);
				
				//7. 热力机械工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1505787525562' and S_SID not in (select s_id from T_RLYZGZP WHERE T_RLYZGZP.S_DEL<>1)", false);
				
				//8. 操作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1506309943847' and S_SID not in (select s_id from T_CZPSC)", false);
				
				//9. 二级动火工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1508322738124' and S_SID not in (select s_id from T_EJDHGZP WHERE T_EJDHGZP.S_DEL<>1)", false);
				
				//10. 动土工作票
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1508409020345' and S_SID not in (select s_id from T_DTGZP WHERE T_DTGZP.S_DEL<>1)", false);
				
				//11. 安全检查问题确认
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1516587886146' and S_SID not in (select s_id from T_AQJCWTQR)", false);
				
				//12. 运行定期工作执行
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1531366190826' and S_SID not in (select s_id from T_YXDQGZZX_R)", false);
				
				//13. 运行定期工作计划
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1531376571473' and S_SID not in (select s_id from T_YXDQGZJH_R)", false);
				
				//14. 班前班后会议记录
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1538216343313' and S_SID not in (select s_id from T_BQHHYJL)", false);
				
				//15. 缺陷记录
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='155609021542713968' and S_SID not in (select s_id from T_QXJL)", false);
				
				//16. 维修定期工作执行
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='155609021542713969' and S_SID not in (select S_ZJ from T_WXDQGZZX)", false);
				
				//17. 生产命令
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1567328930800' and S_SID not in (select s_id from T_SCML)", false);
				
				//18. 钥匙借用
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1520222065484' and S_SID not in (select s_id from T_YSJY)", false);
				
				//19. 检修管理-汇总检修计划
				dbf.exeSqls(SQL_PRE + " where S_PAGECODE='1510924423686' and S_SID not in (select s_zj from T_HZJXJH)", false);
			} catch (Exception e) {
				// TODO: handle exception
			}
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
			e.printStackTrace();
		} finally {
			if (dbf != null) {
				dbf.close();
			}
		}
	}
}

