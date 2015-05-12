package test.account.autopackage;

import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SVNTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String url = "http://192.0.0.5/svn/eams/doc/数据库相关/数据库修改记录";
		String user = "binbin";
		String password = "binbin";
		DAVRepositoryFactory.setup();
		SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIDecoded(url));
		ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user , password );
		repository.setAuthenticationManager(authManager);
		repository.log(new String[] {""}, null,
                repository.getLatestRevision(), repository.getLatestRevision(), true, false);
		System.out.println(repository.getLatestRevision());		
	}

}
