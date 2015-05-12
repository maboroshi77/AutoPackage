package org.account.autopackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import org.account.autopackage.recorder.DataChangeRecoder;
import org.account.autopackage.recorder.ReadmeRecoder;
import org.account.autopackage.svn.SVNOperKernel;
import org.account.autopackage.util.CommonUtil;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNLogEntry;

public class UpdateFileAutoPackager {
	private Logger logger = Logger.getLogger(UpdateFileAutoPackager.class);
	
	private final AutoPackageConfige packageConfig;
	private final UpdateFileCopyer updateFileCopyer;
	
	private final SVNOperKernel updateFileSvn;
	private final SVNOperKernel dataChangeSvn;
	
	public UpdateFileAutoPackager(AutoPackageConfige packageConfig,SVNOperKernel updateFileSvn,SVNOperKernel dataChangeSvn){
		this.packageConfig = packageConfig;
		this.updateFileSvn = updateFileSvn;
		this.dataChangeSvn = dataChangeSvn;
		this.updateFileCopyer = new UpdateFileCopyer(packageConfig.getPackageDirPath(),packageConfig.getEclipseProjectPath());
	}

	public void doPackageFile(String fileCopyDirPath,String mode) throws Exception {	
		logger.info("Eclipse工程路径为："+packageConfig.getEclipseProjectPath());
		logger.info("更新包路径为："+packageConfig.getPackageDirPath());
		
		//log日志信息
		List logLines = new ArrayList();
		logLines.add("");
		logLines.add(" 本次打包时间为："+CommonUtil.formatDateByFormat(new Date(), "yyyy-MM-dd hh:mm:ss")+" 打包人为:"+updateFileSvn.getSvnConfige().getSvnUserName());
		logLines.add("");
		//记录更新文件路径
		List pathLines = new ArrayList();
		// 记录数据库变更的内容
		List dataChangeLines = new ArrayList();
		// 记录与JIRA相关的编号
		List jiraNumList = new ArrayList();
		
		logger.info("读取SVN的更新LOG信息，读取版本号为："+packageConfig.getUpdateSvnRec());
		// 根据版本号读取SVN上的版本的log信息.
		Collection logCol = updateFileSvn.getSvnLogMessage(packageConfig.getUpdateSvnRec());
		logger.info("读取的LOG记录总共为："+logCol.size()+"条.");
		logger.info("开始读取LOG信息，并开始复制更新文件到更新包.");
		
		
		
		for (Iterator iterator = logCol.iterator(); iterator.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) iterator.next();
			//读取svn的备注信息,过滤黑色回车符号(避免在txt显示)
			String logMessage = getMemoMessage(logEntry);
			logLines.add(logMessage);
			
			//如果备注信息中有与JIRA编号关联,记录编号
			String jiraNum = CommonUtil.getJiraNum(logMessage);
			if (jiraNum != null)
				jiraNumList.add(jiraNum);
					
			//复制文件到更新包，并记录文件的路径
			pathLines.addAll(updateFileCopyer.copySvnChangeFile(logEntry,fileCopyDirPath,mode));						
		}
		
		logger.info("复制更新文件到更新包成功......");
		//去掉更新路径中的重复路径
		HashSet hs = new HashSet(pathLines);
		pathLines.clear();
		pathLines.addAll(hs);
		//进行重新排序
		Collections.sort(pathLines);
		//Collections.sort(logLines);
		
		//是否需要读取数据库更新信息
		if(packageConfig.isNeedDataChange()){
			DataChangeRecoder dataChangeRecoder = new DataChangeRecoder(dataChangeSvn,packageConfig.getPackeVersion());
			dataChangeLines = dataChangeRecoder.getDataChangeRecod(jiraNumList);
		}
		
		ReadmeRecoder readmeRecoder = new ReadmeRecoder(packageConfig.getPackageDirPath());
		
		readmeRecoder.addReamdmeLine("更新内容:");
		readmeRecoder.addReamdmeLine(logLines);
		readmeRecoder.addReamdmeLine("");
		readmeRecoder.addReamdmeLine("-----------------------");
		readmeRecoder.addReamdmeLine("数据库更新：");
		readmeRecoder.addReamdmeLine(dataChangeLines);
		readmeRecoder.addReamdmeLine("");
		readmeRecoder.addReamdmeLine("-----------------------");
		readmeRecoder.addReamdmeLine("更新路径：");
		readmeRecoder.addReamdmeLine(pathLines);
		readmeRecoder.addReamdmeLine("");
		readmeRecoder.addReamdmeLine("SVN版本："+packageConfig.getUpdateSvnRec().toString());
		readmeRecoder.writeFile();
	
	}

	private String getMemoMessage(SVNLogEntry logEntry) {
		String logMessage = "[SVN:"+logEntry.getRevision()+"]"+logEntry.getMessage();
		logMessage = logMessage.replaceAll(String.valueOf((char) 10), String
				.valueOf((char) 13).concat(String.valueOf((char) 10)));
		if(logMessage==null ||"".equals(logMessage)) logMessage="没有备注信息！";
		logMessage=logMessage+"  　｜作者："+logEntry.getAuthor()+" ｜提交时间："+CommonUtil.formatDateByFormat(logEntry.getDate(),"yyyy-MM-dd");
		return logMessage;
	}
	
	
}
