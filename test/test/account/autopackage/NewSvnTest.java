package test.account.autopackage;

import org.account.autopackage.svn.SVNOperKernel;

public class NewSvnTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		String svnUser = "lxb";
		String svnPwd = "lxb";
		String svnUrl ="http://192.0.0.136:8090/svn/����������Ŀ/���/��ƹ������ƽ̨";
		SVNOperKernel svn = new SVNOperKernel(svnUrl, svnUser, svnPwd);
	    System.out.println(svn.getLastRevision());
		System.out.println(svn.getSvnLogMessage(svn.getLastRevision(),svn.getLastRevision()).size());
		svn.getSvnLogMessage("200");
		
	}

}
