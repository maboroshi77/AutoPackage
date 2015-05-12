package org.account.autopackage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;

public class UpdateFileCopyer {
	private Logger log = Logger.getLogger(UpdateFileCopyer.class);
	
	private final String fileCopyDir ;
	private final String eclipseWorkPath;
	
	public UpdateFileCopyer(String fileCopyDir,String eclipseWorkPath){
		this.fileCopyDir = fileCopyDir;
		this.eclipseWorkPath = eclipseWorkPath;
	}
	
	/**
	 * ����SVN�ϵ��޸�·��,���ļ����Ƶ����°�,�÷������ݷ�֧�汾.
	 * @param mode 
	 * @param SvnChangePaths svn�޸�·��
	 * @return �����ļ�·��(���·��)
	 */
	public List copySvnChangeFile(SVNLogEntry logEntry,String fileCopyDirPath,String mode) throws Exception{
		
		Map SvnChangePaths=logEntry.getChangedPaths();
		List updatePaths = new ArrayList();	
		if( SvnChangePaths== null || SvnChangePaths.size() == 0)
			return updatePaths;
		
		Set keySet = SvnChangePaths.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			//ȡ��svn��log��־����޸�·��
			SVNLogEntryPath entryPath = (SVNLogEntryPath) SvnChangePaths.get(iterator.next());
			//�����ɾ���������������κδ���
			if (entryPath.getType() == 'D'){
				log.info("ɾ��������·��Ϊ"+entryPath.getPath()+" �����д���.");
				continue;
			}
			String path = entryPath.getPath();
			//�ж��޸�·���Ƿ�src���� WebRootĿ¼
			if(path.indexOf("/src/") != -1 || path.indexOf("/WebRoot/") != -1 ){
				String fileEclipsePath = "";
				String updateFilePath = "";
				
				if(path.indexOf("/src/") != -1){
					//��ȡ�޸�·����� src Ŀ¼��·��  
					String classFilePath = path.substring(path.indexOf("/src/")+5 );
					classFilePath = classFilePath.replaceAll(".java", ".class");
					updatePaths.add("[svn:"+logEntry.getRevision()+"]"+"WEB-INF/classes/"+classFilePath);
					//class�ļ���eclipse���̵ľ���·��
					
					String newClassFilePath=classFilePath.substring(classFilePath.lastIndexOf("/"));
					fileEclipsePath = eclipseWorkPath+"/WebRoot/WEB-INF/classes/"+classFilePath;
					//�����ļ���·��
					if(mode==null)
					updateFilePath = fileCopyDir+"/"+fileCopyDirPath+"/WEB-INF/classes/"+classFilePath;
					else
					updateFilePath = fileCopyDir+newClassFilePath;
				}else{
					//��ȡ�޸�·����� WebRoot Ŀ¼��·��  
					String jspFilePath = path.substring(path.indexOf("/WebRoot/")+9 );
					String newClassFilePath=jspFilePath.substring(jspFilePath.lastIndexOf("/"));
					updatePaths.add("[svn:"+logEntry.getRevision()+"]"+jspFilePath);
					//class�ļ���eclipse���̵ľ���·��
					fileEclipsePath =  eclipseWorkPath+"/WebRoot/"+jspFilePath;
					//�����ļ���·��
					if(mode==null)
					updateFilePath = fileCopyDir+"/"+fileCopyDirPath+"/"+jspFilePath;
					else
					updateFilePath = fileCopyDir+newClassFilePath;
				}

				File classfile = new File(fileEclipsePath);
				if(classfile.isDirectory()){
					log.info("����Ŀ¼"+classfile.getAbsolutePath()+",�����д���");
					continue;
				}
				
				if(classfile.exists()){
					FileUtils.copyFile(classfile, new File(updateFilePath));
				}else
					log.info("���������ڵ�Դ�ļ�"+classfile.getAbsolutePath()+",�����д���");	
				
			}else{
				log.info("�����ǹ���Ŀ¼����:"+path+" �����д���");
			}
		}
				
		return updatePaths;
	}
}
