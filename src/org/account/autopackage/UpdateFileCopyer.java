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
	 * 根据SVN上的修改路径,将文件复制到更新包,该方法兼容分支版本.
	 * @param mode 
	 * @param SvnChangePaths svn修改路径
	 * @return 更新文件路径(相对路径)
	 */
	public List copySvnChangeFile(SVNLogEntry logEntry,String fileCopyDirPath,String mode) throws Exception{
		
		Map SvnChangePaths=logEntry.getChangedPaths();
		List updatePaths = new ArrayList();	
		if( SvnChangePaths== null || SvnChangePaths.size() == 0)
			return updatePaths;
		
		Set keySet = SvnChangePaths.keySet();
		for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
			//取得svn的log日志里的修改路径
			SVNLogEntryPath entryPath = (SVNLogEntryPath) SvnChangePaths.get(iterator.next());
			//如果是删除操作，不进行任何处理
			if (entryPath.getType() == 'D'){
				log.info("删除操作，路径为"+entryPath.getPath()+" 不进行处理.");
				continue;
			}
			String path = entryPath.getPath();
			//判断修改路径是否src或者 WebRoot目录
			if(path.indexOf("/src/") != -1 || path.indexOf("/WebRoot/") != -1 ){
				String fileEclipsePath = "";
				String updateFilePath = "";
				
				if(path.indexOf("/src/") != -1){
					//获取修改路径相对 src 目录的路径  
					String classFilePath = path.substring(path.indexOf("/src/")+5 );
					classFilePath = classFilePath.replaceAll(".java", ".class");
					updatePaths.add("[svn:"+logEntry.getRevision()+"]"+"WEB-INF/classes/"+classFilePath);
					//class文件在eclipse工程的绝对路径
					
					String newClassFilePath=classFilePath.substring(classFilePath.lastIndexOf("/"));
					fileEclipsePath = eclipseWorkPath+"/WebRoot/WEB-INF/classes/"+classFilePath;
					//更新文件的路径
					if(mode==null)
					updateFilePath = fileCopyDir+"/"+fileCopyDirPath+"/WEB-INF/classes/"+classFilePath;
					else
					updateFilePath = fileCopyDir+newClassFilePath;
				}else{
					//获取修改路径相对 WebRoot 目录的路径  
					String jspFilePath = path.substring(path.indexOf("/WebRoot/")+9 );
					String newClassFilePath=jspFilePath.substring(jspFilePath.lastIndexOf("/"));
					updatePaths.add("[svn:"+logEntry.getRevision()+"]"+jspFilePath);
					//class文件在eclipse工程的绝对路径
					fileEclipsePath =  eclipseWorkPath+"/WebRoot/"+jspFilePath;
					//更新文件的路径
					if(mode==null)
					updateFilePath = fileCopyDir+"/"+fileCopyDirPath+"/"+jspFilePath;
					else
					updateFilePath = fileCopyDir+newClassFilePath;
				}

				File classfile = new File(fileEclipsePath);
				if(classfile.isDirectory()){
					log.info("碰到目录"+classfile.getAbsolutePath()+",不进行处理");
					continue;
				}
				
				if(classfile.exists()){
					FileUtils.copyFile(classfile, new File(updateFilePath));
				}else
					log.info("碰到不存在的源文件"+classfile.getAbsolutePath()+",不进行处理");	
				
			}else{
				log.info("碰到非工作目录内容:"+path+" 不进行处理");
			}
		}
				
		return updatePaths;
	}
}
