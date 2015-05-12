package org.account.autopackage.recorder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.account.autopackage.svn.SVNOperKernel;
import org.account.autopackage.util.CommonUtil;
import org.account.autopackage.vo.DataChangeInfoVo;
import org.apache.commons.lang.StringUtils;

public class DataChangeRecoder {

	private final SVNOperKernel dataChangeSvn;
	private final String dataChangeVersion;

	public DataChangeRecoder(SVNOperKernel dataChangeSvn,
			String dataChangeVersion) {
		this.dataChangeSvn = dataChangeSvn;
		this.dataChangeVersion = dataChangeVersion;
	}

	public List getDataChangeRecod(List jiraNumList) throws Exception {
		List dataChangeLines = new ArrayList();
		Collection dataChanges = dataChangeSvn.getFileContents(dataChangeSvn
				.getSvnConfige().getSvnPath());
		boolean startRead = false;
		if (jiraNumList != null && jiraNumList.size() > 0) {
			for (Iterator iterator = dataChanges.iterator(); iterator.hasNext();) {
				String line = (String) iterator.next();
				if ("}".equals(StringUtils.trim(line)))
					startRead = false;

				if (startRead == false) {
					if (line.indexOf("@EAMS-") != -1)
						if (jiraNumList.contains(CommonUtil.getJiraNum(line))) {
							DataChangeInfoVo vo = CommonUtil
									.praseDataChangeInfoVo(line);
							if (dataChangeVersion.equals(vo.getVer())
									|| "all".equals(vo.getVer())) {
								startRead = true;
								dataChangeLines.add("");
							}
						}
				} else
					dataChangeLines.add(line);

			}
		}

		return dataChangeLines;
	}
}