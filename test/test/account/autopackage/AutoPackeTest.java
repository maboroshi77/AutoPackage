package test.account.autopackage;

import java.util.Collection;
import java.util.Iterator;

import org.account.autopackage.svn.SVNOperKernel;
import org.account.autopackage.util.SVNUtil;
import org.account.autopackage.vo.MultiRevisionVo;

import junit.framework.TestCase;

public class AutoPackeTest extends TestCase {
	public void testAutoPacke() throws Exception {
		//SVN���ʵ��û���������
		String svnUser = "binbin";
		String svnPwd = "binbin";
		
		MultiRevisionVo revVo = new MultiRevisionVo();
		revVo.addRangeRevision(1245, 1308);
		
		String eclipseWorkPath = "E:/dev/workspace6/gd-eams-533-02";
		String packagePath = "E:/gx/ʡ���������/0906�·�/";
		String packageName = "090415-0609ʡ�����°�";
		
		//�������svn�ĸ�·����·��
		String projectSvnPath = SVNUtil.getProjectSvnPath(eclipseWorkPath);
		//���ݹ���·������ȡ�Ĺ��̶�Ӧ��SVN��·��
		String svnUrl = SVNUtil.readSvnPathFromProjectPath(eclipseWorkPath);
		
		SVNOperKernel svn = new SVNOperKernel(svnUrl, svnUser, svnPwd);
		// ���ݰ汾�Ŷ�ȡSVN�ϵİ汾��log��Ϣ.
		Collection col = svn.getSvnLogMessage(revVo);
		
		for (Iterator iterator = col.iterator(); iterator.hasNext();) {
			
		}
	}
}
