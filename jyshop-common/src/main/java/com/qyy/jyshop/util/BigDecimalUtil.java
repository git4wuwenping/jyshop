package com.qyy.jyshop.util;

import java.math.BigDecimal;
import java.util.Random;

public final class BigDecimalUtil {

    /**
     * 获取指定区间的数
     * @author hwc
     * @created 2018年4月12日 下午2:56:50
     * @param min 最小值
     * @param max 最大值
     * @param scale 保留位数
     * @return
     */
    public static BigDecimal nextBigDecimal(BigDecimal min, BigDecimal max, int scale) {
        
        if(min==null || max==null)
            return new BigDecimal(0);
        if(min.compareTo(max)>0)
            return min;
        
        System.out.println(100.11 + ((111.21 - 100.11) * new Random().nextDouble()));
        max=max.subtract(min);
        max=max.multiply(new BigDecimal(new Random().nextDouble()));
        max=max.add(min);
        
        return max.setScale(scale,BigDecimal.ROUND_DOWN);
    }
    
    
    public static void main(String[] args){
        System.out.println(BigDecimalUtil.nextBigDecimal(new BigDecimal(99.99), new BigDecimal(111.11), 2));
    }
}
