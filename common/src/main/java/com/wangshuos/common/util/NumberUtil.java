package com.sg.bjftviewprotect.system.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @ClassName NumberUtil
 * @Author wangshuo
 * @Date 2024/5/29 08:47
 * @Version 1.0
 **/
public class NumberUtil {
    /**
     * 保留几位小数，并四舍五入
     */
    public static BigDecimal roundBigDecimal(double value, int decimalPlaces) {
        return BigDecimal.valueOf(value).setScale(decimalPlaces, RoundingMode.HALF_UP);
    }
}
