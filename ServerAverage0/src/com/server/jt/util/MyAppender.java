package com.server.jt.util;

import org.apache.log4j.DailyRollingFileAppender;
import org.apache.log4j.Priority;
/**
 * log4j日志按日期输出重写，实现不同级别输出到不同目录
 * @author Teemo
 *
 */
public class MyAppender extends DailyRollingFileAppender {
	
	 public boolean isAsSevereAsThreshold(Priority priority) {    
		           //只判断是否相等，而不判断优先级     
		         return this.getThreshold().equals(priority);    
		     }  


}
