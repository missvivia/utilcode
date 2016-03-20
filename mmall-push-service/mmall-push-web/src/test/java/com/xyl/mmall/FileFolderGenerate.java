package com.xyl.mmall;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成新的profile
 * <p>
 * 注意：
 * <p>
 * 特殊处理：
 * <p>
 * 增加相应config文件夹下的config类
 * <p>
 * 1.bi-core
 * <p>
 * 2.mainsite-web
 * <p>
 * 3.ip-web
 * <p>
 * 4.itemcenter-web
 * <p>
 * 5.sales-web
 * <p>
 * 6.mmall-security
 * 
 * @author hzzhaozhenzuo
 * 
 */
public class FileFolderGenerate {

	private static final String ROOT_PATH = "/home/zhao/mmallCode/develop2/mmall/mmall-parent";

	private static final String PROJECT_PREFIX = "mmall";

	private static final String CONFIG_PATH = "/src/main/resources/config";

	private static final String SOURCE_APPLICATION_FILE_NAME = "application.properties";

	private static final String SOURCE_PROFILE = "dev";

	private static final String TARGET_PROFILE = "feature1";

	private static final Logger logger = LoggerFactory.getLogger(FileFolderGenerate.class);

	private static final String ehcachePropery = "      <property name=\"configLocation\" value=\"classpath:config/%s/ehcache.xml\" />";

	private static final String profileOfApplicationFile = "spring.profiles.active=%s";

	public static void main(String[] args) {

		List<File> fileList = getProjectsOfRootDir(ROOT_PATH);

		for (File file : fileList) {
			logger.info(file.getName());
		}

		boolean successFlag = processLevel2ForEeachProject(fileList);
		logger.info("-------flag:" + successFlag);

	}

	private static boolean processLevel2ForEeachProject(List<File> fileList) {
		boolean successAll = true;
		for (File file : fileList) {
			// 每个文件夹下二级目录
			List<File> level2List = getLevel2Files(file);
			for (File level2File : level2List) {

				// copy application file
				if (!copyApplicationFiles(level2File)) {
					successAll = false;
				}

				// copy ehcache file
				if (!copyEhcacheFiles(level2File)) {
					successAll = false;
				}
			}
		}

		// mmall-framework需要单独处理
		processSpeFolderLevel2(ROOT_PATH + "/" + "mmall-framework");

		// mmall-security
		processSpeFolderLevel2(ROOT_PATH + "/" + "mmall-security");

		return successAll;
	}

	private static boolean processSpeFolderLevel2(String fileName) {
		boolean successAll = true;
		File frameworkFile = new File(fileName);
		if (frameworkFile.exists()) {
			if (!copyApplicationFiles(frameworkFile)) {
				successAll = false;
			}
			// copy ehcache file
			if (!copyEhcacheFiles(frameworkFile)) {
				successAll = false;
			}
		} else {
			logger.error("找不到" + fileName + "文件," + ROOT_PATH + "/" + fileName);
			successAll = false;
		}
		return successAll;
	}

	private static boolean copyEhcacheFiles(File level2File) {
		logger.info("\n--------------开始拷贝项目" + level2File.getAbsolutePath() + "的ehcache文件-------------------");
		String fileAbPath = level2File.getAbsolutePath();
		String configFoldPath = fileAbPath + CONFIG_PATH;
		File configFile = new File(configFoldPath);
		boolean successFlag = true;
		if (!configFile.exists()) {
			logger.warn("\n当前文件夹:" + level2File.getName() + "不存在config文件夹");
			return true;
		}

		String sourceEhcacheFold = configFoldPath + "/" + SOURCE_PROFILE;
		File sourceEhcacheFile = new File(sourceEhcacheFold);
		if (!sourceEhcacheFile.exists()) {
			logger.warn("\n项目文件:" + level2File.getName() + "不存在ehcache文件夹:" + sourceEhcacheFold);
			return true;
		}

		File[] ehcacheFileArr = sourceEhcacheFile.listFiles();
		if (ehcacheFileArr == null || ehcacheFileArr.length <= 0) {
			logger.error("\n项目:" + level2File.getName() + ",目录:" + sourceEhcacheFold + "不存在ehcache配置文件");
			return false;
		}

		String targetEhcacheFold = configFoldPath + "/" + TARGET_PROFILE;

		for (File cacheFile : ehcacheFileArr) {
			if (!copyFile(cacheFile.getAbsolutePath(), targetEhcacheFold + "/" + cacheFile.getName())) {
				successFlag = false;
			}
		}
		return successFlag;
	}

	private static boolean copyApplicationFiles(File level2File) {
		String fileAbPath = level2File.getAbsolutePath();
		String configFoldPath = fileAbPath + CONFIG_PATH;
		File configFile = new File(configFoldPath);
		if (!configFile.exists()) {
			logger.warn("\n当前文件夹:" + level2File.getName() + "不存在config文件夹");
			return true;
		}

		// 开始拷贝一个新profile的application文件
		String sourceFile = configFoldPath + "/" + SOURCE_APPLICATION_FILE_NAME;
		String targetFile = configFoldPath + "/" + "application-" + TARGET_PROFILE + ".properties";
		boolean flagApplicationFile = copyFile(sourceFile, targetFile);
		logger.info("\n拷贝文件:" + targetFile + ",成功标记flag:" + flagApplicationFile);
		return flagApplicationFile;
	}

	private static boolean copyFile(String sourceFile, String targetFile) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		try {
			br = new BufferedReader(new FileReader(new File(sourceFile)));
			File tf = new File(targetFile);

			if (!tf.getParentFile().exists() || !tf.getParentFile().isDirectory()) {
				boolean flag = tf.getParentFile().mkdir();
				if (!flag) {
					logger.error("创建目录:" + tf.getParentFile().getAbsolutePath() + "失败");
					return false;
				}
			}

			if (!tf.exists()) {
				tf.createNewFile();
			}
			bw = new BufferedWriter(new FileWriter(tf));
			String text = null;
			while ((text = br.readLine()) != null) {
				text = replaceChar(text, sourceFile, targetFile);
				bw.write(text + "\n");
			}
		} catch (FileNotFoundException e) {
			logger.error("打开文件:" + sourceFile + "失败", e);
			return false;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

	// <property name="configLocation"
	// value="classpath:config/performance/ehcache.xml" />
	private static String replaceChar(String s, String sourceFile, String targetFile) {
		if (sourceFile.indexOf("ehcache-bean.xml") > 0) {
			String replaceSourceStr = "classpath:config/" + SOURCE_PROFILE + "/ehcache.xml";
			if (s.indexOf(replaceSourceStr) > 0) {
				logger.info("\n替换ehcache bean文件,targetFile:" + targetFile);
				s = String.format(ehcachePropery, TARGET_PROFILE);
			}
		} else if (sourceFile.indexOf(SOURCE_APPLICATION_FILE_NAME) >= 0) {
			String replaceSourceStr = "spring.profiles.active=";
			logger.info("==============替换:" + sourceFile + ",application文件");
			if (s.indexOf(replaceSourceStr) >= 0) {
				s = String.format(profileOfApplicationFile, TARGET_PROFILE);
				logger.info("\n成功替换appplication文件,targetFile:" + targetFile + ",新值:" + s);
			}
		}
		return s;
	}

	private static List<File> getLevel2Files(File file) {
		List<File> level2List = new ArrayList<File>();
		File[] files = file.listFiles();
		if (files != null && files.length > 0) {
			for (File fileChild : files) {
				if (fileChild.isDirectory() && fileChild.getAbsolutePath().indexOf(".settings") < 0) {
					level2List.add(fileChild);
				}
			}
		}
		return level2List;
	}

	// 一级项目文件夹
	private static List<File> getProjectsOfRootDir(String rootPath) {
		File file = new File(ROOT_PATH);
		File[] files = file.listFiles();
		List<File> resList = new ArrayList<File>();
		if (files != null && files.length > 0) {
			for (File fileChild : files) {
				if (fileChild.isDirectory() && fileChild.getName().indexOf(PROJECT_PREFIX) >= 0) {
					resList.add(fileChild);
				}
			}
		}

		// mmall-front-web处理
		File frontWeb = new File(ROOT_PATH + "/mmall-front-web");
		if (!frontWeb.exists()) {
			logger.error("mmall-front-web not found,path:" + ROOT_PATH + "/mmall-front-web");
		} else {
			File[] childsOfFrontWeb = frontWeb.listFiles();
			if (childsOfFrontWeb != null && childsOfFrontWeb.length > 0) {
				for (File ch : childsOfFrontWeb) {
					if (ch.isDirectory() && ch.getAbsolutePath().indexOf(".settings") < 0) {
						resList.add(ch);
					}
				}
			}
		}

		return resList;
	}

}
