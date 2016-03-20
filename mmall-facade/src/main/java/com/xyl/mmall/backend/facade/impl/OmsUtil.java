package com.xyl.mmall.backend.facade.impl;

import java.util.UUID;

public class OmsUtil {
	public static String createPickOrder(){
		UUID uuid = UUID.randomUUID(); 
		return uuid.toString();
	}
		
}
