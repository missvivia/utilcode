package com.xyl.mmall.jms.util;

import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import com.xyl.mmall.jms.enums.MailType;
import com.xyl.mmall.jms.service.util.ResourceTextUtil;

/**
 * 
 * @author hzzhaozhenzuo
 *
 */
public class SendEmailUtil {

	private Session sessionForNormal;

	private Session sessionForSub;

	private Logger logger = Logger.getLogger(SendEmailUtil.class);

	private static final ResourceBundle mailResouceBundle = ResourceTextUtil
			.getResourceBundleByName("content.mail");

	private static final String MAIL_FROM = mailResouceBundle
			.getString("mail.from");

	private static final String MAIL_REPLYTO = mailResouceBundle
			.getString("mail.replyTo");

	private static final String MAIL_PERSONAL = mailResouceBundle
			.getString("mail.personal");

	private static final String MAIL_FROM_FOR_SUB = mailResouceBundle
			.getString("mail.from.sub");

	private static final String MAIL_REPLYTO_FOR_SUB = mailResouceBundle
			.getString("mail.replyTo.sub");

	private static final String MAIL_HOST_FOR_NORMAL = mailResouceBundle
			.getString("mail.domain");

	private static final String MAIL_HOST_FOR_SUB = mailResouceBundle
			.getString("mail.domain.sub");

	public void init() {
		sessionForNormal = this.createSession(MAIL_HOST_FOR_NORMAL);
		sessionForSub = this.createSession(MAIL_HOST_FOR_SUB);
	}

	private Session createSession(String mailHost) {
		Properties props = System.getProperties();
		props.put("mail.smtp.host", mailHost);
		props.put("mail.smtp.auth", false);
		Session session = Session.getInstance(props);
		session.setDebug(false);
		return session;
	}

	private String[] getMailFromByMailType(MailType mailType) {
		String[] resArr = new String[2];
		if (MailType.NORMAL.equals(mailType)) {
			resArr[0] = MAIL_FROM;
			resArr[1] = MAIL_REPLYTO;
		} else if (MailType.SUBSCRIBE.equals(mailType)) {
			resArr[0] = MAIL_FROM_FOR_SUB;
			resArr[1] = MAIL_REPLYTO_FOR_SUB;
		} else {
			logger.error("error mail type,type:" + mailType);
		}
		return resArr;
	}

	public boolean sendEmail(String[] tto, String ttitle, String tcontent,
			MailType mailType) {
		Session sessionTemp;
		if (MailType.NORMAL.equals(mailType)) {
			sessionTemp = sessionForNormal;
		} else if (MailType.SUBSCRIBE.equals(mailType)) {
			sessionTemp = sessionForSub;
		} else {
			logger.error("unsupport mailType,type:" + mailType);
			return false;
		}
		return this.sendEmail(tto, ttitle, tcontent, mailType, sessionTemp);
	}

	private boolean sendEmail(String[] tto, String ttitle, String tcontent,
			MailType mailType, final Session session) {
		boolean flag = true;

		Message message = new MimeMessage(session); // 邮件会话新建一个消息对象
		Transport transport = null;

		try {

			String[] mailFromArr = this.getMailFromByMailType(mailType);

			// set from
			Address from;
			from = new InternetAddress(mailFromArr[0], MAIL_PERSONAL); // 发件人的邮件地址
			message.setHeader("From", mailFromArr[0]);
			message.setHeader("Reply-To", mailFromArr[1]);
			message.setFrom(from); // 设置发件人

			// set recipient
			Address[] toArr = new Address[tto.length];
			for (int i = 0; i < tto.length; i++) {
				toArr[i] = new InternetAddress(tto[i]); // 收件人的邮件地址
			}
			message.setRecipients(Message.RecipientType.TO, toArr);

			message.setSubject(ttitle); // 设置主题

			// set content
			BodyPart bodypart = new MimeBodyPart();
			bodypart.setContent(tcontent, "text/html;charset=GB2312");

			MimeMultipart multipart = new MimeMultipart("alternative");
			multipart.addBodyPart(bodypart);
			message.setContent(multipart);
			message.setSentDate(new Date()); // 设置发信时间
			message.saveChanges(); // 存储邮件信息
			transport = session.getTransport("smtp");
			transport.connect();

			transport.sendMessage(message, message.getAllRecipients()); // 发送邮件,其中第二个参数是所有已设好的收件人地址
		} catch (Exception e) {
			flag = false;
			logger.info(e.getMessage());
		} finally {
			if (transport != null)
				try {
					transport.close();
				} catch (MessagingException e) {
					logger.info(e.getMessage());
				}
		}

		logger.info("=====send mail,flag:" + flag);

		return flag;
	}

}
