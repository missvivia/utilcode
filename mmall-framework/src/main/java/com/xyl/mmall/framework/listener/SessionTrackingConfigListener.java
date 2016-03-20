/**
 * 
 */
package com.xyl.mmall.framework.listener;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionTrackingMode;

import org.springframework.boot.context.embedded.ServletContextInitializer;

/**
 * @author lihui
 *
 */
public class SessionTrackingConfigListener implements ServletContextInitializer {

	/**
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.boot.context.embedded.ServletContextInitializer#onStartup(javax.servlet.ServletContext)
	 */
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		Set<SessionTrackingMode> sessionTrackingModes = new HashSet<>();
		sessionTrackingModes.add(SessionTrackingMode.COOKIE);
		servletContext.setSessionTrackingModes(sessionTrackingModes);
	}

}
