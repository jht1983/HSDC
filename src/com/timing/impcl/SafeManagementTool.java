/**
 * 
 */
package com.timing.impcl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;

/**
 * @author tianshisheng
 *
 */
public final class SafeManagementTool {
	/**
	 * 
	 * @param request
	 */
	public void generateSerialNumber(HttpServletRequest request) {
		String primaryKey = request.getParameter("S_ID");
		String zy = request.getParameter("T_SBSG$S_ZY");
		
		if (primaryKey == null || "".equals(primaryKey)) {
			return;
		}
		
		TableEx tableEx = null;
		TableEx tableEx2 = null;
		DBFactory dbf = null;
		
		try {
			dbf = new DBFactory();
			String sql = "select S_SGBH from T_SBSG where S_ID='" + primaryKey + "'";
		    tableEx = dbf.query(sql);
		    
		    if (tableEx.getRecordCount() > 0) {
		    	Record record = tableEx.getRecord(0);
		    	String bh = String.valueOf(record.getFieldByName("S_SGBH").value);
		    	if ("".equals(bh) || "null".equalsIgnoreCase(bh)) {
		    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
			        final String strCurYear = sdf.format(new Date());
					StringBuffer newBh = new StringBuffer(strCurYear);
					newBh.append("-");
					
					if ("hd01".equals(zy)) { //汽机
						newBh.append("QJ");
					} else if ("hd02".equals(zy)) { //锅炉
						newBh.append("GL");
					} else if ("hd03".equals(zy)) { //电气
						newBh.append("DQ");
					} else if ("hd04".equals(zy)) { //化学
						newBh.append("HX");
					} else if ("hd05".equals(zy)) { //热控
						newBh.append("RK");
					} else if ("hd06".equals(zy)) { //输煤除灰
						newBh.append("SM");
					} else if ("hd07".equals(zy)) { //综合
						newBh.append("ZH");
					} else if ("hd08".equals(zy)) { //脱硫
						newBh.append("TL");
					} else if ("hd09".equals(zy)) { //信息
						newBh.append("XX");
					} else { //un-define
						newBh.append("UD");
					}
					newBh.append("_");
					
					String sql2 = 
							"select ifnull(max(substring(S_SGBH,9,3))+1,1) ID from T_SBSG where substring(S_SGBH,1,4)=date_FORMAT(NOW(),'%Y') AND S_ZY='" + 
					        zy + "' ORDER BY ID DESC";
					tableEx2 = dbf.query(sql2);
					String newSerialNumber = "1";
					int recordCount = tableEx2.getRecordCount();
					if (recordCount > 0) {
                        String tempStr = String.valueOf(tableEx2.getRecord(0).getFieldByName("ID").value);
                        if (tempStr != null && tempStr.trim().length() > 0) {
                        	newSerialNumber = tempStr.trim();
						}
                        
                        //check whether it has cut off numbers
                        if (recordCount > 1) {
                            List<String> numbers = new ArrayList<String>();
                            for (int k = recordCount - 1; k > 0; k--) {
                            	Record recordTemp = tableEx2.getRecord(k);
                            	numbers.add(recordTemp.getFieldByName("ID").value.toString());
    						}
                            int tempNumber = 0;
                            for (int m = 0; m < numbers.size(); m++) {
                            	tempNumber = Integer.parseInt(numbers.get(m));
                            	if (m == 0 && tempNumber == 3) { //特殊，为1的情况
                            		newSerialNumber = "1";
                            		break;
                            	} else if ((m < numbers.size() - 1) && (tempNumber + 1 < Integer.parseInt(numbers.get(m + 1)))) {
    								newSerialNumber = String.valueOf(tempNumber);
    								break;
    							}
    						}
						}
                    }
					
					final int curLength = newSerialNumber.length();
					if (curLength < 3) {
						for (int i = 0; i < 3 - curLength; ++i) {
							newBh.append("0");
						}
					}
					
					newBh.append(newSerialNumber);
					
					dbf.sqlExe("update T_SBSG set S_SGBH='" + newBh + "' where S_ID='" + primaryKey + "'", false);
				}
		    }
		} catch(Exception e) {
			MantraLog.fileCreateAndWrite(e);
		} finally {
			if(tableEx!=null)
				tableEx.close();
			if(tableEx2!=null)
				tableEx2.close();
			if(dbf!=null)
				dbf.close();
		}
	}
}
