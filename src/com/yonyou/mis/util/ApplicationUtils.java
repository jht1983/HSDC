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
}
