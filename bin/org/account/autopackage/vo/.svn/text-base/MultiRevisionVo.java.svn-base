package org.account.autopackage.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * 存放不同SVN版本的Vo
 * @author 刘晓彬
 * @date   2008-07-22
 * @version 0.0.1
 */
public class MultiRevisionVo {
	private Collection revCol = new ArrayList();
	public void addSinlgeRevision(long rev){
		long[] rev_arry = new long[]{rev,rev};
		revCol.add(rev_arry);
	}
	
	public void addRangeRevision(long startRev,long endRev){
		long[] rev_arry = new long[]{startRev,endRev};
		revCol.add(rev_arry);
	}
	
	public Collection getRevCol(){
		return revCol;
	}
	
	public String toString() {
		String str = "";
		for (Iterator iterator = revCol.iterator(); iterator.hasNext();) {
			long[] type = (long[]) iterator.next();
			if(type[0] == type[1])
				str+=" r"+type[0]+"; ";
			else
				str+="r"+type[0]+"-"+type[1]+"; ";
		}
		
		
		return str;
	}
}
