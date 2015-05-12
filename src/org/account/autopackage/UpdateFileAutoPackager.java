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
		logger.info("Eclipse����·��Ϊ��"+packageConfig.getEclipseProjectPath());
		logger.info("���°�·��Ϊ��"+packageConfig.getPackageDirPath());
		
		//log��־��Ϣ
		List logLines = new ArrayList();
		logLines.add("");
		logLines.add(" ���δ��ʱ��Ϊ��"+CommonUtil.formatDateByFormat(new Date(), "yyyy-MM-dd hh:mm:ss")+" �����Ϊ:"+updateFileSvn.getSvnConfige().getSvnUserName());
		logLines.add("");
		//��¼�����ļ�·��
		List pathLines = new ArrayList();
		// ��¼���ݿ���������
		List dataChangeLines = new ArrayList();
		// ��¼��JIRA��صı��
		List jiraNumList = new ArrayList();
		
		logger.info("��ȡSVN�ĸ���LOG��Ϣ����ȡ�汾��Ϊ��"+packageConfig.getUpdateSvnRec());
		// ���ݰ汾�Ŷ�ȡSVN�ϵİ汾��log��Ϣ.
		Collection logCol = updateFileSvn.getSvnLogMessage(packageConfig.getUpdateSvnRec());
		logger.info("��ȡ��LOG��¼�ܹ�Ϊ��"+logCol.size()+"��.");
		logger.info("��ʼ��ȡLOG��Ϣ������ʼ���Ƹ����ļ������°�.");
		
		
		
		for (Iterator iterator = logCol.iterator(); iterator.hasNext();) {
			SVNLogEntry logEntry = (SVNLogEntry) iterator.next();
			//��ȡsvn�ı�ע��Ϣ,���˺�ɫ�س�����(������txt��ʾ)
			String logMessage = getMemoMessage(logEntry);
			logLines.add(logMessage);
			
			//�����ע��Ϣ������JIRA��Ź���,��¼���
			String jiraNum = CommonUtil.getJiraNum(logMessage);
			if (jiraNum != null)
				jiraNumList.add(jiraNum);
					
			//�����ļ������°�������¼�ļ���·��
			pathLines.addAll(updateFileCopyer.copySvnChangeFile(logEntry,fileCopyDirPath,mode));						
		}
		
		logger.info("���Ƹ����ļ������°��ɹ�......");
		//ȥ������·���е��ظ�·��
		HashSet hs = new HashSet(pathLines);
		pathLines.clear();
		pathLines.addAll(hs);
		//������������
		Collections.sort(pathLines);
		//Collections.sort(logLines);
		
		//�Ƿ���Ҫ��ȡ���ݿ������Ϣ
		if(packageConfig.isNeedDataChange()){
			DataChangeRecoder dataChangeRecoder = new DataChangeRecoder(dataChangeSvn,packageConfig.getPackeVersion());
			dataChangeLines = dataChangeRecoder.getDataChangeRecod(jiraNumList);
		}
		
		ReadmeRecoder readmeRecoder = new ReadmeRecoder(packageConfig.getPackageDirPath());
		
		readmeRecoder.addReamdmeLine("��������:");
		readmeRecoder.addReamdmeLine(logLines);
		readmeRecoder.addReamdmeLine("");
		readmeRecoder.addReamdmeLine("-----------------------");
		readmeRecoder.addReamdmeLine("���ݿ���£�");
		readmeRecoder.addReamdmeLine(dataChangeLines);
		readmeRecoder.addReamdmeLine("");
		readmeRecoder.addReamdmeLine("-----------------------");
		readmeRecoder.addReamdmeLine("����·����");
		readmeRecoder.addReamdmeLine(pathLines);
		readmeRecoder.addReamdmeLine("");
		readmeRecoder.addReamdmeLine("SVN�汾��"+packageConfig.getUpdateSvnRec().toString());
		readmeRecoder.writeFile();
	
	}

	private String getMemoMessage(SVNLogEntry logEntry) {
		String logMessage = "[SVN:"+logEntry.getRevision()+"]"+logEntry.getMessage();
		logMessage = logMessage.replaceAll(String.valueOf((char) 10), String
				.valueOf((char) 13).concat(String.valueOf((char) 10)));
		if(logMessage==null ||"".equals(logMessage)) logMessage="û�б�ע��Ϣ��";
		logMessage=logMessage+"  �������ߣ�"+logEntry.getAuthor()+" ���ύʱ�䣺"+CommonUtil.formatDateByFormat(logEntry.getDate(),"yyyy-MM-dd");
		return logMessage;
	}
	
	
}
