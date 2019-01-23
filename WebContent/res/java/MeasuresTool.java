package com.timing.impcl;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.timing.impcl.MantraUtil;

//两措
public class MeasuresTool {
    MantraUtil mu = new MantraUtil();
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

    /***
	 * 
	 * 两措计划->两措执行:再计划审批通过流程结束后触发
	 * 
	 * 1:按照ID查出所需要的数据 2:更新T_LC_GYZB 新增T_LCZX 数据
	 * 
	 */
	public boolean MeasuresPlanBusinessDeal(String _planPk) {
       	TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record;
		String T_LCJHBZ__S_ZZ ,T_LCJHBZ__S_ID,autoS_id;
		
        if("".equals(_planPk)){
            return false;
        }
        
		StringBuffer sbf = new StringBuffer();
		sbf.append("select ");
		sbf.append("T_LCJHBZ.S_ID T_LCJHBZ__S_ID,");
		sbf.append("T_LCJHBZ.S_ZZ T_LCJHBZ__S_ZZ ,");
		sbf.append(" group_concat(T_LC_GYZB.S_ID) autoS_id ");
		sbf.append(" from T_LCJHBZ left join T_LC_GYZB ");
		sbf.append(" on T_LCJHBZ.S_ID = T_LC_GYZB.S_JHID ");
		sbf.append(" where T_LCJHBZ.S_ID = '"+_planPk+"' ");
		sbf.append(" group by S_ZRBM ");
		try {
			tableEx = dbf.query(sbf.toString());
			long timeStamp = System.currentTimeMillis();
			int iRecordC=tableEx.getRecordCount();
			for (int i = 0; i < iRecordC; i++) {
			    record=tableEx.getRecord(i);
                T_LCJHBZ__S_ID=getBillDataToString(record,"T_LCJHBZ__S_ID");
	            T_LCJHBZ__S_ZZ=getBillDataToString(record,"T_LCJHBZ__S_ZZ");
			    autoS_id=getBillDataToString(record,"autoS_id");

			    dbf.sqlExe("insert into T_LCZX (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_JHMS,S_JHND,S_LCZL,S_TBSJ,S_LXR,S_LXR_BM,S_FJ) select \""+timeStamp+""+i+"\",\""+T_LCJHBZ__S_ZZ+"\",'"+mu.getFlowVer("1513048527561",T_LCJHBZ__S_ZZ)+"','"+mu.getShortUuid()+"',S_JHMS,S_JHND,S_LCZL,S_TBSJ,S_LXR,S_LXR_BM,S_FJSC from T_LCJHBZ where T_LCJHBZ.S_ID = '"+_planPk+"' ", false);
                dbf.sqlExe("update T_LC_GYZB set T_LC_GYZB.S_ZXID=\""+timeStamp+""+i+"\" where T_LC_GYZB.S_ID in ("+autoS_id+");", false);
  
			}
			return true;
		} catch (Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
			    tableEx.close();
			if(dbf!=null)
			    dbf.close();
		}
		return false;
	}
	
	public boolean PrinMeasuresPlanBusinessDeal(String _planPk) {  
		TableEx tableEx = null;
		DBFactory dbf = new DBFactory();
		Record record;
		String T_LCZX__S_ZZ, T_LCZX__S_ID, autoS_id;
		StringBuffer returnSbf = new StringBuffer();

		if ("".equals(_planPk)) {
			return false;
		}
		
		StringBuffer sbf = new StringBuffer();
		sbf.append("select ");
		sbf.append("T_LCZX.S_ID T_LCZX__S_ID, ");
		sbf.append("T_LCZX.S_ZZ T_LCZX__S_ZZ, ");
		sbf.append("T_LC_GYZB.S_FZR S_FZR, ");
		sbf.append("T_LC_GYZB.S_FZR_BM S_FZR_BM, ");
		sbf.append(" group_concat(T_LC_GYZB.S_ID) autoS_id ");
		sbf.append(" from T_LCZX left join T_LC_GYZB ");
		sbf.append(" on T_LCZX.S_ID = T_LC_GYZB.S_ZXID ");
		sbf.append(" where T_LCZX.S_ID = '" + _planPk + "' ");
		sbf.append(" group by T_LC_GYZB.S_FZR_BM ");
		try {
			tableEx = dbf.query(sbf.toString());
			long timeStamp = System.currentTimeMillis();
			int iRecordC = tableEx.getRecordCount();

			for (int i = 0; i < iRecordC; i++) {
				record = tableEx.getRecord(i);
				T_LCZX__S_ID = getBillDataToString(record, "T_LCZX__S_ID");
				T_LCZX__S_ZZ = getBillDataToString(record, "T_LCZX__S_ZZ");
				autoS_id = getBillDataToString(record, "autoS_id");
                
               	dbf.sqlExe( "insert into T_FZRLCZX (S_ID,S_ZZ,SYS_FLOW_VER,S_RUN_ID,S_BZR,S_BZR_BM,S_JHND,S_JHMS,S_JHBM,S_LCZL,S_DJRQ) select '"+timeStamp+""+i+"' ,'"+T_LCZX__S_ZZ+"','"+mu.getFlowVer("1513150086507",T_LCZX__S_ZZ)+"','"+mu.getShortUuid()+"','"+getBillDataToString(record, "S_FZR")+"','"+getBillDataToString(record, "S_FZR_BM")+"',S_JHND,S_JHMS,S_JHBM,S_LCZL,S_TBSJ from T_LCZX where T_LCZX.S_ID = "+ _planPk , false);
				dbf.sqlExe("update T_LC_GYZB set T_LC_GYZB.S_FZRZXID=\"" + timeStamp + "" + i
						+ "\" where T_LC_GYZB.S_ID in (" + autoS_id + ");", false);

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
	
	public String getBillDataToString(Record record, String str) {
        return record.getFieldByName(str).value.toString();
    }
}
