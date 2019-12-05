package com.yulongtao.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.Debug;
import com.yulongtao.db.DBFactory;
import com.yulongtao.db.Record;
import com.yulongtao.db.TableEx;
import com.yulongtao.web.ABSElement;

public class MisSerialUtil
{
    private static final SimpleDateFormat FORMATTER_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final SimpleDateFormat FORMATTER_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
    private static final SimpleDateFormat FORMATTER_yyyy = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat FORMATTER_yyyyMM = new SimpleDateFormat("yyyyMM");
    
    /**
     * 
     * @param _strCodeId
     * @param _request
     * @return
     */
    public static String getSerialNum(final String _strCodeId, final HttpServletRequest _request) {
    	//MUST use SerialUtil's MAPSERIALNUMCONSTANS, it has initialized by the system
        final Vector<String[]> vec = SerialUtil.MAPSERIALNUMCONSTANS.get(_strCodeId);
        final StringBuffer strResult = new StringBuffer();
        final String _strSplit = "_";
        final int count = vec.size();
//        final ParamTree pTree = new ParamTree();
//        pTree.request = _request;
        TableEx tableEx = null;
        for (int i = 0; i < count; ++i) {
            final String[] arrStrCodeRule = vec.get(i);
            strResult.append(arrStrCodeRule[0]);
            final String strType = arrStrCodeRule[1];
            if (strType.equals("yyyyMMddHHmmss")) {
                strResult.append(leftPading(new StringBuilder(String.valueOf(MisSerialUtil.FORMATTER_yyyyMMddHHmmss.format(new Date()))).toString(), arrStrCodeRule[4], Integer.parseInt(("".equals(arrStrCodeRule[3]) || arrStrCodeRule[3] == null) ? "0" : arrStrCodeRule[3])));
            }
            else if (strType.equals("yyyyMMdd")) {
                strResult.append(leftPading(new StringBuilder(String.valueOf(MisSerialUtil.FORMATTER_yyyyMMdd.format(new Date()))).toString(), arrStrCodeRule[4], Integer.parseInt(("".equals(arrStrCodeRule[3]) || arrStrCodeRule[3] == null) ? "0" : arrStrCodeRule[3])));
            }
            else if (strType.equals("yyyy")) {
                strResult.append(leftPading(new StringBuilder(String.valueOf(MisSerialUtil.FORMATTER_yyyy.format(new Date()))).toString(), arrStrCodeRule[4], Integer.parseInt(("".equals(arrStrCodeRule[3]) || arrStrCodeRule[3] == null) ? "0" : arrStrCodeRule[3])));
            }
            else if (strType.equals("yy")) {
                strResult.append(leftPading(new StringBuilder(String.valueOf(MisSerialUtil.FORMATTER_yyyy.format(new Date()))).toString().substring(2), arrStrCodeRule[4], Integer.parseInt(("".equals(arrStrCodeRule[3]) || arrStrCodeRule[3] == null) ? "0" : arrStrCodeRule[3])));
            }
            else if (strType.equals("yyyyMM")) {
            	strResult.append(MisSerialUtil.FORMATTER_yyyyMM.format(new Date()));
            }
            else if (strType.equals("dataset")) {
            	String newSerialNumber = "1";
                try {
                    tableEx = MisSerialUtil.getTableEx(arrStrCodeRule[2], _request);
                    int recordCount = tableEx.getRecordCount();
                    
                    if (recordCount > 0) {
                    	String strTemp = tableEx.getRecord(0).getFieldByName("ID").value.toString();
                    	if (strTemp != null && strTemp.trim().length() > 0) {
                        	newSerialNumber = strTemp.trim();
						}
                        
                        //check whether it has cut off numbers
                        if (recordCount > 1) {
                            List<String> numbers = new ArrayList<String>();
                            for (int k = recordCount - 1; k > 0; k--) {
                            	Record record = tableEx.getRecord(k);
                            	numbers.add(record.getFieldByName("ID").value.toString());
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
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (tableEx != null) {
                        tableEx.close();
                    }
                }
                
                strResult.append(leftPading(newSerialNumber, arrStrCodeRule[4], Integer.parseInt(("".equals(arrStrCodeRule[3]) || arrStrCodeRule[3] == null) ? "0" : arrStrCodeRule[3])));
            }
            else if (strType.equals("param")) {
                strResult.append(arrStrCodeRule[2]);
            }
            strResult.append((i == count - 1) ? "" : _strSplit);
        }
        return strResult.toString();
    }
    
    public static String leftPading(final String strSrc, final String flag, final int strSrcLength) {
        String strReturn = "";
        String strtemp = "";
        final int curLength = strSrc.trim().length();

        if (strSrc != null && curLength >= strSrcLength) {
            strReturn = strSrc.trim();
        }
        else {
            for (int i = 0; i < strSrcLength - curLength; ++i) {
                strtemp = strtemp + flag;
            }
            strReturn = strtemp + strSrc.trim();
        }
        return strReturn;
    }
    
    public static String resultNum(final String[][] _arr, final String _strSplit) {
        final Calendar cal = Calendar.getInstance();
        cal.get(1);
        return null;
    }
    
    public static String getRandom(final int l) {
        final Random rm = new Random();
        final double pross = (1.0 + rm.nextDouble()) * Math.pow(10.0, l);
        final String fixLenthString = String.valueOf(pross);
        return fixLenthString.substring(1, l + 1);
    }
    
    /**
     * 
     * @param strId
     * @param _request
     * @return
     */
    private static TableEx getTableEx(final String strId, final HttpServletRequest _request) {
        TableEx table = null;
        TableEx te = null;
        DBFactory dbff = null;
        String sql = "";
        try {
            table = new TableEx("SSQL,SDATADB", "t_sys_dataset", "SCONID='" + strId + "'");
            final Record record = table.getRecord(0);
            sql = record.getFieldByName("SSQL").value.toString();
            final ABSElement abs = new ABSElement();
            if (_request != null) {
                sql = abs.getFilterData(sql, _request);
            }
            Debug.println("\u3010\u4fe1\u606f\u3011\u6570\u636e\u96c6" + strId + "=" + sql);
            final String strDataDb = record.getFieldByName("SDATADB").value.toString();
            if (!strDataDb.equals("")) {
                dbff = new DBFactory(strDataDb, 1);
            }
            else {
                dbff = new DBFactory();
            }
            te = dbff.query(sql);
        }
        catch (Exception e) {
            Debug.println("\u67e5\u8be2\u6570\u636e\u96c6\u3010" + strId + "\u3011\u9519\u8bef\uff01" + e);
            return te;
        }
        finally {
            table.close();
        }
        table.close();
        return te;
    }
}
