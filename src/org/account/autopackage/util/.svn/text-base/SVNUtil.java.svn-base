package org.account.autopackage.util;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class SVNUtil {
	
	public static String readSvnPathFromProjectPath(String projectPath) throws Exception{
		String svnfile = projectPath + "/.svn/entries";
		List lst = FileUtils.readLines(new File(svnfile));
		return (String)lst.get(4);
	}
	
	public static String readSvnParentPathFromProjectPath(String projectPath) throws Exception{
		String svnfile = projectPath + "/.svn/entries";
		List lst = FileUtils.readLines(new File(svnfile));
		return (String)lst.get(5);
	}
	
	public static String getProjectSvnPath(String projectPath) throws Exception{
		String svnPath = readSvnPathFromProjectPath(projectPath);
		String svnParetnPath = readSvnParentPathFromProjectPath(projectPath);
				
		return svnPath.replaceFirst(svnParetnPath, "");
	}
	
	public static void main(String[] args)  throws Exception {
		System.out.println(SVNUtil.getProjectSvnPath("E:/dev/workspace/AccountAutoPackage"));
	}
}
