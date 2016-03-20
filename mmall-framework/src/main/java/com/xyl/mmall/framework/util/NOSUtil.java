package com.xyl.mmall.framework.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.netease.vstore.photocenter.meta.AlbumImg;
import com.netease.vstore.photocenter.service.AlbumService;
import com.netease.vstore.photocenter.service.impl.AlbumServiceImpl;

/**
 * facade的移至framework
 * @author hzzhanghui,yydx811
 * 
 */
public class NOSUtil {

	private static final AlbumService albumService = new AlbumServiceImpl();

	private NOSUtil() {

	}

	/**
	 * How to use:
	 * 
	 * // 1. build AlbumImg obj, only need to set 'imgName' and 'inputStream'
	 * AlbumImg img1 = new AlbumImg(); img1.setImgName("test1.jpg");
	 * img1.setInputStream(new FileInputStream("c:\\test1.jpg")); AlbumImg img2
	 * = new AlbumImg(); img2.setImgName("test2.jpg"); img2.setInputStream(new
	 * FileInputStream("c:\\test2.jpg"));
	 * 
	 * // 2. build a ArrayList List<AlbumImg> imgList = new
	 * ArrayList<AlbumImg>(); imgList.add(img1); imgList.add(img2);
	 * 
	 * // 3. call API NOSUtil.uploadImgList(imgList);
	 * 
	 * // 4. We can freely use 'width', 'height', 'key', 'url' attribute filled
	 * by NOS System.out.println(img1.getHeight());
	 * System.out.println(img1.getWidth()); System.out.println(img1.getKey());
	 * // The id of image in NOS System.out.println(img1.getImgUrl()); // URL
	 * that can directly access
	 * 
	 * @param imgList
	 * @Throw IllegaArgumentException If imgList is null or size==0, throw.
	 */
	public static void uploadImgList(List<AlbumImg> imgList) {
		albumService.saveImg(imgList);
	}

	/**
	 * Upload a raw stream to NOS
	 * 
	 * @param is
	 *            InputStream that being uploaded to NOS
	 * @String fileName Such as 'test.zip'
	 * @return String Url of file in NOS. If error occurred, return null.
	 * @throws IllegalArgumentException
	 *             If parameter is null
	 */
	public static String uploadFile(InputStream is, String fileName) throws Exception {
		if (is == null || is.available() == 0) {
			throw new IllegalArgumentException("InputStream cannot be null!!");
		}
		if (StringUtils.isEmpty(fileName)) {
			throw new IllegalArgumentException("FileName cannot be null!!");
		}

		AlbumImg img = new AlbumImg();
		img.setInputStream(is);
		img.setImgName(fileName);
		List<AlbumImg> imgList = new ArrayList<AlbumImg>();
		imgList.add(img);
		try {
			albumService.saveImg(imgList);
			return imgList.get(0).getImgUrl();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get file raw stream from NOS
	 * 
	 * @param urlStr
	 *            Such as
	 *            "http://vstore-photocenter-bucket2.nos.netease.com/2c47795c-aafe-48a2-96b8-c84ade9aaf6f.zip"
	 * @return Raw inputStream of the file. Caller has responsibility to close
	 *         the stream.
	 * @throws Exception
	 *             Throws Exception if urlStr is not correct or something wrong
	 *             happen when downloading
	 */
	public static InputStream getFileStream(String urlStr) throws Exception {
		URL url = new URL(urlStr);
		HttpURLConnection httpUrl = (HttpURLConnection) url.openConnection();
		httpUrl.connect();
		InputStream is = httpUrl.getInputStream();
		httpUrl.disconnect();
		return is;
	}
}
