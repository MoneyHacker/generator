package cn.org.rapid.framework.generator.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class FileHelper {
	public static String getRelativePath(File baseDir,File file) {
		if(baseDir.equals(file))
			return "";
		if(baseDir.getParentFile() == null)
			return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length());
		return file.getAbsolutePath().substring(baseDir.getAbsolutePath().length()+1);
	}
	
	public static void listFiles(File file,List<File> collector) throws IOException {
		collector.add(file);
		if((!file.isHidden() && file.isDirectory()) && !isIgnoreFile(file)) {
			File[] subFiles = file.listFiles();
			for(int i = 0; i < subFiles.length; i++) {
				listFiles(subFiles[i],collector);
			}
		}
	}
	
	/**
	 * 忽略的文件
	 * @param file
	 * @return
	 */
	private static boolean isIgnoreFile(File file) {
		List<String> ignoreList = new ArrayList<String>();
		ignoreList.add(".svn");
		ignoreList.add("CVS");
		ignoreList.add(".cvsignore");
		ignoreList.add("SCCS");
		ignoreList.add("vssver.scc");
		ignoreList.add(".DS_Store");
		for(int i = 0; i < ignoreList.size(); i++) {
			if(file.getName().equals(ignoreList.get(i))) {
				return true;
			}
		}
		return false;
	}
}
