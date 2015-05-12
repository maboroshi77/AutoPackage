package org.account.autopackage.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.account.autopackage.vo.RecordVo;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

/**
 * 兰贝斯会计开发组自动打包更新包程序
 * 
 * 记录自动打包情况工具类，将每次打包的更新报名称，时间，SVN版本号记录为Excel格式
 * 
 * @author 刘晓彬
 * @date   2008-07-22
 * @version 0.0.2
 *
 */
public class RecordUtil {
	
	public static void init() throws FileNotFoundException, IOException{
		File dir = new File("record");
		if(!dir.exists())
			dir.mkdir();
		File f = new File("record/AutoPackageRecord.xls");
		if(!f.exists()){
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Sheet1");
			HSSFRow r = sheet.createRow(0);
			HSSFCellStyle style = wb.createCellStyle();
			style.setFillBackgroundColor(HSSFColor.BRIGHT_GREEN.index);

			
			HSSFCell c = r.createCell((short)0);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue("序号");
			c.setCellStyle(style);
			
			
			c = r.createCell((short)1);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue("操作日期");
			c.setCellStyle(style);
			
			c = r.createCell((short)2);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue("更新包名称");
			
			c = r.createCell((short)3);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue("SVN版本号");
			
			c = r.createCell((short)4);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue("会计版本号");
			
			FileOutputStream fo  = new FileOutputStream(f);
			wb.write(fo);
			fo.close();
			
		}
			
	}
	
	public static void addRecord(RecordVo vo){
		try {
			init();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			File f = new File("record/AutoPackageRecord.xls");
			HSSFWorkbook wb  = new HSSFWorkbook(new FileInputStream(f));
			HSSFSheet sheet = wb.getSheet("Sheet1");
			
			int rowNum = sheet.getLastRowNum();
			int curRowNum = rowNum+1;
			
			HSSFRow r = sheet.createRow(curRowNum);
			HSSFCell c = r.createCell((short)0);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue(curRowNum);
			
			c = r.createCell((short)1);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue(df.format(vo.getOperDate()));
			
			c = r.createCell((short)2);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue(vo.getFileName());
			
			c = r.createCell((short)3);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue(vo.getSvnRevNo());
			
			c = r.createCell((short)4);
			c.setEncoding(HSSFCell.ENCODING_UTF_16);
			c.setCellValue(vo.getRev());
			
			wb.write(new FileOutputStream(f));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
