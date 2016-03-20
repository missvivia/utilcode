package com.xyl.mmall.task.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PushException extends Exception{
	private Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 
	 */
	private static final long serialVersionUID = 6325377201269090924L;
	public PushException(String value) {		
		super(value);
		logger.error(value, this);
	}
	
}
