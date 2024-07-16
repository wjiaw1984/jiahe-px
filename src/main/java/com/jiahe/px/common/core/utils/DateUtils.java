package com.jiahe.px.common.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wzm on 2019/3/20.
 */
public class DateUtils {
    public static Date parseDate(String dateStr, String format){
        Date date = null;
        try{
            date =  (new SimpleDateFormat(format)).parse(dateStr);
        }catch (Exception e){
            ExceptionUtils.raise(e.getMessage());
        }
        return date;
    }
}
