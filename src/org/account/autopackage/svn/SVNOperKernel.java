package org.account.autopackage.svn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.account.autopackage.vo.MultiRevisionVo;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNOperKernel {
	private Logger log = Logger.getLogger(SVNOperKernel.class);
	private SvnConfige svnConfige;
	private SVNRepository repository = null;
	
	public SVNOperKernel(String SvnUrl,String user,String password) throws SVNException{		
		DAVRepositoryFactory.setup();
		repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(SvnUrl));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, password);
        repository.setAuthenticationManager(authManager);

        log.info("初始化SVN成功.......");
	}
	
	public SVNOperKernel(SvnConfige svnConfige)  throws SVNException{	
		this(svnConfige.getSvnUrl(),svnConfige.getSvnUserName(),svnConfige.getSvnPassword());
		this.svnConfige = svnConfige;
	}

	public void init(String SvnUrl,String user,String password) throws SVNException{	
		DAVRepositoryFactory.setup();
		SVNURL codeSvnUrl = null;
		if(StringUtils.indexOf(SvnUrl, "%")!=-1)
			codeSvnUrl = SVNURL.parseURIDecoded(SvnUrl);
		else
			codeSvnUrl = SVNURL.parseURIEncoded(SvnUrl);
		
		repository = SVNRepositoryFactory.create(codeSvnUrl);
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(user, password);
        repository.setAuthenticationManager(authManager);
	}
	
	public void checkInit() throws Exception{
		if(repository == null)
			throw new Exception("[严重错误]未初始化SVNRepository信息.....");
	}
	
	/**
	 * 根据传入的版本号集合，获取SVN对应版本的日志信息。
	 * @param revision 
	 * @return
	 * @throws Exception
	 */
	public  List getSvnLogMessage(List revision) throws Exception{
		return null;
	}
	
	public  List getSvnLogMessage(String revision) throws Exception{
		return null;
	}
	
	public  Collection getSvnLogMessage(MultiRevisionVo vo ) throws Exception{
		Collection revCol = vo.getRevCol();
		Collection SvnLogCol = new ArrayList();
		
		for (Iterator iterator = revCol.iterator(); iterator.hasNext();) {
			long[] object = (long[]) iterator.next();
			SvnLogCol.addAll(getSvnLogMessage(object[0], object[1]));
		}
		return SvnLogCol;
	}
	
	public  Collection getSvnLogMessage(long startRevision,long endRevision) throws Exception{
		log.info("读取SVN上的版本 r"+startRevision+"-"+endRevision+".....");
		 Collection logEntries = null;
		 logEntries = repository.log(new String[] {""}, null,
                 startRevision, endRevision, true, false);
				
		return logEntries;
	}
	
	public SVNRepository getRepository() {
		return repository;
	}

	public void setRepository(SVNRepository repository) {
		this.repository = repository;
	}
	
	public Collection getFileContents(String path) throws Exception{
		long rev = getRepository().getLatestRevision();	
		return getFileContents(path,rev);
		
	}
	
	public Collection getFileContents(String path, long revision) throws Exception{
		log.info("读取SVN上文件的内容:"+path+", 版本号为:"+revision);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		getRepository().getFile(path, revision, null, bs);
		return IOUtils.readLines(new ByteArrayInputStream(bs.toByteArray()));
	}
	
	public SvnConfige getSvnConfige(){
		return svnConfige;
	}

	public long getLastRevision() throws Exception{
		return repository.getLatestRevision();
	}
}
