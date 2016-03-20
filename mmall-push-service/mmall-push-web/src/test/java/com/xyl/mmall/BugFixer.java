package com.xyl.mmall;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

import com.netease.push.util.FileMd5Utils;

public class BugFixer {
	 public static final String MD5 = "MD5";
	public static void main(String args[]){
		getFileMD5String(new File("/tmp/348"));
	}
	
	
	   public static String getFileMD5String(File file) {
	        try {
	            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
	            FileInputStream in = new FileInputStream(file);
	            FileChannel ch = in.getChannel();
	            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY,
	                    0, file.length());
	            messageDigest.update(byteBuffer);
	            in.close();
	            byteBuffer.clear();
	            System.out.println(ch.isOpen());
	            return "";
	        } catch (Exception ex) {
	            System.err.println(FileMd5Utils.class.getName() + ","
	                    + ex.getMessage());
	        }
	        return null;
	    }
	   
	   
	   public static String getFileMD5String2(File file) {
	        try {
	        	ByteBuffer buff = ByteBuffer.allocate(102400);
	            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
	            FileInputStream in = new FileInputStream(file);
	            FileChannel ch = in.getChannel();
	            ch.read(buff); 
	            in.close();
	            System.out.println(ch.isOpen());
	            return "";
	        } catch (Exception ex) {
	            System.err.println(FileMd5Utils.class.getName() + ","
	                    + ex.getMessage());
	        }
	        return null;
	    }
}
