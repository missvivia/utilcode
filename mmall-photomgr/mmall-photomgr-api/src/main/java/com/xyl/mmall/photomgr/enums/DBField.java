package com.xyl.mmall.photomgr.enums;

/**
 * Meta字段名。和表字段相同。构造SQL时使用这些常量。避免写死。
 * 
 * @author hzzhanghui
 * 
 */
public class DBField {
	public static class DirField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String dirName = "dirName";

		public static final String dirCreateDate = "dirCreateDate";
	}

	public static class ImgField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String dirId = "dirId";

		public static final String imgName = "imgName";

		public static final String imgUrl = "imgUrl";

		public static final String nosPath = "nosPath";

		public static final String createDate = "createDate";

		public static final String height = "height";

		public static final String width = "width";

		public static final String imgType = "imgType";
	}

	public static class UserField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String userName = "userName";

		public static final String userCreateDate = "userCreateDate";
	}

	public static class WaterField {
		public static final String id = "id";

		public static final String userId = "userId";

		public static final String base = "base";

		public static final String left = "left";

		public static final String top = "top";
	}
}
