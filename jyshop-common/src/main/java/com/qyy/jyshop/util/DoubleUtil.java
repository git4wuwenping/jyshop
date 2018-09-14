package com.qyy.jyshop.util;

import java.math.BigDecimal;

public class DoubleUtil {

	public static String formatDoubleNumber(double value) {
		BigDecimal bd = new BigDecimal(Double.toString(value));
		return bd.toPlainString();
	}
	
	/**
	 * 保留二位小数点
	 * @param d
	 * @return 如果为空返回0
	 */
	public static Double retainmDecimal2(Double d) {
		if(StringUtil.isEmpty(d))
			return 0D;
		
		BigDecimal bd = new BigDecimal(d);
		return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
//	public static void main(String[] args){
//		System.out.println(DoubleUtil.retainmDecimal2(11.11111));
//	}
}
