package test.account.autopackage;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;

public class CharSetTest {

	/**
	 * Ϊ����SVN��url�����ʽ���ж�,
	 * 
	 */
	public static void main(String[] args) {
		String str = "http://192.0.0.153/svn/%e6%b5%8b%e8%af%95%e5%ba%93/";
		String sd = "is the gd��";
		
		System.out.println(StringUtils.indexOf(sd, "%"));
		
	}

}
