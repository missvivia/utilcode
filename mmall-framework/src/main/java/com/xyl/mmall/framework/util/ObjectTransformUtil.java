package com.xyl.mmall.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/** 
 * @author lhp 
 * @version 2015年11月17日 下午4:20:59 
 *  
 */
public class ObjectTransformUtil {
	
	 private static final Logger logger = LoggerFactory.getLogger(ObjectTransformUtil.class);
	
	 public static byte[] objectToByte(Object obj){
        byte[] bytes = null;
        try {
            //object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            bytes = bo.toByteArray();
            bo.close();
            oo.close();    
         } catch(Exception e) {
           logger.error(e.getMessage());
         }
          return(bytes);
	   }
	 
	  public static Object byteToObject(byte[] bytes){
    	 Object obj = null;
         try {
	        //bytearray to object
	        ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
	        ObjectInputStream oi = new ObjectInputStream(bi);
	        obj = (Object)oi.readObject();
	        bi.close();
	        oi.close();
	     }catch(Exception e) {
	    	   logger.error(e.getMessage());
	     }
           return obj;
	   }

}
