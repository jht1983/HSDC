/**
 * 
 */
package com.yonyou.mis.util;

/**
 * @author tianshisheng
 *
 */
public final class ApplicationUtils {
	/**
	 * 数组元素添加单引号---拼接sql语句
	 * @param _array
	 * @return
	 */
	public final static String[] arrayAddSingleQuotes(String[] _array){
		for(int i=0,j=_array.length;i<j;i++){
			_array[i]="'"+_array[i]+"'";
		}
		return _array;
	}
	
	/**
	 * 
	 * @param sourceCode
	 * @return
	 */
	public static final String escapeHTML(String sourceCode) {
		String result = "";
		if (sourceCode == null) {
			return result;
		}
		
		result = sourceCode.replaceAll("\'", "&#x27;");
		result = result.replaceAll("\"", "&quot;");
//		result = result.replaceAll(System.getProperty("line.separator"), "<br/>");
		result = result.replaceAll(System.getProperty("line.separator"), "&nbsp;&nbsp;");
		return result;
	}
	
	public static void main(String[] args) {
		String teString = "5. \"操作开始时间\" ";
		System.out.print(ApplicationUtils.escapeHTML(teString));
	}
}
