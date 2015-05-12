package test.account.autopackage;

import java.util.Collection;
import java.util.Iterator;

import org.account.autopackage.svn.SVNOperKernel;
import org.account.autopackage.util.SVNUtil;
import org.account.autopackage.vo.MultiRevisionVo;

import junit.framework.TestCase;

public class AutoPackeTest extends TestCase {
	public void testAutoPacke() throws Exception {
		//SVN访问的用户名和密码
		String svnUser = "binbin";
		String svnPwd = "binbin";
		
		MultiRevisionVo revVo = new MultiRevisionVo();
		revVo.addRangeRevision(1245, 1308);
		
		String eclipseWorkPath = "E:/dev/workspace6/gd-eams-533-02";
		String packagePath = "E:/gx/省厅服务大厅/0906月份/";
		String packageName = "090415-0609省厅更新包";
		
		//工程相对svn的根路径的路径
		String projectSvnPath = SVNUtil.getProjectSvnPath(eclipseWorkPath);
		//根据工程路径，获取改工程对应的SVN的路径
		String svnUrl = SVNUtil.readSvnPathFromProjectPath(eclipseWorkPath);
		
		SVNOperKernel svn = new SVNOperKernel(svnUrl, svnUser, svnPwd);
		// 根据版本号读取SVN上的版本的log信息.
		Collection col = svn.getSvnLogMessage(revVo);
		
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			
		}
	}
}
