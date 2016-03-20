/**
 * 
 */
package com.xyl.mmall.security.session;

import java.io.Serializable;
import java.util.Collection;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lihui
 *
 */
public class MmallSessionDAO extends AbstractSessionDAO {

	private static final Logger LOGGER = LoggerFactory.getLogger(MmallSessionDAO.class);

	private static final String MMALL_SESSION_ID_PREFIX = "Mmall_Shiro_Session_ID:";

	private NkvSessionHelper sessionHelper;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#update(org.apache.shiro.session.Session)
	 */
	@Override
	public void update(Session session) throws UnknownSessionException {
		if (session == null || session.getId() == null) {
			LOGGER.error("Session to save is empty or id is null!");
			return;
		}
		sessionHelper.putToSession(genMmallSessionId(session.getId()), session);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#delete(org.apache.shiro.session.Session)
	 */
	@Override
	public void delete(Session session) {
		if (session == null || session.getId() == null) {
			LOGGER.error("Session to delete is empty or id is null!");
			return;
		}
		sessionHelper.removeFromSession(genMmallSessionId(session.getId()));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.session.mgt.eis.SessionDAO#getActiveSessions()
	 */
	@Override
	public Collection<Session> getActiveSessions() {
		return sessionHelper.getAllFromSessions();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doCreate(org.apache.shiro.session.Session)
	 */
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = generateSessionId(session);
		assignSessionId(session, sessionId);
		sessionHelper.putToSession(genMmallSessionId(sessionId), session);
		return sessionId;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.apache.shiro.session.mgt.eis.AbstractSessionDAO#doReadSession(java.io.Serializable)
	 */
	@Override
	protected Session doReadSession(Serializable sessionId) {
		if (sessionId == null) {
			LOGGER.error("Session to read id is null!");
			return null;
		}
		return sessionHelper.getFromSession(genMmallSessionId(sessionId));
	}

	private String genMmallSessionId(Serializable sessionId) {
		return MMALL_SESSION_ID_PREFIX + sessionId;
	}

	/**
	 * @return the sessionHelper
	 */
	public NkvSessionHelper getSessionHelper() {
		return sessionHelper;
	}

	/**
	 * @param sessionHelper
	 *            the sessionHelper to set
	 */
	public void setSessionHelper(NkvSessionHelper sessionHelper) {
		this.sessionHelper = sessionHelper;
	}

}
