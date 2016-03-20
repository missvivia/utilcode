package com.xyl.mmall.jms.meta;

import com.xyl.mmall.jms.enums.MailType;

public class MailMessage extends BizMessage {

	private static final long serialVersionUID = 1L;

	private String[] ttoArr;// 接受邮件的地址

	private String ttitle;// 邮件的标题

	private String tcontent;// 邮件内容

	private String fromName;// 发送者姓名

	private MailType mailType;

	public String[] getTtoArr() {
		return ttoArr;
	}

	public void setTtoArr(String[] ttoArr) {
		this.ttoArr = ttoArr;
	}

	public String getTtitle() {
		return ttitle;
	}

	public void setTtitle(String ttitle) {
		this.ttitle = ttitle;
	}

	public String getTcontent() {
		return tcontent;
	}

	public void setTcontent(String tcontent) {
		this.tcontent = tcontent;
	}

	public String getFromName() {
		return fromName;
	}

	public void setFromName(String fromName) {
		this.fromName = fromName;
	}

	public MailType getMailType() {
		return mailType;
	}

	public void setMailType(MailType mailType) {
		this.mailType = mailType;
	}

}
