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
		 //���ǰ������´��룬����֤����ɹ���
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
		/* SVNҪ����İ汾��
		 * ֧�ֶԶ����ͬ�İ汾�Ž��д����ͨ��MultiRevisionVo��֧�ֶ���汾�š�
		 * addRangeRevision()  ����������һ���汾�ŷ�Χ��
		 * addSinlgeRevision() ����һ�������汾�š�
		 */
		MultiRevisionVo revVo = new MultiRevisionVo();
		AutoPackageConfige packageConfige = new AutoPackageConfige();     
	    revVo.addRangeRevision(startIndex, endIndex);   
		packageConfige.setEclipseProjectPath(workSpacePath+projectName);
		//�Ƿ��Զ�������ݿ���� true,false 
		packageConfige.setNeedDataChange(true); 
		//version   Ӧ�ð汾��,������ʶʡ���汾(st)���Ƕ�ݸ�汾(dw) setNeedDataChange(true) ʱ��Ч
		packageConfige.setPackeVersion("st");	
		packageConfige.setPackagePath(updatePackagePath+projectName+"/");
		packageConfige.setPackageName(fileCopyDirPath+"["+startIndex+"~"+endIndex+"]"); 
		packageConfige.setUpdateSvnRec(revVo);  
		packageConfige.setNeedDataChange(false);		
		//�������svn�ĸ�·����·��
		String projectSvnPath = SVNUtil.getProjectSvnPath(packageConfige.getEclipseProjectPath());
		//���ݹ���·������ȡ�Ĺ��̶�Ӧ��SVN��·��
		String svnUrl = SVNUtil.readSvnPathFromProjectPath(packageConfige.getEclipseProjectPath());
		updateSvnConfige.setSvnUrl(svnUrl);
		SVNOperKernel svn = new SVNOperKernel(updateSvnConfige);
		SVNOperKernel dataChangeSvn = new SVNOperKernel(dataChangeSvnConfige);		
		log.info("��ʼ���и��°����........");		
		UpdateFileAutoPackager autoPackager = new UpdateFileAutoPackager(packageConfige,svn,dataChangeSvn);
		autoPackager.doPackageFile(fileCopyDirPath,null);	
		log.info("���°���\""+packageConfige.getPackageName()+"\" ����ɹ� .");		
		// ��/recordĿ¼�¼�¼���°������
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
