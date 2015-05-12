package org.account.autopackage.recorder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

/**
 * 写更新包内容描述txt文件的类，将更新包更新的内容、数据库更新、更新的文件路径记录到readme.txt文件
 * 
 * @author 刘晓彬
 * @date 2009-06-11
 * 
 * 广东兰贝斯开发部
 */
public class ReadmeRecoder {
	// 文件路径
	private final String readmeFilePath;
	// 记录内容
	private List readmeLines = new ArrayList();

	public ReadmeRecoder(String filePath) {
		this.readmeFilePath = filePath + "/readme.txt";
	}

	// 加入一行记录
	public void addReamdmeLine(String line) {
		readmeLines.add(line);
	}
	
	public void addReamdmeLine(List lines) {
		readmeLines.addAll(lines);
	}

	// 写到文件，指定文件路径
	public void writeFile(File file) throws IOException {
		FileUtils.writeLines(file, readmeLines);
	}

	// 写到文件，根据readmeFilePath路径写
	public void writeFile() throws IOException {
		File f = new File(readmeFilePath);
		writeFile(f);
	}

}