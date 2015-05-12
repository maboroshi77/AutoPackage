package org.account.autopackage;

import java.io.IOException;
import java.util.Date;
import org.account.autopackage.recorder.ExcelRecoder;
import org.account.autopackage.svn.SVNOperKernel;
import org.account.autopackage.svn.SvnConfige;
import org.account.autopackage.util.SVNUtil;
import org.account.autopackage.vo.MultiRevisionVo;
import org.account.autopackage.vo.RecordVo;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
/**
 * 
 * 
 * @author moboroshi77
 * @history: 
 *           2009-06-11 V1.0 
 */

public class AutoReadPackager {
	private static Logger log = Logger.getLogger(AutoReadPackager.class);
	public static long startIndex;// start index of revision 
	public static long endIndex;// end index of revision
	public static String projectName="";// the project name in workbench
	public static String fileCopyDirPath="";//package prefix name

	public static void main(String[] args) throws Exception {
		
		String username="ywm";
		String password="asd_146";
		String svnurl="https://172.18.8.79:85/svn/ICT/trunk/eapp_4.0.1";
		String workSpacePath="D:/workspace/"; 
		String updatePackagePath="E:/packages/"; 	
		 //打包前必须更新代码，并保证编译成功。
		setParams4ProjectPackage(45711,45741,"eapp","eapp");	
		startAutoPackage(username, password, svnurl, workSpacePath,updatePackagePath);
	}

	private static void startAutoPackage(String username, String password,
			String svnurl, String workSpacePath, String updatePackagePath)
			throws Exception, SVNException, IOException {
		SvnConfige updateSvnConfige = new SvnConfige();
		updateSvnConfige.setSvnUserName(username);
		updateSvnConfige.setSvnPassword(password);		
		SvnConfige dataChangeSvnConfige = new SvnConfige();
		dataChangeSvnConfige.setSvnUserName(username);
		dataChangeSvnConfige.setSvnPassword(password);
		dataChangeSvnConfige.setSvnUrl(svnurl);	
		/* SVN要打包的版本号
		 * 支持对多个不同的版本号进行打包，通过MultiRevisionVo来支持多个版本号。
		 * addRangeRevision()  方法是增加一个版本号范围。
		 * addSinlgeRevision() 增加一个单个版本号。
		 */
		MultiRevisionVo revVo = new MultiRevisionVo();
		AutoPackageConfige packageConfige = new AutoPackageConfige();     
	    revVo.addRangeRevision(startIndex, endIndex);   
		packageConfige.setEclipseProjectPath(workSpacePath+projectName);
		//是否自动打包数据库更新 true,false 
		packageConfige.setNeedDataChange(true); 
		//version   应用版本号,用来标识省厅版本(st)还是东莞版本(dw) setNeedDataChange(true) 时有效
		packageConfige.setPackeVersion("st");	
		packageConfige.setPackagePath(updatePackagePath+projectName+"/");
		packageConfige.setPackageName(fileCopyDirPath+"["+startIndex+"~"+endIndex+"]"); 
		packageConfige.setUpdateSvnRec(revVo);  
		packageConfige.setNeedDataChange(false);		
		//工程相对svn的根路径的路径
		String projectSvnPath = SVNUtil.getProjectSvnPath(packageConfige.getEclipseProjectPath());
		//根据工程路径，获取改工程对应的SVN的路径
		String svnUrl = SVNUtil.readSvnPathFromProjectPath(packageConfige.getEclipseProjectPath());
		updateSvnConfige.setSvnUrl(svnUrl);
		SVNOperKernel svn = new SVNOperKernel(updateSvnConfige);
		SVNOperKernel dataChangeSvn = new SVNOperKernel(dataChangeSvnConfige);		
		log.info("开始进行更新包打包........");		
		UpdateFileAutoPackager autoPackager = new UpdateFileAutoPackager(packageConfige,svn,dataChangeSvn);
		autoPackager.doPackageFile(fileCopyDirPath,null);	
		log.info("更新包：\""+packageConfige.getPackageName()+"\" 打包成功 .");		
		// 在/record目录下记录更新包打包。
		ExcelRecoder excelRecoder = new ExcelRecoder();		
		RecordVo recVo = new RecordVo();
		recVo.setFileName(packageConfige.getPackageName());
		recVo.setOperDate(new Date());
		recVo.setSvnRevNo(packageConfige.getUpdateSvnRec().toString());
		recVo.setRev(projectSvnPath);	
		excelRecoder.writeExcelLine(recVo);		
		Runtime.getRuntime().exec("cmd /c explorer file:///"+packageConfige.getPackagePath());
	}
	
	public static void setParams4ProjectPackage(long startIndexParam,long endIndexParam,
			String projectNameParam,String fileCopyDirPathParam) 
	{
		startIndex=startIndexParam;
		endIndex=endIndexParam;
		projectName=projectNameParam;
		fileCopyDirPath=fileCopyDirPathParam;
	}

	
}
