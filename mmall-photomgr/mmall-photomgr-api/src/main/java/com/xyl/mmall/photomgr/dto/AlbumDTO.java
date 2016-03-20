package com.xyl.mmall.photomgr.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.xyl.mmall.photomgr.meta.AlbumDir;
import com.xyl.mmall.photomgr.meta.AlbumImg;
import com.xyl.mmall.photomgr.meta.AlbumUser;
import com.xyl.mmall.photomgr.meta.AlbumWaterPrint;

/**
 * 一个DTO=1个user+1个dir+n个img
 * 
 * @author hzzhanghui
 * 
 */
public class AlbumDTO implements Serializable {

	private static final long serialVersionUID = 9104173419652003533L;

	private AlbumUser albumUser;

	private AlbumDir albumDir;

	private AlbumWaterPrint albumWaterPrint;

	private List<AlbumImg> albumImgList = new ArrayList<AlbumImg>();

	// query condition
	private long queryStartTime;

	private long queryEndTime;

	private String queryImgName;

	private int pageSize;

	private int curPage;

	public AlbumUser getAlbumUser() {
		return albumUser;
	}

	public void setAlbumUser(AlbumUser albumUser) {
		this.albumUser = albumUser;
	}

	public AlbumDir getAlbumDir() {
		return albumDir;
	}

	public void setAlbumDir(AlbumDir albumDir) {
		this.albumDir = albumDir;
	}

	public AlbumWaterPrint getAlbumWaterPrint() {
		return albumWaterPrint;
	}

	public void setAlbumWaterPrint(AlbumWaterPrint albumWaterPrint) {
		this.albumWaterPrint = albumWaterPrint;
	}

	public List<AlbumImg> getAlbumImgList() {
		return albumImgList;
	}

	public void setAlbumImgList(List<AlbumImg> albumImgList) {
		this.albumImgList = albumImgList;
	}

	public long getQueryStartTime() {
		return queryStartTime;
	}

	public void setQueryStartTime(long queryStartTime) {
		this.queryStartTime = queryStartTime;
	}

	public long getQueryEndTime() {
		return queryEndTime;
	}

	public void setQueryEndTime(long querySndTime) {
		this.queryEndTime = querySndTime;
	}

	public String getQueryImgName() {
		return queryImgName;
	}

	public void setQueryImgName(String queryImgName) {
		this.queryImgName = queryImgName;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	@Override
	public String toString() {
		return "AlbumDTO [albumUser=" + albumUser + ", albumDir=" + albumDir + ", albumWaterPrint=" + albumWaterPrint
				+ ", albumImgList=" + albumImgList + ", queryStartTime=" + queryStartTime + ", queryEndTime="
				+ queryEndTime + ", queryImgName=" + queryImgName + ", pageSize=" + pageSize + ", curPage=" + curPage
				+ "]";
	}

}
