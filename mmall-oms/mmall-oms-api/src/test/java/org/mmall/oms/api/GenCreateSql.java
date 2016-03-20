package org.mmall.oms.api;
import java.io.BufferedWriter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.tools.ant.DirectoryScanner;

import com.netease.print.common.util.FileOperationUtil;
import com.netease.print.daojar.meta.annotation.AnnonOfClass;
import com.netease.print.daojar.util.SqlGenUtil;

/**
 * @author dingmingliang
 * 
 */
public class GenCreateSql {

	String metaPackageName = "com.xyl.mmall.oms.report.meta";
	
	String filePathOfSql = "C:/var/sql/mmall-oms-create.sql";
	
	boolean hasDropTable = true;

	/**
	 * 获得字符串之间的内容
	 * 
	 * @param str
	 *            字符串
	 * @param bPattern
	 *            起始的pattern
	 * @param ePattern
	 *            结束的pattern
	 * @return
	 */
	public static String subStringWithPattern(String str, String bPattern, String ePattern) {
		int bIndex = str.indexOf(bPattern);
		if (bIndex < 0)
			return null;
		int eIndex = str.indexOf(ePattern, bIndex + 1);
		// 如果eIndex<0,则说明已经遍历到最后一项
		if (eIndex < 0) {
			eIndex = str.length();
		}
		int sIndex = bIndex + bPattern.length();
		return str.substring(sIndex, eIndex).trim();
	}

	/**
	 * @param hasDropTable
	 */
	public void genCreateSql() {
		// 1.读取全部的meta类名
		List<String> classPathList = genClassPathList();
		// 2.调用 SqlGenUtil.genCreateSql 方法输出SQL
		BufferedWriter bw = FileOperationUtil.genBufferedWriter(filePathOfSql, false);
		for (String classPath : classPathList) {
			String createSql;
			try {
				AnnonOfClass annonOfClass = Class.forName(classPath).getAnnotation(AnnonOfClass.class);
				String dropSql = "DROP TABLE " + annonOfClass.tableName() + ";";
				createSql = SqlGenUtil.genCreateSql(Class.forName(classPath).newInstance());
				System.out.println(createSql);
				if (hasDropTable)
					FileOperationUtil.writeBufferedWriterWithEnter(bw, dropSql);
				FileOperationUtil.writeBufferedWriter(bw, createSql);
			} catch (Exception e) {
				System.out.println(classPath);
				e.printStackTrace();
			}
		}
		FileOperationUtil.closeBufferedWriter(bw);
	}

	/**
	 * 读取全部的meta类名
	 * 
	 * @param readFilePath
	 * @param usingClassPath
	 * @param metaContPreffix
	 * @return
	 */
	public List<String> genClassPathList() {
		List<String> classPathList = new ArrayList<>();
		String classesFoldPath = subStringWithPattern(getClass().getResource("/").toString(), "file:/",
				"target/test-classes") + "target/classes/";
		String foldPath = classesFoldPath + metaPackageName.replaceAll("\\.", "\\\\");
		// 1.读取文件夹下的所有文件列表
		DirectoryScanner ds = new DirectoryScanner();
		ds.setBasedir(new File(foldPath));
		ds.setIncludes(new String[] { "**/*.class" });
		ds.scan();
		if (ds.getIncludedFilesCount() <= 0) {
			System.out.println("目录: " + foldPath + " ,不存在符合条件的文件!");
			return classPathList;
		}

		// 读取所有Meta的相对地址
		String[] rfilePathArray = ds.getIncludedFiles();
		for (String rfilePath : rfilePathArray) {
			rfilePath = rfilePath.replaceAll("\\.class", "");
			String classPath = metaPackageName + "." + rfilePath.replaceAll("\\\\", "\\.");
			classPathList.add(classPath);
		}

		return classPathList;
	}

	public static void main(String[] argv) {
		GenCreateSql obj = new GenCreateSql();
		obj.genCreateSql();
	}
}
