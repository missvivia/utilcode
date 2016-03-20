package com.xyl.mmall.photomgr.service;

import java.io.Serializable;


public class Util {
	public static final String DEFAULT_CATEGORY_NAME = "默认分类";

	public static final int RESP_CODE_OK = 200;

	public static final int RESP_CODE_BAD_REQUEST = 400;
	
	public static final long DEFAULT_DIR_ID = 1;
	
	/**
	 * 裁剪信息。结构如下：
	 * {"width":1000,"rows":[{"height":140,"y":0,"cols":[{"x":0,"width"
	 * :998,"id":
	 * "q9oLGoLg3q"}]},{"height":97,"y":140,"cols":[{"x":0,"width":998,
	 * "id":"4mBmgTyacl"
	 * }]},{"height":135,"y":237,"cols":[{"x":0,"width":998,"id"
	 * :"TDc0e1JX3W"}]},{"height":4009,"y":372,"cols":[]}]}
	 */
	public static class CropRect implements Serializable {
		private static final long serialVersionUID = 4148398278952659610L;

		public String id;

		public int x;

		public int y;

		public int w;

		public int h;

		@Override
		public String toString() {
			return "CropRect [id=" + id + ", x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
		}
	}

	public static class ZoomParam implements Serializable {
		private static final long serialVersionUID = 8339672247694865477L;

		public int h;

		public int w;

		public int mode = 1; // 1:x mode; 2:y mode; 3:z mode. default is x mode.

		public String id; // 如果id为空，则默认为原图片名
	}
}
