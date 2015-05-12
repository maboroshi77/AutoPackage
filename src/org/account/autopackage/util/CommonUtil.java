package org.account.autopackage.util;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.account.autopackage.vo.DataChangeInfoVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

public class CommonUtil {
	
	public static String getJiraNum(String logMeg){
		Pattern p = Pattern.compile("EAMS-[0-9]+");	
		Matcher m = p.matcher(logMeg);
		if(m.find())
			return m.group(0);
		else
			return null;
	}
	
	public static DataChangeInfoVo praseDataChangeInfoVo(String dataChangeInfo) throws Exception{
		DataChangeInfoVo vo = new DataChangeInfoVo();
		vo.setJiraNum(getJiraNum(dataChangeInfo));
		String[] infos = StringUtils.split(StringUtils.substringBetween(dataChangeInfo, "(", ")"),',');
		for (int i = 0; i < infos.length; i++) {
			String string = infos[i];
			BeanUtils.setProperty(vo, StringUtils.substringBefore(string, "="), StringUtils.substringAfter(string, "="));
		}
		return vo;
	}
	
	public final static String formatDateByFormat(java.util.Date date,String format) { 
		 String result = ""; 
		 if(date != null) { 
			 try { SimpleDateFormat sdf = new SimpleDateFormat(format); 
			 result = sdf.format(date); } 
			 catch(Exception ex) { 
				 ex.printStackTrace(); } } 
		 return result; 
	 }
}
